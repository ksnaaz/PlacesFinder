package com.app.placesfinderpoc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;
    ProgressDialog progressDialog;
    Context context;
    String type;
    Double currentLat,currentLong;

    public GetNearbyPlacesData(Context con,String locationType,double lat, double longt){
        this.context=con;
        this.type=locationType;
        this.currentLat=lat;
        this.currentLong=longt;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showDialog("Finding your near by "+type);
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        hideDialog();
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            if(type.equalsIgnoreCase("Airport")){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.airport_marker));
            }else if(type.equalsIgnoreCase("ATM")){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.atm_marker));
            }else if(type.equalsIgnoreCase("Bank")){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bank_marker));
            }else if(type.equalsIgnoreCase("Fire Station")){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.fire_station));
            }else if(type.equalsIgnoreCase("Hospital")){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital));
            }else if(type.equalsIgnoreCase("Post Office")){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.post_office));
            }else if(type.equalsIgnoreCase("Police Station")){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.police_station));
            }else if(type.equalsIgnoreCase("Restaurant")){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant_marker));
            }else {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Double latitude = marker.getPosition().latitude;
                Double longitude = marker.getPosition().longitude;


                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr="+ currentLat + "," + currentLong + "&daddr=" + latitude + "," + longitude));
                intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
                context.startActivity(intent);

                return true;
            }
        });
    }



    public void showDialog(String msg) {
        if (progressDialog != null) {
            progressDialog = null;
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please Wait!");
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideDialog() {
        if (progressDialog != null ) {
            progressDialog.dismiss();
        }
    }
}
