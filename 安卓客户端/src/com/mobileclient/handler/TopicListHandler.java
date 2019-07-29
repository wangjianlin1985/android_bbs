package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.Topic;
public class TopicListHandler extends DefaultHandler {
	private List<Topic> topicList = null;
	private Topic topic;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (topic != null) { 
            String valueString = new String(ch, start, length); 
            if ("topicId".equals(tempString)) 
            	topic.setTopicId(new Integer(valueString).intValue());
            else if ("title".equals(tempString)) 
            	topic.setTitle(valueString); 
            else if ("topicClass".equals(tempString)) 
            	topic.setTopicClass(new Integer(valueString).intValue());
            else if ("photo".equals(tempString)) 
            	topic.setPhoto(valueString); 
            else if ("content".equals(tempString)) 
            	topic.setContent(valueString); 
            else if ("studentObj".equals(tempString)) 
            	topic.setStudentObj(valueString); 
            else if ("addTime".equals(tempString)) 
            	topic.setAddTime(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("Topic".equals(localName)&&topic!=null){
			topicList.add(topic);
			topic = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		topicList = new ArrayList<Topic>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("Topic".equals(localName)) {
            topic = new Topic(); 
        }
        tempString = localName; 
	}

	public List<Topic> getTopicList() {
		return this.topicList;
	}
}
