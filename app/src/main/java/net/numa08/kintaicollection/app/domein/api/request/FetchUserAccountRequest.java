package net.numa08.kintaicollection.app.domein.api.request;

import net.numa08.kintaicollection.app.domein.api.KintaiCollectionWebClient;
import net.numa08.kintaicollection.app.models.timeline.User;

import org.json.JSONObject;

public class FetchUserAccountRequest implements KintaiCollectionApiRequest {

    private final User user;

    public FetchUserAccountRequest(User user) {
        this.user = user;
    }

    @Override
    public JSONObject request() {
        return null;
    }

    public static abstract class FetchUserAccountClientCallback implements KintaiCollectionWebClient.KintaiCollectionApiCallback {
        abstract public void newAccount(User user);
        abstract public void hasAccount(User user);

        @Override
        public void onSuccess(JSONObject json){}

        @Override
        public void onFailed(Throwable e){}

        @Override
        public void onFinish() {}
    }
}
