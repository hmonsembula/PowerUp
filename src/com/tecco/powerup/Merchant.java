package com.tecco.powerup;

import com.google.android.gms.maps.model.Marker;

public class Merchant implements Comparable<Merchant> {
	double latitude, longitude;
	double distanceFromMyLocation;

	@com.google.gson.annotations.SerializedName("Area")
	private String mArea;

	@com.google.gson.annotations.SerializedName("id")
	private String mId;

	@com.google.gson.annotations.SerializedName("Merchant")
	private String mMerchant;

	@com.google.gson.annotations.SerializedName("TelNumber")
	private String mTelNumber;

	@com.google.gson.annotations.SerializedName("Address")
	private String mAddress;

	@com.google.gson.annotations.SerializedName("Coordinates")
	private String mCoordinates;

	private Marker marker;

	public Merchant(String name, String telNumber, double latitude,
			double longitude) {

	}

	public Merchant(String name, String telNumber, double latitude,
			double longitude, String address) {
		mMerchant = name;
		mTelNumber = telNumber;
		this.longitude = longitude;
		this.latitude = latitude;
		mAddress = address;
	}

	public double getDistanceFromMyLocation() {
		return distanceFromMyLocation;
	}

	public void setDistanceFromMyLocation(double distanceFromMyLocation) {
		this.distanceFromMyLocation = distanceFromMyLocation;
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

	public String getmArea() {
		return mArea;
	}

	public void setmArea(String mArea) {
		this.mArea = mArea;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmMerchant() {
		return mMerchant;
	}

	public void setmMerchant(String mMerchant) {
		this.mMerchant = mMerchant;
	}

	public String getmTelNumber() {
		return mTelNumber;
	}

	public void setmTelNumber(String mTelNumber) {
		this.mTelNumber = mTelNumber;
	}

	public String getmAddress() {
		return mAddress;
	}

	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	public String getmCoordinates() {
		return mCoordinates;
	}

	public void setmCoordinates(String mCoordinates) {
		this.mCoordinates = mCoordinates;

	}

	public String getName() {
		// TODO Auto-generated method stub
		return getmMerchant();
	}
}
