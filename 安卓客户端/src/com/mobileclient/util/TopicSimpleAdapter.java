package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.TopicClassService;
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

public class TopicSimpleAdapter extends SimpleAdapter { 
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

    public TopicSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) { 
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
	  if (convertView == null) convertView = mInflater.inflate(R.layout.topic_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*绑定该view各个控件*/
	  holder.tv_topicId = (TextView)convertView.findViewById(R.id.tv_topicId);
	  holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
	  holder.tv_topicClass = (TextView)convertView.findViewById(R.id.tv_topicClass);
	  holder.iv_photo = (ImageView)convertView.findViewById(R.id.iv_photo);
	  holder.tv_studentObj = (TextView)convertView.findViewById(R.id.tv_studentObj);
	  holder.tv_addTime = (TextView)convertView.findViewById(R.id.tv_addTime);
	  /*设置各个控件的展示内容*/
	  holder.tv_topicId.setText("话题id：" + mData.get(position).get("topicId").toString());
	  holder.tv_title.setText("标题：" + mData.get(position).get("title").toString());
	  holder.tv_topicClass.setText("话题类别：" + (new TopicClassService()).GetTopicClass(Integer.parseInt(mData.get(position).get("topicClass").toString())).getTopicClassName());
	  holder.iv_photo.setImageResource(R.drawable.default_photo);
	  ImageLoadListener photoLoadListener = new ImageLoadListener(mListView,R.id.iv_photo);
	  syncImageLoader.loadImage(position,(String)mData.get(position).get("photo"),photoLoadListener);  
	  holder.tv_studentObj.setText("学生：" + (new StudentService()).GetStudent(mData.get(position).get("studentObj").toString()).getStudentName());
	  holder.tv_addTime.setText("发布时间：" + mData.get(position).get("addTime").toString());
	  /*返回修改好的view*/
	  return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_topicId;
    	TextView tv_title;
    	TextView tv_topicClass;
    	ImageView iv_photo;
    	TextView tv_studentObj;
    	TextView tv_addTime;
    }
} 
