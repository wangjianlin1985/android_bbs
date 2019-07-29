package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.College;
public class CollegeListHandler extends DefaultHandler {
	private List<College> collegeList = null;
	private College college;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (college != null) { 
            String valueString = new String(ch, start, length); 
            if ("collegeNumber".equals(tempString)) 
            	college.setCollegeNumber(valueString); 
            else if ("collegeName".equals(tempString)) 
            	college.setCollegeName(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("College".equals(localName)&&college!=null){
			collegeList.add(college);
			college = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		collegeList = new ArrayList<College>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("College".equals(localName)) {
            college = new College(); 
        }
        tempString = localName; 
	}

	public List<College> getCollegeList() {
		return this.collegeList;
	}
}
