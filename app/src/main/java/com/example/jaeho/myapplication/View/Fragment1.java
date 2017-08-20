package com.example.jaeho.myapplication.View;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaeho.myapplication.Controler.GPSUpdateService;
import com.example.jaeho.myapplication.R;
import com.example.jaeho.myapplication.utils.Constants;
import com.example.jaeho.myapplication.utils.NMapPOIflagType;
import com.example.jaeho.myapplication.utils.NMapViewerResourceProvider;
import com.example.jaeho.myapplication.utils.PermissionActivity;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

import static com.example.jaeho.myapplication.utils.Constants.doSiDongName;
import static com.example.jaeho.myapplication.utils.Constants.myLat;
import static com.example.jaeho.myapplication.utils.Constants.myLng;
import static com.example.jaeho.myapplication.utils.Constants.tostost;


/**
 * Created by jaeho on 2017. 8. 19..
 */

public class Fragment1 extends PermissionActivity {
    private NMapContext mMapContext;
    private NMapView mapView;
    public double lat,lng;
    public boolean flag=false;
    private boolean serviceFlag = true;
    public Fragment1(){
        //\init();
    }
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] CAMERA_PERMS={
            Manifest.permission.CAMERA
    };
    private static final String[] CONTACTS_PERMS={
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int CAMERA_REQUEST=INITIAL_REQUEST+1;
    private static final int CONTACTS_REQUEST=INITIAL_REQUEST+2;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    private static final String CLIENT_ID = "EzpY2nBNeLRnW5p6Z50T";// 애플리케이션 클라이언트 아이디 값
    NMapLocationManager nMapLocationManager;
    NMapPlacemark nMapPlacemark;
    NMapOverlayManager mOverlayManager;
    NMapViewerResourceProvider vr;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment1, container, false);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapContext =  new NMapContext(super.getActivity());
        mMapContext.onCreate();

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapView = (NMapView)getView().findViewById(R.id.mapView);
        mapView.setClientId(CLIENT_ID);// 클라이언트 아이디 설정
        mapView.setClickable(true);
        mapView.setEnabled(true);
        mapView.setFocusable(true);
        mapView.setFocusableInTouchMode(true);
        mapView.requestFocus();
        mMapContext.setupMapView(mapView);
        mMapContext.setMapDataProviderListener(onDataProviderListener);
        nMapLocationManager = new NMapLocationManager(getContext());
        vr = new NMapViewerResourceProvider(getActivity());
        mOverlayManager = new NMapOverlayManager(getActivity(),mapView,vr);

        init();


    }
    public void init(){
        if(Constants.checkFineLocationPermission(Fragment1.this)||Constants.checkCoarseLocationPermission(Fragment1.this)) {
            nMapLocationManager.enableMyLocation(true);
            nMapLocationManager.setOnLocationChangeListener(new NMapLocationManager.OnLocationChangeListener() {
                @Override
                public boolean onLocationChanged(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {
                    myLng = nMapLocationManager.getMyLocation().getLongitude();
                    myLat = nMapLocationManager.getMyLocation().getLatitude();

                    mOverlayManager.clearOverlays();
                    NMapPOIdata poiData = new NMapPOIdata(2, vr);
                    poiData.beginPOIdata(2);

                    if(flag){
                        mMapContext.findPlacemarkAtLocation(lng,lat);
                        poiData.addPOIitem(lng, lat, " 도와주세요!", NMapPOIflagType.PIN, null);
                        mapView.getMapController().animateTo(new NGeoPoint(lng,lat));
                    }

                        NMapPOIitem item = poiData.addPOIitem(myLng, myLat, "현재 내 위치", NMapPOIflagType.PIN, null);
                        mMapContext.findPlacemarkAtLocation(myLng,myLat);
                        mapView.getMapController().animateTo(new NGeoPoint(myLng,myLat));

                    poiData.endPOIdata();
                    NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
                    // select an item
                    poiDataOverlay.selectPOIitem(0, true);
                    // tostost(myLat+" "+myLng,getActivity());
                    if(serviceFlag){
                        getContext().startService(new Intent(getContext(),GPSUpdateService.class));
                        serviceFlag=false;
                    }
                    return true;
                }

                @Override
                public void onLocationUpdateTimeout(NMapLocationManager nMapLocationManager) {

                }

                @Override
                public void onLocationUnavailableArea(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {

                }
            });

        }

    }
    private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {
        @Override
        public void onReverseGeocoderResponse(NMapPlacemark nMapPlacemark, NMapError nMapError) {
            if(nMapError!=null){
             //   Toast.makeText(getActivity(), nMapError.toString(), Toast.LENGTH_SHORT).show();
                return;
            }
           // Toast.makeText(getActivity(), nMapPlacemark.toString(), Toast.LENGTH_SHORT).show();
            doSiDongName = nMapPlacemark.toString();
            ((TextView)getActivity().findViewById(R.id.locationText)).setText(doSiDongName);

        }
    };
    @Override
    public void onStart(){
        super.onStart();
        mMapContext.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapContext.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapContext.onPause();
    }
    @Override
    public void onStop() {
        mMapContext.onStop();
        super.onStop();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        mMapContext.onDestroy();
        super.onDestroy();
    }
    public Context getContext(){
        return getActivity();
    }
}