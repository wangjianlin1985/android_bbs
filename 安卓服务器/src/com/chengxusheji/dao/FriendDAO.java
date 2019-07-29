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
import com.chengxusheji.domain.Student;
import com.chengxusheji.domain.Student;
import com.chengxusheji.domain.Friend;

@Service @Transactional
public class FriendDAO {

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
    public void AddFriend(Friend friend) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(friend);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Friend> QueryFriendInfo(Student studentObj1,Student studentObj2,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Friend friend where 1=1";
    	if(null != studentObj1 && !studentObj1.getStudentNumber().equals("")) hql += " and friend.studentObj1.studentNumber='" + studentObj1.getStudentNumber() + "'";
    	if(null != studentObj2 && !studentObj2.getStudentNumber().equals("")) hql += " and friend.studentObj2.studentNumber='" + studentObj2.getStudentNumber() + "'";
    	Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List friendList = q.list();
    	return (ArrayList<Friend>) friendList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Friend> QueryFriendInfo(Student studentObj1,Student studentObj2) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Friend friend where 1=1";
    	if(null != studentObj1 && !studentObj1.getStudentNumber().equals("")) hql += " and friend.studentObj1.studentNumber='" + studentObj1.getStudentNumber() + "'";
    	if(null != studentObj2 && !studentObj2.getStudentNumber().equals("")) hql += " and friend.studentObj2.studentNumber='" + studentObj2.getStudentNumber() + "'";
    	Query q = s.createQuery(hql);
    	List friendList = q.list();
    	return (ArrayList<Friend>) friendList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Friend> QueryAllFriendInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From Friend";
        Query q = s.createQuery(hql);
        List friendList = q.list();
        return (ArrayList<Friend>) friendList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(Student studentObj1,Student studentObj2) {
        Session s = factory.getCurrentSession();
        String hql = "From Friend friend where 1=1";
        if(null != studentObj1 && !studentObj1.getStudentNumber().equals("")) hql += " and friend.studentObj1.studentNumber='" + studentObj1.getStudentNumber() + "'";
        if(null != studentObj2 && !studentObj2.getStudentNumber().equals("")) hql += " and friend.studentObj2.studentNumber='" + studentObj2.getStudentNumber() + "'";
        Query q = s.createQuery(hql);
        List friendList = q.list();
        recordNumber = friendList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Friend GetFriendByFriendId(int friendId) {
        Session s = factory.getCurrentSession();
        Friend friend = (Friend)s.get(Friend.class, friendId);
        return friend;
    }

    /*更新Friend信息*/
    public void UpdateFriend(Friend friend) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(friend);
    }

    /*删除Friend信息*/
    public void DeleteFriend (int friendId) throws Exception {
        Session s = factory.getCurrentSession();
        Object friend = s.load(Friend.class, friendId);
        s.delete(friend);
    }

}
