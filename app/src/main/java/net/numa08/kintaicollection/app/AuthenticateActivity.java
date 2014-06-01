package net.numa08.kintaicollection.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;

import java.net.MalformedURLException;

import fj.Effect;
import fj.data.Option;


public class AuthenticateActivity extends Activity implements UserAuthenticationCallback{

    private Option<MobileServiceClient> azureClient = Option.none();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authoricate);

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
        Option.fromNull(mobileServiceUser).foreach(new Effect<MobileServiceUser>() {
            @Override
            public void e(MobileServiceUser mobileServiceUser) {
            }
        });
    }

}
