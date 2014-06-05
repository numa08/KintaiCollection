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

public class KintaiListFragment extends ListFragment implements AbsListView.OnItemClickListener {

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

    private final BroadcastReceiver receiveTimelineUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };
}
