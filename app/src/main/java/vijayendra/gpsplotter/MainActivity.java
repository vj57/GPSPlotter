package vijayendra.gpsplotter;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;




public class MainActivity extends FragmentActivity
        implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{
    private GoogleMap myMap; //map reference
    private LocationClient myLocationClient; //location client to listen to location update from location services
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)   //5 seconds
            .setFastestInterval(16)//16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected  void onResume(){
        super.onResume();
        getMapReference();
        wakeUpLocationClient();
        myLocationClient.connect();
    }


    public void onPause(){
        super.onPause();
        if(myLocationClient != null){
            myLocationClient.disconnect();
        }
    }

    /*        When we recieve focus, we need to getback our LocationClient
        creates a new LocationClient object if there is none*/
    private void gotoMyLocation(double lat, double lng) {
        changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(lat, lng))
                    .zoom(15.5f)
                    .bearing(0)
                    .tilt(25)
                    .build()
    ), new GoogleMap.CancelableCallback() {
        @Override
        public void onFinish() {
            // Your code here to do something after the Map is rendered
        }

        @Override
        public void onCancel() {
            // Your code here to do something after the Map rendering is cancelled
        }
    });
}

    private void wakeUpLocationClient(){
        if(myLocationClient == null){
            myLocationClient = new LocationClient(getApplicationContext(),
                    this,   //connection callbacks
                    this); //onConnectionFailedListener
        }
    }
    private void getMapReference() {
        if(myMap == null){
            myMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }
        if(myMap != null){
            myMap.setMyLocationEnabled(true);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        myLocationClient.requestLocationUpdates( REQUEST, this);
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    private void changeCamera(CameraUpdate update, GoogleMap.CancelableCallback callback) {
        myMap.moveCamera(update);
    }

}
