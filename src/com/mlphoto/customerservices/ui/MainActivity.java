package com.mlphoto.customerservices.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.XMPPException;
import com.mlphoto.customerservice.service.ChatService;
import com.mlphoto.customerservice.service.ClientConServer;
import com.mlphoto.customerservice.service.UserOperateService;
import com.mlphoto.customerservice.util.CommonUtils;
import com.mlphoto.customerservice.util.Config; 
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import om.mlphoto.customerservices.interfaces.TotalMessageInterface;

public class MainActivity extends Activity implements TotalMessageInterface {

	private Button cus_btn, register_bnt, service_bnt;
	private TextView tv_recent_unread;

	private UserOperateService userOperateService;

	private Thread mThread;
 
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_main);

		initRunable();
		initView();
		initClick();

	}

	private void initView() {

		tv_recent_unread = (TextView) findViewById(R.id.tv_recent_unread);
		cus_btn = (Button) findViewById(R.id.cus_bnt);
		register_bnt = (Button) findViewById(R.id.register_bnt);
		service_bnt = (Button) findViewById(R.id.service_bnt);

	}

	private void initClick() {
		cus_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv_recent_unread.setVisibility(View.GONE);

				// 跳转到 聊天界面时 把 全局消息监听取消
				ChatService.getInstance(Config.chatToService).setTotalMessageInterface(null);

				// 不管有没有成功都跳到聊天界面，因为没登录成功也可以看 历史聊天记录
				Intent intent = new Intent(MainActivity.this, CustomerServiceActivity.class);

				startActivity(intent);

			}
		});

		register_bnt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断有没有连网的 工具类
				if (CommonUtils.isNetworkAvailable(getApplicationContext())) {
					// 执行注册操作
					new Thread(regRunnable).start();

				}
			}
		});

	}

	private void initRunable() {

		// 注册
		if (CommonUtils.isNetworkAvailable(getApplicationContext())) {
			new Thread(regRunnable).start();

		}

		// 启动登陆线程,奇怪这个多次登录操作，再发消息会出错，不知道是不是
		// Connection 类的 connection 对象被回收
		mThread = new Thread(loginRunable);
		mThread.start();
	}

	/**
	 * 注册线程
	 */
	Runnable regRunnable = new Runnable() {

		@Override
		public void run() {
			try {

				new ClientConServer(Config.ServerIp, Config.ServerPort);

				userOperateService = new UserOperateService();
				Map<String, String> attributes = new HashMap<String, String>();
				// attributes cant't null
				attributes.put("date", "2-2-2");

				if (userOperateService.regAccount(Config.userAccount, Config.userPassword, attributes)) {

				} else {

				}
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}

		}
	};

	/**
	 * 登陆线程
	 */
	Runnable loginRunable = new Runnable() {

		@Override
		public void run() {
			// 登陆子线程

			ClientConServer ccs = new ClientConServer(MainActivity.this);
			boolean loginStatus = ccs.login(Config.userAccount, Config.userPassword, Config.ServerIp,
					Config.ServerPort);
			if (loginStatus) {
				Log.i("loginStatus", "+++++++loginStatus");
			} else {
				Log.i("loginStatus", "-------loginStatus");
			}

		}
	};

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i("Activitylive", "+++++onRestart");
		// 重新注册消息监听
		
		msgNum = 0;
		
		ChatService.getInstance(Config.chatToService).setTotalMessageInterface(this);

		android.os.Message handlerMsg = android.os.Message.obtain();
		handlerMsg.obj = msgNum;
		totalOnlineMessage.sendMessage(handlerMsg);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("Activitylive", "+++++onStart");

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("Activitylive", "+++++onResume");

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("Activitylive", "+++++onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("Activitylive", "+++++onStop");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("Activitylive", "+++++onDestroy");
	}

	private static int msgNum = 0;

	@Override
	public void getTotalOnlineMessage(String msg) {

		if (msg != null && msg != "") {
			msgNum++;

			android.os.Message handlerMsg = android.os.Message.obtain();
			handlerMsg.obj = msgNum;
			totalOnlineMessage.sendMessage(handlerMsg);

		}
	}

	Handler totalOnlineMessage = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);

			int msgNum = (Integer) msg.obj;

			if (msgNum == 0) {
				tv_recent_unread.setVisibility(View.GONE);
			} else {
				tv_recent_unread.setVisibility(View.VISIBLE);
				tv_recent_unread.setText(msgNum + "");
			}
		}

	};
}
