package com.kth.ppomppu.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class NaverParser {	
	
	private static final String NAVER_API_KEY = "3a2b15266e6879020917091f85a4bc98";
	
	
	static ArrayList<XmlData> m_xmlData;
	

	public NaverParser() {
		// TODO Auto-generated constructor stub
	}		


	public static ArrayList<XmlData> GetXmlData() {

		m_xmlData = new ArrayList<XmlData>();
		
		XmlData xmlData = null;

		
		// XML ��� �Ľ��ϱ�
		try {
			
			
			URL text = new URL("http://openapi.naver.com/search?key="+NAVER_API_KEY+"&query=nexearch&target=rank");

			XmlPullParserFactory parserCreator = XmlPullParserFactory
					.newInstance();
			XmlPullParser parser = parserCreator.newPullParser();

			parser.setInput(text.openStream(), null);

			Log.i("NET", "Parsing...");

			 
            BufferedReader in = new BufferedReader(new InputStreamReader( text.openStream()));
            String inputLine;

            StringBuilder sb = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null)
                sb.append( inputLine.trim());
            
            in.close();
            
           // JSONObject jsonObj = (JSONObject)new XMLSerializer().read(xml);
			
			// status.setText("Parsing....");

			int parseEvent = parser.getEventType();
			while (parseEvent != XmlPullParser.END_DOCUMENT) {

				switch (parseEvent) {
				case XmlPullParser.START_TAG:
					String tag = parser.getName();				

					if (tag.compareTo("K") == 0) {
						xmlData = new XmlData();
						//�ڷ� ���� 1�� ��

						String titleSrc = parser.nextText();
						xmlData.d_title = titleSrc;
						
						m_xmlData.add(xmlData);
						Log.d("HONG", "xml data "+xmlData.d_title);
					}
					
					break;
				}
				parseEvent = parser.next();

				// xmlData = null;
				// While next �������� �ѱ��.
			}
			Log.i("NET", "End...");

		} catch (Exception e) {
			// TODO: handle exception
			Log.i("HONG", "Parsing Failed! exception = "+e.getMessage());
		}	
		return m_xmlData;
	}
	
}
