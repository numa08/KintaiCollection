package net.numa08.kintaicollection.app.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import net.numa08.kintaicollection.app.R;
import net.numa08.kintaicollection.app.models.timeline.Kintai;
import net.numa08.kintaicollection.app.models.timeline.KintaiTimelineItem;

import java.util.HashMap;
import java.util.Map;

public class KintaiItemsAdapter extends ArrayAdapter<KintaiTimelineItem>{

    private static final Map<Kintai.Status, Integer> KINTAI_BACKGROUND = new HashMap<Kintai.Status, Integer>(){{
        put(Kintai.Status.Syussya, R.color.syussya_background);
        put(Kintai.Status.Taisya, R.color.taisya_background);
    }};

    private final ImageLoader loader;

    public KintaiItemsAdapter(Context context, RequestQueue queue) {
        super(context, 0);
        loader = new ImageLoader(queue, new ImageCache());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.row_kintai_timeline, null);
            holder = new ViewHolder();
            holder.icon = (ImageView)convertView.findViewById(R.id.workerIcon);
            holder.name = (TextView)convertView.findViewById(R.id.workerName);
            holder.kintai = (TextView)convertView.findViewById(R.id.kintaiLog);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final KintaiTimelineItem item = getItem(position);
        holder.name.setText(item.getUser().name);
        holder.kintai.setText(item.getKintai().toLogMessage(getContext()));
        final int color = getContext().getResources().getColor(KINTAI_BACKGROUND.get(item.getKintai().status()));
        convertView.setBackgroundColor(color);
        final ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.icon, android.R.drawable.spinner_background, android.R.drawable.ic_dialog_alert);
        loader.get(item.getUser().icon, listener);

        return convertView;
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
