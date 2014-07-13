package com.tecco.powerup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Vendors extends Activity {

	ArrayList<Marker> addressMarkers = new ArrayList<Marker>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapfragment);
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();

		LocationAdapter gps = new LocationAdapter(this);
		if (gps.canGetLocation) {

			LatLng mapCenter = new LatLng(gps.getLatitude(), gps.getLongitude());

			map.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, 13));

			// Flat markers will rotate when the map is rotated,
			// and change perspective when the map is tilted.;

			map.addMarker(new MarkerOptions().position(
					new LatLng(gps.getLatitude(), gps.getLongitude())).title(
					"your position"));

			CameraPosition cameraPosition = CameraPosition.builder()
					.target(mapCenter).zoom(13).bearing(90).build();

			// Animate the change in camera view over 2 seconds
			map.animateCamera(
					CameraUpdateFactory.newCameraPosition(cameraPosition),
					2000, null);

			List<Address> addresses = getVendorAddress("gas stations Johannesburg");
			if (addresses != null) {
				for (Address address : addresses) {
					int numAddressLines = address.getMaxAddressLineIndex();
					String addressLines = "";
					for (int i = 0; i < numAddressLines; i++) {
						addressLines += (address.getAddressLine(i) + ",\n");
					}

					addVendorMarker(map, address.getLatitude(),
							address.getLongitude(), addressLines);
				}
				showAllMarkersOnMap(map);
			}
		}
	}

	private void showAllMarkersOnMap(GoogleMap map) {
		// Show all pins on the map
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (Marker marker : addressMarkers) {
			builder.include(marker.getPosition());
		}
        LatLngBounds bounds = builder.build();
        int padding = 0; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,700,700, padding);
        map.moveCamera(cu);
        map.animateCamera(cu);
	}

	private List<Address> getVendorAddress(String addressString) {
		Geocoder geocoder = new Geocoder(this);
		List<Address> addresses = null;
		try {
			addresses = geocoder.getFromLocationName(addressString, 5);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (addresses != null) {
			return addresses;
		} else {
			return null;
		}
	}

	private void addVendorMarker(GoogleMap map, double latitude,
			double longitude, String vendorName) {
		Marker addressMarker = map.addMarker(new MarkerOptions()
				.position(new LatLng(latitude, longitude)).title(vendorName.split(",")[0])
				.snippet(vendorName));
		addressMarkers.add(addressMarker);
	}
}
