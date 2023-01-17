package com.example.android.miwok

import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ColorsActivity : AppCompatActivity() {

    private lateinit var player: MediaPlayer
    private val completionListener = OnCompletionListener {
        releaseMediaPlayer()
    }

    private lateinit var audioManager: AudioManager
    private lateinit var focusRequest: AudioFocusRequest
    private val attributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()
    private val focusChangeListener = OnAudioFocusChangeListener { focusChange: Int ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> player.start()
            AudioManager.AUDIOFOCUS_LOSS -> releaseMediaPlayer()
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT, AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> player.pause()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.word_list)

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        val words = createColorsCollection()

        val itemsAdapter = WordAdapter(this, words, R.color.category_colors)
        val listView = findViewById<ListView>(R.id.wordsList)
        listView.adapter = itemsAdapter

        listView.onItemClickListener =
            OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                val currentWord = words[position]
                if (makeFocusRequest() == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Release player if it currently exits to play a different sound file
                    releaseMediaPlayer()
                    player = MediaPlayer.create(this, currentWord.audioResourceId)
                    player.start()
                    player.setOnCompletionListener(completionListener)
                }
            }
    }

    override fun onStop() {
        super.onStop()
        // Media player resource releasing when activity enters in stopped state
        releaseMediaPlayer()
    }

    private fun makeFocusRequest(): Int {
        // request for api >= 26
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                .setAudioAttributes(attributes)
                .setOnAudioFocusChangeListener(focusChangeListener)
                .build()
            audioManager.requestAudioFocus(focusRequest)
        } else {
            audioManager.requestAudioFocus(
                focusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            )
        }
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private fun releaseMediaPlayer() {
        if (this::player.isInitialized)
            player.release()

        // Abandon audio focus depending on device version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            audioManager.abandonAudioFocusRequest(focusRequest)
        else
            audioManager.abandonAudioFocus(focusChangeListener)
    }

    private fun createColorsCollection(): List<Word> {
        val words: MutableList<Word> = ArrayList()
        words.add(Word("red", "weṭeṭṭi", R.drawable.color_red, R.raw.color_red))
        words.add(Word("green", "chokokki", R.drawable.color_green, R.raw.color_green))
        words.add(Word("brown", "ṭakaakki", R.drawable.color_brown, R.raw.color_brown))
        words.add(Word("gray", "ṭopoppi", R.drawable.color_gray, R.raw.color_gray))
        words.add(Word("black", "kululli", R.drawable.color_black, R.raw.color_black))
        words.add(Word("white", "kelelli", R.drawable.color_white, R.raw.color_white))
        words.add(
            Word(
                "dusty yellow",
                "ṭopiisә",
                R.drawable.color_dusty_yellow,
                R.raw.color_dusty_yellow
            )
        )
        words.add(
            Word(
                "mustard yellow",
                "chiwiiṭә",
                R.drawable.color_mustard_yellow,
                R.raw.color_mustard_yellow
            )
        )
        return words
    }
}