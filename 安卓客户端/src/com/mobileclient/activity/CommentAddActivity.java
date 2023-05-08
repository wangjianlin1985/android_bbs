package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.Comment;
import com.mobileclient.service.CommentService;
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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class CommentAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明评论的话题下拉框
	private Spinner spinner_topicObj;
	private ArrayAdapter<String> topicObj_adapter;
	private static  String[] topicObj_ShowText  = null;
	private List<Topic> topicList = null;
	/*评论的话题管理业务逻辑层*/
	private TopicService topicService = new TopicService();
	// 声明评论内容输入框
	private EditText ET_content;
	// 声明评论的学生下拉框
	private Spinner spinner_studentObj;
	private ArrayAdapter<String> studentObj_adapter;
	private static  String[] studentObj_ShowText  = null;
	private List<Student> studentList = null;
	/*评论的学生管理业务逻辑层*/
	private StudentService studentService = new StudentService();
	// 声明评论时间输入框
	private EditText ET_commentTime;
	protected String carmera_path;
	/*要保存的评论信息*/
	Comment comment = new Comment();
	/*评论管理业务逻辑层*/
	private CommentService commentService = new CommentService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.comment_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("添加评论");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		spinner_topicObj = (Spinner) findViewById(R.id.Spinner_topicObj);
		// 获取所有的评论的话题
		try {
			topicList = topicService.QueryTopic(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int topicCount = topicList.size();
		topicObj_ShowText = new String[topicCount];
		for(int i=0;i<topicCount;i++) { 
			topicObj_ShowText[i] = topicList.get(i).getTitle();
		}
		// 将可选内容与ArrayAdapter连接起来
		topicObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, topicObj_ShowText);
		// 设置下拉列表的风格
		topicObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_topicObj.setAdapter(topicObj_adapter);
		// 添加事件Spinner事件监听
		spinner_topicObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				comment.setTopicObj(topicList.get(arg2).getTopicId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_topicObj.setVisibility(View.VISIBLE);
		ET_content = (EditText) findViewById(R.id.ET_content);
		spinner_studentObj = (Spinner) findViewById(R.id.Spinner_studentObj);
		// 获取所有的评论的学生
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
		// 设置下拉列表的风格
		studentObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_studentObj.setAdapter(studentObj_adapter);
		// 添加事件Spinner事件监听
		spinner_studentObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				comment.setStudentObj(studentList.get(arg2).getStudentNumber()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_studentObj.setVisibility(View.VISIBLE);
		ET_commentTime = (EditText) findViewById(R.id.ET_commentTime);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加评论按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取评论内容*/ 
					if(ET_content.getText().toString().equals("")) {
						Toast.makeText(CommentAddActivity.this, "评论内容输入不能为空!", Toast.LENGTH_LONG).show();
						ET_content.setFocusable(true);
						ET_content.requestFocus();
						return;	
					}
					comment.setContent(ET_content.getText().toString());
					/*验证获取评论时间*/ 
					if(ET_commentTime.getText().toString().equals("")) {
						Toast.makeText(CommentAddActivity.this, "评论时间输入不能为空!", Toast.LENGTH_LONG).show();
						ET_commentTime.setFocusable(true);
						ET_commentTime.requestFocus();
						return;	
					}
					comment.setCommentTime(ET_commentTime.getText().toString());
					/*调用业务逻辑层上传评论信息*/
					CommentAddActivity.this.setTitle("正在上传评论信息，稍等...");
					String result = commentService.AddComment(comment);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
