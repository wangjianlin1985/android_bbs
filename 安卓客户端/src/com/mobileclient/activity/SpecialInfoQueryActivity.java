package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.SpecialInfo;
import com.mobileclient.domain.College;
import com.mobileclient.service.CollegeService;

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
public class SpecialInfoQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明所属学院下拉框
	private Spinner spinner_collegeObj;
	private ArrayAdapter<String> collegeObj_adapter;
	private static  String[] collegeObj_ShowText  = null;
	private List<College> collegeList = null; 
	/*学院信息管理业务逻辑层*/
	private CollegeService collegeService = new CollegeService();
	// 声明专业编号输入框
	private EditText ET_specialNumber;
	// 声明专业名称输入框
	private EditText ET_specialName;
	/*查询过滤条件保存到这个对象中*/
	private SpecialInfo queryConditionSpecialInfo = new SpecialInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.specialinfo_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("设置专业信息查询条件");
		ImageView back_btn = (ImageView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnQuery = (Button) findViewById(R.id.btnQuery);
		spinner_collegeObj = (Spinner) findViewById(R.id.Spinner_collegeObj);
		// 获取所有的学院信息
		try {
			collegeList = collegeService.QueryCollege(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int collegeCount = collegeList.size();
		collegeObj_ShowText = new String[collegeCount+1];
		collegeObj_ShowText[0] = "不限制";
		for(int i=1;i<=collegeCount;i++) { 
			collegeObj_ShowText[i] = collegeList.get(i-1).getCollegeName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		collegeObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, collegeObj_ShowText);
		// 设置所属学院下拉列表的风格
		collegeObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_collegeObj.setAdapter(collegeObj_adapter);
		// 添加事件Spinner事件监听
		spinner_collegeObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionSpecialInfo.setCollegeObj(collegeList.get(arg2-1).getCollegeNumber()); 
				else
					queryConditionSpecialInfo.setCollegeObj("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_collegeObj.setVisibility(View.VISIBLE);
		ET_specialNumber = (EditText) findViewById(R.id.ET_specialNumber);
		ET_specialName = (EditText) findViewById(R.id.ET_specialName);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionSpecialInfo.setSpecialNumber(ET_specialNumber.getText().toString());
					queryConditionSpecialInfo.setSpecialName(ET_specialName.getText().toString());
					Intent intent = getIntent();
					//这里使用bundle绷带来传输数据
					Bundle bundle =new Bundle();
					//传输的内容仍然是键值对的形式
					bundle.putSerializable("queryConditionSpecialInfo", queryConditionSpecialInfo);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
