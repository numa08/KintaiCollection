package net.numa08.kintaicollection.app.domein;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.deploygate.sdk.DeployGate;
import com.google.gson.JsonElement;
import com.microsoft.windowsazure.mobileservices.ApiJsonOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import net.numa08.kintaicollection.app.R;
import net.numa08.kintaicollection.app.models.azure.MobileService;
import net.numa08.kintaicollection.app.net.numa08.utils.FragmentUtils;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPut;

import fj.Effect;
import fj.F;
import fj.P2;
import fj.data.Either;
import fj.data.Option;

public class RegisterUserData extends Fragment {

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
                Log.d(getString(R.string.app_name), "invoke user/me");
                client.invokeApi("user/me", HttpPut.METHOD_NAME, null, RegisterUserData.this.onRequestMeInfoCallback);
            }
        });

    }

    private final ApiJsonOperationCallback onRequestMeInfoCallback = new ApiJsonOperationCallback() {
        @Override
        public void onCompleted(JsonElement jsonElement, Exception e, ServiceFilterResponse serviceFilterResponse) {
            if (!FragmentUtils.isActive(RegisterUserData.this)) {
                return;
            }

            final Either<Exception, ServiceFilterResponse> eitherResponse;
            if (e == null ) {
                eitherResponse = Either.right(serviceFilterResponse);
            } else {
                eitherResponse = Either.left(e);
            }

            eitherResponse.left()
                          .toOption()
                          .foreach(new Effect<Exception>() {
                              @Override
                              public void e(Exception e) {
                                  Log.e(getString(R.string.app_name), "user/me error", e);
                                  DeployGate.logError(e.getMessage());
                              }
                          });
            eitherResponse.right()
                          .toOption()
                          .filter(new F<ServiceFilterResponse, Boolean>() {
                              @Override
                              public Boolean f(ServiceFilterResponse serviceFilterResponse) {
                                  return serviceFilterResponse.getStatus().getStatusCode() == HttpStatus.SC_ACCEPTED;
                              }})
                          .foreach(new Effect<ServiceFilterResponse>() {
                              @Override
                              public void e(ServiceFilterResponse serviceFilterResponse) {
                                  client.foreach(new Effect<MobileServiceClient>() {
                                      @Override
                                      public void e(MobileServiceClient client) {
                                          Log.d(getString(R.string.app_name), "invoke user/register");
                                          client.invokeApi("user/register", HttpPut.METHOD_NAME, null, RegisterUserData.this.onRegisterMyInfoCallback);
                                      }
                                  });
                              }
                          });
        }
    };

    private final ApiJsonOperationCallback onRegisterMyInfoCallback = new ApiJsonOperationCallback() {
        @Override
        public void onCompleted(JsonElement jsonElement, Exception e, ServiceFilterResponse serviceFilterResponse) {
            if (!FragmentUtils.isActive(RegisterUserData.this)) {
                return;
            }
            if (e != null) {
                Log.e(getString(R.string.app_name), "user/register error", e);
            } else {
                Log.d(getString(R.string.app_name), "user/register ok");
            }
        }
    };
}
