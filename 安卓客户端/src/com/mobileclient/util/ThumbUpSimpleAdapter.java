package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.TopicService;
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

public class ThumbUpSimpleAdapter extends SimpleAdapter { 
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

    public ThumbUpSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) { 
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
	  if (convertView == null) convertView = mInflater.inflate(R.layout.thumbup_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*绑定该view各个控件*/
	  holder.tv_thumbUpId = (TextView)convertView.findViewById(R.id.tv_thumbUpId);
	  holder.tv_topObj = (TextView)convertView.findViewById(R.id.tv_topObj);
	  holder.tv_studentObj = (TextView)convertView.findViewById(R.id.tv_studentObj);
	  holder.tv_thumpTime = (TextView)convertView.findViewById(R.id.tv_thumpTime);
	  /*设置各个控件的展示内容*/
	  holder.tv_thumbUpId.setText("记录编号：" + mData.get(position).get("thumbUpId").toString());
	  holder.tv_topObj.setText("话题：" + (new TopicService()).GetTopic(Integer.parseInt(mData.get(position).get("topObj").toString())).getTitle());
	  holder.tv_studentObj.setText("学生：" + (new StudentService()).GetStudent(mData.get(position).get("studentObj").toString()).getStudentName());
	  holder.tv_thumpTime.setText("点赞时间：" + mData.get(position).get("thumpTime").toString());
	  /*返回修改好的view*/
	  return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_thumbUpId;
    	TextView tv_topObj;
    	TextView tv_studentObj;
    	TextView tv_thumpTime;
    }
} 
