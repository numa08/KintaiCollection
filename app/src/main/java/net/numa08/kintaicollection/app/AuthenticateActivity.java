package net.numa08.kintaicollection.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;

import java.net.MalformedURLException;

import fj.Effect;
import fj.F;
import fj.Unit;
import fj.data.Either;
import fj.data.Option;


public class AuthenticateActivity extends Activity implements UserAuthenticationCallback {

    public static final String KEY_USER_TOKEN = AuthenticateActivity.class.getName() + "KEY_USER_TOKEN";
    public static final String KEY_USER_ID = AuthenticateActivity.class.getName() + "KEY_USER_ID";


    private Option<MobileServiceClient> azureClient = Option.none();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.contains(AuthenticateActivity.KEY_USER_TOKEN) && preferences.contains(AuthenticateActivity.KEY_USER_ID)) {
            final Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_authoricate);
        }

        try {
            azureClient = Option.fromNull(new MobileServiceClient(getText(R.string.azure_endpoint).toString(), getText(R.string.azure_key).toString(), this));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void signIn(View view) {
        azureClient.foreach(new Effect<MobileServiceClient>() {
            @Override
            public void e(MobileServiceClient client) {
                client.login(MobileServiceAuthenticationProvider.Twitter, AuthenticateActivity.this);
            }
        });
    }

    @Override
    public void onCompleted(MobileServiceUser mobileServiceUser, Exception e, ServiceFilterResponse serviceFilterResponse) {
        final Either<Exception, MobileServiceUser> either;
        if (e == null) {
            either = Either.right(mobileServiceUser);
        } else {
            either = Either.left(e);
        }
        either.right()
               .map(new F<MobileServiceUser, Unit>() {
                  @Override
                  public Unit f(MobileServiceUser mobileServiceUser) {
                      final String id = mobileServiceUser.getUserId();
                      final String token = mobileServiceUser.getAuthenticationToken();
                      PreferenceManager.getDefaultSharedPreferences(AuthenticateActivity.this)
                              .edit()
                              .putString(AuthenticateActivity.KEY_USER_ID, id)
                              .putString(AuthenticateActivity.KEY_USER_TOKEN, token)
                              .commit();
                      final Intent intent = new Intent(AuthenticateActivity.this, MainActivity.class);
                      startActivity(intent);
                      finish();
                      return Unit.unit();
                  }
               })
               .left()
               .foreach(new Effect<Exception>() {
                   @Override
                   public void e(Exception e) {
                       Toast.makeText(AuthenticateActivity.this, R.string.msg_login_failed, Toast.LENGTH_LONG).show();
                       Log.e(getString(R.string.app_name), "Login failed", e);
                   }
               });
    }

}
