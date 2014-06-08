package net.numa08.kintaicollection.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.microsoft.windowsazure.mobileservices.ApiJsonOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import net.numa08.kintaicollection.app.models.azure.MobileService;
import net.numa08.kintaicollection.app.views.ProgressActivity;

import org.apache.http.client.methods.HttpPut;

import java.util.logging.Handler;

import fj.Effect;
import fj.F;
import fj.P2;
import fj.data.Either;
import fj.data.List;
import fj.data.Option;

public class KintaiReportFragment extends Fragment implements ApiJsonOperationCallback {

    private Option<MobileServiceClient> client = Option.none();
    private Option<View> taisyaButton = Option.none();
    private Option<View> syussyaButton = Option.none();
    private int mProgress = 0;
    private volatile boolean isProgressRunning = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kintai_report, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taisyaButton = Option.fromNull(view.findViewById(R.id.taisyaButton))
                             .map(new F<View, View>() {
                                 @Override
                                 public View f(View view) {
                                     view.setOnClickListener(tasyaButtonClicked);
                                     return view;
                                 }});
        syussyaButton = Option.fromNull(view.findViewById(R.id.syussyaButton))
                              .map(new F<View, View>() {
                                  @Override
                                  public View f(View view) {
                                      view.setOnClickListener(syussyaButtonClicked);
                                      return view;
                                  }});
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final MobileService service = new MobileService(Option.fromNull((Context)getActivity()));
        client = service.user()
                        .bindProduct(service.client().right().toOption())
                        .map(new F<P2<MobileServiceUser, MobileServiceClient>, MobileServiceClient>() {
                            @Override
                            public MobileServiceClient f(P2<MobileServiceUser, MobileServiceClient> product) {
                                product._2().setCurrentUser(product._1());
                                return product._2();
                            }
                        });

    }



    private final View.OnClickListener syussyaButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            List.list(taisyaButton, syussyaButton)
                .foreach(new Effect<Option<View>>() {
                    @Override
                    public void e(Option<View> view) {
                        view.foreach(new Effect<View>() {
                            @Override
                            public void e(View view) {
                                view.setEnabled(false);
                            }
                        });
                    }
                });
            startProgress();
            client.foreach(new Effect<MobileServiceClient>() {
                @Override
                public void e(MobileServiceClient client) {
                    client.invokeApi("report/syussya", HttpPut.METHOD_NAME, null, KintaiReportFragment.this);
                }
            });
        }
    };

    private final View.OnClickListener tasyaButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            List.list(taisyaButton, syussyaButton)
                .foreach(new Effect<Option<View>>() {
                    @Override
                    public void e(Option<View> view) {
                        view.foreach(new Effect<View>() {
                            @Override
                            public void e(View view) {
                                view.setEnabled(false);
                            }
                        });
                    }
                });
            startProgress();
            client.foreach(new Effect<MobileServiceClient>() {
                @Override
                public void e(MobileServiceClient client) {
                    client.invokeApi("report/taisya", HttpPut.METHOD_NAME, null, KintaiReportFragment.this);
                }
            });
        }
    };

    @Override
    public void onCompleted(JsonElement jsonObject, Exception exception, ServiceFilterResponse response) {
        List.list(syussyaButton, taisyaButton)
            .foreach(new Effect<Option<View>>() {
                @Override
                public void e(Option<View> view) {
                    view.foreach(new Effect<View>() {
                        @Override
                        public void e(View view) {
                            view.setEnabled(true);
                        }
                    });
                }
            });
        stopProgress();
        final Either<Exception, JsonElement> either;
        if (exception == null) {
            either = Either.right(jsonObject);
        } else {
            either = Either.left(exception);
        }

        either.right()
              .toOption()
              .bindProduct(Option.fromNull(getActivity()))
              .foreach(new Effect<P2<JsonElement, Activity>>() {
                  @Override
                  public void e(P2<JsonElement, Activity> product) {
                      Toast.makeText(product._2(), "Report Ok", Toast.LENGTH_LONG).show();
                      Log.d(getString(R.string.app_name), product._2().toString());
                      final Intent intent = new Intent();
                      intent.setAction(MainActivity.Action.UPDATE_KINTAI_TIMELINE);
                      product._2().sendBroadcast(intent);
                  }
              });
        either.left()
              .toOption()
              .bindProduct(Option.fromNull(getActivity()))
              .foreach(new Effect<P2<Exception, Activity>>() {
                  @Override
                  public void e(P2<Exception, Activity> product) {
                      Toast.makeText(product._2(), "Report Failed", Toast.LENGTH_LONG).show();
                      Log.e(getString(R.string.app_name), "Failed PUT", product._1());
                  }
              });
    }

    private void startProgress() {
        mProgress = 0;
        isProgressRunning = true;
        Option.fromNull(getActivity())
                .map(new F<Activity, Either<? extends Exception, ProgressActivity>>() {
                    @Override
                    public Either<? extends Exception, ProgressActivity> f(Activity activity) {
                        Either<? extends Exception, ProgressActivity> eitherActivity;
                        try {
                            eitherActivity = Either.right((ProgressActivity)activity);
                        } catch (ClassCastException e) {
                            eitherActivity = Either.left(e);
                        }
                        return eitherActivity;
                    }})
                .orSome(Either.<Exception, ProgressActivity>left(new Exception("Activity is null")))
                .right()
                .toOption()
                .map(new F<ProgressActivity, Thread>() {
                    @Override
                    public Thread f(final ProgressActivity progressActivity) {
                        progressActivity.setActivityProgressBarVisible(true);
                        progressActivity.setActivityProgress(0);
                        return new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(isProgressRunning) {
                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    progressActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mProgress += 5;
                                            progressActivity.setActivityProgress(mProgress);
                                            Log.d(getString(R.string.app_name), "Progress is " + mProgress);
                                        }
                                    });
                                }
                            }
                        });
                    }})
                    .foreach(new Effect<Thread>() {
                        @Override
                        public void e(Thread thread) {
                            thread.start();
                        }
                    });
    }

    private void stopProgress() {
        isProgressRunning = false;
        mProgress = 0;
        Option.fromNull(getActivity())
                .map(new F<Activity, Either<? extends Exception, ProgressActivity>>() {
                    @Override
                    public Either<? extends Exception, ProgressActivity> f(Activity activity) {
                        Either<? extends Exception, ProgressActivity> eitherActivity;
                        try {
                            eitherActivity = Either.right((ProgressActivity)activity);
                        } catch (ClassCastException e) {
                            eitherActivity = Either.left(e);
                        }
                        return eitherActivity;
                    }})
                .orSome(Either.<Exception, ProgressActivity>left(new Exception("Activity is null")))
                .right()
                .foreach(new Effect<ProgressActivity>() {
                    @Override
                    public void e(final ProgressActivity progressActivity) {
                        progressActivity.setActivityProgress(100);
                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressActivity.setActivityProgressBarVisible(false);
                            }
                        }, 500);
                    }
                });
    }
}
