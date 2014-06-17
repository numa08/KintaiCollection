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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.deploygate.sdk.DeployGate;
import com.google.gson.JsonElement;
import com.microsoft.windowsazure.mobileservices.ApiJsonOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import net.numa08.kintaicollection.app.models.azure.MobileService;

import org.apache.http.client.methods.HttpPut;

import java.lang.ref.WeakReference;

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
    private Option<ProgressBar> syussyaProgress = Option.none();
    private Option<ProgressBar> taisyaProgress = Option.none();

    private enum Operation {
        Syussya,
        Taisya;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kintai_report, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final WeakReference<KintaiReportFragment> fragment = new WeakReference<>(this);
        taisyaButton = Option.fromNull(view.findViewById(R.id.taisyaButton))
                             .map(new F<View, View>() {
                                 @Override
                                 public View f(View view) {
                                     view.setOnClickListener(new ReportButtonClickListener(fragment, "report/taisya"));
                                     return view;
                                 }});
        syussyaButton = Option.fromNull(view.findViewById(R.id.syussyaButton))
                              .map(new F<View, View>() {
                                  @Override
                                  public View f(View view) {
                                      view.setOnClickListener(new ReportButtonClickListener(fragment, "report/syussya"));
                                      return view;
                                  }});
        syussyaProgress = Option.fromNull((ProgressBar)view.findViewById(R.id.syussyaProgress));
        taisyaProgress = Option.fromNull((ProgressBar)view.findViewById(R.id.taisyaProgress));
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

    private final static class ReportButtonClickListener implements View.OnClickListener {

        private final WeakReference<KintaiReportFragment> parentFragment;
        private final String apiPath;

        private ReportButtonClickListener(WeakReference<KintaiReportFragment> parentFragment, String apiPath) {
            this.parentFragment = parentFragment;
            this.apiPath = apiPath;
        }

        @Override
        public void onClick(final View v) {
            final Option<KintaiReportFragment> fragment = Option.fromNull(parentFragment.get());
            fragment.foreach(new Effect<KintaiReportFragment>() {
                @Override
                public void e(final KintaiReportFragment f) {
                    List.list(f.taisyaButton, f.syussyaButton)
                        .foreach(new Effect<Option<View>>() {
                            @Override
                            public void e(Option<View> v) {
                                v.foreach(new Effect<View>() {
                                    @Override
                                    public void e(View view) {
                                        view.setEnabled(false);
                                    }
                                });

                            }
                        });
                    if (v.getId() == R.id.syussyaButton) {
                        f.startProgress(Operation.Syussya);
                    } else if (v.getId() == R.id.taisyaButton) {
                        f.startProgress(Operation.Taisya);
                    }
                    f.client.foreach(new Effect<MobileServiceClient>() {
                        @Override
                        public void e(MobileServiceClient client) {
                            client.invokeApi(apiPath, HttpPut.METHOD_NAME, null, f);
                        }
                    });
                }
            });
        }
    }

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
                      DeployGate.logDebug("Completed Kintai Report");
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

    private void startProgress(Operation operation) {
        final Option<ProgressBar> progress;
        if (operation == Operation.Syussya) {
            progress = syussyaProgress;
        } else {
            progress = taisyaProgress;
        }
        progress.foreach(new Effect<ProgressBar>() {
            @Override
            public void e(ProgressBar progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void stopProgress() {
        List.list(syussyaProgress, taisyaProgress)
            .foreach(new Effect<Option<ProgressBar>>() {
                @Override
                public void e(Option<ProgressBar> progress) {
                    progress.foreach(new Effect<ProgressBar>() {
                        @Override
                        public void e(ProgressBar progressBar) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            });
    }
}
