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

import java.util.List;

import fj.F;
import fj.data.Option;

public class KintaiListFragmentFragment extends ListFragment implements AbsListView.OnItemClickListener ,LoaderManager.LoaderCallbacks<List<DummyContent.DummyItem>>{

    private AbsListView mListView;

    private ListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
               R.layout.row_kintai_timeline, R.id.workerName, DummyContent.ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kintailistfragment, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}

    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public Loader<List<DummyContent.DummyItem>> onCreateLoader(int id, Bundle args) {
        final Option<Loader<List<DummyContent.DummyItem>>> loader = Option.fromNull(getActivity()).map(new F<Activity, Loader<List<DummyContent.DummyItem>>>() {
            @Override
            public Loader<List<DummyContent.DummyItem>> f(Activity activity) {
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
    public void onLoadFinished(Loader<List<DummyContent.DummyItem>> loader, List<DummyContent.DummyItem> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<DummyContent.DummyItem>> loader) {

    }
}
