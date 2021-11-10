package com.example.android.miwok;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {
    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;



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

    public NumbersActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        ArrayList<Word> englishWords = new ArrayList<Word>();
        englishWords.add(new Word("One", "lutti", R.drawable.number_one, R.raw.number_one));
        englishWords.add(new Word("Two", "otiiko", R.drawable.number_two, R.raw.number_two));
        englishWords.add(new Word("Three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        englishWords.add(new Word("Four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        englishWords.add(new Word("Five", "massokka", R.drawable.number_five, R.raw.number_five));
        englishWords.add(new Word("Six", "temmokka", R.drawable.number_six, R.raw.number_six));
        englishWords.add(new Word("Seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        englishWords.add(new Word("Eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        englishWords.add(new Word("Nine", "wo'e", R.drawable.number_nine, R.raw.number_nine));
        englishWords.add(new Word("Ten", "na'aacha", R.drawable.number_ten, R.raw.number_ten));


        WordAdapter Adapter = new WordAdapter(this, englishWords, R.color.category_numbers);
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

                mMediaPlayer = MediaPlayer.create(NumbersActivity.this,englishWords.get(position).getmAudio());

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



    /**
     * Clean up the media player by releasing its resources.
     */
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
