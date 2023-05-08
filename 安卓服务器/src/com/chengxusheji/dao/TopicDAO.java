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
import com.chengxusheji.domain.TopicClass;
import com.chengxusheji.domain.Student;
import com.chengxusheji.domain.Topic;

@Service @Transactional
public class TopicDAO {

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
    public void AddTopic(Topic topic) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(topic);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Topic> QueryTopicInfo(String title,TopicClass topicClass,Student studentObj,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Topic topic where 1=1";
    	if(!title.equals("")) hql = hql + " and topic.title like '%" + title + "%'";
    	if(null != topicClass && topicClass.getTopicClassId()!=0) hql += " and topic.topicClass.topicClassId=" + topicClass.getTopicClassId();
    	if(null != studentObj && !studentObj.getStudentNumber().equals("")) hql += " and topic.studentObj.studentNumber='" + studentObj.getStudentNumber() + "'";
    	Query q = s.createQuery(hql);
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List topicList = q.list();
    	return (ArrayList<Topic>) topicList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Topic> QueryTopicInfo(String title,TopicClass topicClass,Student studentObj) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Topic topic where 1=1";
    	if(!title.equals("")) hql = hql + " and topic.title like '%" + title + "%'";
    	if(null != topicClass && topicClass.getTopicClassId()!=0) hql += " and topic.topicClass.topicClassId=" + topicClass.getTopicClassId();
    	if(null != studentObj && !studentObj.getStudentNumber().equals("")) hql += " and topic.studentObj.studentNumber='" + studentObj.getStudentNumber() + "'";
    	Query q = s.createQuery(hql);
    	List topicList = q.list();
    	return (ArrayList<Topic>) topicList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Topic> QueryAllTopicInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From Topic";
        Query q = s.createQuery(hql);
        List topicList = q.list();
        return (ArrayList<Topic>) topicList;
    }

    /*�����ܵ�ҳ���ͼ�¼��*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String title,TopicClass topicClass,Student studentObj) {
        Session s = factory.getCurrentSession();
        String hql = "From Topic topic where 1=1";
        if(!title.equals("")) hql = hql + " and topic.title like '%" + title + "%'";
        if(null != topicClass && topicClass.getTopicClassId()!=0) hql += " and topic.topicClass.topicClassId=" + topicClass.getTopicClassId();
        if(null != studentObj && !studentObj.getStudentNumber().equals("")) hql += " and topic.studentObj.studentNumber='" + studentObj.getStudentNumber() + "'";
        Query q = s.createQuery(hql);
        List topicList = q.list();
        recordNumber = topicList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Topic GetTopicByTopicId(int topicId) {
        Session s = factory.getCurrentSession();
        Topic topic = (Topic)s.get(Topic.class, topicId);
        return topic;
    }

    /*����Topic��Ϣ*/
    public void UpdateTopic(Topic topic) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(topic);
    }

    /*ɾ��Topic��Ϣ*/
    public void DeleteTopic (int topicId) throws Exception {
        Session s = factory.getCurrentSession();
        Object topic = s.load(Topic.class, topicId);
        s.delete(topic);
    }

}
