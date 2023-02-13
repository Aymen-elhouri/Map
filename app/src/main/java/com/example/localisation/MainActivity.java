package com.example.localisation;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button bt_location ;
    TextView latitude ;
    TextView localite ;
    TextView longitude ;
    TextView pay ;
    TextView adresse ;
    LocationManager locationManager;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        function();
        checkAndRequestLocationPermission();


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

           bt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(MainActivity.this , "Please enable GPS", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
                getLoc();
            }
        });


    }

    private void function(){
        bt_location = findViewById(R.id.bt_location);
        latitude = findViewById(R.id.latitude);
        localite = findViewById(R.id.localite);
        longitude = findViewById(R.id.longitude);
        pay = findViewById(R.id.pay);
        adresse = findViewById(R.id.adresse);
    }

    private void checkAndRequestLocationPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void getLoc(){
        checkAndRequestLocationPermission();
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            private static final String TAG = "MainActivity";
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    try {
                        //initialiser geocoder
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        //initialiser l’adresse de localisation
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                location.getLongitude(), 1);
                        //afficher la latitude dans le textview
                        latitude.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Latitude : </b><br></font>"
                                        + addresses.get(0).getLatitude()
                        ));

                        // afficher la longitude dans le textview
                        longitude.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Longitude : </b><br></font>"
                                        + addresses.get(0).getLongitude()
                        ));
                        // afficher le pays dans le textview
                        pay.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Nom de pays : </b><br></font>"
                                        + addresses.get(0).getCountryName()
                        ));
                        // afficher la localité dans le textview
                        localite.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Localité : </b><br></font>"
                                        + addresses.get(0).getLocality()
                        ));
                        // afficher l’adresse dans le textview
                        adresse.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Adresse : </b><br></font>"
                                        + addresses.get(0).getAddressLine(0)
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Aucune position enregistrée",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        fusedLocationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}