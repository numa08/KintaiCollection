package net.numa08.kintaicollection.app;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;


public class KintaiReportWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId) {

        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.kintai_report_widget);
        views.setOnClickPendingIntent(R.id.syussyaButton, createSyussyaService(context, appWidgetId));
        views.setOnClickPendingIntent(R.id.taisyaButton, createTaisyaService(context, appWidgetId));
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private PendingIntent createSyussyaService(Context context, int appWidgetId) {
        final Intent intent = new Intent(context, KintaiReportWidgetService.class);
        intent.setAction(KintaiReportWidgetService.Action.SYUSSYA);
        return PendingIntent.getService(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent createTaisyaService(Context context, int appWidgetId) {
        final Intent intent = new Intent(context, KintaiReportWidgetService.class);
        intent.setAction(KintaiReportWidgetService.Action.TAISYA);
        return PendingIntent.getService(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}


