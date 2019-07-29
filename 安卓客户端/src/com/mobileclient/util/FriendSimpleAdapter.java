package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.StudentService;
import com.mobileclient.service.StudentService;
import com.mobileclient.activity.R;
import com.mobileclient.imgCache.ImageLoadListener;
import com.mobileclient.imgCache.ListViewOnScrollListener;
import com.mobileclient.imgCache.SyncImageLoader;
import android.content.Context;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.ViewGroup;  
import android.widget.ImageView; 
import android.widget.ListView;
import android.widget.SimpleAdapter; 
import android.widget.TextView; 

public class FriendSimpleAdapter extends SimpleAdapter { 
	/*需要绑定的控件资源id*/
    private int[] mTo;
    /*map集合关键字数组*/
    private String[] mFrom;
/*需要绑定的数据*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    private ListView mListView;
    //图片异步缓存加载类,带内存缓存和文件缓存
    private SyncImageLoader syncImageLoader;

    public FriendSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) { 
        super(context, data, resource, from, to); 
        mTo = to; 
        mFrom = from; 
        mData = data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context= context;
        mListView = listView; 
        syncImageLoader = SyncImageLoader.getInstance();
        ListViewOnScrollListener onScrollListener = new ListViewOnScrollListener(syncImageLoader,listView,getCount());
        mListView.setOnScrollListener(onScrollListener);
    } 

  public View getView(int position, View convertView, ViewGroup parent) { 
	  ViewHolder holder = null;
	  ///*第一次装载这个view时=null,就新建一个调用inflate渲染一个view*/
	  if (convertView == null) convertView = mInflater.inflate(R.layout.friend_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*绑定该view各个控件*/
	  holder.tv_friendId = (TextView)convertView.findViewById(R.id.tv_friendId);
	  holder.tv_studentObj1 = (TextView)convertView.findViewById(R.id.tv_studentObj1);
	  holder.tv_studentObj2 = (TextView)convertView.findViewById(R.id.tv_studentObj2);
	  holder.tv_addTime = (TextView)convertView.findViewById(R.id.tv_addTime);
	  /*设置各个控件的展示内容*/
	  holder.tv_friendId.setText("记录编号：" + mData.get(position).get("friendId").toString());
	  holder.tv_studentObj1.setText("学生1：" + (new StudentService()).GetStudent(mData.get(position).get("studentObj1").toString()).getStudentName());
	  holder.tv_studentObj2.setText("好友：" + (new StudentService()).GetStudent(mData.get(position).get("studentObj2").toString()).getStudentName());
	  holder.tv_addTime.setText("添加时间：" + mData.get(position).get("addTime").toString());
	  /*返回修改好的view*/
	  return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_friendId;
    	TextView tv_studentObj1;
    	TextView tv_studentObj2;
    	TextView tv_addTime;
    }
} 
