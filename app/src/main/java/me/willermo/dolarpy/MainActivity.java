package me.willermo.dolarpy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import me.willermo.dolarpy.models.MainObject;


public class MainActivity extends AppCompatActivity {
    @Bind(R.id.valor_compra_ch) TextView tCompraChaco;
    @Bind(R.id.valor_venta_ch) TextView tVentaChaco;
    @Bind(R.id.valor_compra_al) TextView tCompraAlberdi;
    @Bind(R.id.valor_venta_al) TextView tVentaAlberdi;
    @Bind(R.id.valor_compra_max) TextView tCompraMaxi;
    @Bind(R.id.valor_venta_max) TextView tVentaMaxi;
    @Bind(R.id.toolbar_title) TextView tTitulo;
    @Bind(R.id.tUltimaActualizacion) TextView tUltimaActualizacion;
    @Bind(R.id.container_linear)
    LinearLayout linearLayout;
    SharedPreferences sharedPreferences;
    private String data="";
    private Gson gson;
    private MainObject mainObject;
    private Toolbar toolbar;
    private SimpleDateFormat simpleDateFormat;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        toolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);

        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setWaveColor(getResources().getColor(R.color.primary));
        Typeface typeface = Typeface.createFromAsset(this.getAssets(),"RobotoCondensed-Light.ttf");
        tTitulo.setTypeface(typeface);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(MainActivity.this,FetchCotizacionService.class);
                startService(intent);
            }
        });
        if(toolbar!=null){
            toolbar.setTitle("Cotización del dolar");
        }
        ViewGroup view;
        TextView textView;
        Typeface RobotoCondensedBold = Typeface.createFromAsset(getAssets(),"RobotoCondensed-Bold.ttf");
        Typeface RobotoREgular =Typeface.createFromAsset(getAssets(),"Roboto-Regular.ttf");
        Typeface RobotoLight = Typeface.createFromAsset(getAssets(),"Roboto-Light.ttf");
        for(int i=0;i<linearLayout.getChildCount();i++){
            if(linearLayout.getChildAt(i).getTag()!=null&&linearLayout.getChildAt(i).getTag().equals("actualizacion")){
                break;
            }
            view= (ViewGroup) linearLayout.getChildAt(i);
            for(int j=0;j<view.getChildCount();j++){
                if(view.getChildAt(j).getTag()!=null){
                    if(view.getChildAt(j).getTag().equals("casa_nombre")){
                        ((TextView)view.getChildAt(j)).setTypeface(RobotoCondensedBold);
                    }
                    if(view.getChildAt(j).getTag().equals("tipo")){
                        ((TextView)view.getChildAt(j)).setTypeface(RobotoREgular);
                    }
                    if(view.getChildAt(j).getTag().equals("valor")){
                        ((TextView)view.getChildAt(j)).setTypeface(RobotoLight);
                    }

                }
            }

        }
        tUltimaActualizacion.setTypeface(RobotoLight);

        Intent intent = new Intent(this,FetchCotizacionService.class);
        startService(intent);
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sharedPreferences = getSharedPreferences("dolarpy",MODE_PRIVATE);
        showData();
    }
    public void showData(){
            data = sharedPreferences.getString("cotizacion","");
                if(data.length()>1){
                    mainObject = gson.fromJson(data,MainObject.class);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //toolbar.setSubtitle(new StringBuilder().append("Actualizado: ").append(simpleDateFormat.format(mainObject.getUpdated())));
                            tUltimaActualizacion.setText(new StringBuilder().append("Ultima actualización: ").append(simpleDateFormat.format(mainObject.getUpdated())));
                            tCompraChaco.setText(mainObject.getDolarpy().getCambioschaco().getCompra());
                            tVentaChaco.setText(mainObject.getDolarpy().getCambioschaco().getVenta());
                            tCompraAlberdi.setText(mainObject.getDolarpy().getCambiosalberdi().getCompra());
                            tVentaAlberdi.setText(mainObject.getDolarpy().getCambiosalberdi().getVenta());
                            tCompraMaxi.setText(mainObject.getDolarpy().getMaxicambios().getCompra());
                            tVentaMaxi.setText(mainObject.getDolarpy().getMaxicambios().getVenta());
                        }
                    });
                }
    }

    public void onEvent(MessageCotizacion messageCotizacion){
        try{
            showData();
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWaveSwipeRefreshLayout.setRefreshing(false);
                }
            });

        }catch (Exception e){

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
