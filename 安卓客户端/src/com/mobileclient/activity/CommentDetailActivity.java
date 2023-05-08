package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.Comment;
import com.mobileclient.service.CommentService;
import com.mobileclient.domain.Topic;
import com.mobileclient.service.TopicService;
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
public class CommentDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明记录编号控件
	private TextView TV_commentId;
	// 声明评论的话题控件
	private TextView TV_topicObj;
	// 声明评论内容控件
	private TextView TV_content;
	// 声明评论的学生控件
	private TextView TV_studentObj;
	// 声明评论时间控件
	private TextView TV_commentTime;
	/* 要保存的评论信息 */
	Comment comment = new Comment(); 
	/* 评论管理业务逻辑层 */
	private CommentService commentService = new CommentService();
	private TopicService topicService = new TopicService();
	private StudentService studentService = new StudentService();
	private int commentId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.comment_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看评论详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_commentId = (TextView) findViewById(R.id.TV_commentId);
		TV_topicObj = (TextView) findViewById(R.id.TV_topicObj);
		TV_content = (TextView) findViewById(R.id.TV_content);
		TV_studentObj = (TextView) findViewById(R.id.TV_studentObj);
		TV_commentTime = (TextView) findViewById(R.id.TV_commentTime);
		Bundle extras = this.getIntent().getExtras();
		commentId = extras.getInt("commentId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CommentDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    comment = commentService.GetComment(commentId); 
		this.TV_commentId.setText(comment.getCommentId() + "");
		Topic topicObj = topicService.GetTopic(comment.getTopicObj());
		this.TV_topicObj.setText(topicObj.getTitle());
		this.TV_content.setText(comment.getContent());
		Student studentObj = studentService.GetStudent(comment.getStudentObj());
		this.TV_studentObj.setText(studentObj.getStudentName());
		this.TV_commentTime.setText(comment.getCommentTime());
	} 
}
