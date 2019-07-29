package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.Friend;
import com.mobileclient.domain.Student;
import com.mobileclient.service.StudentService;
import com.mobileclient.domain.Student;
import com.mobileclient.service.StudentService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.ImageView;
import android.widget.TextView;
public class FriendQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明学生1下拉框
	private Spinner spinner_studentObj1;
	private ArrayAdapter<String> studentObj1_adapter;
	private static  String[] studentObj1_ShowText  = null;
	private List<Student> studentList = null; 
	/*学生信息管理业务逻辑层*/
	private StudentService studentService = new StudentService();
	// 声明好友下拉框
	private Spinner spinner_studentObj2;
	private ArrayAdapter<String> studentObj2_adapter;
	private static  String[] studentObj2_ShowText  = null;
	/*查询过滤条件保存到这个对象中*/
	private Friend queryConditionFriend = new Friend();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.friend_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("设置好友信息查询条件");
		ImageView back_btn = (ImageView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnQuery = (Button) findViewById(R.id.btnQuery);
		spinner_studentObj1 = (Spinner) findViewById(R.id.Spinner_studentObj1);
		// 获取所有的学生信息
		try {
			studentList = studentService.QueryStudent(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int studentCount = studentList.size();
		studentObj1_ShowText = new String[studentCount+1];
		studentObj1_ShowText[0] = "不限制";
		for(int i=1;i<=studentCount;i++) { 
			studentObj1_ShowText[i] = studentList.get(i-1).getStudentName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		studentObj1_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, studentObj1_ShowText);
		// 设置学生1下拉列表的风格
		studentObj1_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_studentObj1.setAdapter(studentObj1_adapter);
		// 添加事件Spinner事件监听
		spinner_studentObj1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionFriend.setStudentObj1(studentList.get(arg2-1).getStudentNumber()); 
				else
					queryConditionFriend.setStudentObj1("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_studentObj1.setVisibility(View.VISIBLE);
		spinner_studentObj2 = (Spinner) findViewById(R.id.Spinner_studentObj2);
		studentObj2_ShowText = new String[studentCount+1];
		studentObj2_ShowText[0] = "不限制";
		for(int i=1;i<=studentCount;i++) { 
			studentObj2_ShowText[i] = studentList.get(i-1).getStudentName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		studentObj2_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, studentObj2_ShowText);
		// 设置好友下拉列表的风格
		studentObj2_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_studentObj2.setAdapter(studentObj2_adapter);
		// 添加事件Spinner事件监听
		spinner_studentObj2.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionFriend.setStudentObj2(studentList.get(arg2-1).getStudentNumber()); 
				else
					queryConditionFriend.setStudentObj2("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_studentObj2.setVisibility(View.VISIBLE);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					Intent intent = getIntent();
					//这里使用bundle绷带来传输数据
					Bundle bundle =new Bundle();
					//传输的内容仍然是键值对的形式
					bundle.putSerializable("queryConditionFriend", queryConditionFriend);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
