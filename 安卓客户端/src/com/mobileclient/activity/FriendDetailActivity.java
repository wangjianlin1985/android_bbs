package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.Friend;
import com.mobileclient.service.FriendService;
import com.mobileclient.domain.Student;
import com.mobileclient.service.StudentService;
import com.mobileclient.domain.Student;
import com.mobileclient.service.StudentService;
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
public class FriendDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明记录编号控件
	private TextView TV_friendId;
	// 声明学生1控件
	private TextView TV_studentObj1;
	// 声明好友控件
	private TextView TV_studentObj2;
	// 声明添加时间控件
	private TextView TV_addTime;
	/* 要保存的好友信息信息 */
	Friend friend = new Friend(); 
	/* 好友信息管理业务逻辑层 */
	private FriendService friendService = new FriendService();
	private StudentService studentService = new StudentService();
	private int friendId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.friend_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看好友信息详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_friendId = (TextView) findViewById(R.id.TV_friendId);
		TV_studentObj1 = (TextView) findViewById(R.id.TV_studentObj1);
		TV_studentObj2 = (TextView) findViewById(R.id.TV_studentObj2);
		TV_addTime = (TextView) findViewById(R.id.TV_addTime);
		Bundle extras = this.getIntent().getExtras();
		friendId = extras.getInt("friendId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FriendDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    friend = friendService.GetFriend(friendId); 
		this.TV_friendId.setText(friend.getFriendId() + "");
		Student studentObj1 = studentService.GetStudent(friend.getStudentObj1());
		this.TV_studentObj1.setText(studentObj1.getStudentName());
		Student studentObj2 = studentService.GetStudent(friend.getStudentObj2());
		this.TV_studentObj2.setText(studentObj2.getStudentName());
		this.TV_addTime.setText(friend.getAddTime());
	} 
}
