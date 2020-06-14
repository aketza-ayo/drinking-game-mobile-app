package com.devapp.drinkinggame

import android.app.DialogFragment
import android.graphics.drawable.AnimationDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.ToneGenerator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast


class MurcielagoDialog : DialogFragment(){

    private var beep = ToneGenerator(AudioManager.STREAM_MUSIC, 100);

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
            Toast.makeText(activity,resources.getString(R.string.those_in_the_dark), Toast.LENGTH_SHORT).show()
            beep.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MurcielagoDialog)
    }

}