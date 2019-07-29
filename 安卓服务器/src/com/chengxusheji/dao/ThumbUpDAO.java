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
    /*每页显示记录数目*/
    private final int PAGE_SIZE = 10;

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加图书信息*/
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
    	/*计算当前显示页码的开始记录*/
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

    /*计算总的页数和记录数*/
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

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ThumbUp GetThumbUpByThumbUpId(int thumbUpId) {
        Session s = factory.getCurrentSession();
        ThumbUp thumbUp = (ThumbUp)s.get(ThumbUp.class, thumbUpId);
        return thumbUp;
    }

    /*更新ThumbUp信息*/
    public void UpdateThumbUp(ThumbUp thumbUp) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(thumbUp);
    }

    /*删除ThumbUp信息*/
    public void DeleteThumbUp (int thumbUpId) throws Exception {
        Session s = factory.getCurrentSession();
        Object thumbUp = s.load(ThumbUp.class, thumbUpId);
        s.delete(thumbUp);
    }

}
