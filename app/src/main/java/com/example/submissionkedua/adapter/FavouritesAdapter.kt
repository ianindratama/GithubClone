package com.example.submissionkedua.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionkedua.data.local.entity.FavouriteUserEntity
import com.example.submissionkedua.databinding.ItemRowUserFavouriteBinding

class FavouritesAdapter(private val onBookmarkClick: (String) -> Unit) : ListAdapter<FavouriteUserEntity, FavouritesAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowUserFavouriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val favouriteUser = getItem(position)
        holder.bind(favouriteUser)

        holder.binding.ivFavourite.setOnClickListener {
            onBookmarkClick(favouriteUser.username)
        }

    }

    inner class MyViewHolder(val binding: ItemRowUserFavouriteBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(favouriteUser: FavouriteUserEntity) {

            binding.itemUsername.text = favouriteUser.username

            val itemUrl = "https://github.com/${favouriteUser.username}"

            binding.itemUrl.text = itemUrl

            Glide.with(itemView.context)
                .load(favouriteUser.avatarUrl)
                .circleCrop()
                .into(binding.itemPhoto)

            itemView.setOnClickListener{ onItemClickCallback.onItemClicked(favouriteUser.username) }

        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(username: String)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<FavouriteUserEntity> =
            object : DiffUtil.ItemCallback<FavouriteUserEntity>() {
                override fun areItemsTheSame(oldUser: FavouriteUserEntity, newUser: FavouriteUserEntity): Boolean {
                    return oldUser.username == newUser.username
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: FavouriteUserEntity, newUser: FavouriteUserEntity): Boolean {
                    return oldUser == newUser
                }
            }
    }

}