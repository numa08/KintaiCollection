package net.numa08.kintaicollection.app.domein.api.request;

import junit.framework.TestCase;

import net.numa08.kintaicollection.app.models.timeline.User;

import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FetchUserAccountRequestTest extends TestCase{

    public void testParseToJsonRequest() throws Exception {
        final User iam = new User("numa08", "12345", "", "token");
        final FetchUserAccountRequest request = new FetchUserAccountRequest(iam);
        final JSONObject expected = new JSONObject(String.format("{id : %s, name : %s, token : %s}", iam.id, iam.name, iam.token));
        final JSONObject acctual = request.request();

        assertEquals("id is invalid", expected.getString("id"), acctual.getString("id"));
        assertEquals("name is invalid", expected.getString("name"), acctual.getString("name"));
        assertEquals("token is invalid", expected.getString("token"), acctual.getString("token"));
    }

    public void testInvokeNewAccountOnSuccessRequest() throws Exception {
        final JSONObject result = new JSONObject("{is_newaccount : true, user : {id : 123455, name : numa08, icon : hogehoge}}");
        final CountDownLatch countdown = new CountDownLatch(1);
        final FetchUserAccountRequest.FetchUserAccountClientCallback callback = new FetchUserAccountRequest.FetchUserAccountClientCallback() {
            @Override
            public void newAccount(User user) {
                countdown.countDown();
            }

            @Override
            public void hasAccount(User user) {}
        };
        callback.onSuccess(result);
        final boolean called = countdown.await(2000, TimeUnit.MILLISECONDS);
        assertTrue("new account is not invoked", called);
    }

    public void testInvokeHasAccountOnSuccessRequest() throws Exception {
        final JSONObject result = new JSONObject("{is_newaccount : false, user :  {id : 123455, name : numa08, icon : hogehoge}}");
        final CountDownLatch countdown = new CountDownLatch(1);
        final FetchUserAccountRequest.FetchUserAccountClientCallback callback = new FetchUserAccountRequest.FetchUserAccountClientCallback() {
            @Override
            public void newAccount(User user) {}

            @Override
            public void hasAccount(User user) {
                countdown.countDown();
            }
        };
        callback.onSuccess(result);
        final boolean called = countdown.await(2000, TimeUnit.MILLISECONDS);
        assertTrue("has account is not invoked", called);
    }

    public void testNotInvokeSuccessCallbackInvalidResponse() throws Exception {
        final JSONObject result = new JSONObject();
        final CountDownLatch countdown = new CountDownLatch(1);
        final FetchUserAccountRequest.FetchUserAccountClientCallback callback = new FetchUserAccountRequest.FetchUserAccountClientCallback() {
            @Override
            public void newAccount(User user) {
                countdown.countDown();
            }

            @Override
            public void hasAccount(User user) {
                countdown.countDown();
            }
        };
        callback.onSuccess(result);
        final boolean called = countdown.await(2000, TimeUnit.MILLISECONDS);
        assertFalse("callback is called!!", called);
    }
}
