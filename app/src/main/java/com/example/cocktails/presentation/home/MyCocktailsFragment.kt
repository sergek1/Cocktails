package com.example.cocktails.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cocktails.R
import com.example.cocktails.databinding.FragmentMyCocktailsBinding
import com.example.cocktails.presentation.home.adapter.CocktailAdapter
import com.example.cocktails.presentation.home.viewmodel.MyCocktailsViewModel
import com.example.domain.model.Cocktail
import com.example.domain.model.Result
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyCocktailsFragment : Fragment() {
    private var _binding: FragmentMyCocktailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var cocktailAdapter: CocktailAdapter
    private val viewModel by viewModel<MyCocktailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        observeViewModel()
        _binding = FragmentMyCocktailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observeViewModel()
    }

    private fun observeViewModel() {
//        viewModel.items.removeObservers(viewLifecycleOwner)
        viewModel.items.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    it.data?.let { it1 -> successUI(it1) }
                }

                is Result.Loading -> {
                    loadingUI()
                }

                is Result.Failure -> {
                    failureUI(it.message)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        observeViewModel()
    }

    private fun failureUI(message: String?) {
        with(binding) {
            clNoCocktails.visibility = View.GONE
            clCocktails.visibility = View.GONE
        }
    }

    private fun loadingUI() {
        with(binding) {
            clNoCocktails.visibility = View.GONE
            clCocktails.visibility = View.GONE
            //indicator add
        }
    }

    private fun successUI(cocktails: List<Cocktail>) {
        with(binding) {
            cocktailAdapter.items = cocktails
            if (cocktails.isEmpty()) {
                clNoCocktails.visibility = View.VISIBLE
                clCocktails.visibility = View.GONE
            } else {
                clNoCocktails.visibility = View.GONE
                clCocktails.visibility = View.VISIBLE
            }

            ivShare.setOnClickListener {
                createIntent(cocktails)
            }
        }
    }

    private fun createIntent(cocktails: List<Cocktail>) {
        var cocktailsText = cocktails.map { it.name }.take(cocktails.size - 1).joinToString(", ")
        cocktailsText += ", " + cocktails[cocktails.size - 1].name
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Смотри какие коктейли я создал в приложении Cocktail Bar!\n" +
                    "$cocktailsText.\n\nХочешь попробовать?"
        )
        shareIntent.type = "text/plain"
        startActivity(
            Intent.createChooser(
                shareIntent,
                "Поделись названиями!"
            )
        )
    }

    private fun setupAdapter() {
        cocktailAdapter = CocktailAdapter {
            findNavController().navigate(
                R.id.action_myCocktailsFragment_to_cocktailDetailFragment,
                bundleOf(
                    "id" to it
                )
            )
        }


        binding.recyclerView.apply {
            adapter = cocktailAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }
}