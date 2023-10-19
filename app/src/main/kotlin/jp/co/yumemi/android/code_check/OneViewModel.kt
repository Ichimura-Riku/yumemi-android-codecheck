/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.content.Context
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.ViewModel
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import timber.log.Timber
import java.lang.Exception
import java.util.*

/**
 * TwoFragment で使う
 */
class OneViewModel(
    private val context: Context
) : ViewModel() {

    // 検索結果
//    suspend fun searchResults(inputText: String): List<Item> = withContext(Dispatchers.IO) {
//        val client = HttpClient(Android)
//
//        try {
//
//            val response: HttpResponse = client.get("https://api.github.com/search/repositories") {
//                header("Accept", "application/vnd.github.v3+json")
//                parameter("q", inputText)
//            }
//
//            val jsonBody = JSONObject(response.receive<String>())
//
//            val jsonItems = jsonBody.optJSONArray("items")!!
//
//            val items = mutableListOf<Item>()
//
//            /**
//             * アイテムの個数分ループする
//             */
//            for (i in 0 until jsonItems.length()) {
//                val jsonItem = jsonItems.optJSONObject(i)!!
//                val name = jsonItem.optString("full_name")
//                val ownerIconUrl = jsonItem.optJSONObject("owner")?.optString("avatar_url") ?: ""
//                val language = jsonItem.optString("language") ?: ""
//                val stargazersCount = jsonItem.optLong("stargazers_count")
//                val watchersCount = jsonItem.optLong("watchers_count")
//                val forksCount = jsonItem.optLong("forks_count")
//                val openIssuesCount = jsonItem.optLong("open_issues_count")
//
//                items.add(
//                    Item(
//                        name = name,
//                        ownerIconUrl = ownerIconUrl,
//                        language = context.getString(R.string.written_language, language),
//                        stargazersCount = stargazersCount,
//                        watchersCount = watchersCount,
//                        forksCount = forksCount,
//                        openIssuesCount = openIssuesCount
//                    )
//                )
//            }
//
//            lastSearchDate = Date()
//
//            return@withContext items.toList()
//        } catch (e: Exception) {
//            Log.e("SearchError", "$e")
//            return@withContext emptyList()
//        }
//    }

    suspend fun searchResults(inputText: String): List<Item> = withContext(Dispatchers.IO) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://api.github.com/")
//            .addConverterFactory(GsonConverterFactory.create()) // JSONパーサーとしてGsonを使用
//            .build()
//
//        val apiService = retrofit.create(GitHubApiService::class.java)

        val logging = HttpLoggingInterceptor {
            Timber.tag("OkHttp").d(it)
        }

        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val moshi = Moshi.Builder()
            .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .build()

        val gitHubInfoRequests = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GitHubInfoRequests::class.java)

        launch {
            Log.d("debug----", "4")

            try {
                val response = gitHubInfoRequests.getRepositories()
                Log.d("debug----", "5")
                if (response.isSuccessful) {
                    val repositories = response.body()
                    Log.d("debug-------", "$repositories")
                } else {
                    val repositories = response.body()
                    Log.d("debug-------", "$repositories")
                }
            } catch (e: Exception) {
                Log.e("error -----", "$e")
            }
        }
        return@withContext emptyList()
    }
}

@Parcelize
data class Item(
    val name: String,
    val ownerIconUrl: String,
    val language: String?,
    val stargazersCount: Long,
    val watchersCount: Long,
    val forksCount: Long,
    val openIssuesCount: Long,
) : Parcelable

interface GitHubInfoRequests {
    @GET("search/repositories")
    suspend fun getRepositories(
        @Header("Accept") acceptHeader: String = "application/vnd.github.v3+json",
        @Query("q") query: String = "a",
    ): Response<ResponseInfo> // SearchResponseはAPIレスポンスのモデルクラス
}

data class ResponseInfo(
    val items: List<Item>
)
