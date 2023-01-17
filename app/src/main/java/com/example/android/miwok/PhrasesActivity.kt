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

class PhrasesActivity : AppCompatActivity() {

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
        val words = createPhrasesCollection()

        val itemsAdapter = WordAdapter(this, words, R.color.category_phrases)
        val listView = findViewById<ListView>(R.id.wordsList)
        listView.adapter = itemsAdapter

        listView.onItemClickListener =
            OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                val currentWord = words[position]
                if (makeFocusRequest() == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Release player if it currently exits to play a different sound file
                    releaseMediaPlayer()
                    player = MediaPlayer.create(this@PhrasesActivity, currentWord.audioResourceId)
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

    private fun createPhrasesCollection(): List<Word> {
        val words: MutableList<Word> = ArrayList()
        words.add(Word("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going))
        words.add(Word("What is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name))
        words.add(Word("My name is...", "oyaaset...", R.raw.phrase_my_name_is))
        words.add(Word("How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling))
        words.add(Word("I'm feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good))
        words.add(Word("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming))
        words.add(Word("Yes, I'm coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming))
        words.add(Word("I'm coming.", "әәnәm", R.raw.phrase_im_coming))
        words.add(Word("Let's go.", "yoowutis", R.raw.phrase_lets_go))
        words.add(Word("Come here.", "әnni'nem", R.raw.phrase_come_here))
        return words
    }
}