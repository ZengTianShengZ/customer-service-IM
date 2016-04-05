package com.mlphoto.customerservice.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.xutils.DbManager;
import com.mlphoto.customerservice.bean.ChatMessage;
import com.mlphoto.customerservice.db.DbManagerUtil;
import com.mlphoto.customerservice.service.ChatService;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * �������-���ڹ������죺��������������Ϣ��������Ϣ
 * 
 * @ClassName: HandleChatManager
 * @Description: TODO
 * @author ZSS
 * @date 2016-3-25 AM
 */
public class HandleChatManager {

	Context context;
	private SimpleDateFormat formatter;
 
	private Thread mChatThred;
	
	private String messageContent;
 
	
	private final int STATUS_SEND_SUCCESS = 1;
	private final int STATUS_SEND_FAIL = 2;

	// ����private static��ʵ��
	private volatile static HandleChatManager INSTANCE;
	// ͬ����
	private static Object INSTANCE_LOCK = new Object();

	/**
	 * ʹ�õ���ģʽ����--˫������
	 */
	public static HandleChatManager getInstance(Context context,Handler _handler) {
		if (INSTANCE == null)
			synchronized (INSTANCE_LOCK) {
				if (INSTANCE == null) {
					INSTANCE = new HandleChatManager();
				}
				INSTANCE.init(context,_handler);
			}
		return INSTANCE;
	}

	/**
	 * ��ʼ�� @Title: init @Description: TODO @param context @return void @throws
	 */
	public void init(Context context,Handler _handler) {
		this.context = context;

		formatter = new SimpleDateFormat("yyyy��MM��dd��    HH:mm:ss");
	     
		DbManagerUtil.getInstance().create("userAccount");
		 
	    Log.i("chatservice", "+++++++chatservice");
	}

	/**
	 * ��װ������Ϣ��˳�㽫��Ϣ��� ���ݿ�
	 * @param content
	 * @return
	 */
	public ChatMessage sendSendTextMessage(String sengContent,boolean isConnectOK) {
		this.messageContent = sengContent;
    	ChatMessage chatMessage = new ChatMessage("userAccount", sengContent, getData(), Config.TYPE_SEND_TXT);
		
    	DbManagerUtil.getInstance().setMessage2Db(chatMessage);
 
    	if(isConnectOK){
        	mChatThred = new Thread(chatRunnable);
    		mChatThred.start();
    	}
    	return chatMessage;
	}
	
	/**
	 * ��װ������Ϣ��˳�㽫��Ϣ��� ���ݿ�
	 * @param content
	 * @return
	 */
	public ChatMessage saveReceiverTextMessage(String receiverContent) {
 
    	ChatMessage chatMessage = new ChatMessage("customerService", 
    												receiverContent,
    												getData(),
    												Config.TYPE_RECEIVER_TXT);
		
    	DbManagerUtil.getInstance().setMessage2Db(chatMessage);
 
    	return chatMessage;
	}
	

	/**
	 * �����ݿ�ȡ����Ϣ
	 * @param MsgPagerNum
	 * @return
	 */
	public List<ChatMessage> getTextMessage(int MsgPagerNum) {
 
		return DbManagerUtil.getInstance().queryMessages(MsgPagerNum);
 
	}
	/**
	 * �õ����ݿ�����
	 * @return
	 */
	public int getChatTotalCount(){
		return DbManagerUtil.getInstance().queryChatTotalCount();
	}

	private String getData() {
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��

		return formatter.format(curDate);
	}

	
	/* �����߳� */
	Runnable chatRunnable = new Runnable() {

		@Override
		public void run() {
			 
			ChatService.getInstance(Config.chatToService).sendMessage(Config.chatToService, messageContent);

		}

	};
	
}
