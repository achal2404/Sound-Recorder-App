package com.example.a19012021038_soundrecorder

import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_audio_player.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.Duration

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var btnplay:ImageButton
    private lateinit var btnBackward:ImageButton
    private lateinit var btnForward:ImageButton
    private lateinit var speedChip: Chip
    private lateinit var seekbar:SeekBar

    private lateinit var toolbar: MaterialToolbar
    private lateinit var tvFilename:TextView

    private lateinit var tvTrackProgress:TextView
    private lateinit var tvTrackDuration:TextView

    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    private var delax=1000L
    private var jumpValue=1000

    private var playbackSpeed = 1.0f

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        var filePath = intent.getStringExtra("filepath")
        var fileName = intent.getStringExtra("filename")

        toolbar = findViewById(R.id.toolbar)
        tvFilename = findViewById(R.id.tvFilename)

        tvTrackProgress = findViewById(R.id.tvTrackProgress)
        tvTrackDuration = findViewById(R.id.tvTrackDuration)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        tvFilename.text = fileName

        mediaPlayer = MediaPlayer()
        mediaPlayer.apply {
            setDataSource(filePath)
            prepare()
        }

        tvTrackDuration.text = dateFormat(mediaPlayer.duration)

        btnBackward=findViewById(R.id.btnBackward)
        btnForward=findViewById(R.id.btnForward)
        btnplay=findViewById(R.id.btnplay)
        speedChip=findViewById(R.id.chip)
        seekbar=findViewById(R.id.seekbar)

        handler=Handler(Looper.getMainLooper())
        runnable= Runnable {
            seekbar.progress=mediaPlayer.currentPosition
            tvTrackProgress.text=dateFormat(mediaPlayer.currentPosition)
            handler.postDelayed(runnable,delax)
        }


        btnplay.setOnClickListener {
            playPausePlayer()
        }
        playPausePlayer()
        seekbar.max =mediaPlayer.duration


        mediaPlayer.setOnCompletionListener {
            btnplay.background =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_play_circle, theme)
            handler.removeCallbacks(runnable)
        }

        btnForward.setOnClickListener {
            mediaPlayer.seekTo(mediaPlayer.currentPosition+jumpValue)
            seekbar.progress += jumpValue
        }
        btnBackward.setOnClickListener {
            mediaPlayer.seekTo(mediaPlayer.currentPosition-jumpValue)
            seekbar.progress -= jumpValue
        }
        chip.setOnClickListener {
            if(playbackSpeed!=2f)
                playbackSpeed += 0.5f
            else
                playbackSpeed = 0.5f

            mediaPlayer.playbackParams = PlaybackParams().setSpeed(playbackSpeed)
            chip.text = "x $playbackSpeed"
        }

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if(p2)
                    mediaPlayer.seekTo(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

    }
    private fun playPausePlayer() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            btnplay.background =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_pause_circle, theme)
            handler.postDelayed(runnable,delax)
        } else {
            mediaPlayer.pause()
            btnplay.background =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_play_circle, theme)
            handler.removeCallbacks(runnable)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mediaPlayer.stop()
        mediaPlayer.release()
        handler.removeCallbacks(runnable)
    }
    private fun dateFormat(duration:Int):String{
        var d = duration/1000
        var s = d%6
        var m = (d/60%60)
        var h = ((d-m*60)/360).toInt()

        var f:NumberFormat = DecimalFormat("00")
        var str = "$m:${f.format(s)}"

        if(h>0)
            str = "$h:$str"
        return str
    }
}