package net.numa08.kintaicollection.app.domein.api.request;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TaisyaRequestTest extends TestCase {

    public void testParseToJsonObject() throws Exception {
        final Date date = new GregorianCalendar(2014, Calendar.JUNE, 2, 23, 0, 0).getTime();
        final String id = "12345";
        final TaisyaRequest request = new TaisyaRequest(date, id);

        final JSONObject expected = new JSONObject(String.format("{id:%s, time:%s}", id, Long.toString(date.getTime())));
        final JSONObject actual = request.request();

        assertEquals("invalid id", expected.getString("id"), actual.getString("id"));
        assertEquals("invalid time", expected.getString("time"), actual.getString("time"));
    }

    public void testInvokeSuccessWhenResponsJsonIsSucceed() throws JSONException, InterruptedException {
        final String json = "{success : true}";
        final CountDownLatch countdown = new CountDownLatch(1);
        final TaisyaRequest.TaisyaRequestCallback callback = new TaisyaRequest.TaisyaRequestCallback() {
            @Override
            public void onTaisyaSuccess() {
                countdown.countDown();
            }
        };
        callback.onSuccess(new JSONObject(json));
        final boolean called = countdown.await(2000, TimeUnit.MILLISECONDS);
        assertTrue("Syussya success is not invoked", called);
    }

    public void testNotInvokeSuccessWhenResponseInvalidJson() throws Exception {
        final CountDownLatch countdown = new CountDownLatch(1);
        final TaisyaRequest.TaisyaRequestCallback callback = new TaisyaRequest.TaisyaRequestCallback() {
            @Override
            public void onTaisyaSuccess() {
                countdown.countDown();
            }
        };
        callback.onSuccess(new JSONObject());
        final boolean called = countdown.await(2000, TimeUnit.MILLISECONDS);
        assertFalse("Syussya success is called", called);
    }

}
