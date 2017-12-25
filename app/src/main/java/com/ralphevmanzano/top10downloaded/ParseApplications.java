package com.ralphevmanzano.top10downloaded;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by ralphemerson on 12/5/2017.
 */

public class ParseApplications {
    private static final String TAG = "ParseApplications";
    private ArrayList<FeedEntry> applications;

    public ParseApplications() {
        this.applications = new ArrayList<>();
    }

    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }

    public boolean parse (String xmlData) {
        boolean status = true;
        boolean inEntry = false;

        FeedEntry currentRecord = null;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                //String tagName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parse: Starting tag for " +getTagName(xmlPullParser));
                        if ("entry".equalsIgnoreCase(getTagName(xmlPullParser))) {
                            inEntry = true;
                            currentRecord = new FeedEntry();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xmlPullParser.getText();
                        Log.d(TAG, "============" + textValue);
                        break;

                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parse: Ending tag for " + getTagName(xmlPullParser));
                        if (inEntry) {
                            if ("entry".equalsIgnoreCase(getTagName(xmlPullParser))) {
                                applications.add(currentRecord);
                                inEntry = false;
                            } else if ("name".equalsIgnoreCase(getTagName(xmlPullParser))) {
                                currentRecord.setName(textValue);
                            } else if ("artist".equalsIgnoreCase(getTagName(xmlPullParser))) {
                                currentRecord.setArtist(textValue);
                            } else if ("releasedate".equalsIgnoreCase(xmlPullParser.getName())) {
                                currentRecord.setReleaseDate(textValue);
                            } else if ("summary".equalsIgnoreCase(getTagName(xmlPullParser))) {
                                currentRecord.setSummary(textValue);
                            } else if ("image".equalsIgnoreCase(getTagName(xmlPullParser))) {
                                currentRecord.setImageURL(textValue);
                            }
                        }
                        break;

                    default:
                        //Nothing else to do
                }

                eventType = xmlPullParser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    private String getTagName(XmlPullParser xmlPullParser) {
        return xmlPullParser.getName();
    }
}
