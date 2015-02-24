package com.thwrayman.matchar;

import android.content.res.Configuration;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.location.Criteria;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.beyondar.android.fragment.BeyondarFragmentSupport;
        import com.beyondar.android.fragment.*;
        import com.beyondar.android.view.BeyondarGLSurfaceView;
        import com.beyondar.android.world.*;

        import android.support.v4.app.FragmentActivity;
        import android.support.v4.app.FragmentManager;
        import android.app.*;




        import android.os.Bundle;
        import android.app.Activity;
        import android.view.Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class GoToCam extends FragmentActivity implements LocationListener {

    private BeyondarFragmentSupport mBeyondarFragment;

    private LocationManager locationManager;
    private String provider;
    String longitude;
    String latitude;

    // ...
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_cam);


        //create a beyondar fragment
        mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(R.id.beyondarFragment);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



        //check if GPS is active
        if (!((LocationManager) this.getSystemService(Context.LOCATION_SERVICE))
                .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
            //prompt user to enable gps
        }



        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);






        World world = new World(this);

        // The user can set the default bitmap. This is useful if you are
        // loading images form Internet and the connection get lost
        world.setDefaultImage(R.drawable.ic_action_person);

        // User position (you can change it using the GPS listeners form Android
        // API)
        world.setGeoPosition(41.26533734214473d, 1.925848038959814d);

        // Create an object with an image in the app resources.
        GeoObject go1 = new GeoObject(1l);
        go1.setGeoPosition(41.26523339794433d, 1.926036406654116d);
        go1.setImageResource(R.drawable.ic_action_person);
        go1.setName("message");

        // Is it also possible to load the image asynchronously form internet
        GeoObject go2 = new GeoObject(2l);
        go2.setGeoPosition(41.26518966360719d, 1.92582424468222d);
        go2.setImageUri("http://beyondar.com/sites/default/files/logo_reduced.png");
        go2.setName("Online image");

        // Also possible to get images from the SDcard
        GeoObject go3 = new GeoObject(3l);
        go3.setGeoPosition(41.26550959641445d, 1.925873388087619d);
        go3.setImageUri("/sdcard/someImageInYourSDcard.jpeg");
        go3.setName("IronMan from sdcard");

        // And the same goes for the app assets
        GeoObject go4 = new GeoObject(4l);
        go4.setGeoPosition(41.26518862002349d, 1.925662767707665d);
        go4.setImageUri("assets://creature_7.png");
        go4.setName("Image from assets");

        // We add this GeoObjects to the world
        world.addBeyondarObject(go1);
        world.addBeyondarObject(go2);
        world.addBeyondarObject(go3);
        world.addBeyondarObject(go4);

        // Finally we add the Wold data in to the fragment
        mBeyondarFragment.setWorld(world);


    }

    public void onClickBeyondarObject(ArrayList<BeyondarObject> beyondarObjects) {
        // The first element in the array belongs to the closest BeyondarObject
        Toast.makeText(this, "Clicked on: " + beyondarObjects.get(0).getName(), Toast.LENGTH_LONG).show();
    }

    public void onTouchBeyondarView(MotionEvent event, BeyondarGLSurfaceView beyondarView) {


        float x = event.getX();
        float y = event.getY();

        ArrayList<BeyondarObject> geoObjects = new ArrayList<BeyondarObject>();

        // This method call is better to don't do it in the UI thread!
        // This method is also available in the BeyondarFragment
       mBeyondarFragment.getBeyondarObjectsOnScreenCoordinates(x, y, geoObjects);

        String textEvent = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                textEvent = "Event type ACTION_DOWN: ";
                break;
            case MotionEvent.ACTION_UP:
                textEvent = "Event type ACTION_UP: ";
                break;
            case MotionEvent.ACTION_MOVE:
                textEvent = "Event type ACTION_MOVE: ";
                break;
            default:
                break;
        }


        Iterator<BeyondarObject> iterator = geoObjects.iterator();
        while (iterator.hasNext()) {
            BeyondarObject geoObject = iterator.next();
            // ...
            // Do something to interact with server
            // ...
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // ignore orientation/keyboard change
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
/*        getMenuInflater().inflate(R.menu.activity_go_to_cam, menu);*/
        return true;
    }


    //GPS COORDINATES



    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public void onLocationChanged(Location location) {
        double lat = (location.getLatitude());
        double lng =  (location.getLongitude());
        latitude = String.valueOf(lat);
        longitude = String.valueOf(lng);

        Log.d("latitude: ",latitude);
        Log.d("longitude: ", longitude);

        //write the co-ordinates to file
        try {
            writeToFile("Time: "+getTimeDate() +", latitude: " + latitude + ", longitude: " + longitude + "\n", 1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public int getTimeDate(){
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        return seconds;
    }


    private void writeToFile(String data, int i) throws FileNotFoundException {

        String path = this.getFilesDir().getAbsolutePath();

        File file = new File(path + "/userData.txt");

        FileOutputStream stream = new FileOutputStream(file,true);

        String newline = "/n";
        //if we're writing new details, wipe any existing ones
        if(i==0){
            FileOutputStream stream1 = new FileOutputStream(file,false);
            String blank = "";
            try {
                stream1.write(blank.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            stream.write(data.getBytes());
            stream.write(newline.getBytes());
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }





    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }




    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}

