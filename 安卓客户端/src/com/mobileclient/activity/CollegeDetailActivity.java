package com.mobileclient.activity;

import java.util.Date;
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
public class CollegeDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明学院编号控件
	private TextView TV_collegeNumber;
	// 声明学院名称控件
	private TextView TV_collegeName;
	/* 要保存的学院信息信息 */
	College college = new College(); 
	/* 学院信息管理业务逻辑层 */
	private CollegeService collegeService = new CollegeService();
	private String collegeNumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.college_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看学院信息详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_collegeNumber = (TextView) findViewById(R.id.TV_collegeNumber);
		TV_collegeName = (TextView) findViewById(R.id.TV_collegeName);
		Bundle extras = this.getIntent().getExtras();
		collegeNumber = extras.getString("collegeNumber");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CollegeDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    college = collegeService.GetCollege(collegeNumber); 
		this.TV_collegeNumber.setText(college.getCollegeNumber());
		this.TV_collegeName.setText(college.getCollegeName());
	} 
}
