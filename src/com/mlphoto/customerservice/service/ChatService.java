package com.mlphoto.customerservice.service;


import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import om.mlphoto.customerservices.interfaces.MessageInterface;
import om.mlphoto.customerservices.interfaces.TotalMessageInterface;

/**
 * 聊天服务类，处理聊天信息
 * 
 * @ClassName: XmppTool
 * @Description: TODO
 * @author ZSS
 * @date 2016-3-28 PM
 */
public class ChatService {
	XMPPConnection connection = (XMPPConnection) ClientConServer.connection;
	private Chat chat;

	// 消息监听器
	private NewMessageListener messagelistener = new NewMessageListener();
 
 
	// chatmanger用于处理当前的聊天
	private ChatManager chatmanger = connection.getChatManager();
	
	private MessageInterface messageInterface;
	
	private TotalMessageInterface totalMessageInterface;

	/**
	 * 构造方法
	 * 
	 * @param _handler
	 * @param _userJID  用户的JID，这里用作聊天线程
	 */
 	
	// 创建private static类实例
	private volatile static ChatService INSTANCE;
	// 同步锁
	private static Object INSTANCE_LOCK = new Object();

	/**
	 * 使用单例模式创建--双重锁定
	 */
	public static ChatService getInstance(String _userJID) {
		if (INSTANCE == null)
			synchronized (INSTANCE_LOCK) {
				if (INSTANCE == null) {
					INSTANCE = new ChatService();
				}
				INSTANCE.init(_userJID);
			}
		return INSTANCE;
	}
	
	private void init(String _userJID) {
		if (chat == null) {
			chat = chatmanger.createChat(_userJID, messagelistener);
		} else {
			chat.addMessageListener(messagelistener);
		}
	}
 

	/**
	 * 发送消息（简单消息，不包括附加内容）
	 * @param _userJID 消息接收人的账号
	 * @param _message 发送的消息内容
	 */
	public void sendMessage(String _userJID, String _message) {

		try {
		 
			chat.sendMessage(_message);

		} catch (XMPPException e) {
			e.printStackTrace();
	 
		}
	}

	/**
	 * 监听消息(内部类)   
	 * 
	 * @author michael
	 *
	 */
	class NewMessageListener implements MessageListener {
 
		// 消息bean

		@Override
		public void processMessage(Chat chat, Message message) { 
			
			if(messageInterface != null){
				messageInterface.getOnlineMessage(message.getBody());
			}
			if(totalMessageInterface != null){
				totalMessageInterface.getTotalOnlineMessage(message.getBody());
			}
		}
	}
 
	public void setMessageInterface(MessageInterface messageInterface){
		this.messageInterface = messageInterface;
	}
	
	/**
	 * 全局消息监听器
	 * 
	 * @author michael
	 *
	 */
	public void setTotalMessageInterface(TotalMessageInterface totalMessageInterface){
		this.totalMessageInterface = totalMessageInterface;
	}
  
	
}
