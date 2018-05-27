package mysalary.dj.com.mysalary;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class Spending extends Fragment{
    private View searchLocation;
    private FusedLocationProviderClient client;
    LocationRequest locationRequest;
    LocationCallback locationCallback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.spending, container, false);
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.category);
        this.searchLocation = (Button) rootView.findViewById(R.id.searchLocation);

        List<String> categories = new ArrayList<String>();
        categories.add("Food");
        categories.add("Grocer");
        categories.add("Entertainment");
        categories.add("Gadgets");
        categories.add("Bills");
        categories.add("Others");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        client = LocationServices.getFusedLocationProviderClient(getActivity());
        return rootView;
    }

    @Override
    public void onViewCreated(final View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        this.searchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!runtime_permission()) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                String text = "" + location.getLatitude() + " " + location.getLongitude();
                                Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                                //TextView locationText = rootView.findViewById(R.id.locationText);
                                //locationText.setText(text);
                            }
                            else{
                                final LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE );
                                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                                    Toast.makeText(getContext(), "Please Turn On Your GPS Setting", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }
                                else {
                                    Toast.makeText(getContext(), "Location Loading..", Toast.LENGTH_LONG).show();
                                    requestLocationUpdate();
                                    buildLocationCallBack();
                                    client.requestLocationUpdates(locationRequest,locationCallback,Looper.myLooper());
                                }

                            }
                        }
                    });
                }
            }
        });
    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for(Location location:locationResult.getLocations()){
                    Toast.makeText(getContext(), "haha"+String.valueOf(location.getLatitude())+" "+location.getLongitude(), Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void requestLocationUpdate() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(3000);
        locationRequest.setInterval(5000);
        locationRequest.setSmallestDisplacement(10);
    }

    private boolean runtime_permission() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==100 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){

            }
            else{
                Toast.makeText(getContext(),"Location Permission is needed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
