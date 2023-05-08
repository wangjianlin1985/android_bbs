package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
public class CollegeAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明学院编号输入框
	private EditText ET_collegeNumber;
	// 声明学院名称输入框
	private EditText ET_collegeName;
	protected String carmera_path;
	/*要保存的学院信息信息*/
	College college = new College();
	/*学院信息管理业务逻辑层*/
	private CollegeService collegeService = new CollegeService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.college_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("添加学院信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		ET_collegeNumber = (EditText) findViewById(R.id.ET_collegeNumber);
		ET_collegeName = (EditText) findViewById(R.id.ET_collegeName);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加学院信息按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取学院编号*/
					if(ET_collegeNumber.getText().toString().equals("")) {
						Toast.makeText(CollegeAddActivity.this, "学院编号输入不能为空!", Toast.LENGTH_LONG).show();
						ET_collegeNumber.setFocusable(true);
						ET_collegeNumber.requestFocus();
						return;
					}
					college.setCollegeNumber(ET_collegeNumber.getText().toString());
					/*验证获取学院名称*/ 
					if(ET_collegeName.getText().toString().equals("")) {
						Toast.makeText(CollegeAddActivity.this, "学院名称输入不能为空!", Toast.LENGTH_LONG).show();
						ET_collegeName.setFocusable(true);
						ET_collegeName.requestFocus();
						return;	
					}
					college.setCollegeName(ET_collegeName.getText().toString());
					/*调用业务逻辑层上传学院信息信息*/
					CollegeAddActivity.this.setTitle("正在上传学院信息信息，稍等...");
					String result = collegeService.AddCollege(college);
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
