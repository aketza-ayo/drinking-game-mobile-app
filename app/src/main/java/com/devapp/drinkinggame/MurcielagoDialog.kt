package com.devapp.drinkinggame

import android.app.DialogFragment
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView


class MurcielagoDialog : DialogFragment(){

    private lateinit var mediaPlayer: MediaPlayer

    companion object{
        private var instance: MurcielagoDialog? = null

        fun getInstace(): MurcielagoDialog? {
            if(instance == null) {
                instance = MurcielagoDialog()
            }
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.murcielago_image, container, false)

        var imageViewMurcielago = view.findViewById<ImageView>(R.id.imageViewMurcielago)
        imageViewMurcielago.setImageResource(R.drawable.running_murcielago)
        var flyingMurcielago = imageViewMurcielago.drawable as AnimationDrawable
        flyingMurcielago.start()


        imageViewMurcielago.setOnClickListener {
           Log.d(Constants.APP_NAME,"Murcielago is clicked")
            mediaPlayer.start()
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer.create(activity, R.raw.glitch_in_the_matrix)

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MurcielagoDialog)
    }

}