package com.mlphoto.customerservice.service;

import java.util.Map;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class UserOperateService {

	//这里不需要关注connection是否初始化，交由注册时点击按钮时初始化
	private XMPPConnection conncetion = (XMPPConnection) ClientConServer.connection;
	
	private AccountManager accountmanger = conncetion.getAccountManager();
	
	private Roster roster = conncetion.getRoster();
	
	/**
	 * 注册新用户
	 * @param _username 用户名
	 * @param _password 密码
	 * @param attributes 附加值，比如邮箱等
	 * @return 注册是否成功
	 */
	public boolean regAccount(String _username, String _password, Map<String,String> attributes){
		boolean regmsg = false;//注册消息返回信息，用于显示给用户的提示
		
		//这里有点疑惑，这里使用AccountManger中的createAccount方法和使用Registration的区别是什么
		try {
			accountmanger.createAccount(_username, _password, attributes);
			regmsg = true;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		return regmsg;
	}
	
}
