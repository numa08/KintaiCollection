package net.numa08.kintaicollection.app.net.numa08.kintaicollection.app.views;

import android.test.AndroidTestCase;

import com.android.volley.toolbox.Volley;

import net.numa08.kintaicollection.app.models.timeline.Kintai;
import net.numa08.kintaicollection.app.models.timeline.KintaiTimelineItem;
import net.numa08.kintaicollection.app.models.timeline.User;
import net.numa08.kintaicollection.app.views.KintaiItemsAdapter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
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
                new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date(0))),
                new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date(1))),
                new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date(2)))
        );
        mAdapter.addAll(items);
        assertThat(mAdapter.getCount(), is(3));
    }

    public void testaddSameObjectArray() throws Exception {
        final List<KintaiTimelineItem> items = Arrays.asList(
                new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date(0))),
                new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date(1))),
                new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date(2)))
        );
        mAdapter.addAll(items);
        mAdapter.addAll(items);
        assertThat(mAdapter.getCount(), is(3));
    }

    public void testAddNewItem() throws Exception {
        final List<KintaiTimelineItem> items = Arrays.asList(
                new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date(0))),
                new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date(1))),
                new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date(2)))
        );
        mAdapter.addAll(items);
        final KintaiTimelineItem item = new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date(3)));
        mAdapter.add(item);
        assertThat(mAdapter.getCount(), is(4));
    }

    public void testAddNewItemOfArray() throws Exception {
        final List<KintaiTimelineItem> items = Arrays.asList(
                new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date(0))),
                new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date(1))),
                new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date(2)))
        );
        mAdapter.addAll(items);
        final List<KintaiTimelineItem> newItems = new ArrayList<>(items);
        newItems.add(new KintaiTimelineItem(new User("name", "id", "http://www.broccoli.co.jp/dejiko/15th/img/dejiko01.png"), new Kintai.Syussya(new Date(3))));
        mAdapter.addAll(newItems);
        assertThat(mAdapter.getCount(), is(4));

    }
}
