package me.odie.heylisten;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.Random;

/**
 * Created by mikeodom on 3/23/16.
 */
@EViewGroup(R.layout.list_item)
public class ListItem extends FrameLayout {
    MainActivity.Sound mSound;

    @ViewById(R.id.name_text_view)
    TextView mTextView;

    @ViewById(R.id.frequency_seek_bar)
    SeekBarWithLabel mSeekBar;

    static ListItem getInstance(Context context) {
        return ListItem_.build(context);
    }

    public ListItem(final Context context) {
        super(context);
    }

    public ListItem(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public ListItem(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ListItem(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setSound(MainActivity.Sound sound) {
        mSeekBar.setMax(100);

        mSound = sound;

        mTextView.setText(mSound.name());

        mSeekBar.setKey(mSound.getPrefsKey());
    }


    @Click(R.id.play_button)
    protected void playButton(View button) {

        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), mSound.getRandom());

        mediaPlayer.start();
    }

}
