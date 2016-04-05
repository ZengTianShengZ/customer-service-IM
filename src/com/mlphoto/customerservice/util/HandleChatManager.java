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
 * 聊天管理-用于管理聊天：包括发送聊天消息、保存消息
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

	// 创建private static类实例
	private volatile static HandleChatManager INSTANCE;
	// 同步锁
	private static Object INSTANCE_LOCK = new Object();

	/**
	 * 使用单例模式创建--双重锁定
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
	 * 初始化 @Title: init @Description: TODO @param context @return void @throws
	 */
	public void init(Context context,Handler _handler) {
		this.context = context;

		formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss");
	     
		DbManagerUtil.getInstance().create("userAccount");
		 
	    Log.i("chatservice", "+++++++chatservice");
	}

	/**
	 * 封装发送消息，顺便将消息存进 数据库
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
	 * 封装接收消息，顺便将消息存进 数据库
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
	 * 从数据库取出消息
	 * @param MsgPagerNum
	 * @return
	 */
	public List<ChatMessage> getTextMessage(int MsgPagerNum) {
 
		return DbManagerUtil.getInstance().queryMessages(MsgPagerNum);
 
	}
	/**
	 * 得到数据库总数
	 * @return
	 */
	public int getChatTotalCount(){
		return DbManagerUtil.getInstance().queryChatTotalCount();
	}

	private String getData() {
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间

		return formatter.format(curDate);
	}

	
	/* 聊天线程 */
	Runnable chatRunnable = new Runnable() {

		@Override
		public void run() {
			 
			ChatService.getInstance(Config.chatToService).sendMessage(Config.chatToService, messageContent);

		}

	};
	
}
