package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.TopicClass;
import com.mobileclient.service.TopicClassService;
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

public class TopicClassEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明分类idTextView
	private TextView TV_topicClassId;
	// 声明分类名称输入框
	private EditText ET_topicClassName;
	protected String carmera_path;
	/*要保存的话题分类信息*/
	TopicClass topicClass = new TopicClass();
	/*话题分类管理业务逻辑层*/
	private TopicClassService topicClassService = new TopicClassService();

	private int topicClassId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.topicclass_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑话题分类信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_topicClassId = (TextView) findViewById(R.id.TV_topicClassId);
		ET_topicClassName = (EditText) findViewById(R.id.ET_topicClassName);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		topicClassId = extras.getInt("topicClassId");
		/*单击修改话题分类按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取分类名称*/ 
					if(ET_topicClassName.getText().toString().equals("")) {
						Toast.makeText(TopicClassEditActivity.this, "分类名称输入不能为空!", Toast.LENGTH_LONG).show();
						ET_topicClassName.setFocusable(true);
						ET_topicClassName.requestFocus();
						return;	
					}
					topicClass.setTopicClassName(ET_topicClassName.getText().toString());
					/*调用业务逻辑层上传话题分类信息*/
					TopicClassEditActivity.this.setTitle("正在更新话题分类信息，稍等...");
					String result = topicClassService.UpdateTopicClass(topicClass);
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
	    topicClass = topicClassService.GetTopicClass(topicClassId);
		this.TV_topicClassId.setText(topicClassId+"");
		this.ET_topicClassName.setText(topicClass.getTopicClassName());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
