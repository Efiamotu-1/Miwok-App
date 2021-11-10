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

public class PhrasesActivity extends AppCompatActivity {

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

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);


        ArrayList<Word> englishWords = new ArrayList<Word>();
        englishWords.add(new Word("Where are you going", "mimto wuksus", R.raw.phrase_where_are_you_going));
        englishWords.add(new Word ("What is your name", "tinne oyaase'na", R.raw.phrase_what_is_your_name));
        englishWords.add(new Word("My name is...", "oyaaset...", R.raw.phrase_my_name_is));
        englishWords.add(new Word ("How are you feeling?", "michakses?", R.raw.phrase_how_are_you_feeling));
        englishWords.add(new Word ("I'm feeling good", "kuchi achit", R.raw.phrase_im_feeling_good));
        englishWords.add(new Word("Are you coming?", "aanas'aa?", R.raw.phrase_are_you_coming));
        englishWords.add(new Word("Yes, I'm coming", "haa' aanan", R.raw.phrase_yes_im_coming));
        englishWords.add(new Word("I'm coming", "aanam", R.raw.phrase_im_coming));
        englishWords.add(new Word("Let's go", "yoowutis", R.raw.phrase_lets_go));
        englishWords.add(new Word("Come here","anni'nem", R.raw.phrase_come_here));

        WordAdapter Adapter = new WordAdapter(this, englishWords, R.color.category_phrases);
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

                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this,englishWords.get(position).getmAudio());

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