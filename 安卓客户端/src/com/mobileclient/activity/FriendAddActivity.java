package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.Friend;
import com.mobileclient.service.FriendService;
import com.mobileclient.domain.Student;
import com.mobileclient.service.StudentService;
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
public class FriendAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明学生1下拉框
	private Spinner spinner_studentObj1;
	private ArrayAdapter<String> studentObj1_adapter;
	private static  String[] studentObj1_ShowText  = null;
	private List<Student> studentList = null;
	/*学生1管理业务逻辑层*/
	private StudentService studentService = new StudentService();
	// 声明好友下拉框
	private Spinner spinner_studentObj2;
	private ArrayAdapter<String> studentObj2_adapter;
	private static  String[] studentObj2_ShowText  = null;
	// 声明添加时间输入框
	private EditText ET_addTime;
	protected String carmera_path;
	/*要保存的好友信息信息*/
	Friend friend = new Friend();
	/*好友信息管理业务逻辑层*/
	private FriendService friendService = new FriendService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.friend_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("添加好友信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		spinner_studentObj1 = (Spinner) findViewById(R.id.Spinner_studentObj1);
		// 获取所有的学生1
		try {
			studentList = studentService.QueryStudent(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int studentCount = studentList.size();
		studentObj1_ShowText = new String[studentCount];
		for(int i=0;i<studentCount;i++) { 
			studentObj1_ShowText[i] = studentList.get(i).getStudentName();
		}
		// 将可选内容与ArrayAdapter连接起来
		studentObj1_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, studentObj1_ShowText);
		// 设置下拉列表的风格
		studentObj1_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_studentObj1.setAdapter(studentObj1_adapter);
		// 添加事件Spinner事件监听
		spinner_studentObj1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				friend.setStudentObj1(studentList.get(arg2).getStudentNumber()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_studentObj1.setVisibility(View.VISIBLE);
		spinner_studentObj2 = (Spinner) findViewById(R.id.Spinner_studentObj2);
		studentObj2_ShowText = new String[studentCount];
		for(int i=0;i<studentCount;i++) { 
			studentObj2_ShowText[i] = studentList.get(i).getStudentName();
		}
		// 将可选内容与ArrayAdapter连接起来
		studentObj2_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, studentObj2_ShowText);
		// 设置下拉列表的风格
		studentObj2_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_studentObj2.setAdapter(studentObj2_adapter);
		// 添加事件Spinner事件监听
		spinner_studentObj2.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				friend.setStudentObj2(studentList.get(arg2).getStudentNumber()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_studentObj2.setVisibility(View.VISIBLE);
		ET_addTime = (EditText) findViewById(R.id.ET_addTime);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加好友信息按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取添加时间*/ 
					if(ET_addTime.getText().toString().equals("")) {
						Toast.makeText(FriendAddActivity.this, "添加时间输入不能为空!", Toast.LENGTH_LONG).show();
						ET_addTime.setFocusable(true);
						ET_addTime.requestFocus();
						return;	
					}
					friend.setAddTime(ET_addTime.getText().toString());
					/*调用业务逻辑层上传好友信息信息*/
					FriendAddActivity.this.setTitle("正在上传好友信息信息，稍等...");
					String result = friendService.AddFriend(friend);
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
