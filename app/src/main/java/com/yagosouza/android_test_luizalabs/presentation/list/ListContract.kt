package com.yagosouza.android_test_luizalabs.presentation.list

import com.yagosouza.android_test_luizalabs.base.BaseContract
import com.yagosouza.android_test_luizalabs.domain.model.Gist

interface ListContract : BaseContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun fetchGist(page: Int = 0)
    }

    interface View : BaseContract.View {

        fun displayGist(list: List<Gist>)

        fun displayLoading(isLoading: Boolean)

        fun showError(error: Throwable)

        fun onItemSelected(id: String)
    }
}