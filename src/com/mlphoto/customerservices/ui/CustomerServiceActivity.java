package com.mlphoto.customerservices.ui;

import java.util.ArrayList;
import java.util.List;

import com.mlphoto.customerservice.bean.ChatMessage;
import com.mlphoto.customerservice.bean.FaceText;
import com.mlphoto.customerservice.db.DbManagerUtil;
import com.mlphoto.customerservice.service.ChatService;
import com.mlphoto.customerservice.service.ClientConServer;
import com.mlphoto.customerservice.util.CommonUtils;
import com.mlphoto.customerservice.util.Config;
import com.mlphoto.customerservice.util.FaceTextUtils;
import com.mlphoto.customerservice.util.HandleChatManager;
import com.mlphoto.customerservice.util.MlConstants;
import com.mlphoto.customerservice.view.xlist.XListView;
import com.mlphoto.customerservice.view.xlist.XListView.IXListViewListener;
import com.mlphoto.customerservice.views.EmoticonsEditText;
import com.mlphoto.customerservices.adapter.EmoViewPagerAdapter;
import com.mlphoto.customerservices.adapter.EmoteAdapter;
import com.mlphoto.customerservices.adapter.MessageChatAdapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import om.mlphoto.customerservices.interfaces.MessageInterface;

/**
 * �������
 * 
 * @ClassName: CustomerServiceActivity
 * @author ZSS
 * @date 2016-3-23 PM
 */

