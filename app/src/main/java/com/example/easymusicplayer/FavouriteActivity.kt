package com.example.easymusicplayer

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easymusicplayer.databinding.ActivityFavouriteBinding

class FavouriteActivity : AppCompatActivity() {

    private lateinit var  binding : ActivityFavouriteBinding
    private lateinit var  adapter: FavouriteAdapter


    companion object{
        var favouriteSongs: ArrayList<Music> = ArrayList()
        var favouritesChanged: Boolean = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(MainActivity.currentTheme[MainActivity.themeIndex])
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        favouriteSongs = checkPlaylist(favouriteSongs)
        binding.backBtnFA?.setOnClickListener { finish() }
        binding.favouriteRV?.setHasFixedSize(true)
        binding.favouriteRV?.setItemViewCacheSize(13)
        binding.favouriteRV?.layoutManager = GridLayoutManager(this,4)
       adapter = FavouriteAdapter(this, favouriteSongs)
        binding.favouriteRV?.adapter = adapter
        if (favouriteSongs.size < 1) binding.shuffleBtnFA?.visibility  =View.INVISIBLE
        binding.shuffleBtnFA?.setOnClickListener {
            val intent =  Intent(this,PlayerActivity::class.java)
            intent.putExtra("index",0)
            intent.putExtra("class","FavouriteShuffle")
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        if(favouritesChanged) {
            adapter.updateFavourites(favouriteSongs)
            favouritesChanged = false
        }
    }
}