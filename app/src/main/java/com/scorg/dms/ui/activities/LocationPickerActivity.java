package com.scorg.dms.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.scorg.dms.R;
import com.scorg.dms.adapters.chat.LocationAdapter;
import com.scorg.dms.helpers.database.AppDBHelper;
import com.scorg.dms.model.login.ClinicList;
import com.scorg.dms.model.login.LoginModel;
import com.scorg.dms.util.DMSConstants;
import com.scorg.dms.util.location.MyLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LocationPickerActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, LocationAdapter.ItemListener {

    protected static final String TAG = "location-updates";

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 10;
    public static final int LOCATION_REQUEST = 3212;
    public static final String LAT_LONG = "lat_long";

    private GoogleApiClient mGoogleApiClient;

    @BindView(R.id.detectLocation)
    Button detectLocation;

    @BindView(R.id.locationList)
    RecyclerView locationList;

    private MyLocation myLocation;
    private GoogleMap googleMap;
    private AppDBHelper dbHelper;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);
        ButterKnife.bind(this);

        dbHelper = new AppDBHelper(this);

        buildGoogleApiClient();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Gson gson = new Gson();
        LoginModel loginModel = gson.fromJson(getOfflineData(DMSConstants.TASK_LOGIN), LoginModel.class);

        ArrayList<ClinicList> clinicList = loginModel.getDoctorLoginData().getDocDetail().getClinicList();
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        locationList.setLayoutManager(mLayoutManager);
        LocationAdapter locationAdapter = new LocationAdapter(clinicList, LocationPickerActivity.this);
        locationList.setAdapter(locationAdapter);
    }

    private String getOfflineData(String mDataTag) {
        if (dbHelper.dataTableNumberOfRows(mDataTag) > 0) {
            Cursor cursor = dbHelper.getData(mDataTag);
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(AppDBHelper.COLUMN_DATA));
        } else return "{}";
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void startUpdatesButtonHandler() {
        if (!isPlayServicesAvailable(this)) return;

        if (Build.VERSION.SDK_INT < 23) {
            myLocation.startLocationUpdates();
            detectLocation.setEnabled(false);
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myLocation.startLocationUpdates();
            detectLocation.setEnabled(false);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                showRationaleDialog();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myLocation.startLocationUpdates();
                    detectLocation.setEnabled(false);
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Toast.makeText(LocationPickerActivity.this, "Location Permission required.", Toast.LENGTH_SHORT).show();
                        detectLocation.setEnabled(true);
                    } else {
                        showRationaleDialog();
                    }
                }
                break;
            }
        }
    }

    private void showRationaleDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(LocationPickerActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                })
                .setCancelable(false)
                .setMessage("Location Permission needed for sharing location.")
                .show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    }

    public static boolean isPlayServicesAvailable(Context context) {

        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog((Activity) context, resultCode, 2).show();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        myLocation.startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        isPlayServicesAvailable(this);

        myLocation = new MyLocation(this, mGoogleApiClient, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                detectLocation.setEnabled(true);
                String address = getAddress(location.getLatitude(), location.getLongitude());
                setMarker(new LatLng(location.getLatitude(), location.getLongitude()), address);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            myLocation.stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        myLocation.startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setPadding(0, getResources().getDimensionPixelSize(R.dimen.dp50), 0, 0);
        setMarker(myLocation.getLastLocation() == null ? new LatLng(18.5204, 73.8567) : new LatLng(myLocation.getLastLocation().getLatitude(), myLocation.getLastLocation().getLongitude()), "Current Location"); // default location
        myLocation.startLocationUpdates();
    }

    @SuppressLint("CheckResult")
    private void showMapDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.map_dialog_ok);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        String latLong = latLng.latitude + ", " + latLng.longitude;
        String urlWithLatLong = "https://maps.googleapis.com/maps/api/staticmap?center=" + latLong + "&markers=color:red%7Clabel:D%7C" + latLong + "&zoom=14&size=400x280";
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.staticmap);
        requestOptions.error(R.drawable.staticmap);

        Glide.with(dialog.findViewById(R.id.staticMap).getContext())
                .load(urlWithLatLong)
                .apply(requestOptions)
                .into((ImageView) dialog.findViewById(R.id.staticMap));

        dialog.findViewById(R.id.changeLocationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.shareLocationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra(LAT_LONG, latLng);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        dialog.show();
    }

    private void setMarker(LatLng latLng, String address) {
        googleMap.clear();
        this.latLng = latLng;
        googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title(address));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(LocationPickerActivity.this, Locale.getDefault());
        String address = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

            if (!addresses.isEmpty()) {
                Address obj = addresses.get(0);
                address = getArea(obj) + ", " + obj.getLocality();
            } else {
                Toast.makeText(this, "Address not found.", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // update UI
        detectLocation.setText(address);

        return address;
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return p1;
    }

    private String getArea(Address obj) {

        if (obj.getThoroughfare() != null)
            return obj.getThoroughfare();
        else if (obj.getSubLocality() != null)
            return obj.getSubLocality();
        else if (obj.getSubAdminArea() != null)
            return obj.getSubAdminArea();
        else if (obj.getLocality() != null)
            return obj.getLocality();
        else if (obj.getAdminArea() != null)
            return obj.getAdminArea();
        else
            return obj.getCountryName();
    }

    @OnClick({R.id.detectLocation, R.id.selectLocationButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.detectLocation:
                startUpdatesButtonHandler();
                break;
            case R.id.selectLocationButton:
                showMapDialog();
                break;
        }
    }

    @Override
    public void onClinicClick(ClinicList clinicList) {
        myLocation.stopLocationUpdates();
        detectLocation.setEnabled(true);
        LatLng locationFromAddress = getLocationFromAddress(clinicList.getClinicAddress());
        // update UI
        detectLocation.setText(clinicList.getClinicAddress());
        setMarker(locationFromAddress, clinicList.getClinicAddress());
    }
}
