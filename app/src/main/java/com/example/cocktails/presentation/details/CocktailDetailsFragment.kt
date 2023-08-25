package com.example.cocktails.presentation.details

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cocktails.R
import com.example.cocktails.databinding.FragmentCocktailDetailsBinding
import com.example.cocktails.presentation.details.viewmodel.CocktailDetailsViewModel
import com.example.domain.model.Cocktail
import com.example.domain.model.Result
import org.koin.androidx.viewmodel.ext.android.viewModel

class CocktailDetailsFragment : Fragment() {
    private var _binding: FragmentCocktailDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<CocktailDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCocktailDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt("id")?.let { viewModel.getCocktail(it) }

        observeViewModel()

        binding.btnEdit.setOnClickListener {
            findNavController().navigate(
                R.id.action_cocktailDetailFragment_to_editCocktailFragment,
                bundleOf(
                    "id" to arguments?.getInt("id")!!
                )
            )
        }
    }

    private fun observeViewModel() {
        viewModel.cocktail.removeObservers(viewLifecycleOwner)
        viewModel.cocktail.observe(viewLifecycleOwner) {
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

    private fun successUI(cocktail: Cocktail) {
        with(binding) {
            progressIndicator.visibility = View.GONE

            if (cocktail.image != null) {
                val uri = Uri.parse(cocktail.image)
                ivCocktail.setImageURI(uri)
            } else {
                ivCocktail.setBackgroundResource(R.drawable.detail)
            }

            if (cocktail.name != null) {
                tvTitle.text = cocktail.name
            } else {
                tvTitle.visibility = View.GONE
            }
            if (cocktail.description != null && cocktail.description!!.isNotEmpty()) {
                tvDescription.visibility = View.VISIBLE
                tvDescription.text = cocktail.description
            } else {
                tvDescription.visibility = View.GONE
            }

            if (cocktail.recipe != null && cocktail.recipe!!.isNotEmpty()) {
                tvRecipe.visibility = View.VISIBLE
                tvRecipe.text = cocktail.recipe
                tvRecipeTitle.visibility = View.VISIBLE
            } else {
                tvRecipeTitle.visibility = View.GONE
                tvRecipe.visibility = View.GONE
            }

            if (cocktail.ingredients.any { it.trim() != "" }) {
                tvIngredients.visibility = View.VISIBLE
                val ingredients = cocktail.ingredients.filter { it.trim() != "" }
                    .joinToString(separator = "\n-\n")
                tvIngredients.text = ingredients
            } else {
                tvIngredients.visibility = View.GONE
            }

        }
    }

    private fun failureUI(message: String?) {
    }

    private fun loadingUI() {
        binding.progressIndicator.visibility = View.VISIBLE
    }
}