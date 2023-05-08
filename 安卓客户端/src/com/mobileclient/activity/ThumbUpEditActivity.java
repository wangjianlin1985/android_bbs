package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.ThumbUp;
import com.mobileclient.service.ThumbUpService;
import com.mobileclient.domain.Topic;
import com.mobileclient.service.TopicService;
import com.mobileclient.domain.Student;
import com.mobileclient.service.StudentService;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class ThumbUpEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明记录编号TextView
	private TextView TV_thumbUpId;
	// 声明话题下拉框
	private Spinner spinner_topObj;
	private ArrayAdapter<String> topObj_adapter;
	private static  String[] topObj_ShowText  = null;
	private List<Topic> topicList = null;
	/*话题管理业务逻辑层*/
	private TopicService topicService = new TopicService();
	// 声明学生下拉框
	private Spinner spinner_studentObj;
	private ArrayAdapter<String> studentObj_adapter;
	private static  String[] studentObj_ShowText  = null;
	private List<Student> studentList = null;
	/*学生管理业务逻辑层*/
	private StudentService studentService = new StudentService();
	// 声明点赞时间输入框
	private EditText ET_thumpTime;
	protected String carmera_path;
	/*要保存的点赞信息信息*/
	ThumbUp thumbUp = new ThumbUp();
	/*点赞信息管理业务逻辑层*/
	private ThumbUpService thumbUpService = new ThumbUpService();

	private int thumbUpId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.thumbup_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑点赞信息信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_thumbUpId = (TextView) findViewById(R.id.TV_thumbUpId);
		spinner_topObj = (Spinner) findViewById(R.id.Spinner_topObj);
		// 获取所有的话题
		try {
			topicList = topicService.QueryTopic(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int topicCount = topicList.size();
		topObj_ShowText = new String[topicCount];
		for(int i=0;i<topicCount;i++) { 
			topObj_ShowText[i] = topicList.get(i).getTitle();
		}
		// 将可选内容与ArrayAdapter连接起来
		topObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, topObj_ShowText);
		// 设置图书类别下拉列表的风格
		topObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_topObj.setAdapter(topObj_adapter);
		// 添加事件Spinner事件监听
		spinner_topObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				thumbUp.setTopObj(topicList.get(arg2).getTopicId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_topObj.setVisibility(View.VISIBLE);
		spinner_studentObj = (Spinner) findViewById(R.id.Spinner_studentObj);
		// 获取所有的学生
		try {
			studentList = studentService.QueryStudent(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int studentCount = studentList.size();
		studentObj_ShowText = new String[studentCount];
		for(int i=0;i<studentCount;i++) { 
			studentObj_ShowText[i] = studentList.get(i).getStudentName();
		}
		// 将可选内容与ArrayAdapter连接起来
		studentObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, studentObj_ShowText);
		// 设置图书类别下拉列表的风格
		studentObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_studentObj.setAdapter(studentObj_adapter);
		// 添加事件Spinner事件监听
		spinner_studentObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				thumbUp.setStudentObj(studentList.get(arg2).getStudentNumber()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_studentObj.setVisibility(View.VISIBLE);
		ET_thumpTime = (EditText) findViewById(R.id.ET_thumpTime);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		thumbUpId = extras.getInt("thumbUpId");
		/*单击修改点赞信息按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取点赞时间*/ 
					if(ET_thumpTime.getText().toString().equals("")) {
						Toast.makeText(ThumbUpEditActivity.this, "点赞时间输入不能为空!", Toast.LENGTH_LONG).show();
						ET_thumpTime.setFocusable(true);
						ET_thumpTime.requestFocus();
						return;	
					}
					thumbUp.setThumpTime(ET_thumpTime.getText().toString());
					/*调用业务逻辑层上传点赞信息信息*/
					ThumbUpEditActivity.this.setTitle("正在更新点赞信息信息，稍等...");
					String result = thumbUpService.UpdateThumbUp(thumbUp);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
		initViewData();
	}

	/* 初始化显示编辑界面的数据 */
	private void initViewData() {
	    thumbUp = thumbUpService.GetThumbUp(thumbUpId);
		this.TV_thumbUpId.setText(thumbUpId+"");
		for (int i = 0; i < topicList.size(); i++) {
			if (thumbUp.getTopObj() == topicList.get(i).getTopicId()) {
				this.spinner_topObj.setSelection(i);
				break;
			}
		}
		for (int i = 0; i < studentList.size(); i++) {
			if (thumbUp.getStudentObj().equals(studentList.get(i).getStudentNumber())) {
				this.spinner_studentObj.setSelection(i);
				break;
			}
		}
		this.ET_thumpTime.setText(thumbUp.getThumpTime());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
