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
 * ��������࣬����������Ϣ
 * 
 * @ClassName: XmppTool
 * @Description: TODO
 * @author ZSS
 * @date 2016-3-28 PM
 */
public class ChatService {
	XMPPConnection connection = (XMPPConnection) ClientConServer.connection;
	private Chat chat;

	// ��Ϣ������
	private NewMessageListener messagelistener = new NewMessageListener();
 
 
	// chatmanger���ڴ���ǰ������
	private ChatManager chatmanger = connection.getChatManager();
	
	private MessageInterface messageInterface;
	
	private TotalMessageInterface totalMessageInterface;

	/**
	 * ���췽��
	 * 
	 * @param _handler
	 * @param _userJID  �û���JID���������������߳�
	 */
 	
	// ����private static��ʵ��
	private volatile static ChatService INSTANCE;
	// ͬ����
	private static Object INSTANCE_LOCK = new Object();

	/**
	 * ʹ�õ���ģʽ����--˫������
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
	 * ������Ϣ������Ϣ���������������ݣ�
	 * @param _userJID ��Ϣ�����˵��˺�
	 * @param _message ���͵���Ϣ����
	 */
	public void sendMessage(String _userJID, String _message) {

		try {
		 
			chat.sendMessage(_message);

		} catch (XMPPException e) {
			e.printStackTrace();
	 
		}
	}

	/**
	 * ������Ϣ(�ڲ���)   
	 * 
	 * @author michael
	 *
	 */
	class NewMessageListener implements MessageListener {
 
		// ��Ϣbean

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
	 * ȫ����Ϣ������
	 * 
	 * @author michael
	 *
	 */
	public void setTotalMessageInterface(TotalMessageInterface totalMessageInterface){
		this.totalMessageInterface = totalMessageInterface;
	}
  
	
}
