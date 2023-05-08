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
import com.chengxusheji.domain.College;

@Service @Transactional
public class CollegeDAO {

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
    public void AddCollege(College college) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(college);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<College> QueryCollegeInfo(int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From College college where 1=1";
    	Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List collegeList = q.list();
    	return (ArrayList<College>) collegeList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<College> QueryCollegeInfo() { 
    	Session s = factory.getCurrentSession();
    	String hql = "From College college where 1=1";
    	Query q = s.createQuery(hql);
    	List collegeList = q.list();
    	return (ArrayList<College>) collegeList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<College> QueryAllCollegeInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From College";
        Query q = s.createQuery(hql);
        List collegeList = q.list();
        return (ArrayList<College>) collegeList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber() {
        Session s = factory.getCurrentSession();
        String hql = "From College college where 1=1";
        Query q = s.createQuery(hql);
        List collegeList = q.list();
        recordNumber = collegeList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public College GetCollegeByCollegeNumber(String collegeNumber) {
        Session s = factory.getCurrentSession();
        College college = (College)s.get(College.class, collegeNumber);
        return college;
    }

    /*更新College信息*/
    public void UpdateCollege(College college) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(college);
    }

    /*删除College信息*/
    public void DeleteCollege (String collegeNumber) throws Exception {
        Session s = factory.getCurrentSession();
        Object college = s.load(College.class, collegeNumber);
        s.delete(college);
    }

}
