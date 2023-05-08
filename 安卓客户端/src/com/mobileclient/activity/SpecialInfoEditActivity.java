package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
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
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class SpecialInfoEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明专业编号TextView
	private TextView TV_specialNumber;
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

	private String specialNumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.specialinfo_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑专业信息信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_specialNumber = (TextView) findViewById(R.id.TV_specialNumber);
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
		// 设置图书类别下拉列表的风格
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
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		specialNumber = extras.getString("specialNumber");
		/*单击修改专业信息按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取专业名称*/ 
					if(ET_specialName.getText().toString().equals("")) {
						Toast.makeText(SpecialInfoEditActivity.this, "专业名称输入不能为空!", Toast.LENGTH_LONG).show();
						ET_specialName.setFocusable(true);
						ET_specialName.requestFocus();
						return;	
					}
					specialInfo.setSpecialName(ET_specialName.getText().toString());
					/*获取出版日期*/
					Date startDate = new Date(dp_startDate.getYear()-1900,dp_startDate.getMonth(),dp_startDate.getDayOfMonth());
					specialInfo.setStartDate(new Timestamp(startDate.getTime()));
					/*验证获取专业介绍*/ 
					if(ET_introduction.getText().toString().equals("")) {
						Toast.makeText(SpecialInfoEditActivity.this, "专业介绍输入不能为空!", Toast.LENGTH_LONG).show();
						ET_introduction.setFocusable(true);
						ET_introduction.requestFocus();
						return;	
					}
					specialInfo.setIntroduction(ET_introduction.getText().toString());
					/*调用业务逻辑层上传专业信息信息*/
					SpecialInfoEditActivity.this.setTitle("正在更新专业信息信息，稍等...");
					String result = specialInfoService.UpdateSpecialInfo(specialInfo);
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
	    specialInfo = specialInfoService.GetSpecialInfo(specialNumber);
		this.TV_specialNumber.setText(specialNumber);
		for (int i = 0; i < collegeList.size(); i++) {
			if (specialInfo.getCollegeObj().equals(collegeList.get(i).getCollegeNumber())) {
				this.spinner_collegeObj.setSelection(i);
				break;
			}
		}
		this.ET_specialName.setText(specialInfo.getSpecialName());
		Date startDate = new Date(specialInfo.getStartDate().getTime());
		this.dp_startDate.init(startDate.getYear() + 1900,startDate.getMonth(), startDate.getDate(), null);
		this.ET_introduction.setText(specialInfo.getIntroduction());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
