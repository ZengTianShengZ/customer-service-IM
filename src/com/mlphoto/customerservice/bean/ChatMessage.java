package com.mlphoto.customerservice.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 聊天消息表
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
	 * 消息具体内容：根据类型存放：TEXT-字符串文本，IMAGE-图片地址 
	 */
	@Column(name = "content")
	private String content; 
	
	/**
	 * 接受者昵称 即 客服
	 */
	@Column(name = "customerServiceName")
	private String customerServiceName;
	
	/**
	 * 用户账号
	 */
	@Column(name = "accountNumber")
	private String accountNumber;// 
	
	/**
	 * 用户名字
	 */
	@Column(name = "userName")
	private String userName;// 
	/**
	 * 发送时间
	 */
	@Column(name = "msgTime")
	private String msgTime;// 
	/**
	 * 标示该消息未读、已读状态
	 */
	
	@Column(name = "isReaded")
	private Integer isReaded;// 
	/**
	 * 消息类型，是发送还是接收
	 */
	@Column(name = "msgType")
	private int msgType;
 
	/**
	 * 该消息状态：发送成功、失败、已收到
	 */
	@Column(name = "status")
	private int status; // 
	/**
	 * 额外字段
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
	 *  当做数据库表 需要有一个无参的 构造方法！！！
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
