package com.ceydanursimsek.weatherapp.view;

import static com.ceydanursimsek.weatherapp.utils.API_KEY;
import static com.ceydanursimsek.weatherapp.utils.BASE_URL;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ceydanursimsek.weatherapp.R;
import com.ceydanursimsek.weatherapp.model.WeatherResponse;
import com.ceydanursimsek.weatherapp.service.WeatherAPI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ceydanursimsek.weatherapp.databinding.ActivityMapsBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {
    Retrofit retrofit;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ActivityResultLauncher<String> permissionLauncher; //herhangi bir metod altında yazıp oncreate altında register(kayıt)etmek zorundayız.
    LocationManager locationManager;
    LocationListener locationListener;
    private WeatherResponse weatherResponse;
    private boolean isCameraMoved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        registerLauncher();

        //Retrofit&JSON
        Gson gson=new GsonBuilder().setLenient().create();
        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);//oluşturduğum arayüzü ve override ettiğim methodu güncel haritamda kullanıcağımı söylüyorum

        //casting
         locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
         locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {//konum değiştiğinde ne yapacak

                LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 14));
                    isCameraMoved = true; // Kamerayı hareket ettirdiğini belirt

                    mMap.addMarker(new MarkerOptions().position(currentLoc).title("You are here."));

                loadData(location.getLatitude(),location.getLongitude(),false);
                /*String locationInfo = String.format("Lat: %.2f, Lng: %.2f", location.getLatitude(), location.getLongitude());
                binding.phoneLocation.setText(locationInfo);*/
            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) //eğer izin verilmediyse
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                Snackbar.make(binding.getRoot(),"Permission needed for maps",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() { //buton setAction ,butona tıklandığında izin göstermek istiyorum
                    @Override
                    public void onClick(View v) { //izni nasıl istiycez? ActivityResultLauncher larla yapacağız
                        //request permission
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                    }
                }).show();
            }else{
                //request permission
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);

            }


        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,20000,locationListener);
        }

        /*// Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }
    private void registerLauncher(){
        permissionLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean o) {
                if(o){
                    if(ContextCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        //izin verildi permission granted
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,20000,locationListener);}
                }else{
                    //permission denied
                    Toast.makeText(MapsActivity.this, "Permission needed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void loadData(double latitude, double longitude,boolean isForSelectedLocation){
        WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);
        Call<WeatherResponse> call=weatherAPI.getCurrentWeather(latitude,longitude,API_KEY);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if(response.isSuccessful()){
                WeatherResponse weatherResponse=new WeatherResponse();
                weatherResponse=response.body();
                if(weatherResponse!=null){

                    String weatherInfo = String.format("%s,%.2f°C, %.0f",
                            weatherResponse.getTimezone(),
                            (weatherResponse.getCurrent().getTemp())-273.15,    // %.2f -> Double değer, iki ondalık basamaklı olarak gösterilir.
                            weatherResponse.getCurrent().getHumidity() // %.0f -> Double değer, tam sayı gibi gösterilir.
                    );

                    if(isForSelectedLocation){
                        binding.selectedLocation.setText(weatherInfo);
                    }else{
                        binding.phoneLocation.setText(weatherInfo);
                    }



                }else{
                    System.out.println("response code:"+response.code());
                }
                }

            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                System.out.println("API call failed: " + t.getMessage());
            }
        });

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng)); //tıklanılan yere marker koy

        loadData(latLng.latitude, latLng.longitude,true);




    }
}