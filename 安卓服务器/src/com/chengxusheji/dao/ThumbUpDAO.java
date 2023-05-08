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
import com.chengxusheji.domain.ThumbUp;

@Service @Transactional
public class ThumbUpDAO {

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
    public void AddThumbUp(ThumbUp thumbUp) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(thumbUp);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<ThumbUp> QueryThumbUpInfo(Topic topObj,Student studentObj,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From ThumbUp thumbUp where 1=1";
    	if(null != topObj && topObj.getTopicId()!=0) hql += " and thumbUp.topObj.topicId=" + topObj.getTopicId();
    	if(null != studentObj && !studentObj.getStudentNumber().equals("")) hql += " and thumbUp.studentObj.studentNumber='" + studentObj.getStudentNumber() + "'";
    	Query q = s.createQuery(hql);
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List thumbUpList = q.list();
    	return (ArrayList<ThumbUp>) thumbUpList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<ThumbUp> QueryThumbUpInfo(Topic topObj,Student studentObj) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From ThumbUp thumbUp where 1=1";
    	if(null != topObj && topObj.getTopicId()!=0) hql += " and thumbUp.topObj.topicId=" + topObj.getTopicId();
    	if(null != studentObj && !studentObj.getStudentNumber().equals("")) hql += " and thumbUp.studentObj.studentNumber='" + studentObj.getStudentNumber() + "'";
    	Query q = s.createQuery(hql);
    	List thumbUpList = q.list();
    	return (ArrayList<ThumbUp>) thumbUpList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<ThumbUp> QueryAllThumbUpInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From ThumbUp";
        Query q = s.createQuery(hql);
        List thumbUpList = q.list();
        return (ArrayList<ThumbUp>) thumbUpList;
    }

    /*�����ܵ�ҳ���ͼ�¼��*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(Topic topObj,Student studentObj) {
        Session s = factory.getCurrentSession();
        String hql = "From ThumbUp thumbUp where 1=1";
        if(null != topObj && topObj.getTopicId()!=0) hql += " and thumbUp.topObj.topicId=" + topObj.getTopicId();
        if(null != studentObj && !studentObj.getStudentNumber().equals("")) hql += " and thumbUp.studentObj.studentNumber='" + studentObj.getStudentNumber() + "'";
        Query q = s.createQuery(hql);
        List thumbUpList = q.list();
        recordNumber = thumbUpList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ThumbUp GetThumbUpByThumbUpId(int thumbUpId) {
        Session s = factory.getCurrentSession();
        ThumbUp thumbUp = (ThumbUp)s.get(ThumbUp.class, thumbUpId);
        return thumbUp;
    }

    /*����ThumbUp��Ϣ*/
    public void UpdateThumbUp(ThumbUp thumbUp) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(thumbUp);
    }

    /*ɾ��ThumbUp��Ϣ*/
    public void DeleteThumbUp (int thumbUpId) throws Exception {
        Session s = factory.getCurrentSession();
        Object thumbUp = s.load(ThumbUp.class, thumbUpId);
        s.delete(thumbUp);
    }

}
