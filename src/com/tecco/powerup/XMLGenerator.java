package com.tecco.powerup;

import java.net.MalformedURLException;
import java.util.List;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

public class XMLGenerator extends Service {
	// import com.example.GetStartedWithData.R;

	/**
	 * Represents an item in a ToDo list
	 */

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

	private MobileServiceClient mClient;
	private MobileServiceTable<XMLGenerator> mVendors;

	public XMLGenerator() {

	}

	public XMLGenerator(String Area, String id, String Merchant,
			String Address, String Coordinates) {
		this.setmArea(Area);
		this.setId(id);
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

	public String getMerchant() {
		return mMerchant;
	}

	public String getId() {
		return mId;
	}

	public final void setId(String id) {
		mId = id;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
