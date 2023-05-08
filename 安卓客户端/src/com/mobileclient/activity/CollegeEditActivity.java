package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
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

public class CollegeEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明学院编号TextView
	private TextView TV_collegeNumber;
	// 声明学院名称输入框
	private EditText ET_collegeName;
	protected String carmera_path;
	/*要保存的学院信息信息*/
	College college = new College();
	/*学院信息管理业务逻辑层*/
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
		setContentView(R.layout.college_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑学院信息信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_collegeNumber = (TextView) findViewById(R.id.TV_collegeNumber);
		ET_collegeName = (EditText) findViewById(R.id.ET_collegeName);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		collegeNumber = extras.getString("collegeNumber");
		/*单击修改学院信息按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取学院名称*/ 
					if(ET_collegeName.getText().toString().equals("")) {
						Toast.makeText(CollegeEditActivity.this, "学院名称输入不能为空!", Toast.LENGTH_LONG).show();
						ET_collegeName.setFocusable(true);
						ET_collegeName.requestFocus();
						return;	
					}
					college.setCollegeName(ET_collegeName.getText().toString());
					/*调用业务逻辑层上传学院信息信息*/
					CollegeEditActivity.this.setTitle("正在更新学院信息信息，稍等...");
					String result = collegeService.UpdateCollege(college);
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
	    college = collegeService.GetCollege(collegeNumber);
		this.TV_collegeNumber.setText(collegeNumber);
		this.ET_collegeName.setText(college.getCollegeName());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
