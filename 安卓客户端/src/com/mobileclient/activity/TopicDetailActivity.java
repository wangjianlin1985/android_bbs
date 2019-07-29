package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.Topic;
import com.mobileclient.service.TopicService;
import com.mobileclient.domain.TopicClass;
import com.mobileclient.service.TopicClassService;
import com.mobileclient.domain.Student;
import com.mobileclient.service.StudentService;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
public class TopicDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明话题id控件
	private TextView TV_topicId;
	// 声明标题控件
	private TextView TV_title;
	// 声明话题类别控件
	private TextView TV_topicClass;
	// 声明话题图片图片框
	private ImageView iv_photo;
	// 声明内容控件
	private TextView TV_content;
	// 声明学生控件
	private TextView TV_studentObj;
	// 声明发布时间控件
	private TextView TV_addTime;
	/* 要保存的话题信息 */
	Topic topic = new Topic(); 
	/* 话题管理业务逻辑层 */
	private TopicService topicService = new TopicService();
	private TopicClassService topicClassService = new TopicClassService();
	private StudentService studentService = new StudentService();
	private int topicId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.topic_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看话题详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_topicId = (TextView) findViewById(R.id.TV_topicId);
		TV_title = (TextView) findViewById(R.id.TV_title);
		TV_topicClass = (TextView) findViewById(R.id.TV_topicClass);
		iv_photo = (ImageView) findViewById(R.id.iv_photo); 
		TV_content = (TextView) findViewById(R.id.TV_content);
		TV_studentObj = (TextView) findViewById(R.id.TV_studentObj);
		TV_addTime = (TextView) findViewById(R.id.TV_addTime);
		Bundle extras = this.getIntent().getExtras();
		topicId = extras.getInt("topicId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TopicDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    topic = topicService.GetTopic(topicId); 
		this.TV_topicId.setText(topic.getTopicId() + "");
		this.TV_title.setText(topic.getTitle());
		TopicClass topicClass = topicClassService.GetTopicClass(topic.getTopicClass());
		this.TV_topicClass.setText(topicClass.getTopicClassName());
		byte[] photo_data = null;
		try {
			// 获取图片数据
			photo_data = ImageService.getImage(HttpUtil.BASE_URL + topic.getPhoto());
			Bitmap photo = BitmapFactory.decodeByteArray(photo_data, 0,photo_data.length);
			this.iv_photo.setImageBitmap(photo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.TV_content.setText(topic.getContent());
		Student studentObj = studentService.GetStudent(topic.getStudentObj());
		this.TV_studentObj.setText(studentObj.getStudentName());
		this.TV_addTime.setText(topic.getAddTime());
	} 
}
