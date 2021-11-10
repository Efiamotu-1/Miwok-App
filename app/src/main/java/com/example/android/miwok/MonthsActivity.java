package com.example.android.miwok;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MonthsActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;

    private AudioManager mAudioManager;

    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);

            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();

            }
            else if ( focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mMediaPlayer.start();

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        mAudioManager =(AudioManager) getSystemService(AUDIO_SERVICE);

        ArrayList <Word> englishWords = new ArrayList<Word>();
        englishWords.add(new Word("January", "lutti", R.raw.sample_music));
        englishWords.add(new Word ("February", "otiiko", R.raw.sample_music));
        englishWords.add(new Word("March", "tolookosu", R.raw.sample_music));
        englishWords.add(new Word ("April", "oyyisa", R.raw.sample_music));
        englishWords.add(new Word ("May", "massokka", R.raw.sample_music));
        englishWords.add(new Word("June", "temmokka", R.raw.sample_music));
        englishWords.add(new Word("July", "kenekaku", R.raw.sample_music));
        englishWords.add(new Word("August", "kawinta", R.raw.sample_music));
        englishWords.add(new Word("September", "wo'e", R.raw.sample_music));
        englishWords.add(new Word("October","na'aacha", R.raw.sample_music));
        englishWords.add(new Word("November","na'aacha", R.raw.sample_music));
        englishWords.add(new Word("December","na'aacha", R.raw.sample_music));

        WordAdapter Adapter = new WordAdapter(this, englishWords, R.color.category_months);
        ListView listView = findViewById((R.id.list));
        listView.setAdapter(Adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                int result = mAudioManager.requestAudioFocus(audioFocusChangeListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if ( result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){

                    mMediaPlayer = MediaPlayer.create(MonthsActivity.this,englishWords.get(position).getmAudio());

                    mMediaPlayer.start();

                    mMediaPlayer.setOnCompletionListener(completionListener);
                }
            }

        });

    }
    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            mAudioManager.abandonAudioFocus(audioFocusChangeListener);
        }
    }
}