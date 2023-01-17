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

class FamilyActivity : AppCompatActivity() {

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
        val words = createFamilyCollection()

        val itemsAdapter = WordAdapter(this, words, R.color.category_family)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && this::focusRequest.isInitialized)
            audioManager.abandonAudioFocusRequest(focusRequest)
        else
            audioManager.abandonAudioFocus(focusChangeListener)
    }

    private fun createFamilyCollection(): List<Word> {
        val words: MutableList<Word> = ArrayList()
        words.add(Word("father", "әpә", R.drawable.family_father, R.raw.family_father))
        words.add(Word("mother", "әṭa", R.drawable.family_mother, R.raw.family_mother))
        words.add(Word("son", "angsi", R.drawable.family_son, R.raw.family_son))
        words.add(Word("daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter))
        words.add(
            Word(
                "older brother",
                "taachi",
                R.drawable.family_older_brother,
                R.raw.family_older_brother
            )
        )
        words.add(
            Word(
                "younger brother",
                "chalitti",
                R.drawable.family_younger_brother,
                R.raw.family_younger_brother
            )
        )
        words.add(
            Word(
                "older sister",
                "teṭe",
                R.drawable.family_older_sister,
                R.raw.family_older_sister
            )
        )
        words.add(
            Word(
                "younger sister",
                "kolliti",
                R.drawable.family_younger_sister,
                R.raw.family_younger_sister
            )
        )
        words.add(
            Word(
                "grandmother",
                "ama",
                R.drawable.family_grandmother,
                R.raw.family_grandmother
            )
        )
        words.add(
            Word(
                "grandfather",
                "paapa",
                R.drawable.family_grandfather,
                R.raw.family_grandfather
            )
        )
        return words
    }
}