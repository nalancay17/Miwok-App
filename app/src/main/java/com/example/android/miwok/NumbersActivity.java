package com.example.android.miwok;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NumbersActivity extends AppCompatActivity {

    private MediaPlayer player;
    private final MediaPlayer.OnCompletionListener completionListener = completion -> releaseMediaPlayer();

    private AudioManager audioManager;
    private AudioFocusRequest focusRequest;
    private final AudioAttributes attributes = new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build();
    private final AudioManager.OnAudioFocusChangeListener focusChangeListener = focusChange -> {
        if (focusChange == AudioManager.AUDIOFOCUS_GAIN)
            player.start();
        else if (focusChange == AudioManager.AUDIOFOCUS_LOSS)
            releaseMediaPlayer();
        else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
            player.pause();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        List<Word> words = createNumbersCollection();

        WordAdapter itemsAdapter = new WordAdapter(this, words, R.color.category_numbers);
        ListView listView = findViewById(R.id.wordsList);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            Word currentWord = words.get(position);

            if (makeFocusRequest() == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                // Release player if it currently exits to play a different sound file
                releaseMediaPlayer();
                player = MediaPlayer.create(NumbersActivity.this, currentWord.getAudioResourceId());
                player.start();
                player.setOnCompletionListener(completionListener);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Media player resource releasing when activity enters in stopped state
        releaseMediaPlayer();
    }

    private int makeFocusRequest() {
        // request for api >= 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                    .setAudioAttributes(attributes)
                    .setOnAudioFocusChangeListener(focusChangeListener)
                    .build();
            return audioManager.requestAudioFocus(focusRequest);
        }
        // request for api < 26. deprecated but needed
        else
            return audioManager.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        if (player != null) {
            player.release();

            // For our code, we've decided that setting the media player to null is an easy way to
            // tell that the media player is not configured to play an audio file at the moment.
            player = null;

            // Abandon audio focus depending on device version
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                audioManager.abandonAudioFocusRequest(focusRequest);
            else
                audioManager.abandonAudioFocus(focusChangeListener);
        }
    }

    private List<Word> createNumbersCollection() {
        ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine", "wo´e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten", "na´aacha", R.drawable.number_ten, R.raw.number_ten));
        return words;
    }
}