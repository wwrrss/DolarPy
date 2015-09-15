package me.willermo.dolarpy;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import me.willermo.dolarpy.models.MainObject;

/**
 * Created by william on 9/5/15.
 */
public class FetchCotizacionService extends IntentService {
    public FetchCotizacionService(){
        super("FetchCotizacionService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        client.setReadTimeout(10,TimeUnit.SECONDS);
        Request request = new Request.Builder().url("http://dolar.melizeche.com/api/1.0/").build();
        Response response=null;


        try {
             response = client.newCall(request).execute();
            if(response.code()==200){
                if(!response.header("Content-Type").equals("application/json")){
                    //si la respuesta no es un JSON salir de aqui!!!
                    return;

                }
                SharedPreferences sharedPreferences = getApplication().getSharedPreferences("dolarpy",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String data = response.body().string();
                editor.putString("cotizacion",data);
                editor.commit();

                Bundle bundle = intent.getExtras();
                if(bundle!=null){
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    MainObject mainObject = gson.fromJson(data,MainObject.class);
                    int[] ids = bundle.getIntArray("ids");
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                    RemoteViews remoteViews=null;
                    for (int i=0;i<ids.length;i++){
                        remoteViews = new RemoteViews(getApplicationContext().getPackageName(),R.layout.dolar_py_widget);
                        remoteViews.setTextViewText(R.id.tValorCompra,mainObject.getDolarpy().getCambioschaco().getCompra());
                        remoteViews.setTextViewText(R.id.tValorVenta,mainObject.getDolarpy().getCambioschaco().getVenta());
                        remoteViews.setTextViewText(R.id.tActualizado,simpleDateFormat.format(mainObject.getUpdated()));
                        appWidgetManager.updateAppWidget(ids[i],remoteViews);
                    }


                }


            }


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            EventBus.getDefault().post(new MessageCotizacion());
            if(response!=null){
                try {
                    response.body().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
