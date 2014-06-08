package net.numa08.kintaicollection.app.views;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import net.numa08.kintaicollection.app.R;

public abstract class ProgressActivity extends Activity {

    private ProgressBar mProgressBar;

    @Override
    public void setContentView(View view) {
        init().addView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        getLayoutInflater().inflate(layoutResID, init(), true);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        init().addView(view, params);
    }

    private ViewGroup init() {
        super.setContentView(R.layout.progress_activity);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        return (ViewGroup)findViewById(R.id.activityFrame);
    }

    public void setActivityProgressBarVisible(boolean isVisible) {
        if (isVisible) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void setActivityProgress(int progress) {
        mProgressBar.setProgress(progress);
    }
}
