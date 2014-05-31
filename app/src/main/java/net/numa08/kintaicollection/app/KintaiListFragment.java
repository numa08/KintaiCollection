package net.numa08.kintaicollection.app;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import net.numa08.kintaicollection.app.domein.KintaiTimelineLoader;
import net.numa08.kintaicollection.app.dummy.DummyContent;
import net.numa08.kintaicollection.app.models.timeline.Kintai;
import net.numa08.kintaicollection.app.models.timeline.KintaiTimelineItem;

import java.util.List;

import fj.Effect;
import fj.F;
import fj.P2;
import fj.data.Option;

public class KintaiListFragment extends ListFragment implements AbsListView.OnItemClickListener ,LoaderManager.LoaderCallbacks<List<KintaiTimelineItem>>{

    private Option<ArrayAdapter<KintaiTimelineItem>> adapter = Option.none();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}

    @Override
    public Loader<List<KintaiTimelineItem>> onCreateLoader(int id, Bundle args) {
        final Option<Loader<List<KintaiTimelineItem>>> loader = Option.fromNull(getActivity()).map(new F<Activity, Loader<List<KintaiTimelineItem>>>() {
            @Override
            public Loader<List<KintaiTimelineItem>> f(Activity activity) {
                return new KintaiTimelineLoader(activity);
            }
        });
        if (loader.isSome()) {
            return loader.some();
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<List<KintaiTimelineItem>> loader, List<KintaiTimelineItem> data) {
        adapter.bindProduct(Option.fromNull(data)).foreach(new Effect<P2<ArrayAdapter<KintaiTimelineItem>, List<KintaiTimelineItem>>>() {
            @Override
            public void e(P2<ArrayAdapter<KintaiTimelineItem>, List<KintaiTimelineItem>> product) {
                product._1().addAll(product._2());
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<List<KintaiTimelineItem>> loader) {}
}
