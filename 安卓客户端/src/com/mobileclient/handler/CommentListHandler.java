package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.Comment;
public class CommentListHandler extends DefaultHandler {
	private List<Comment> commentList = null;
	private Comment comment;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (comment != null) { 
            String valueString = new String(ch, start, length); 
            if ("commentId".equals(tempString)) 
            	comment.setCommentId(new Integer(valueString).intValue());
            else if ("topicObj".equals(tempString)) 
            	comment.setTopicObj(new Integer(valueString).intValue());
            else if ("content".equals(tempString)) 
            	comment.setContent(valueString); 
            else if ("studentObj".equals(tempString)) 
            	comment.setStudentObj(valueString); 
            else if ("commentTime".equals(tempString)) 
            	comment.setCommentTime(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("Comment".equals(localName)&&comment!=null){
			commentList.add(comment);
			comment = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		commentList = new ArrayList<Comment>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("Comment".equals(localName)) {
            comment = new Comment(); 
        }
        tempString = localName; 
	}

	public List<Comment> getCommentList() {
		return this.commentList;
	}
}
