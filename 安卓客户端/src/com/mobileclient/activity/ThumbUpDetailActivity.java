package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.ThumbUp;
import com.mobileclient.service.ThumbUpService;
import com.mobileclient.domain.Topic;
import com.mobileclient.service.TopicService;
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
public class ThumbUpDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明记录编号控件
	private TextView TV_thumbUpId;
	// 声明话题控件
	private TextView TV_topObj;
	// 声明学生控件
	private TextView TV_studentObj;
	// 声明点赞时间控件
	private TextView TV_thumpTime;
	/* 要保存的点赞信息信息 */
	ThumbUp thumbUp = new ThumbUp(); 
	/* 点赞信息管理业务逻辑层 */
	private ThumbUpService thumbUpService = new ThumbUpService();
	private TopicService topicService = new TopicService();
	private StudentService studentService = new StudentService();
	private int thumbUpId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.thumbup_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看点赞信息详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_thumbUpId = (TextView) findViewById(R.id.TV_thumbUpId);
		TV_topObj = (TextView) findViewById(R.id.TV_topObj);
		TV_studentObj = (TextView) findViewById(R.id.TV_studentObj);
		TV_thumpTime = (TextView) findViewById(R.id.TV_thumpTime);
		Bundle extras = this.getIntent().getExtras();
		thumbUpId = extras.getInt("thumbUpId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ThumbUpDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    thumbUp = thumbUpService.GetThumbUp(thumbUpId); 
		this.TV_thumbUpId.setText(thumbUp.getThumbUpId() + "");
		Topic topObj = topicService.GetTopic(thumbUp.getTopObj());
		this.TV_topObj.setText(topObj.getTitle());
		Student studentObj = studentService.GetStudent(thumbUp.getStudentObj());
		this.TV_studentObj.setText(studentObj.getStudentName());
		this.TV_thumpTime.setText(thumbUp.getThumpTime());
	} 
}
