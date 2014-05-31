package net.numa08.kintaicollection.app.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import net.numa08.kintaicollection.app.models.timeline.KintaiTimelineItem;

public class KintaiItemsAdapter extends ArrayAdapter<KintaiTimelineItem>{

    public KintaiItemsAdapter(Context context) {
        super(context, 0);
    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView name;
        public TextView kintai;
    }

    private static class ImageCache implements ImageLoader.ImageCache {

        private final LruCache<String, Bitmap> memoryCache;

        public ImageCache() {
            final int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            memoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String s) {
            return memoryCache.get(s);
        }

        @Override
        public void putBitmap(String s, Bitmap bitmap) {
            memoryCache.put(s, bitmap);
        }
    }
}
