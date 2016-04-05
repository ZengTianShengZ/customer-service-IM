package com.mlphoto.customerservice.service;

import java.util.Map;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class UserOperateService {

	//���ﲻ��Ҫ��עconnection�Ƿ��ʼ��������ע��ʱ�����ťʱ��ʼ��
	private XMPPConnection conncetion = (XMPPConnection) ClientConServer.connection;
	
	private AccountManager accountmanger = conncetion.getAccountManager();
	
	private Roster roster = conncetion.getRoster();
	
	/**
	 * ע�����û�
	 * @param _username �û���
	 * @param _password ����
	 * @param attributes ����ֵ�����������
	 * @return ע���Ƿ�ɹ�
	 */
	public boolean regAccount(String _username, String _password, Map<String,String> attributes){
		boolean regmsg = false;//ע����Ϣ������Ϣ��������ʾ���û�����ʾ
		
		//�����е��ɻ�����ʹ��AccountManger�е�createAccount������ʹ��Registration��������ʲô
		try {
			accountmanger.createAccount(_username, _password, attributes);
			regmsg = true;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		return regmsg;
	}
	
}
