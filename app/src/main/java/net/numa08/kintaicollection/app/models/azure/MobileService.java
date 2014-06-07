package net.numa08.kintaicollection.app.models.azure;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;

import net.numa08.kintaicollection.app.AuthenticateActivity;
import net.numa08.kintaicollection.app.R;

import java.net.MalformedURLException;

import fj.F;
import fj.P;
import fj.P2;
import fj.data.Either;
import fj.data.Option;

public class MobileService {

    final private Option<Context> context;

    public MobileService(Option<Context> context) {
        this.context = context;
    }

    public Option<MobileServiceUser> user() {
            return  context
                     .map(new F<Context, P2<Id, Token>>() {
                         @Override
                         public P2<Id, Token> f(Context context) {
                             final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
                             final String id = pref.getString(AuthenticateActivity.KEY_USER_ID, "");
                             final String token = pref.getString(AuthenticateActivity.KEY_USER_TOKEN, "");
                             return P.p(new Id(id), new Token(token));
                         }})
                     .map(new F<P2<Id, Token>, MobileServiceUser>() {
                         @Override
                         public MobileServiceUser f(P2<Id, Token> production) {
                             final MobileServiceUser user = new MobileServiceUser(production._1().id);
                             user.setAuthenticationToken(production._2().token);
                             return user;
                         }
                     });
    }

    public Either<? extends Exception, MobileServiceClient> client() {
            return  context
                     .map(new F<Context, Either<? extends Exception, MobileServiceClient>>() {
                         @Override
                         public Either<? extends Exception, MobileServiceClient> f(Context context) {
                             final String endpoint = context.getString(R.string.azure_endpoint);
                             final String key = context.getString(R.string.azure_key);
                             Either<? extends Exception, MobileServiceClient> eitherClient;
                             try {
                                 final MobileServiceClient client = new MobileServiceClient(endpoint, key, context);
                                 eitherClient = Either.right(client);
                             } catch (MalformedURLException e) {
                                 e.printStackTrace();
                                 eitherClient = Either.left(e);
                             }
                             return eitherClient;
                         }})
                     .orSome(Either.<Exception, MobileServiceClient>left(new Exception("Context is null")));
    }

    public static class Token {
        public final String token;

        public Token(String token) {
            this.token = token;
        }
    }

    public static class Id {
        public final String id;

        public Id(String id) {
            this.id = id;
        }
    }
}
