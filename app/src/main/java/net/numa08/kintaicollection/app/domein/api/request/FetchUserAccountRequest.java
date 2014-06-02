package net.numa08.kintaicollection.app.domein.api.request;

import net.numa08.kintaicollection.app.domein.api.KintaiCollectionWebClient;
import net.numa08.kintaicollection.app.models.timeline.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FetchUserAccountRequest implements KintaiCollectionApiRequest {

    private final User user;

    public FetchUserAccountRequest(User user) {
        this.user = user;
    }

    @Override
    public JSONObject request() {
        return new JSONObject(new HashMap<String, String>(){{
            put("id", user.id);
            put("name", user.name);
            put("token", user.token);
        }});
    }

    public static abstract class FetchUserAccountClientCallback implements KintaiCollectionWebClient.KintaiCollectionApiCallback {
        abstract public void newAccount(User user);
        abstract public void hasAccount(User user);

        @Override
        public void onSuccess(JSONObject json){
            try {
                final boolean newAccount = json.getBoolean("is_newaccount");
                final JSONObject userObj = json.getJSONObject("user");
                final String id = userObj.getString("id");
                final String name = userObj.getString("name");
                final String icon = userObj.getString("icon");
                final User user = new User(name, id, icon, "");
                if (newAccount) {
                    newAccount(user);
                } else {
                    hasAccount(user);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                onFailed(new Exception("Fetch user error"));
            }
        }

        @Override
        public void onFailed(Throwable e){}

        @Override
        public void onFinish() {}
    }
}
