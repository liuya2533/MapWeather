package com.example.lab9map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener,OnGetGeoCoderResultListener{

    MapView mMapView = null;
    BaiduMap baiduMap=null;
    TextView markerText;
    TextView tv;
    Button button;
    EditText cityText;
    GeoCoder geoCoder=null;
    EditText addressText;

    final private int MENU_TRAFFIC= Menu.FIRST;
    final private int MENU_NORMAL=Menu.FIRST+1;
    final private int MENU_SATELLITE=Menu.FIRST+2;
    final private int MENU_MYLOCATION=Menu.FIRST+3;
    final private int MENU_OVERLAY=Menu.FIRST+4;

    public LocationClient locationClient=null;
    double mlatitude,flatitude;
    double mlongitude,flongtitude;

    Boolean isFirstLoc=true;

    BitmapDescriptor bitmap;


    private RelativeLayout mMarkerlout;
    private RelativeLayout shouLayout;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    markerText.setText(((WeatherInfo)msg.obj).toString());
                    break;
                case 0:
                    Toast.makeText(MainActivity2.this, "访问失败",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    public BDLocationListener myListener =new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder().accuracy(bdLocation.getRadius())
                    .direction(100).latitude(bdLocation.getLatitude()).longitude(bdLocation.getLongitude())
                    .build();
            baiduMap.setMyLocationData(locData);
            mlatitude=bdLocation.getLatitude();
            mlongitude=bdLocation.getLongitude();
            //是否是第一次定位
            if (isFirstLoc) {
                isFirstLoc = false;
                flatitude=bdLocation.getLatitude();
                flongtitude=bdLocation.getLongitude();
                LatLng II = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(II, 16);
                baiduMap.animateMapStatus(u);
                String s1, s2;
                s1 = Double.toString(mlatitude);
                s2 = Double.toString(mlongitude);
                tv.setText("经度：" + s1 + " 纬度：" + s2);
                geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                       .location(II));
                maddOverlay(mlatitude,mlongitude);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main2);
        mMapView = (MapView) findViewById(R.id.bmapView);
        button=(Button)findViewById(R.id.shouButton);
        cityText=(EditText)findViewById(R.id.shouText);
        markerText=(TextView)findViewById(R.id.markerText);
        addressText=(EditText)findViewById(R.id.shouaddressText);
        shouLayout=(RelativeLayout)findViewById(R.id.shouLayout);
        tv=(TextView)findViewById(R.id.latLngText);
        baiduMap=mMapView.getMap();
        button.setOnClickListener(this);
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
        shouLayout.setVisibility(View.VISIBLE);
        initLocation();
        initMarker();

    }
    public void getWeather(final String ci){
        new Thread(new Runnable(){
            String st = null;
            public void run() {
                WeatherInfo weatherinfo=new WeatherInfo();
                try {
                    URL url=new URL("http://ws.webxml.com.cn/WebServices/WeatherWebService.asmx/getWeatherbyCityName?theCityName="+java.net.URLEncoder.encode(ci));//java.net.URLEncoder.encode(ci)
                    HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                    conn.setConnectTimeout(10000);// 设置连接超时
                    conn.setReadTimeout(5000);// 设置读取超时
                    conn.connect();
                    if ((conn.getResponseCode()) == 200)
                    {
                        // 解析返回信息
                        //parseResponseXML(conn.getInputStream());
                        st=StreamToString(conn.getInputStream());
                        weatherinfo=getWeatherInfo(st);
                        conn.disconnect();
                        Message msg=new Message();
                        msg.what=1;
                        msg.obj=weatherinfo;
                        handler.sendMessage(msg);
                    }
                    else{
                        Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    String StreamToString(InputStream in){
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int length;
        String str=null;
        try {
            while ((length = in.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            in.close();
            result.close();
            str= result.toString();
        }catch(IOException e) {

            e.printStackTrace();
        }
        return str;
    }
    WeatherInfo getWeatherInfo(String st){
        StringTokenizer tokenizer = new StringTokenizer(st,"<>");
        WeatherInfo om=new WeatherInfo();
        String value = "";
        int j=0;
        while (tokenizer.hasMoreTokens()) {
            if(value.equals("string")){
                value=tokenizer.nextToken();
                switch(j)
                {
                    case 0:om.setProvice(value);break;
                    case 1:om.setCity(value);break;
                    case 4:om.setTime(value);break;
                    case 5:om.setNowTem(value);break;
                    case 6:om.setState(value);break;
                    case 7:om.setWind(value);break;
                    case 10:om.setNowState(value);break;
                    default:break;
                }
                j++;
                if(j==11)break;
            }
            value=tokenizer.nextToken();
        }
        return om;
    }
    public void initLocation()
    {
        baiduMap.setMyLocationEnabled(true);
        locationClient =new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(myListener);
        this.setLocationOption();
        locationClient.start();
    }
    public void initMarker()
    {
        bitmap= BitmapDescriptorFactory.fromResource(R.drawable.map7);
        mMarkerlout=(RelativeLayout)findViewById(R.id.markerLayout);
    }

    public void maddOverlay(double mlatitude,double mlongitude)
    {
        baiduMap.clear();
        Marker marker=null;
        LatLng point=new LatLng(mlatitude,mlongitude);
        OverlayOptions option=new MarkerOptions().position(point).icon(bitmap).zIndex(5);
        marker=(Marker)baiduMap.addOverlay(option);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mMarkerlout.setVisibility(View.VISIBLE);
                return true;
            }
        });
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMarkerlout.setVisibility(View.GONE);
            }
            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0,MENU_TRAFFIC,0,R.string.traffic);
        menu.add(0,MENU_NORMAL,0,R.string.normal);
        menu.add(0,MENU_SATELLITE,0,R.string.satellite);
        menu.add(0,MENU_MYLOCATION,0,R.string.mylocation);
        //menu.add(0,MENU_OVERLAY,0,"显示天气");
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case MENU_TRAFFIC:
                if(baiduMap.isTrafficEnabled())
                {
                    baiduMap.setTrafficEnabled(false);
                }
                else
                {
                    baiduMap.setTrafficEnabled(true);
                }
                break;
            case MENU_NORMAL:
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case MENU_SATELLITE:
                baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case MENU_MYLOCATION:
            {
                LatLng II = new LatLng(flatitude, flongtitude);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(II, 16);
                baiduMap.animateMapStatus(u);
                maddOverlay(flatitude,flongtitude);
            }
            break;
            /*case MENU_OVERLAY:
            {
                maddOverlay(mlatitude,mlongitude);

            }
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    private void setLocationOption()
    {
        LocationClientOption option=new LocationClientOption();
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll"); //定位坐标类型
        option.setScanSpan(1000);//定位时间间隔
        option.setIsNeedAddress(true);//是否需要地址信息
        option.setNeedDeviceDirect(true);//
        locationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {

        locationClient.stop();
        baiduMap.setMyLocationEnabled(false);
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mMapView=null;
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.shouButton) {
            String cityStr,addressStr;
            cityStr=cityText.getText().toString();
            addressStr=addressText.getText().toString();
            geoCoder.geocode(new GeoCodeOption().city(cityStr).address(addressStr));
            getWeather(cityStr);
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MainActivity2.this, "抱歉，未能找到经纬度结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        LatLng latLng=new LatLng(geoCodeResult.getLocation().latitude,geoCodeResult.getLocation().longitude);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(latLng, 16);
        baiduMap.animateMapStatus(u);
        maddOverlay(geoCodeResult.getLocation().latitude,geoCodeResult.getLocation().longitude);
        //定位
        String strInfo = String.format("纬度：%f 经度：%f",
                geoCodeResult.getLocation().latitude, geoCodeResult.getLocation().longitude);
        tv.setText(strInfo);
        Toast.makeText(MainActivity2.this, strInfo, Toast.LENGTH_LONG).show();
        //text.setText(strInfo);
        //result保存地理编码的结果 城市-->坐标
    }


    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MainActivity2.this, "抱歉，未能找到地址结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        //mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
        //      .getLocation()));
        //定位
        Toast.makeText(MainActivity2.this, "您在:"+reverseGeoCodeResult.getAddress(),
                Toast.LENGTH_LONG).show();
        StringTokenizer tokenizer = new StringTokenizer(reverseGeoCodeResult.getAddress(), "市省");
        String value = "";
        int i=0;
        tokenizer.nextToken();
        value=tokenizer.nextToken();
        getWeather(value);
        //tv.setText(mlatitude,mlongitude);
        //text.setText(reverseGeoCodeResult.getAddress());
        //result保存反地理编码的结果 坐标-->城市
    }
}
