package com.tecco.powerup;

import com.google.android.gms.maps.model.Marker;

public class Merchant implements Comparable<Merchant> {
	String name, telNumber, address;
	double latitude, longitude;
	double distanceFromMyLocation;
	private Marker marker;

	public Merchant(String name, String telNumber, double latitude,
			double longitude) {
		this.name = name;
		this.telNumber = telNumber;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Merchant(String name, String telNumber, double latitude,
			double longitude, String address) {
		this.name = name;
		this.telNumber = telNumber;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
	}

	public double getDistanceFromMyLocation() {
		return distanceFromMyLocation;
	}

	public void setDistanceFromMyLocation(double distanceFromMyLocation) {
		this.distanceFromMyLocation = distanceFromMyLocation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumner(String telNumner) {
		this.telNumber = telNumner;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public int compareTo(Merchant another) {
		if (this.distanceFromMyLocation < another.distanceFromMyLocation) {
			return -1;
		} else if (this.distanceFromMyLocation > another.distanceFromMyLocation) {
			return 1;
		}
		return 0;
	}

	public void setMarker(Marker merchantMarker) {
		marker = merchantMarker;
		
	}

	public Marker getMarker() {
		return marker;
	}

}
