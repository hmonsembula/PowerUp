package com.tecco.powerup;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class MerchantXmlParser {
	private static final String ns = null;

	public List<Merchant> parse(InputStream inputFile)
			throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(inputFile, null);
			parser.nextTag();
			return readFeed(parser);
		} finally {
			inputFile.close();
		}
	}

	private List<Merchant> readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		List<Merchant> merchants = new ArrayList<Merchant>();

		parser.require(XmlPullParser.START_TAG, ns, "feed");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tag = parser.getName();
			// Starts by looking for the merchant tag
			if (tag.equals("merchant")) {
				merchants.add(readEntry(parser));
			} else {
				skip(parser);
			}
		}
		return merchants;
	}

	// Parses the contents of an entry. If it encounters a name, telNumber,
	// latitude,
	// it hands them off
	// to their respective "read" methods for processing. Otherwise, skips the
	// tag.
	private Merchant readEntry(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "merchant");
		String name = null;
		String telNumber = null;
		double latitude = 0;
		double longitude = 0;
		String address = "";
		String area = "";
		String coordinates = "";
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tag = parser.getName();
			if (tag.equals("name")) {
				name = readName(parser);
			} else if (tag.equals("telNumber")) {
				telNumber = readTelNumber(parser);
			} else if (tag.equals("latitude")) {
				latitude = readLatitude(parser);
			} else if (tag.equals("longitude")) {
				longitude = readLongitude(parser);
			} else if (tag.equals("address")) {
				address = readAddress(parser);
			} else if (tag.equals("mCoordinates")) {
				coordinates = readCoordinates(parser);
			} else if (tag.equals("area")) {
				area = readArea(parser);
			} else {
				skip(parser);
			}
		}
		return new Merchant(name, telNumber, latitude, longitude, address);
	}

	private String readArea(XmlPullParser parser)
			throws XmlPullParserException, IOException {

		try {
			parser.require(XmlPullParser.START_TAG, ns, "area");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String address = readText(parser);

		parser.require(XmlPullParser.END_TAG, ns, "area");
		return address;
	}

	private String readAddress(XmlPullParser parser)
			throws XmlPullParserException, IOException {

		try {
			parser.require(XmlPullParser.START_TAG, ns, "address");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String address = readText(parser);

		parser.require(XmlPullParser.END_TAG, ns, "address");
		return address;
	}

	private void skip(XmlPullParser parser) {

	}

	private double readLongitude(XmlPullParser parser) {
		// TODO Auto-generated method stub
		try {
			parser.require(XmlPullParser.START_TAG, ns, "longitude");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double longitude = Double.parseDouble(readText(parser));
		try {
			parser.require(XmlPullParser.END_TAG, ns, "longitude");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return longitude;
	}

	private double readLatitude(XmlPullParser parser) {
		// TODO Auto-generated method stub
		try {
			parser.require(XmlPullParser.START_TAG, ns, "latitude");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double latitude = Double.parseDouble(readText(parser));
		try {
			parser.require(XmlPullParser.END_TAG, ns, "latitude");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return latitude;
	}

	private String readTelNumber(XmlPullParser parser) {
		// TODO Auto-generated method stub
		try {
			parser.require(XmlPullParser.START_TAG, ns, "telNumber");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String telNumber = readText(parser);
		try {
			parser.require(XmlPullParser.END_TAG, ns, "telNumber");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return telNumber;
	}

	private String readCoordinates(XmlPullParser parser) {
		// TODO Auto-generated method stub
		try {
			parser.require(XmlPullParser.START_TAG, ns, "mCoordinates");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String telNumber = readText(parser);
		try {
			parser.require(XmlPullParser.END_TAG, ns, "mCoordinates");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return telNumber;
	}

	private String readName(XmlPullParser parser) {
		// TODO Auto-generated method stub
		try {
			parser.require(XmlPullParser.START_TAG, ns, "longitude");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String name = readText(parser);
		try {
			parser.require(XmlPullParser.END_TAG, ns, "longitude");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}

	private String readText(XmlPullParser parser) {
		String result = "";
		try {
			if (parser.next() == XmlPullParser.TEXT) {
				result = parser.getText();
				parser.nextTag();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}