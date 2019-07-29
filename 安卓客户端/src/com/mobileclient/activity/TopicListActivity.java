package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.Topic;
import com.mobileclient.service.TopicService;
import com.mobileclient.util.ActivityUtils;import com.mobileclient.util.TopicSimpleAdapter;
import com.mobileclient.util.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class TopicListActivity extends Activity {
	TopicSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	int topicId;
	/* 话题操作业务逻辑层对象 */
	TopicService topicService = new TopicService();
	/*保存查询参数条件的话题对象*/
	private Topic queryConditionTopic;

	private MyProgressDialog dialog; //进度条	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.topic_list);
		dialog = MyProgressDialog.getInstance(this);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		//标题栏控件
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(TopicListActivity.this, TopicQueryActivity.class);
				startActivityForResult(intent,ActivityUtils.QUERY_CODE);//此处的requestCode应与下面结果处理函中调用的requestCode一致
			}
		});
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("话题查询列表");
		ImageView add_btn = (ImageView) this.findViewById(R.id.add_btn);
		add_btn.setOnClickListener(new android.view.View.OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(TopicListActivity.this, TopicAddActivity.class);
				startActivityForResult(intent,ActivityUtils.ADD_CODE);
			}
		});
		setViews();
	}

	//结果处理函数，当从secondActivity中返回时调用此函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ActivityUtils.QUERY_CODE && resultCode==RESULT_OK){
        	Bundle extras = data.getExtras();
        	if(extras != null)
        		queryConditionTopic = (Topic)extras.getSerializable("queryConditionTopic");
        	setViews();
        }
        if(requestCode==ActivityUtils.EDIT_CODE && resultCode==RESULT_OK){
        	setViews();
        }
        if(requestCode == ActivityUtils.ADD_CODE && resultCode == RESULT_OK) {
        	queryConditionTopic = null;
        	setViews();
        }
    }

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		dialog.show();
		final Handler handler = new Handler();
		new Thread(){
			@Override
			public void run() {
				//在子线程中进行下载数据操作
				list = getDatas();
				//发送消失到handler，通知主线程下载完成
				handler.post(new Runnable() {
					@Override
					public void run() {
						dialog.cancel();
						adapter = new TopicSimpleAdapter(TopicListActivity.this, list,
	        					R.layout.topic_list_item,
	        					new String[] { "topicId","title","topicClass","photo","studentObj","addTime" },
	        					new int[] { R.id.tv_topicId,R.id.tv_title,R.id.tv_topicClass,R.id.iv_photo,R.id.tv_studentObj,R.id.tv_addTime,},lv);
	        			lv.setAdapter(adapter);
					}
				});
			}
		}.start(); 

		// 添加长按点击
		lv.setOnCreateContextMenuListener(topicListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	int topicId = Integer.parseInt(list.get(arg2).get("topicId").toString());
            	Intent intent = new Intent();
            	intent.setClass(TopicListActivity.this, TopicDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putInt("topicId", topicId);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener topicListItemListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "编辑话题信息"); 
			menu.add(0, 1, 0, "删除话题信息");
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //编辑话题信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取话题id
			topicId = Integer.parseInt(list.get(position).get("topicId").toString());
			Intent intent = new Intent();
			intent.setClass(TopicListActivity.this, TopicEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("topicId", topicId);
			intent.putExtras(bundle);
			startActivityForResult(intent,ActivityUtils.EDIT_CODE);
		} else if (item.getItemId() == 1) {// 删除话题信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取话题id
			topicId = Integer.parseInt(list.get(position).get("topicId").toString());
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// 删除
	protected void dialog() {
		Builder builder = new Builder(TopicListActivity.this);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = topicService.DeleteTopic(topicId);
				Toast.makeText(getApplicationContext(), result, 1).show();
				setViews();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private List<Map<String, Object>> getDatas() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			/* 查询话题信息 */
			List<Topic> topicList = topicService.QueryTopic(queryConditionTopic);
			for (int i = 0; i < topicList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("topicId", topicList.get(i).getTopicId());
				map.put("title", topicList.get(i).getTitle());
				map.put("topicClass", topicList.get(i).getTopicClass());
				/*byte[] photo_data = ImageService.getImage(HttpUtil.BASE_URL+ topicList.get(i).getPhoto());// 获取图片数据
				BitmapFactory.Options photo_opts = new BitmapFactory.Options();  
				photo_opts.inJustDecodeBounds = true;  
				BitmapFactory.decodeByteArray(photo_data, 0, photo_data.length, photo_opts); 
				photo_opts.inSampleSize = photoListActivity.computeSampleSize(photo_opts, -1, 100*100); 
				photo_opts.inJustDecodeBounds = false; 
				try {
					Bitmap photo = BitmapFactory.decodeByteArray(photo_data, 0, photo_data.length, photo_opts);
					map.put("photo", photo);
				} catch (OutOfMemoryError err) { }*/
				map.put("photo", HttpUtil.BASE_URL+ topicList.get(i).getPhoto());
				map.put("studentObj", topicList.get(i).getStudentObj());
				map.put("addTime", topicList.get(i).getAddTime());
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

}
