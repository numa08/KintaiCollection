package net.numa08.kintaicollection.app.domein;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonElement;
import com.microsoft.windowsazure.mobileservices.ApiJsonOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import net.numa08.kintaicollection.app.R;
import net.numa08.kintaicollection.app.models.azure.MobileService;

import org.apache.http.client.methods.HttpPut;

import fj.Effect;
import fj.F;
import fj.P2;
import fj.data.Option;

public class RegisterUserData extends Fragment implements ApiJsonOperationCallback{

    Option<MobileServiceClient> client = Option.none();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final MobileService service = new MobileService(Option.fromNull((Context)getActivity()));
        client = service.client()
                        .right()
                        .toOption()
                        .bindProduct(service.user())
                        .map(new F<P2<MobileServiceClient, MobileServiceUser>, MobileServiceClient>() {
                            @Override
                            public MobileServiceClient f(P2<MobileServiceClient, MobileServiceUser> product) {
                                product._1().setCurrentUser(product._2());
                                return product._1();
                            }
                        });
        client.foreach(new Effect<MobileServiceClient>() {
            @Override
            public void e(MobileServiceClient client) {
                Log.d(getString(R.string.app_name), "invoke user/register");
                client.invokeApi("user/register", HttpPut.METHOD_NAME, null, RegisterUserData.this);
            }
        });

    }

    @Override
    public void onCompleted(JsonElement jsonElement, Exception e, ServiceFilterResponse serviceFilterResponse) {
        if (e != null) {
            Log.e(getString(R.string.app_name), "user/register error", e);
        }
    }
}
