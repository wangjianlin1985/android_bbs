package com.chengxusheji.dao;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.chengxusheji.domain.Topic;
import com.chengxusheji.domain.Student;
import com.chengxusheji.domain.Comment;

@Service @Transactional
public class CommentDAO {

	@Resource SessionFactory factory;
    /*ÿҳ��ʾ��¼��Ŀ*/
    private final int PAGE_SIZE = 10;

    /*�����ѯ���ܵ�ҳ��*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*�����ѯ�����ܼ�¼��*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*���ͼ����Ϣ*/
    public void AddComment(Comment comment) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(comment);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Comment> QueryCommentInfo(Topic topicObj,Student studentObj,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Comment comment where 1=1";
    	if(null != topicObj && topicObj.getTopicId()!=0) hql += " and comment.topicObj.topicId=" + topicObj.getTopicId();
    	if(null != studentObj && !studentObj.getStudentNumber().equals("")) hql += " and comment.studentObj.studentNumber='" + studentObj.getStudentNumber() + "'";
    	Query q = s.createQuery(hql);
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List commentList = q.list();
    	return (ArrayList<Comment>) commentList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Comment> QueryCommentInfo(Topic topicObj,Student studentObj) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Comment comment where 1=1";
    	if(null != topicObj && topicObj.getTopicId()!=0) hql += " and comment.topicObj.topicId=" + topicObj.getTopicId();
    	if(null != studentObj && !studentObj.getStudentNumber().equals("")) hql += " and comment.studentObj.studentNumber='" + studentObj.getStudentNumber() + "'";
    	Query q = s.createQuery(hql);
    	List commentList = q.list();
    	return (ArrayList<Comment>) commentList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Comment> QueryAllCommentInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From Comment";
        Query q = s.createQuery(hql);
        List commentList = q.list();
        return (ArrayList<Comment>) commentList;
    }

    /*�����ܵ�ҳ���ͼ�¼��*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(Topic topicObj,Student studentObj) {
        Session s = factory.getCurrentSession();
        String hql = "From Comment comment where 1=1";
        if(null != topicObj && topicObj.getTopicId()!=0) hql += " and comment.topicObj.topicId=" + topicObj.getTopicId();
        if(null != studentObj && !studentObj.getStudentNumber().equals("")) hql += " and comment.studentObj.studentNumber='" + studentObj.getStudentNumber() + "'";
        Query q = s.createQuery(hql);
        List commentList = q.list();
        recordNumber = commentList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Comment GetCommentByCommentId(int commentId) {
        Session s = factory.getCurrentSession();
        Comment comment = (Comment)s.get(Comment.class, commentId);
        return comment;
    }

    /*����Comment��Ϣ*/
    public void UpdateComment(Comment comment) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(comment);
    }

    /*ɾ��Comment��Ϣ*/
    public void DeleteComment (int commentId) throws Exception {
        Session s = factory.getCurrentSession();
        Object comment = s.load(Comment.class, commentId);
        s.delete(comment);
    }

}
