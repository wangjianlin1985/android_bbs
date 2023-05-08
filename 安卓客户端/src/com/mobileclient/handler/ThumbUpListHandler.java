package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.ThumbUp;
public class ThumbUpListHandler extends DefaultHandler {
	private List<ThumbUp> thumbUpList = null;
	private ThumbUp thumbUp;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (thumbUp != null) { 
            String valueString = new String(ch, start, length); 
            if ("thumbUpId".equals(tempString)) 
            	thumbUp.setThumbUpId(new Integer(valueString).intValue());
            else if ("topObj".equals(tempString)) 
            	thumbUp.setTopObj(new Integer(valueString).intValue());
            else if ("studentObj".equals(tempString)) 
            	thumbUp.setStudentObj(valueString); 
            else if ("thumpTime".equals(tempString)) 
            	thumbUp.setThumpTime(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("ThumbUp".equals(localName)&&thumbUp!=null){
			thumbUpList.add(thumbUp);
			thumbUp = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		thumbUpList = new ArrayList<ThumbUp>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("ThumbUp".equals(localName)) {
            thumbUp = new ThumbUp(); 
        }
        tempString = localName; 
	}

	public List<ThumbUp> getThumbUpList() {
		return this.thumbUpList;
	}
}
