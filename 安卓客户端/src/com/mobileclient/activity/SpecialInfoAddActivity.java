package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.SpecialInfo;
import com.mobileclient.service.SpecialInfoService;
import com.mobileclient.domain.College;
import com.mobileclient.service.CollegeService;
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
public class SpecialInfoAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明专业编号输入框
	private EditText ET_specialNumber;
	// 声明所属学院下拉框
	private Spinner spinner_collegeObj;
	private ArrayAdapter<String> collegeObj_adapter;
	private static  String[] collegeObj_ShowText  = null;
	private List<College> collegeList = null;
	/*所属学院管理业务逻辑层*/
	private CollegeService collegeService = new CollegeService();
	// 声明专业名称输入框
	private EditText ET_specialName;
	// 出版开办日期控件
	private DatePicker dp_startDate;
	// 声明专业介绍输入框
	private EditText ET_introduction;
	protected String carmera_path;
	/*要保存的专业信息信息*/
	SpecialInfo specialInfo = new SpecialInfo();
	/*专业信息管理业务逻辑层*/
	private SpecialInfoService specialInfoService = new SpecialInfoService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.specialinfo_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("添加专业信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		ET_specialNumber = (EditText) findViewById(R.id.ET_specialNumber);
		spinner_collegeObj = (Spinner) findViewById(R.id.Spinner_collegeObj);
		// 获取所有的所属学院
		try {
			collegeList = collegeService.QueryCollege(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int collegeCount = collegeList.size();
		collegeObj_ShowText = new String[collegeCount];
		for(int i=0;i<collegeCount;i++) { 
			collegeObj_ShowText[i] = collegeList.get(i).getCollegeName();
		}
		// 将可选内容与ArrayAdapter连接起来
		collegeObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, collegeObj_ShowText);
		// 设置下拉列表的风格
		collegeObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_collegeObj.setAdapter(collegeObj_adapter);
		// 添加事件Spinner事件监听
		spinner_collegeObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				specialInfo.setCollegeObj(collegeList.get(arg2).getCollegeNumber()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_collegeObj.setVisibility(View.VISIBLE);
		ET_specialName = (EditText) findViewById(R.id.ET_specialName);
		dp_startDate = (DatePicker)this.findViewById(R.id.dp_startDate);
		ET_introduction = (EditText) findViewById(R.id.ET_introduction);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加专业信息按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取专业编号*/
					if(ET_specialNumber.getText().toString().equals("")) {
						Toast.makeText(SpecialInfoAddActivity.this, "专业编号输入不能为空!", Toast.LENGTH_LONG).show();
						ET_specialNumber.setFocusable(true);
						ET_specialNumber.requestFocus();
						return;
					}
					specialInfo.setSpecialNumber(ET_specialNumber.getText().toString());
					/*验证获取专业名称*/ 
					if(ET_specialName.getText().toString().equals("")) {
						Toast.makeText(SpecialInfoAddActivity.this, "专业名称输入不能为空!", Toast.LENGTH_LONG).show();
						ET_specialName.setFocusable(true);
						ET_specialName.requestFocus();
						return;	
					}
					specialInfo.setSpecialName(ET_specialName.getText().toString());
					/*获取开办日期*/
					Date startDate = new Date(dp_startDate.getYear()-1900,dp_startDate.getMonth(),dp_startDate.getDayOfMonth());
					specialInfo.setStartDate(new Timestamp(startDate.getTime()));
					/*验证获取专业介绍*/ 
					if(ET_introduction.getText().toString().equals("")) {
						Toast.makeText(SpecialInfoAddActivity.this, "专业介绍输入不能为空!", Toast.LENGTH_LONG).show();
						ET_introduction.setFocusable(true);
						ET_introduction.requestFocus();
						return;	
					}
					specialInfo.setIntroduction(ET_introduction.getText().toString());
					/*调用业务逻辑层上传专业信息信息*/
					SpecialInfoAddActivity.this.setTitle("正在上传专业信息信息，稍等...");
					String result = specialInfoService.AddSpecialInfo(specialInfo);
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
