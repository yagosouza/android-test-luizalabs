package com.yagosouza.android_test_luizalabs.presentation.detail

import com.yagosouza.android_test_luizalabs.base.BaseContract
import com.yagosouza.android_test_luizalabs.domain.model.Gist

interface DetailContract : BaseContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun fetchDetail(id: String)
    }

    interface View : BaseContract.View {

        fun displayGist(gistDetail: Gist)

        fun displayLoading(isLoading: Boolean)

        fun showError(error: Throwable)
    }
}