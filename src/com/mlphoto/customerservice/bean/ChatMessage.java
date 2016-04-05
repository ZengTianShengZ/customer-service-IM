package com.mlphoto.customerservice.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * ������Ϣ��
 * @ClassName: ChatMessage
 * @author ZSS
 * @date 2016-3-24 PM
 */
@Table(name = "msg")
public class ChatMessage {

	/**
	 * key
	 */
	@Column(name = "id", isId = true)
	private int id;
	
	/**
	 * ��Ϣ�������ݣ��������ʹ�ţ�TEXT-�ַ����ı���IMAGE-ͼƬ��ַ 
	 */
	@Column(name = "content")
	private String content; 
	
	/**
	 * �������ǳ� �� �ͷ�
	 */
	@Column(name = "customerServiceName")
	private String customerServiceName;
	
	/**
	 * �û��˺�
	 */
	@Column(name = "accountNumber")
	private String accountNumber;// 
	
	/**
	 * �û�����
	 */
	@Column(name = "userName")
	private String userName;// 
	/**
	 * ����ʱ��
	 */
	@Column(name = "msgTime")
	private String msgTime;// 
	/**
	 * ��ʾ����Ϣδ�����Ѷ�״̬
	 */
	
	@Column(name = "isReaded")
	private Integer isReaded;// 
	/**
	 * ��Ϣ���ͣ��Ƿ��ͻ��ǽ���
	 */
	@Column(name = "msgType")
	private int msgType;
 
	/**
	 * ����Ϣ״̬�����ͳɹ���ʧ�ܡ����յ�
	 */
	@Column(name = "status")
	private int status; // 
	/**
	 * �����ֶ�
	 */
	@Column(name = "extra")
	private String extra;//
	
	
	public  ChatMessage(String accountNumber,String content,String msgTime,int msgType){
		this.accountNumber = accountNumber;
		this.content = content;
		this.msgTime = msgTime;
		this.msgType = msgType;
	}
	public  ChatMessage(String accountNumber,String content,String msgTime,int msgType,int status){
		this.accountNumber = accountNumber;
		this.content = content;
		this.msgTime = msgTime;
		this.msgType = msgType;
		this.status = status;
	}
	/**
	 *  �������ݿ�� ��Ҫ��һ���޲ε� ���췽��������
	 */
	public ChatMessage(){}
	 
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCustomerServiceName() {
		return customerServiceName;
	}
	public void setCustomerServiceName(String customerServiceName) {
		this.customerServiceName = customerServiceName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMsgTime() {
		return msgTime;
	}
	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}
	public Integer getIsReaded() {
		return isReaded;
	}
	public void setIsReaded(Integer isReaded) {
		this.isReaded = isReaded;
	}

	public int getMsgType() {
		return msgType;
	}
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}

 
	
	
}
