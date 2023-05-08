package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.TopicClass;
public class TopicClassListHandler extends DefaultHandler {
	private List<TopicClass> topicClassList = null;
	private TopicClass topicClass;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (topicClass != null) { 
            String valueString = new String(ch, start, length); 
            if ("topicClassId".equals(tempString)) 
            	topicClass.setTopicClassId(new Integer(valueString).intValue());
            else if ("topicClassName".equals(tempString)) 
            	topicClass.setTopicClassName(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("TopicClass".equals(localName)&&topicClass!=null){
			topicClassList.add(topicClass);
			topicClass = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		topicClassList = new ArrayList<TopicClass>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("TopicClass".equals(localName)) {
            topicClass = new TopicClass(); 
        }
        tempString = localName; 
	}

	public List<TopicClass> getTopicClassList() {
		return this.topicClassList;
	}
}
