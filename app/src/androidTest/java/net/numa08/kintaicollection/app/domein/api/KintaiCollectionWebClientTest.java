package net.numa08.kintaicollection.app.domein.api;

import android.test.InstrumentationTestCase;

public class KintaiCollectionWebClientTest extends InstrumentationTestCase {

    public void testInitializeApplicationURLWhenConstruct() throws Exception {
        final KintaiCollectionWebClient client = new KintaiCollectionWebClient(getInstrumentation().getContext());
        final String expected = "http://192.168.1.31";
        final String actual = client.apiUrl;
        assertEquals("propertie file is invalid", expected, actual);
    }
}