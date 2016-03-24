package me.odie.heylisten;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by mikeodom on 3/23/16.
 */
@EViewGroup(R.layout.seek_bar)
public class SeekBarWithLabel extends FrameLayout {
    @ViewById(R.id.seek_bar)
    SeekBar mSeekBar;

    @ViewById(R.id.text_label)
    TextView mTextLabel;

    String mKey;

    public SeekBarWithLabel(final Context context) {
        super(context);
    }

    public SeekBarWithLabel(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public SeekBarWithLabel(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SeekBarWithLabel(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setMax(int max) {
        mSeekBar.setMax(max);
    }

    public int getMax() {
        return mSeekBar.getMax();
    }

    public int getProgress() {
        return mSeekBar.getProgress();
    }

    public void setKey(String key) {
        mKey = key;

        SharedPreferences preferences = MainActivity.getSharedPreferences(getContext());
        mSeekBar.setProgress(preferences.getInt(key, 10));
    }

    @AfterViews
    protected void UpdateAfterViewBinding() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
                mTextLabel.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                SharedPreferences preferences = MainActivity.getSharedPreferences(getContext());

                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(mKey, seekBar.getProgress());
                editor.apply();
            }
        });
    }

}
