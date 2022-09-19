package com.example.easymusicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlin.system.exitProcess

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
              ApplicationClass.PREVIOUS -> prevNextSong(increment = false , context = context!!)
              ApplicationClass.PLAY -> if (PlayerActivity.isPlaying) pauseMusic() else playMusic()
              ApplicationClass.NEXT -> prevNextSong(increment = true,context = context!!)
              ApplicationClass.EXIT -> {
                   exitApplication()
              }
        }
    }
    private fun playMusic(){
        PlayerActivity.isPlaying = true
        PlayerActivity.musicService!!.mediaPlayer!!.start()
        PlayerActivity.musicService!!.showNotification(R.drawable.pause_icon)
        PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.pause_icon)
        NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.pause_icon)
    }
    private fun pauseMusic(){
        PlayerActivity.isPlaying = false
        PlayerActivity.musicService!!.mediaPlayer!!.pause()
        PlayerActivity.musicService!!.showNotification(R.drawable.play_icon)
        PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.play_icon)
        NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.play_icon)
    }

    private fun prevNextSong(increment: Boolean, context: Context){
        setSongPosition(increment = increment)
        PlayerActivity.musicService!!.createMediaPlayer()
        Glide.with(context)
            .load(PlayerActivity.musicListPA[PlayerActivity.songposition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.easy_music_player_icon_splash_screen).centerCrop())
            .into(PlayerActivity.binding.SongImgPA)
        PlayerActivity.binding.SongNamePA.text = PlayerActivity.musicListPA[PlayerActivity.songposition].title
        Glide.with(context)
            .load(PlayerActivity.musicListPA[PlayerActivity.songposition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.easy_music_player_icon_splash_screen).centerCrop())
            .into(NowPlaying.binding.SongImgPA)
        NowPlaying.binding.songNameNP.text = PlayerActivity.musicListPA[PlayerActivity.songposition].title
        playMusic()
        PlayerActivity.fIndex = favouriteChecker(PlayerActivity.musicListPA[PlayerActivity.songposition].id)
        if (PlayerActivity.isFavourite) PlayerActivity.binding.favouriteBtnPA.setImageResource(R.drawable.favourite_icon)
        else PlayerActivity.binding.favouriteBtnPA.setImageResource(R.drawable.favourite_empty_icon)
    }
    fun favouriteChecker(id: String): Int{
        PlayerActivity.isFavourite = false
        FavouriteActivity.favouriteSongs.forEachIndexed { index, music ->
            if (id == music.id){
                PlayerActivity.isFavourite = true
                return index
            }
        }
        return -1
    }
    fun exitApplication(){
        if (PlayerActivity.musicService !=null){
            PlayerActivity.musicService!!.audioManager.abandonAudioFocus(PlayerActivity.musicService)
            PlayerActivity.musicService!!.stopForeground(true)
            PlayerActivity.musicService!!.mediaPlayer!!.release()
            PlayerActivity.musicService = null}
        exitProcess(1)
    }
}