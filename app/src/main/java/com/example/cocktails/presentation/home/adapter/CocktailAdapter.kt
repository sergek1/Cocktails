package com.example.cocktails.presentation.home.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cocktails.R
import com.example.cocktails.databinding.ItemCocktailBinding
import com.example.domain.model.Cocktail

class CocktailAdapter(
    private val onClicked: (Int) -> Unit
) :
    RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder>() {

    var items: List<Cocktail> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    inner class CocktailViewHolder(
        private val binding: ItemCocktailBinding,
        private val onClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cocktail: Cocktail) = with(binding) {
            tvCocktailTitle.text = cocktail.name
            if (cocktail.image != null) {
                val uri = Uri.parse(cocktail.image)
                ivCocktail.setImageURI(uri)
            } else {
                ivCocktail.setBackgroundResource(R.drawable.detail)
            }
            cardView.setOnClickListener {
                cocktail.id?.let { it1 -> onClicked(it1) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocktailViewHolder {
        val binding =
            ItemCocktailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CocktailViewHolder(binding, onClicked)
    }

    override fun onBindViewHolder(holder: CocktailViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


}