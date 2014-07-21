package com.tecco.powerup;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Vendors extends Activity {
	ArrayList<Marker> merchantMarkers = new ArrayList<Marker>();
	ArrayList<Merchant> merchants = new ArrayList<Merchant>();
	private double myLongitude;
	private double myLatitude;
	private LatLng myLocation;
	private Marker myMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapfragment);

		MerchantXmlParser merchantParser = new MerchantXmlParser();
		InputStream merchantsXmlFile = null;
		try {
			merchantsXmlFile = getApplicationContext().getAssets().open(
					"vendors.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			merchants = (ArrayList<Merchant>) merchantParser
					.parse(merchantsXmlFile);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();

		LocationAdapter gps = new LocationAdapter(this);
		map.setMyLocationEnabled(false);
		if (gps.canGetLocation) {
			LatLng mapCenter = new LatLng(gps.getLatitude(), gps.getLongitude());
			// map.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, 13));
			// Flat markers will rotate when the map is rotated,
			// and change perspective when the map is tilted.;
			myLongitude = gps.getLongitude();
			myLatitude = gps.getLatitude();
			myLocation = new LatLng(myLatitude, myLongitude);

			myMarker = map.addMarker(new MarkerOptions()
					.position(new LatLng(myLatitude, myLongitude))
					.title("your position")
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

			CameraPosition cameraPosition = CameraPosition.builder()
					.target(mapCenter).zoom(13).bearing(90).build();

			// Animate the change in camera view over 2 seconds
			map.animateCamera(
					CameraUpdateFactory.newCameraPosition(cameraPosition),
					2000, null);

			// List<Address> addresses =
			// getVendorAddress("gas stations Johannesburg");
			// if (addresses != null) {
			// for (Address address : addresses) {
			// int numAddressLines = address.getMaxAddressLineIndex();
			// String addressLines = "";
			// for (int i = 0; i < numAddressLines; i++) {
			// addressLines += (address.getAddressLine(i) + ",\n");
			// }

			for (Merchant merchant : merchants) {
				addVendorMarker(map, merchant);
				merchant.setDistanceFromMyLocation(calculateDistanceFromLyLocation(
						myLocation, merchant));
			}
		}
		Collections.sort(merchants);
		for (Merchant merchant : merchants) {
			Log.d(merchant.name,
					String.valueOf(merchant.getDistanceFromMyLocation()));
		}
		showMarkersOnMap(map);
	}

	private double calculateDistanceFromLyLocation(LatLng myLocation,
			Merchant merchant) {
		double destinationLat = merchant.getLatitude();

		double destinationLongitude = merchant.getLongitude();
		double sourceLatitude = myLocation.latitude;
		double sourceLongitude = myLocation.longitude;

		double earthRadius = 3958.75;
		double dLat = Math.toRadians(destinationLat - sourceLatitude);
		double dLng = Math.toRadians(destinationLongitude - sourceLongitude);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(sourceLatitude))
				* Math.cos(Math.toRadians(destinationLat)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;
		int meterConversion = 1609;
		return Double.valueOf(dist * meterConversion).floatValue();

	}

	private void showMarkersOnMap(GoogleMap map) {
		// Show all pins on the map
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		if (merchants.size() > 5) {
			for (int i = 0; i < 5; i++) {
				Marker marker = merchants.get(i).getMarker();
				builder.include(marker.getPosition());
				marker.setVisible(true);
			}
		} else {
			for (int i = 0; i < merchants.size(); i++) {
				Marker marker = merchants.get(i).getMarker();
				builder.include(marker.getPosition());
				marker.setVisible(true);
			}
		}

		builder.include(myMarker.getPosition());
		LatLngBounds bounds = builder.build();
		int padding = 0; // offset from edges of the map in pixels
		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 1000,
				1000, padding);
		map.moveCamera(cu);
		map.animateCamera(cu);
	}

	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
	private void addVendorMarker(GoogleMap map, double latitude,
			double longitude, String vendorName, String address) {
		Marker addressMarker = map.addMarker(new MarkerOptions()
				.position(new LatLng(latitude, longitude)).title(vendorName)
				.visible(false).snippet(address));
		merchantMarkers.add(addressMarker);
	}

	private void addVendorMarker(GoogleMap map, Merchant merchant) {
		Marker merchantMarker = map.addMarker(new MarkerOptions()
				.position(
						new LatLng(merchant.getLatitude(), merchant
								.getLongitude())).title(merchant.getName())
				.visible(false).snippet(merchant.getAddress()));
		merchant.setMarker(merchantMarker);
	}

}