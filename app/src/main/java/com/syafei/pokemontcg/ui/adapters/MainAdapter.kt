package com.syafei.pokemontcg.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.syafei.pokemontcg.R
import com.syafei.pokemontcg.coredata.model.PokemonData
import com.syafei.pokemontcg.databinding.ItemPokemonBinding

class MainAdapter : PagingDataAdapter<PokemonData, MainAdapter.PokeViewHolder>(DIFFUTILS) {

    var onItemClick: ((PokemonData) -> Unit)? = null

    inner class PokeViewHolder(private val binding: ItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: PokemonData) {
            Log.d("Main adapter", "data: $data")
            binding.ivTitle.text = data.name
            Glide.with(binding.root)
                .load(data.images?.small)
                .apply(
                    RequestOptions.placeholderOf(
                        R.drawable.ic_baseline_refresh_24
                    ).error(R.drawable.ic_baseline_broken_image_24)
                ).into(binding.ivImage)

            binding.cvItemFrame.setOnClickListener {
                onItemClick?.invoke(data)
            }
        }
    }

    override fun onBindViewHolder(holder: PokeViewHolder, position: Int) {
        val itemData = getItem(position)
        if (itemData != null) {
            holder.bind(itemData)
        }
        holder.setIsRecyclable(false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeViewHolder {
        return PokeViewHolder(
            ItemPokemonBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }


    object DIFFUTILS : DiffUtil.ItemCallback<PokemonData>() {
        // DiffUtil uses this test to help discover if an item was added, removed, or moved.
        override fun areItemsTheSame(oldItem: PokemonData, newItem: PokemonData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PokemonData, newItem: PokemonData): Boolean {
            return oldItem == newItem
        }
    }

}