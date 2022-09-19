package com.example.easymusicplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.easymusicplayer.databinding.MusicViewBinding

class MusicAdapter(private val context: Context, private var Musiclist: ArrayList<Music>, private val playlistDetails: Boolean = false,
   private val SelectionActivity: Boolean = false)
    : RecyclerView.Adapter<MusicAdapter.MyHolder>() {

    class MyHolder(binding: MusicViewBinding):RecyclerView.ViewHolder(binding.root){
        val  title = binding.SongNameMv
        val  albums = binding.SongAlbumMv
        val image = binding.imageMV
        val duration=binding.songDuration
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(MusicViewBinding.inflate(LayoutInflater.from(context),parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.title.text = Musiclist[position].title
        holder.albums.text = Musiclist[position].album
        holder.duration.text = formatDuration(Musiclist[position].duration)
        Glide.with(context)
            .load(Musiclist[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.easy_music_player_icon_splash_screen).centerCrop())
            .into(holder.image)
        when{
            playlistDetails ->{
                holder.root.setOnClickListener {
                    sendIntent(ref = "PlaylistDetailsAdapter", pos = position)
                }
            }
            SelectionActivity->{
                holder.root.setOnClickListener {
                    if (addSong(Musiclist[position]))
                        holder.root.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500))
                    else
                        holder.root.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                }
            }
            else -> {
                holder.root.setOnClickListener {
                when{
                    MainActivity.search -> sendIntent(ref = "MusicAdapterSearch", pos = position)
                    Musiclist[position].id == PlayerActivity.nowPlaying ->
                        sendIntent(ref = "NowPlaying", pos = PlayerActivity.songposition)
                    else->  sendIntent(ref = "MusicAdapter", pos = position) }}

            }
        }
    }
    override fun getItemCount(): Int {
        return Musiclist.size
    }
    fun updateMusicList(searchList : ArrayList<Music>){
        Musiclist = ArrayList()
        Musiclist.addAll(searchList)
        notifyDataSetChanged()
    }
    private fun sendIntent(ref: String, pos: Int){
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("index",pos)
        intent.putExtra("class",ref)
        ContextCompat.startActivity(context,intent,null)
    }
    private fun  addSong(song: Music): Boolean{
        PlaylistActivity.musicPlaylist.ref[PlaylistDetails.currentPlaylistPos].playlist.forEachIndexed { index, music ->
            if (song.id == music.id){
                PlaylistActivity.musicPlaylist.ref[PlaylistDetails.currentPlaylistPos].playlist.removeAt(index)
                return false
        }
    }
        PlaylistActivity.musicPlaylist.ref[PlaylistDetails.currentPlaylistPos].playlist.add(song)
        return true
    }
    fun refreshPlaylist(){
        Musiclist = ArrayList()
        Musiclist = PlaylistActivity.musicPlaylist.ref[PlaylistDetails.currentPlaylistPos].playlist
        notifyDataSetChanged()
    }
}

