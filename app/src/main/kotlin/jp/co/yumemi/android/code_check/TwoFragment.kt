/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.databinding.FragmentTwoBinding

class TwoFragment : Fragment(R.layout.fragment_two) {

    private val args: TwoFragmentArgs by navArgs()

    private var binding: FragmentTwoBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("検索した日時", lastSearchDate.toString())

        binding = FragmentTwoBinding.bind(view)

        val item = args.item

        binding?.apply {
            ownerIconView.load(item.ownerIconUrl)
            nameView.text = item.name
            languageView.text = item.language
            starsView.text = getString(R.string.stars, "${item.stargazersCount}")
            watchersView.text = getString(R.string.watchers, "${item.watchersCount}")
            forksView.text = getString(R.string.forks, "${item.forksCount}")
            openIssuesView.text = getString(R.string.open_issues, "${item.openIssuesCount}")
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // Fragmentが破棄される際にbindingも解放する
        binding = null
    }
}
