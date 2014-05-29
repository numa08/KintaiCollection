package net.numa08.kintaicollection.app.domein;

import android.content.AsyncTaskLoader;
import android.content.Context;

import net.numa08.kintaicollection.app.dummy.DummyContent;

import java.util.List;

public class KintaiTimelineLoader extends AsyncTaskLoader<List<DummyContent.DummyItem>>{

    public KintaiTimelineLoader(Context context) {
        super(context);
    }

    @Override
    public List<DummyContent.DummyItem> loadInBackground() {
        return null;
    }
}
