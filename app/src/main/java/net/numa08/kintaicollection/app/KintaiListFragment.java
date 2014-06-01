package net.numa08.kintaicollection.app;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import net.numa08.kintaicollection.app.domein.KintaiTimelineLoader;
import net.numa08.kintaicollection.app.dummy.DummyContent;
import net.numa08.kintaicollection.app.models.timeline.Kintai;
import net.numa08.kintaicollection.app.models.timeline.KintaiTimelineItem;
import net.numa08.kintaicollection.app.views.KintaiItemsAdapter;

import java.util.List;

import fj.Effect;
import fj.F;
import fj.P;
import fj.P2;
import fj.data.Option;

public class KintaiListFragment extends ListFragment implements AbsListView.OnItemClickListener ,LoaderManager.LoaderCallbacks<List<KintaiTimelineItem>>{

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Option.fromNull(getActivity()).map(new F<Activity, P2<Activity, RequestQueue>>() {
            @Override
            public P2<Activity, RequestQueue> f(Activity activity) {
                final RequestQueue queue = Volley.newRequestQueue(activity);
                return P.p(activity, queue);
            }
        }).map(new F<P2<Activity, RequestQueue>, ArrayAdapter>() {
            @Override
            public ArrayAdapter f(P2<Activity, RequestQueue> product) {
                return new KintaiItemsAdapter(product._1(), product._2());
            }
        }).foreach(new Effect<ArrayAdapter>() {
            @Override
            public void e(ArrayAdapter arrayAdapter) {
                setListAdapter(arrayAdapter);
            }
        });

        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Option.fromNull(getActivity())
              .foreach(new Effect<Activity>() {
                  @Override
                  public void e(Activity activity) {
                      final IntentFilter filter = new IntentFilter();
                      filter.addAction(MainActivity.Action.UPDATE_KINTAI_TIMELINE);
                      activity.registerReceiver(receiveTimelineUpdate,filter);
                  }
              });
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
            loader.some().forceLoad();
            return loader.some();
        } else {
            return null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Option.fromNull(getActivity())
              .foreach(new Effect<Activity>() {
                  @Override
                  public void e(Activity activity) {
                      activity.unregisterReceiver(receiveTimelineUpdate);
                  }
              });
    }

    @Override
    public void onLoadFinished(Loader<List<KintaiTimelineItem>> loader, List<KintaiTimelineItem> data) {
        Option.fromNull(getListAdapter()).filter(new F<ListAdapter, Boolean>() {
            @Override
            public Boolean f(ListAdapter listAdapter) {
                return listAdapter instanceof KintaiItemsAdapter;
            }
        }).map(new F<ListAdapter, KintaiItemsAdapter>() {
            @Override
            public KintaiItemsAdapter f(ListAdapter listAdapter) {
                return (KintaiItemsAdapter)listAdapter;
            }
        }).bindProduct(Option.fromNull(data))
          .foreach(new Effect<P2<KintaiItemsAdapter, List<KintaiTimelineItem>>>() {
              @Override
              public void e(P2<KintaiItemsAdapter, List<KintaiTimelineItem>> product) {
                  product._1().addAll(product._2());
              }
          });
    }

    @Override
    public void onLoaderReset(Loader<List<KintaiTimelineItem>> loader) {}

    private final BroadcastReceiver receiveTimelineUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            KintaiListFragment.this.getLoaderManager().restartLoader(0, null, KintaiListFragment.this);
        }
    };
}
