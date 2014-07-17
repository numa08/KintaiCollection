package net.numa08.kintaicollection.app;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.deploygate.sdk.DeployGate;
import com.google.gson.JsonElement;
import com.microsoft.windowsazure.mobileservices.ApiJsonOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import net.numa08.kintaicollection.app.models.azure.MobileService;

import org.apache.http.client.methods.HttpPut;

import fj.Effect;
import fj.F;
import fj.P2;
import fj.data.Either;
import fj.data.List;
import fj.data.Option;

public class KintaiReportWidgetService extends IntentService implements ApiJsonOperationCallback {

    public static class Action {
        public static final String SYUSSYA = KintaiReportWidgetService.Action.class.getName() + ".SYUSSYA";
        public static final String TAISYA = KintaiReportWidgetService.Action.class.getName() + ".TAISYA";
    }

    private Option<MobileServiceClient> client = Option.none();


    public KintaiReportWidgetService() {
        super("KintaiReportWidgetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final MobileService service = new MobileService(Option.fromNull((Context)this));
        client = service.user()
                .bindProduct(service.client().right().toOption())
                .map(new F<P2<MobileServiceUser, MobileServiceClient>, MobileServiceClient>() {
                    @Override
                    public MobileServiceClient f(P2<MobileServiceUser, MobileServiceClient> product) {
                        product._2().setCurrentUser(product._1());
                        return product._2();
                    }
                });

        Option.fromNull(intent)
              .map(new F<Intent, String>() {
                  @Override
                  public String f(Intent intent) {
                      return intent.getAction();
                  }})
              .filter(new F<String, Boolean>() {
                  @Override
                  public Boolean f(String s) {
                      return s.equals(Action.SYUSSYA) || s.equals(Action.TAISYA);
                  }})
              .foreach(new Effect<String>() {
                  @Override
                  public void e(String action) {
                      reportKintai(action);
                  }});
    }

    private void reportKintai(String  action) {
        final String apiPath;
        if (action.equals(Action.SYUSSYA)) {
            apiPath = "report/syussya";
        } else {
            apiPath = "report/taisya";
        }

        client.foreach(new Effect<MobileServiceClient>() {
            @Override
            public void e(MobileServiceClient client) {
                client.invokeApi(apiPath, HttpPut.METHOD_NAME, null, KintaiReportWidgetService.this);
            }
        });
    }


    @Override
    public void onCompleted(JsonElement jsonObject, Exception exception, ServiceFilterResponse serviceFilterResponse) {
        final Either<Exception, JsonElement> either;
        if (exception == null) {
            either = Either.right(jsonObject);
        } else {
            either = Either.left(exception);
        }

        either.right()
                .toOption()
                .bindProduct(Option.fromNull(this))
                .foreach(new Effect<P2<JsonElement, KintaiReportWidgetService>>() {
                    @Override
                    public void e(P2<JsonElement, KintaiReportWidgetService> product) {
                        DeployGate.logDebug("Completed Kintai Report");
                        Log.d(getString(R.string.app_name), product._2().toString());
                        final Intent intent = new Intent();
                        intent.setAction(MainActivity.Action.UPDATE_KINTAI_TIMELINE);
                        product._2().sendBroadcast(intent);
                    }
                });

        either.left()
                .toOption()
                .bindProduct(Option.fromNull(this))
                .foreach(new Effect<P2<Exception, KintaiReportWidgetService>>() {
                    @Override
                    public void e(P2<Exception, KintaiReportWidgetService> product) {
                        Log.e(getString(R.string.app_name), "Failed PUT", product._1());
                    }
                });

    }
}
