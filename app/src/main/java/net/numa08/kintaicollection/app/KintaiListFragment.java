package net.numa08.kintaicollection.app;

import android.app.Activity;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonElement;
import com.microsoft.windowsazure.mobileservices.ApiJsonOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import net.numa08.kintaicollection.app.views.KintaiItemsAdapter;

import org.apache.http.client.methods.HttpGet;

import java.net.MalformedURLException;

import fj.Effect;
import fj.F;
import fj.P;
import fj.P2;
import fj.data.Either;
import fj.data.Option;

public class KintaiListFragment extends ListFragment implements AbsListView.OnItemClickListener , ApiJsonOperationCallback {

    Option<MobileServiceClient> client = Option.none();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Either<Exception, MobileServiceClient> eitherClient = Option.fromNull(getActivity())
                       .map(new F<Activity, P2<Activity, MobileServiceUser>>() {
                           @Override
                           public P2<Activity, MobileServiceUser> f(Activity activity) {
                               final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
                               final String id = pref.getString(AuthenticateActivity.KEY_USER_ID, "");
                               final String token = pref.getString(AuthenticateActivity.KEY_USER_TOKEN, "");
                               final MobileServiceUser user = new MobileServiceUser(id);
                               user.setAuthenticationToken(token);
                               return P.p(activity, user);
                           }})
                       .map(new F<P2<Activity, MobileServiceUser>, Either<Exception, MobileServiceClient>>() {
                           @Override
                           public Either<Exception, MobileServiceClient> f(P2<Activity, MobileServiceUser> production) {
                               Either<Exception, MobileServiceClient> eitherClient;
                               try {
                                   final MobileServiceClient client = new MobileServiceClient(getString(R.string.azure_endpoint), getString(R.string.azure_key), production._1());
                                   client.setCurrentUser(production._2());
                                   eitherClient = Either.right(client);
                               } catch (MalformedURLException e) {
                                   e.printStackTrace();
                                   eitherClient = Either.left(new Exception(e));
                               }
                               return eitherClient;
                           }})
                       .orSome(Either.<Exception, MobileServiceClient>left(new Exception("Activity is not created")));
        client = eitherClient.right().toOption();

        Option.fromNull(getActivity()).map(new F<Activity, P2<Activity, RequestQueue>>() {
                                            @Override
                                            public P2<Activity, RequestQueue> f(Activity activity) {
                                                final RequestQueue queue = Volley.newRequestQueue(activity);
                                                return P.p(activity, queue);
                                            }})
                                      .map(new F<P2<Activity, RequestQueue>, ArrayAdapter>() {
                                          @Override
                                          public ArrayAdapter f(P2<Activity, RequestQueue> product) {
                                              return new KintaiItemsAdapter(product._1(), product._2());
                                          }
                                      })
                                      .foreach(new Effect<ArrayAdapter>() {
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
                      activity.registerReceiver(receiveTimelineUpdate, filter);
                  }
              });
        updateTimeline();
    }

    private void updateTimeline() {
        client.foreach(new Effect<MobileServiceClient>() {
            @Override
            public void e(MobileServiceClient client) {
                client.invokeApi("timeline", null, HttpGet.METHOD_NAME, null, KintaiListFragment.this);
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
            updateTimeline();
        }
    };

    @Override
    public void onCompleted(JsonElement jsonElement, Exception e, ServiceFilterResponse serviceFilterResponse) {
        final Either<Exception, JsonElement> either;
        if (e == null) {
            either = Either.right(jsonElement);
        } else {
            either = Either.left(e);
        }
        either.right().foreach(new Effect<JsonElement>() {
            @Override
            public void e(JsonElement jsonElement) {
                Log.d(getString(R.string.app_name), jsonElement.toString());
            }});
        either.left()
              .toOption()
              .map(new F<Exception, Activity>() {
                    @Override
                    public Activity f(Exception e) {
                        Log.e(getString(R.string.app_name), "Failed Get", e);
                        return getActivity();
                    }})
              .foreach(new Effect<Activity>() {
                  @Override
                  public void e(Activity activity) {
                      Toast.makeText(activity, "Failed Get", Toast.LENGTH_LONG).show();
                  }
              });
    }
}
