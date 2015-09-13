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

                    try{
                        mainObject = gson.fromJson(data,MainObject.class);
                    }catch (Exception e){
                        //con una linea tigo si uno queda sin saldo para internet a cualquier peticion http se responde con una pagina html
                        //y esto rompe el json
                        //la respuesta es esta grrrrr
                        /*
                           <html xmlns="http://www.w3.org/1999/xhtml"><head id="j_idt3"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                        <title>Internet Móvil Tigo </title>
                        <link type="image/x-icon" rel="shortcut icon" href="./welcome_desktop_files/resources/fs/img/favicon.ico"><script type="text/javascript" src="./welcome_desktop_files/jquery-1.7.1.min.js.faces"></script><script type="text/javascript" src="./welcome_desktop_files/jquery.blockUI.js.faces"></script><script type="text/javascript" src="./welcome_desktop_files/ajaxUIHandler.js.faces"></script><script type="text/javascript" src="./welcome_desktop_files/messagesUIHandler.js.faces"></script><script type="text/javascript" src="./welcome_desktop_files/fvarias.js.faces"></script><script type="text/javascript" src="./welcome_desktop_files/sessionControl.js.faces"></script><script type="text/javascript" src="./welcome_desktop_files/jquery-ui-1.8.17.custom.min.js.faces"></script><script type="text/javascript" src="./welcome_desktop_files/jquery.ui.datepicker-es-py.js.faces"></script><link type="text/css" rel="stylesheet" href="./welcome_desktop_files/general.css"><link type="text/css" rel="stylesheet" href="./welcome_desktop_files/jquery-ui-1.8.14.custom.css"></head><body style="cursor: default;">
                        <script type="text/javascript" charset="UTF-8">
                        //<![CDATA[
                        disableSelection(document.body);
                        $(document).ready(function() {
                        $('.datepicker').datepicker();
                        });
                        //]]>
                        </script>
                        <div id="container">
                        <div id="header">
                        <div id="headerTop">
                        <div class="inside">
                        <div id="logo"><a href="http://www.tigo.com.py/"><img src="./welcome_desktop_files/logo.png.faces" height="62" title="Tigo" width="87"></a>
                        </div>
                        <div id="titulositio"><img src="./welcome_desktop_files/internetmovil.jpg.faces" height="61" title="" width="222">
                        </div>
                        </div>
                        </div>
                        <div id="headerBottom"><div><div id="menu"></div><div id="submenu"></div></div></div>
                        </div>
                        <div id="body">
                        <div id="internas_bkg">
                        <form id="portalForm" name="portalForm" method="post" action="" enctype="application/x-www-form-urlencoded">
                        <input type="hidden" name="portalForm" value="portalForm">
                        <div id="contenido2"><span id="portalForm:accountInfoPanelGroup"><a href="http://www.tigo.com.py/seccion/internet-movil-tigo"><img src="./welcome_desktop_files/banner-welcome.jpg.faces" style="width: 504px; margin-left: 18px;"></a>
                        <div id="cajawhite_big">
                        <div id="topwhite_big"></div>
                        <div id="bodywhite_big" style="text-align: justify;">
                        <h3>Bienvenidos al Portal Movil Tigo</h3>
                        <br><span style="font-size: 13px;">Aquí encontrarás todas las opciones para disfrutar al máximo de tu Internet Móvil con Tigo!</span>
                        <br><br><br><input type="button" onclick="window.location.href=&#39;/fs/portal.faces&#39;; return false;" value="Haz click aqui para ir al Portal" class="btn_green_m" style="margin-left: 194px;">
                        <br><br>
                        </div>
                        </div>
                        <div id="footerwhite_big"></div></span>
                        </div>
                        <div id="columna_3ra"></div>
                        </form>
                        <div id="borde_inf"></div>
                        </div>
                        </div>
                        <div id="footer">
                        <div id="footerBottom">
                        <div class="inside">Todos los derechos reservados por TELECEL S.A. | Desarrollado por Soluciones Corporativas - Lothar S.A.
                        <ul>
                        <li><a href="http://www.mic.gov.py/" target=
                         */
                        return;
                    }

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
