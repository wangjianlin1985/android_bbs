package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.Friend;
public class FriendListHandler extends DefaultHandler {
	private List<Friend> friendList = null;
	private Friend friend;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (friend != null) { 
            String valueString = new String(ch, start, length); 
            if ("friendId".equals(tempString)) 
            	friend.setFriendId(new Integer(valueString).intValue());
            else if ("studentObj1".equals(tempString)) 
            	friend.setStudentObj1(valueString); 
            else if ("studentObj2".equals(tempString)) 
            	friend.setStudentObj2(valueString); 
            else if ("addTime".equals(tempString)) 
            	friend.setAddTime(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("Friend".equals(localName)&&friend!=null){
			friendList.add(friend);
			friend = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		friendList = new ArrayList<Friend>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("Friend".equals(localName)) {
            friend = new Friend(); 
        }
        tempString = localName; 
	}

	public List<Friend> getFriendList() {
		return this.friendList;
	}
}
