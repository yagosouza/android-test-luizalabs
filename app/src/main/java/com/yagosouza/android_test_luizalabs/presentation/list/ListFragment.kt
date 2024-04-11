package com.yagosouza.android_test_luizalabs.presentation.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yagosouza.android_test_luizalabs.R
import com.yagosouza.android_test_luizalabs.databinding.FragmentListBinding
import com.yagosouza.android_test_luizalabs.domain.model.Gist
import com.yagosouza.android_test_luizalabs.presentation.detail.DetailFragment
import com.yagosouza.android_test_luizalabs.presentation.list.adapter.ListAdapter
import org.koin.android.ext.android.inject


class ListFragment : Fragment(), ListContract.View {

    private lateinit var binding: FragmentListBinding

    private val listAdapter by lazy {
        ListAdapter(
            onItemClick = ::onItemSelected,
            onItemFavoriteClick = ::onItemFavoriteSelected
        )
    }
    private val presenter: ListPresenterImpl by inject()

    private var page = 1

    private val listGists: MutableList<Gist> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)

        setupView()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)

    }

    private fun FragmentListBinding.setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        with(recyclerView) {
            layoutManager = linearLayoutManager
            adapter = listAdapter
            isNestedScrollingEnabled = false

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        presenter.fetchGist(page++)
                    }
                }
            })
        }
    }

    private fun setupView() {
        binding.setupRecyclerView()

        lifecycle.addObserver(presenter)
        presenter.fetchGist()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun displayGist(list: List<Gist>) {
        listGists.addAll(list)
        listAdapter.submitList(listGists)
        listAdapter.notifyDataSetChanged()
    }

    override fun displayLoading(isLoading: Boolean) {
        binding.progressBarList.isVisible = isLoading
    }

    override fun showError(error: Throwable) {
        Log.d("ERRO_API", error.message ?: "Sem mensagem de erro")
    }

    private fun onItemFavoriteSelected(isFavorite: Boolean, gist: Gist) {
        val textToast = if (isFavorite) "Adicionado aos Favoritos" else "Removido dos Favoritos"
        Toast.makeText(context, textToast, Toast.LENGTH_SHORT).show()
        presenter.saveFavorite(gist)
    }

    override fun onItemSelected(id: String) {
        navigateToDetail(id)
    }

    private fun navigateToDetail(id: String) {
        //TODO usar metodo activity??
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = DetailFragment.newInstance(id)

        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    companion object {
        fun newInstance() = ListFragment()
    }
}
