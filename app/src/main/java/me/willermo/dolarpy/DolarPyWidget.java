package me.willermo.dolarpy;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import me.willermo.dolarpy.models.MainObject;


/**
 * Implementation of App Widget functionality.
 */
public class DolarPyWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        final int N = appWidgetIds.length;
        Intent intent = new Intent(context,FetchCotizacionService.class);
        Bundle bundle = new Bundle();
        bundle.putIntArray("ids",appWidgetIds);
        intent.putExtras(bundle);
        context.startService(intent);

    }


    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }



}


