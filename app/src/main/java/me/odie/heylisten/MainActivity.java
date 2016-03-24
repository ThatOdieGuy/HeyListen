package me.odie.heylisten;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "mainSharedPrefs";

    @ViewById(R.id.list_view)
    ListView mListView;

    @ViewById(R.id.min_seek_bar)
    SeekBarWithLabel mMinSeekBar;

    @ViewById(R.id.max_seek_bar)
    SeekBarWithLabel mMaxSeekBar;

    @ViewById(R.id.go_button)
    Button mGoButton;

    volatile boolean mRunning;

    enum Sound {
        Hey("hey", 5),
        Listen("listen", 5),
        Hello("hello", 5),
        Bonk("bonk", 1),
        Float("float", 1),
        In("in", 1),
        Out("out", 1),
        WatchOut("watchout", 5),
        Eep("eep", 1);

        String mName;
        int mCount;
        List<Integer> mResources;

        static Random sRandom = new Random();

        Sound(String name, int count) {
            mName = name;
            mCount = count;
        }

        int getRandom() {
            return mResources.get(sRandom.nextInt(mResources.size()));
        }

        String getPrefsKey() {
            return "sound_" + mName;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    Handler mHandler = new Handler(Looper.getMainLooper());

    static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupSounds();
    }

    void setupButton() {
        if (mRunning) {
            mGoButton.setText("Stop");
        } else {
            mGoButton.setText("Go");
        }
    }

    @Click(R.id.go_button)
    void goButtonClicked(View button) {
        if (mRunning) {
            mHandler.removeCallbacks(mMainRunnable);
        } else {
            mHandler.post(mMainRunnable);
        }

        mRunning = !mRunning;

        setupButton();
    }

    Random mRandom = new Random();
    Runnable mMainRunnable = new Runnable() {
        @Override
        public void run() {
            RandomCollection<Sound> collection = new RandomCollection<>();

            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.this);

            for (Sound sound : Sound.values()) {
                int weight = sharedPreferences.getInt(sound.getPrefsKey(), 0);

                collection.add(weight, sound);
            }

            Sound sound = collection.next();

            if (sound != null) {
                MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, sound.getRandom());

                mediaPlayer.start();
            }

            //TODO: Get random next start time.
            int min = mMinSeekBar.getProgress();
            int max = mMaxSeekBar.getProgress();

            max = Math.max(min + 1, max);
            int nextTime = mRandom.nextInt(max - min) + min;

            nextTime = Math.max(1, nextTime);

            mHandler.postDelayed(this, nextTime * 1000);
        }
    };

    @AfterViews
    void UpdateAfterViewBinding() {
        setupButton();

        mListView.setAdapter(new SoundsAdapter());

        mMinSeekBar.setMax(600);
        mMinSeekBar.setKey("seconds_min");
        mMaxSeekBar.setMax(600);
        mMaxSeekBar.setKey("seconds_max");
    }

    private void setupSounds() {
        for (Sound sound : Sound.values()) {
            List<Integer> sounds = new ArrayList<>();

            if (sound.mCount == 1) {
                sounds.add(getIdentifier(sound.mName));
            } else {
                for (int t = 0; t < sound.mCount; t++) {
                    sounds.add(getIdentifier(sound.mName + (t + 1)));
                }
            }

            sound.mResources = sounds;
        }
    }

    private int getIdentifier(String fileName) {
        return getResources().getIdentifier("navi_" + fileName, "raw", getPackageName());
    }

    class SoundsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return Sound.values().length;
        }

        @Override
        public Object getItem(final int position) {
            return Sound.values()[position];
        }

        @Override
        public long getItemId(final int position) {
            return 0;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            ListItem listItem =  ListItem.getInstance(MainActivity.this);

            listItem.setSound(Sound.values()[position]);

            return listItem;
        }
    }
}
