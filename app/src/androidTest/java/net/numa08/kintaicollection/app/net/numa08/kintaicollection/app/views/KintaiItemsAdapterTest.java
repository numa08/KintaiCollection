package net.numa08.kintaicollection.app.net.numa08.kintaicollection.app.views;

import android.test.AndroidTestCase;

import com.android.volley.toolbox.Volley;

import net.numa08.kintaicollection.app.models.timeline.Kintai;
import net.numa08.kintaicollection.app.models.timeline.KintaiTimelineItem;
import net.numa08.kintaicollection.app.models.timeline.User;
import net.numa08.kintaicollection.app.views.KintaiItemsAdapter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class KintaiItemsAdapterTest extends AndroidTestCase {

    private KintaiItemsAdapter mAdapter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mAdapter = new KintaiItemsAdapter(getContext(), Volley.newRequestQueue(getContext()));
    }

    public void testAddOneObject() throws Exception {
        final KintaiTimelineItem item = new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date()));
        mAdapter.add(item);
        assertThat(mAdapter.getCount(), is(1));
    }

    public void testAddSameObject() throws Exception {
        final KintaiTimelineItem item = new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date()));
        mAdapter.add(item);
        mAdapter.add(item);
        assertThat(mAdapter.getCount(), is(1));
    }

    public void testAddObjectArray() throws Exception {
        final List<KintaiTimelineItem> items = Arrays.asList(
                new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date())),
                new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date())),
                new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date()))
        );
        mAdapter.addAll(items);
        assertThat(mAdapter.getCount(), is(3));
    }

    public void testaddSameObjectArray() throws Exception {
        final List<KintaiTimelineItem> items = Arrays.asList(
                new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date())),
                new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date())),
                new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date()))
        );
        mAdapter.addAll(items);
        mAdapter.addAll(items);
        assertThat(mAdapter.getCount(), is(3));
    }
}
