package net.numa08.kintaicollection.app.domein;

import android.content.AsyncTaskLoader;
import android.content.Context;

import net.numa08.kintaicollection.app.models.timeline.Kintai;
import net.numa08.kintaicollection.app.models.timeline.KintaiTimelineItem;
import net.numa08.kintaicollection.app.models.timeline.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class KintaiTimelineLoader extends AsyncTaskLoader<List<KintaiTimelineItem>>{

    public KintaiTimelineLoader(Context context) {
        super(context);
    }

    @Override
    public List<KintaiTimelineItem> loadInBackground() {
        return new ArrayList<>();
    }
}
