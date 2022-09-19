package com.example.easymusicplayer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.easymusicplayer.PlayerActivity.Companion.musicListPA
import com.example.easymusicplayer.databinding.FavouriteViewBinding

class FavouriteAdapter(private val context: Context, private var Musiclist: ArrayList<Music>) : RecyclerView.Adapter<FavouriteAdapter.MyHolder>(){
    class MyHolder(binding: FavouriteViewBinding):RecyclerView.ViewHolder(binding.root){
        val image = binding.SongImgFV
        val name = binding.SongNameFV
        val root = binding.root

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(FavouriteViewBinding.inflate(LayoutInflater.from(context),parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.name.text = Musiclist[position].title
        Glide.with(context)
            .load(musicListPA[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.easy_music_player_icon_splash_screen).centerCrop())
            .into(holder.image)
        holder.root.setOnClickListener {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("index",position)
            intent.putExtra("class","FavouriteAdapter")
            ContextCompat.startActivity(context,intent,null)
        }
    }
    override fun getItemCount(): Int {
        return Musiclist.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateFavourites(newList: ArrayList<Music>){
        Musiclist = ArrayList()
        Musiclist.addAll(newList)
        notifyDataSetChanged()
    }

    }

