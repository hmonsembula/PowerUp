package com.tecco.powerup;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Vendors extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapfragment);
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		
		LocationAdapter gps = new LocationAdapter(this);
		if(gps.canGetLocation){

		LatLng mapCenter = new LatLng(gps.getLatitude(), gps.getLongitude());

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, 13));

		// Flat markers will rotate when the map is rotated,
		// and change perspective when the map is tilted.;

		map.addMarker(new MarkerOptions()
		        .position(new LatLng(gps.getLatitude(), gps.getLongitude()))
		        .title("Hello world"));
		
		CameraPosition cameraPosition = CameraPosition.builder()
				.target(mapCenter).zoom(13).bearing(90).build();

		// Animate the change in camera view over 2 seconds
		map.animateCamera(
				CameraUpdateFactory.newCameraPosition(cameraPosition), 2000,
				null);
		}
	}

}
