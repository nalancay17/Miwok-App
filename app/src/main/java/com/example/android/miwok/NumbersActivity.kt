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

class NumbersActivity : AppCompatActivity() {

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
        val words = createNumbersCollection()

        val itemsAdapter = WordAdapter(this, words, R.color.category_numbers)
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

    private fun createNumbersCollection(): List<Word> {
        val words = ArrayList<Word>()
        words.add(Word("one", "lutti", R.drawable.number_one, R.raw.number_one))
        words.add(Word("two", "otiiko", R.drawable.number_two, R.raw.number_two))
        words.add(Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three))
        words.add(Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four))
        words.add(Word("five", "massokka", R.drawable.number_five, R.raw.number_five))
        words.add(Word("six", "temmokka", R.drawable.number_six, R.raw.number_six))
        words.add(Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven))
        words.add(Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight))
        words.add(Word("nine", "wo´e", R.drawable.number_nine, R.raw.number_nine))
        words.add(Word("ten", "na´aacha", R.drawable.number_ten, R.raw.number_ten))
        return words
    }
}