package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.SpecialInfo;
import com.mobileclient.service.SpecialInfoService;
import com.mobileclient.domain.College;
import com.mobileclient.service.CollegeService;
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
public class SpecialInfoDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明所属学院控件
	private TextView TV_collegeObj;
	// 声明专业编号控件
	private TextView TV_specialNumber;
	// 声明专业名称控件
	private TextView TV_specialName;
	// 声明开办日期控件
	private TextView TV_startDate;
	// 声明专业介绍控件
	private TextView TV_introduction;
	/* 要保存的专业信息信息 */
	SpecialInfo specialInfo = new SpecialInfo(); 
	/* 专业信息管理业务逻辑层 */
	private SpecialInfoService specialInfoService = new SpecialInfoService();
	private CollegeService collegeService = new CollegeService();
	private String specialNumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.specialinfo_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看专业信息详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_collegeObj = (TextView) findViewById(R.id.TV_collegeObj);
		TV_specialNumber = (TextView) findViewById(R.id.TV_specialNumber);
		TV_specialName = (TextView) findViewById(R.id.TV_specialName);
		TV_startDate = (TextView) findViewById(R.id.TV_startDate);
		TV_introduction = (TextView) findViewById(R.id.TV_introduction);
		Bundle extras = this.getIntent().getExtras();
		specialNumber = extras.getString("specialNumber");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SpecialInfoDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    specialInfo = specialInfoService.GetSpecialInfo(specialNumber); 
		College collegeObj = collegeService.GetCollege(specialInfo.getCollegeObj());
		this.TV_collegeObj.setText(collegeObj.getCollegeName());
		this.TV_specialNumber.setText(specialInfo.getSpecialNumber());
		this.TV_specialName.setText(specialInfo.getSpecialName());
		Date startDate = new Date(specialInfo.getStartDate().getTime());
		String startDateStr = (startDate.getYear() + 1900) + "-" + (startDate.getMonth()+1) + "-" + startDate.getDate();
		this.TV_startDate.setText(startDateStr);
		this.TV_introduction.setText(specialInfo.getIntroduction());
	} 
}
