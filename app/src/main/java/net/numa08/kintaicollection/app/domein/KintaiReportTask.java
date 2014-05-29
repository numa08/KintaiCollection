package net.numa08.kintaicollection.app.domein;

import android.os.AsyncTask;

import net.numa08.kintaicollection.app.models.KintaiReportRequest;

import java.lang.ref.WeakReference;

import fj.Effect;
import fj.data.Option;

public class KintaiReportTask extends AsyncTask<Void, Integer, Boolean>{

    public interface KintaiReportTaskListener {
        public void onSuccess();
        public void onFinish();
        public void onProgress(int progress);
        public void onFailed(Throwable e);
    }

    private final KintaiReportRequest request;
    private Throwable exception;
    private Option<WeakReference<KintaiReportTaskListener>> listener;

    public KintaiReportTask(KintaiReportRequest request) {
        this.request = request;
    }

    public void registListener(KintaiReportTaskListener listener) {
        this.listener = Option.some(new WeakReference<>(listener));
    }

    public void unRegitListener() {
        listener.foreach(new Effect<WeakReference<KintaiReportTaskListener>>() {
            @Override
            public void e(WeakReference<KintaiReportTaskListener> listen) {
                if (listen.get() == null) {
                    return;
                }
                listen.clear();
            }
        });
    }


    @Override
    protected void onProgressUpdate(final Integer... values) {
        listener.foreach(new Effect<WeakReference<KintaiReportTaskListener>>() {
            @Override
            public void e(WeakReference<KintaiReportTaskListener> listen) {
                if (listen.get() == null) {
                    return;
                }
                listen.get().onProgress(values[0]);
            }
        });
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean result) {
        super.onPostExecute(result);
        listener.foreach(new Effect<WeakReference<KintaiReportTaskListener>>() {
            @Override
            public void e(WeakReference<KintaiReportTaskListener> listen) {
                if (listen.get() == null) {
                    return;
                }
                if (result) {
                    listen.get().onSuccess();
                } else {
                    listen.get().onFailed(exception);
                }
                listen.get().onFinish();
            }
        });
    }
}
