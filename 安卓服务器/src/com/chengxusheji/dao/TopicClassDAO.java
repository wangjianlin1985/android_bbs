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

@Service @Transactional
public class TopicClassDAO {

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
    public void AddTopicClass(TopicClass topicClass) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(topicClass);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<TopicClass> QueryTopicClassInfo(int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From TopicClass topicClass where 1=1";
    	Query q = s.createQuery(hql);
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List topicClassList = q.list();
    	return (ArrayList<TopicClass>) topicClassList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<TopicClass> QueryTopicClassInfo() { 
    	Session s = factory.getCurrentSession();
    	String hql = "From TopicClass topicClass where 1=1";
    	Query q = s.createQuery(hql);
    	List topicClassList = q.list();
    	return (ArrayList<TopicClass>) topicClassList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<TopicClass> QueryAllTopicClassInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From TopicClass";
        Query q = s.createQuery(hql);
        List topicClassList = q.list();
        return (ArrayList<TopicClass>) topicClassList;
    }

    /*�����ܵ�ҳ���ͼ�¼��*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber() {
        Session s = factory.getCurrentSession();
        String hql = "From TopicClass topicClass where 1=1";
        Query q = s.createQuery(hql);
        List topicClassList = q.list();
        recordNumber = topicClassList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public TopicClass GetTopicClassByTopicClassId(int topicClassId) {
        Session s = factory.getCurrentSession();
        TopicClass topicClass = (TopicClass)s.get(TopicClass.class, topicClassId);
        return topicClass;
    }

    /*����TopicClass��Ϣ*/
    public void UpdateTopicClass(TopicClass topicClass) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(topicClass);
    }

    /*ɾ��TopicClass��Ϣ*/
    public void DeleteTopicClass (int topicClassId) throws Exception {
        Session s = factory.getCurrentSession();
        Object topicClass = s.load(TopicClass.class, topicClassId);
        s.delete(topicClass);
    }

}
