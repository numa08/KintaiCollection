package net.numa08.kintaicollection.app.models.timeline;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class KintaiTimelineItemTest extends TestCase {
    final String validJson = "[{\"id\": \"2561268324\", \"name\": \"kintai_collection\", \"profileimageurl\": \"http://pbs.twimg.com/profile_images/463705376991375360/JAKULHHL_normal.png\", \"status\": \"ｼｭｯｼｬ\", \"__createdAt\": \"2014-06-12T22:14:12.031Z\", \"__updatedAt\": \"2014-06-12T22:14:12.047Z\"}, {\"id\": \"2561268324\", \"name\": \"kintai_collection\", \"profileimageurl\": \"http://pbs.twimg.com/profile_images/463705376991375360/JAKULHHL_normal.png\", \"status\": \"ﾀｲｼｬ\", \"__createdAt\": \"2014-06-12T22:14:55.375Z\", \"__updatedAt\": \"2014-06-12T22:14:55.375Z\"}]";

    public void testParseJson() throws Exception {
        final JsonElement json = new JsonParser().parse(validJson);
        final List<KintaiTimelineItem> expected = Arrays.asList(
                new KintaiTimelineItem(new User("kintai_collection", "2561268324", "http://pbs.twimg.com/profile_images/463705376991375360/JAKULHHL_normal.png"), new Kintai.Syussya(new Date(1402611252))),
                new KintaiTimelineItem(new User("kintai_collection", "2561268324", "http://pbs.twimg.com/profile_images/463705376991375360/JAKULHHL_normal.png"), new Kintai.Taisya(new Date(1402611295)))
        );

        final List<KintaiTimelineItem> actual = new ArrayList<>();
        for (JsonElement element : json.getAsJsonArray()) {
            final KintaiTimelineItem item = KintaiTimelineItem.parseJson(element);
            actual.add(item);
        }

        assertEquals("collection items are invalid", expected, actual);
    }
}
