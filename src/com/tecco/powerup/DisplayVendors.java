package com.tecco.powerup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.thoughtworks.xstream.XStream;

public class DisplayVendors extends Activity {
	ArrayList<Marker> merchantMarkers = new ArrayList<Marker>();
	ArrayList<Merchant> merchants = new ArrayList<Merchant>();
	private double myLongitude;
	private double myLatitude;
	private LatLng myLocation;
	private Marker myMarker;
	RandomAccessFile writer;
	BufferedReader reader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapfragment);

		VendorsUpdate task = new VendorsUpdate();
		try {
			task.execute(new URL(
					"http://www.vogella.com/tutorials/AndroidBackgroundProcessing/article.html#androidbackground"));
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		MerchantXmlParser merchantParser = new MerchantXmlParser();
		FileInputStream merchantsXmlFile = null;
		try {
			merchantsXmlFile = new FileInputStream(getApplicationContext()
					.getFilesDir() + "/dbTest.xml");
			Log.i("Files", "File Opened");
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

			map.setOnMarkerClickListener((new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker arg0) {
					Toast.makeText(getBaseContext(),
							"Click on marker information to navigate there",
							Toast.LENGTH_LONG).show();
					return false;
				}
			}));

			map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker arg0) {
					Intent intent = null;
					if (!arg0.getTitle().equals("your position")) // if marker
																	// source is
																	// clicked
						intent = new Intent(Intent.ACTION_VIEW, Uri
								.parse("google.navigation:q="
										+ arg0.getPosition().latitude + ","
										+ arg0.getPosition().longitude));
					startActivity(intent);
				}

			});

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
			Log.d(merchant.getName(),
					String.valueOf(merchant.getDistanceFromMyLocation()));
		}
		showMarkersOnMap(map);
	}

	protected void writeOuterXMLTagsAfter() {
		try {
			writer.write("</feed>".getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void writeOuterXMLTagsBefore() {
		try {
			writer.seek(0);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			writer.write(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
					+ "<feed xmlns=\"http://www.w3.org/2005/Atom\" xmlns:creativeCommons=\"http://backend.userland.com/BiteMecreativeCommonsRssModule\" >\n")
					.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createAndShowDialog(Exception exception, String title) {
		createAndShowDialog(exception.toString(), title);
	}

	/**
	 * Creates a dialog and shows it
	 * 
	 * @param message
	 *            The dialog message
	 * @param title
	 *            The dialog title
	 */
	private void createAndShowDialog(String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(message);
		builder.setTitle(title);
		builder.create().show();
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
				.visible(false).snippet(merchant.getmAddress()));
		merchant.setMarker(merchantMarker);
	}

	private void createXMLEntry(Merchant item) throws IOException {
		item.setLatitude(Double
				.parseDouble(item.getmCoordinates().split(",")[0]));
		item.setLongitude(Double
				.parseDouble(item.getmCoordinates().split(",")[1]));

		XStream xstream = new XStream();
		xstream.omitField(Merchant.class, "distanceFromMyLocation");
		xstream.omitField(Merchant.class, "mId");
		xstream.alias("merchant", Merchant.class);
		xstream.aliasField("name", Merchant.class, "mMerchant");
		xstream.aliasField("telNumber", Merchant.class, "mTelNumber");
		xstream.aliasField("address", Merchant.class, "mAddress");
		xstream.aliasField("area", Merchant.class, "mArea");
		// xstream.aliasField("id", Merchant.class, "mId");
		String xml = xstream.toXML(item) + "\n";
		writer.write(xml.getBytes());

		// String line;
		// while ((line = reader.readLine()) != null) {
		// Log.d("dbTest", "<item>" + item.getmCoordinates() + "</item>" +
		// "\n");
		// }
	}

	private class VendorsUpdate extends AsyncTask<URL, Integer, Boolean> {
		Boolean isSuccessful = false;
		private MobileServiceClient mClient;
		private MobileServiceTable<Merchant> mVendors;

		protected Boolean doInBackground(URL... urls) {
			// android.os.Debug.waitForDebugger();
			try {
				// Create the Mobile Service Client instance, using the provided
				// Mobile Service URL and key
				writer = new RandomAccessFile(getApplicationContext()
						.getFilesDir() + "/dbTest.xml", "rw");
				// reader = new BufferedReader(new FileReader(
				// getApplicationContext().getFilesDir() + "/dbTest.xml"));
				mClient = new MobileServiceClient(
						"https://powerupmobileservice.azure-mobile.net/",
						"aqZSDIbTNEMJcitHFpxitOnkbzmgSQ63",
						DisplayVendors.this.getBaseContext());

				// Get the Mobile Service Table instance to use
				mVendors = mClient.getTable("Vendors", Merchant.class);
			} catch (MalformedURLException e) {
				Toast.makeText(
						DisplayVendors.this.getBaseContext(),
						"There was an error creating the Mobile Service. Verify the URL",
						Toast.LENGTH_SHORT).show();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mVendors.execute(new TableQueryCallback<Merchant>() {
				public void onCompleted(List<Merchant> result, int count,
						Exception exception, ServiceFilterResponse response) {
					if (exception == null) {
						Log.d("MS", "I made it here");
						isSuccessful = true;
						writeOuterXMLTagsBefore();
						for (Merchant item : result) {
							try {
								createXMLEntry(item);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						writeOuterXMLTagsAfter();
						try {
							writer.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						// Log.e("Azure", "General Error");
						createAndShowDialog(exception, "Error");
					}

				}
			});
			return isSuccessful;
		}

		protected void onPostExecute(Boolean result) {
		}
	}
}