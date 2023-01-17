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

public class FamilyActivity extends AppCompatActivity {

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
        List<Word> words = createFamilyCollection();

        WordAdapter itemsAdapter = new WordAdapter(this, words, R.color.category_family);
        ListView listView = findViewById(R.id.wordsList);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            Word currentWord = words.get(position);

            if (makeFocusRequest() == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                // Release player if it currently exits to play a different sound file
                releaseMediaPlayer();
                player = MediaPlayer.create(FamilyActivity.this, currentWord.getAudioResourceId());
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

    private List<Word> createFamilyCollection() {
        List<Word> words = new ArrayList<>();
        words.add(new Word("father", "әpә", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("mother", "әṭa", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("son", "angsi", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("older brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("younger brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("older sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("younger sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word("grandmother", "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word("grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather));
        return words;
    }
}