public class CustomerServiceActivity extends BaseActivity
		implements OnClickListener, IXListViewListener, MessageInterface {

	private Button btn_chat_emo, btn_chat_send, btn_chat_add, btn_chat_keyboard, btn_speak, btn_chat_voice, btn_back;

	private LinearLayout layout_more, layout_emo, layout_add;

	private ViewPager pager_emo;

	private TextView tv_picture, tv_camera, tv_location;

	private XListView mListView;
	protected LinearLayout mHeaderLayout;
	// �����й�
	RelativeLayout layout_record;
	TextView tv_voice_tips;
	ImageView iv_record;
	EmoticonsEditText edit_user_comment;
 

	// ���������
	private HandleChatManager handleChatManager;
	// ���������Ӽ����߳�
	private Thread conncetToServerListenerThread;

	private Drawable[] drawable_Anims;// ��Ͳ����
	 
	private boolean isConnectOK = false;// ��ʾ��ǰ�������������״̬,Ĭ�ϵ�ǰΪ������

	private static int MsgPagerNum;

	private ArrayList<String> messageList = new ArrayList<String>();

	@Override
	public int initsetView() {
		// TODO Auto-generated method stub
		return R.layout.activity_customer_service;
	}

	@Override
	public void initView(Context context, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mListView = (XListView) findViewById(R.id.mListView);
		btn_back = (Button) findViewById(R.id.btn_bach);
		 

		Log.i("Activitylive", "-------------------------------initView");

		initViewData();
		initBottomView();
		initXListView();
		initVoiceView();
	}

	@Override
	public void initData() {
		// ����������״̬���Ӽ����߳�
				conncetToServerListenerThread = new Thread(connectToServeListerRunable);
				conncetToServerListenerThread.start();
	}

	@Override
	public void initEvent() {
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// uMessageHandler.removeCallbacks(null);
				finish();

			}
		});
 
	}

	private void initViewData() {
 
		ChatService.getInstance(Config.chatToService).setMessageInterface(this);
 
		handleChatManager = HandleChatManager.getInstance(getApplicationContext(), null);
	}
  

	private void initBottomView() {
		// ����� ��Ӱ�ť �� ���鰴ť
		btn_chat_add = (Button) findViewById(R.id.btn_chat_add);
		btn_chat_emo = (Button) findViewById(R.id.btn_chat_emo);
		btn_chat_add.setOnClickListener(this);
		btn_chat_emo.setOnClickListener(this);
		// ���ұ� ������ť �����̰�ť����Ϣ���Ͱ�ť
		btn_chat_keyboard = (Button) findViewById(R.id.btn_chat_keyboard);
		btn_chat_voice = (Button) findViewById(R.id.btn_chat_voice);
		btn_chat_voice.setOnClickListener(this);
		btn_chat_keyboard.setOnClickListener(this);
		btn_chat_send = (Button) findViewById(R.id.btn_chat_send);
		btn_chat_send.setOnClickListener(this);
		// ������ ���鲼�� �� ��Ӳ���
		layout_more = (LinearLayout) findViewById(R.id.layout_more);
		layout_emo = (LinearLayout) findViewById(R.id.layout_emo);
		layout_add = (LinearLayout) findViewById(R.id.layout_add);

		initAddView();
		initEmoView();

		// ���м�
		// ������
		btn_speak = (Button) findViewById(R.id.btn_speak);
		// �����
		edit_user_comment = (EmoticonsEditText) findViewById(R.id.edit_user_comment);
		edit_user_comment.setOnClickListener(this);
		edit_user_comment.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(s)) {
					btn_chat_send.setVisibility(View.VISIBLE);
					btn_chat_keyboard.setVisibility(View.GONE);
					btn_chat_voice.setVisibility(View.GONE);
				} else {
					if (btn_chat_voice.getVisibility() != View.VISIBLE) {
						btn_chat_voice.setVisibility(View.VISIBLE);
						btn_chat_send.setVisibility(View.GONE);
						btn_chat_keyboard.setVisibility(View.GONE);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});

	}

	private MessageChatAdapter mAdapter;
 

	private void initXListView() {
		// ���Ȳ�������ظ���
		mListView.setPullLoadEnable(false);
		// ��������
		mListView.setPullRefreshEnable(true);
		// ���ü�����
		mListView.setXListViewListener(this);
		mListView.pullRefreshing();
		mListView.setDividerHeight(0);
		// ��������
		initOrRefresh();
		mListView.setSelection(mAdapter.getCount() - 1);
		mListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				hideSoftInputView();
				layout_more.setVisibility(View.GONE);
				layout_add.setVisibility(View.GONE);
				btn_chat_voice.setVisibility(View.VISIBLE);
				btn_chat_keyboard.setVisibility(View.GONE);
				btn_chat_send.setVisibility(View.GONE);
				return false;
			}
		});

		// �ط���ť�ĵ���¼�
		mAdapter.setOnInViewClickListener(R.id.iv_fail_resend, new MessageChatAdapter.onInternalClickListener() {

			@Override
			public void OnClickListener(View parentV, View v, Integer position, Object values) {
				// �ط���Ϣ
				// showResendDialog(parentV, v, values);
			}
		});

	}

	/**
	 * ��ʼ����������
	 * 
	 * @Title: initVoiceView @Description: TODO @param @return void @throws
	 */
	private void initVoiceView() {
		layout_record = (RelativeLayout) findViewById(R.id.layout_record);
		tv_voice_tips = (TextView) findViewById(R.id.tv_voice_tips);
		iv_record = (ImageView) findViewById(R.id.iv_record);
		btn_speak.setOnTouchListener(new VoiceTouchListen());

		initVoiceAnimRes();
		// initRecordManager();

	}

	/**
	 * ����˵��
	 * 
	 * @ClassName: VoiceTouchListen
	 * @Description: TODO
	 * @author smile
	 * @date 2014-7-1 ����6:10:16
	 */
	class VoiceTouchListen implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!CommonUtils.checkSdCard()) {
					ShowToast("����������Ҫsdcard֧�֣�");
					return false;
				}
				try {
					v.setPressed(true);
					layout_record.setVisibility(View.VISIBLE);
					tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
					// ��ʼ¼��
					// recordManager.startRecording(targetId);
				} catch (Exception e) {
				}
				return true;
			case MotionEvent.ACTION_MOVE: {
				if (event.getY() < 0) {
					tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
					tv_voice_tips.setTextColor(Color.GRAY);
				} else {
					tv_voice_tips.setText(getString(R.string.voice_up_tips));
					tv_voice_tips.setTextColor(Color.WHITE);
				}
				return true;
			}
			case MotionEvent.ACTION_UP:
				v.setPressed(false);
				layout_record.setVisibility(View.INVISIBLE);
				try {
					if (event.getY() < 0) {// ����¼��
						/*
						 * recordManager.cancelRecording(); BmobLog.i("voice",
						 * "������������");
						 */
					} else {
						/*
						 * int recordTime = recordManager.stopRecording(); if
						 * (recordTime > 1) { // ���������ļ� BmobLog.i("voice",
						 * "��������"); sendVoiceMessage(
						 * recordManager.getRecordFilePath(targetId),
						 * recordTime); } else {// ¼��ʱ����̣�����ʾ¼�����̵���ʾ
						 * layout_record.setVisibility(View.GONE);
						 * showShortToast().show(); }
						 */
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				return true;
			default:
				return false;
			}
		}
	}

	/**
	 * ��ʼ������������Դ @Title: initVoiceAnimRes @Description: TODO @param @return
	 * void @throws
	 */
	private void initVoiceAnimRes() {
		drawable_Anims = new Drawable[] { getResources().getDrawable(R.drawable.chat_icon_voice2),
				getResources().getDrawable(R.drawable.chat_icon_voice3),
				getResources().getDrawable(R.drawable.chat_icon_voice4),
				getResources().getDrawable(R.drawable.chat_icon_voice5),
				getResources().getDrawable(R.drawable.chat_icon_voice6) };
	}

	private void initAddView() {
		tv_picture = (TextView) findViewById(R.id.tv_picture);
		tv_camera = (TextView) findViewById(R.id.tv_camera);
		tv_location = (TextView) findViewById(R.id.tv_location);
		tv_picture.setOnClickListener(this);
		tv_location.setOnClickListener(this);
		tv_camera.setOnClickListener(this);
	}

	List<FaceText> emos;

	/**
	 * ��ʼ�����鲼�� @Title: initEmoView @Description: TODO @param @return
	 * void @throws
	 */
	private void initEmoView() {
		pager_emo = (ViewPager) findViewById(R.id.pager_emo);
		emos = FaceTextUtils.faceTexts;

		List<View> views = new ArrayList<View>();
		for (int i = 0; i < 2; ++i) {
			views.add(getGridView(i));
		}
		pager_emo.setAdapter(new EmoViewPagerAdapter(views));
	}

	private View getGridView(final int i) {
		View view = View.inflate(this, R.layout.include_emo_gridview, null);
		GridView gridview = (GridView) view.findViewById(R.id.gridview);
		List<FaceText> list = new ArrayList<FaceText>();
		if (i == 0) {
			list.addAll(emos.subList(0, 21));
		} else if (i == 1) {
			list.addAll(emos.subList(21, emos.size()));
		}
		final EmoteAdapter gridAdapter = new EmoteAdapter(CustomerServiceActivity.this, list);
		gridview.setAdapter(gridAdapter);

		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				FaceText name = (FaceText) gridAdapter.getItem(position);
				String key = name.text.toString();
				try {
					if (edit_user_comment != null && !TextUtils.isEmpty(key)) {
						int start = edit_user_comment.getSelectionStart();
						CharSequence content = edit_user_comment.getText().insert(start, key);
						edit_user_comment.setText(content);
						// ��λ���λ��
						CharSequence info = edit_user_comment.getText();
						if (info instanceof Spannable) {
							Spannable spanText = (Spannable) info;
							Selection.setSelection(spanText, start + key.length());
						}
					}
				} catch (Exception e) {

				}

			}
		});
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cus_bnt: // ����˳�
			finish();
		case R.id.edit_user_comment:// ����ı������
			mListView.setSelection(mListView.getCount() - 1);
			if (layout_more.getVisibility() == View.VISIBLE) {
				layout_add.setVisibility(View.GONE);
				layout_emo.setVisibility(View.GONE);
				layout_more.setVisibility(View.GONE);
			}
			break;
		case R.id.btn_chat_emo:// ���Ц��ͼ��
			if (layout_more.getVisibility() == View.GONE) {
				showEditState(true);
			} else {
				if (layout_add.getVisibility() == View.VISIBLE) {
					layout_add.setVisibility(View.GONE);
					layout_emo.setVisibility(View.VISIBLE);
				} else {
					layout_more.setVisibility(View.GONE);
				}
			}

			break;
		case R.id.btn_chat_add:// ��Ӱ�ť-��ʾͼƬ������
			if (layout_more.getVisibility() == View.GONE) {
				layout_more.setVisibility(View.VISIBLE);
				layout_add.setVisibility(View.VISIBLE);
				layout_emo.setVisibility(View.GONE);
				hideSoftInputView();
			} else {
				if (layout_emo.getVisibility() == View.VISIBLE) {
					layout_emo.setVisibility(View.GONE);
					layout_add.setVisibility(View.VISIBLE);
				} else {
					layout_more.setVisibility(View.GONE);
				}
			}

			break;
		case R.id.btn_chat_voice:// ������ť
			edit_user_comment.setVisibility(View.GONE);
			layout_more.setVisibility(View.GONE);
			btn_chat_voice.setVisibility(View.GONE);
			btn_chat_keyboard.setVisibility(View.VISIBLE);
			btn_speak.setVisibility(View.VISIBLE);
			hideSoftInputView();
			break;
		case R.id.btn_chat_keyboard:// ���̰�ť������͵������̲����ص�������ť
			showEditState(false);
			break;
		case R.id.btn_chat_send:// �����ı�
			final String msg = edit_user_comment.getText().toString();
			if (msg.equals("")) {
				ShowToast("�����뷢����Ϣ!");
				return;
			}

			boolean isNetConnected = CommonUtils.isNetworkAvailable(this);

			if (!isNetConnected) {
				ShowToast(R.string.network_tips);
				// return;
			}

			Log.i("isConnectOK", "+++++++++isConnectOK+++++"+isConnectOK);
			 
			if( ! isConnectOK){
				// ��ʵ���Ӧ������ �������Ӳ��ǣ���ά����
				ShowToast(R.string.network_tips);
				return ;
			}
			// ����Ϣ���浽���ݿⲢ����
			refreshMessage(handleChatManager.sendSendTextMessage(msg,isConnectOK));
			edit_user_comment.setText("");
	 

			break;
		case R.id.tv_camera:// ����
			// selectImageFromCamera();
			break;
		case R.id.tv_picture:// ͼƬ
			selectImageFromLocal();
			break;

		}
	}

	/**
	 * �����Ƿ���Ц������ʾ�ı�������״̬ @Title: showEditState @Description:
	 * TODO @param @param isEmo: �����������ֺͱ��� @return void @throws
	 */
	private void showEditState(boolean isEmo) {
		edit_user_comment.setVisibility(View.VISIBLE);
		btn_chat_keyboard.setVisibility(View.GONE);
		btn_chat_voice.setVisibility(View.VISIBLE);
		btn_speak.setVisibility(View.GONE);
		edit_user_comment.requestFocus();
		if (isEmo) {
			layout_more.setVisibility(View.VISIBLE);
			layout_more.setVisibility(View.VISIBLE);
			layout_emo.setVisibility(View.VISIBLE);
			layout_add.setVisibility(View.GONE);
			hideSoftInputView();
		} else {
			layout_more.setVisibility(View.GONE);
			showSoftInputView();
		}
	}

	// ��ʾ�����
	public void showSoftInputView() {
		if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(edit_user_comment, 0);
		}
	}

	/**
	 * ѡ��ͼƬ @Title: selectImage @Description: TODO @param @return void @throws
	 */
	public void selectImageFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
		} else {
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, MlConstants.REQUESTCODE_TAKE_LOCAL);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case MlConstants.REQUESTCODE_TAKE_CAMERA:// ��ȡ��ֵ��ʱ����ϴ�path·���µ�ͼƬ��������
				/*
				 * ShowLog("����ͼƬ�ĵ�ַ��" + localCameraPath);
				 * sendImageMessage(localCameraPath);
				 */
				break;
			case MlConstants.REQUESTCODE_TAKE_LOCAL:
				if (data != null) {
					Uri selectedImage = data.getData();
					if (selectedImage != null) {
						Cursor cursor = getContentResolver().query(selectedImage, null, null, null, null);
						cursor.moveToFirst();
						int columnIndex = cursor.getColumnIndex("_data");
						String localSelectPath = cursor.getString(columnIndex);
						cursor.close();
						if (localSelectPath == null || localSelectPath.equals("null")) {
							ShowToast("�Ҳ�������Ҫ��ͼƬ");
							return;
						}
						// sendImageMessage(localSelectPath);
					}
				}
				break;
			}
		}
	}

 

	private static List<ChatMessage> msgList;

	/**
	 * ����ˢ�� @Title: initOrRefresh @Description: TODO @param @return void @throws
	 */
	private void initOrRefresh() {
		if (mAdapter != null) {
		 
			if (initMsgData().size() != 0) {
				// mAdapter.add(initMsgData().get(0));
				msgList.add(initMsgData().get(0));
				mListView.setSelection(mAdapter.getCount() - 1);
				mAdapter.notifyDataSetChanged();
			} else {
				mAdapter.notifyDataSetChanged();
			}

		} else {
			if (initMsgData() != null) {
				mAdapter = new MessageChatAdapter(this, initMsgData());
				mListView.setAdapter(mAdapter);
			}

		}
	}

	/**
	 * ������Ϣ��ʷ�������ݿ��ж���
	 */
	private List<ChatMessage> initMsgData() {
		msgList = handleChatManager.getTextMessage(MsgPagerNum);
		return msgList;
	}

	/**
	 * ˢ�½��� @Title: refreshMessage @Description: TODO @param @param
	 * message @return void @throws
	 */
	private void refreshMessage(ChatMessage msg) {
		// ���½���
		Log.i("MessageListener", "-r-r-r-r-" + msg.getContent());

		mAdapter.add(msg);

		mListView.setSelection(msgList.size() - 1);
		// mAdapter.notifyDataSetChanged();

	}
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
 
		}
	};
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				MsgPagerNum++;
				int total = handleChatManager.getChatTotalCount();

				int currents = mAdapter.getCount();
				if (total <= currents) {
					ShowToast("�����¼��������Ŷ!");
				} else {
					List<ChatMessage> msgList = initMsgData();
					// msgList.addAll(msgList);
					// mAdapter.notifyDataSetChanged();
					mAdapter.setList(msgList);
					mListView.setSelection(mAdapter.getCount() - currents - 1);
				}
				mListView.stopRefresh();
			}
		}, 1000);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}
 
	/**
	 * ����������״̬������
	 */
	private ClientConServer clintconnserver;
	Runnable connectToServeListerRunable = new Runnable() {

		@Override
		public void run() {
			clintconnserver = new ClientConServer(cononcetToServerHandler);
			clintconnserver.listeningConnectToServer(); 
		}

	};
	/**
	 * ����������״̬Handler
	 */
	Handler cononcetToServerHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			boolean isConnected = (Boolean) msg.obj;
			isConnectOK = isConnected;
		}

	};

	@Override
	public void getOnlineMessage(String msg) {

		if (msg != null && msg != "") {
 
			// �����յ�����Ϣ���浽���ݿⲢ��ʾ
			Log.i("MessageListener", "-----------------------" + msg);
			// refreshMessage(handleChatManager.saveReceiverTextMessage(msg));

			android.os.Message handlerMsg = android.os.Message.obtain();
			handlerMsg.obj = msg;
			OnlineMessage.sendMessage(handlerMsg);
			 
	 
			ShowToast("handleMessage++++++++");
		}
	}
 
	Handler OnlineMessage = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
 
			String onlineMsg = (String) msg.obj;
			
			mAdapter.add(handleChatManager.saveReceiverTextMessage(onlineMsg));
 	 
			mListView.setSelection(msgList.size() - 1);
			
		}

	};

}
