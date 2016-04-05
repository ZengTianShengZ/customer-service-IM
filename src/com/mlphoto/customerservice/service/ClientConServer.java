package com.mlphoto.customerservice.service;

import java.io.File;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

/**
 * XMPP 通信协议与服务器建立连接
 * 
 * @ClassName: XmppTool
 * @Description: TODO
 * @author ZSS
 * @date 2016-3-28 AM
 */
public class ClientConServer {
 
	private Context context;

	boolean isOnline = false;// 标志，用来标示用户是否在线，初始为不在线

	private Handler isConnectServerhandler;// Handler，主要用于软件联网情况，

	public static Connection connection; // 声明XMPPconnection对象，将在login方法中进行初始化

	public static Connection fileconnection;//文件传输连接
	/**
	 * 构造方法
	 * 
	 * @param _context
	 */
	public ClientConServer(Context _context) {
		this.context = _context;
	}

	/**
	 * 构造函数，用于初始化处理与UI主线程之间的交互Handler
	 * 
	 * @param _handler
	 */
	public ClientConServer(Handler _handler) {
		this.isConnectServerhandler = _handler;
	}

	public ClientConServer() {
	}
	
	
	/**
	 * 构造方法，用于初始化XmppTool,此方法主要用在注册时的初始化
	 * x
	 * @param _serverIp
	 * @param _serverPort
	 * @throws XMPPException
	 */
	public ClientConServer(String _serverIp, int _serverPort)
			throws XMPPException {
		connection = (XMPPConnection) getConnection(_serverIp, _serverPort);
		connection.connect();
		 
	}
	
	
 
	/**
	 * 获取连接
	 * 
	 * @param _serverIp
	 *            服务器IP地址
	 * @param _serverPort
	 *            服务器端口
	 * @return
	 */
	public Connection getConnection(String _serverIp, int _serverPort) {
	 
		//初始化配置
		this.configure(ProviderManager.getInstance());
		
		ConnectionConfiguration config = new ConnectionConfiguration(_serverIp,
				_serverPort);

		/* 是否启用调试模式 */
		// config.setDebuggerEnabled(true);
		// config.setReconnectionAllowed(true);
		config.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
		/* 是否启用安全验证 */
		config.setSASLAuthenticationEnabled(false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			config.setTruststoreType("AndroidCAStore");
			config.setTruststorePassword(null);
			config.setTruststorePath(null);
		} else {

			String path = System.getProperty("javax.net.ssl.trustStore");
			if (path == null)
				path = System.getProperty("java.home") + File.separator + "etc"
						+ File.separator + "security" + File.separator
						+ "cacerts.bks";
			config.setTruststorePath(path);
			config.setTruststoreType("BKS");
		}

		/* 创建Connection链接 */
		Connection connection = new XMPPConnection(config);
		return connection;
	}
	
	

	/**
	 * 登陆
	 * 
	 * @param _username
	 *            用户名
	 * @param _password
	 *            密码
	 * @param _serverIp
	 *            服务器IP地址
	 * @param _serverPort
	 *            服务器端口号
	 * @return
	 */
	public boolean login(String _username, String _password, String _serverIp,
			int _serverPort) {

		// 初始化connection对象
		connection = getConnection(_serverIp, _serverPort);

		try {
			connection.connect();
			connection.login(_username, _password);
			
			return true;
		} catch (Throwable  e) {
			e.printStackTrace();
			return false;

		}

	}
	
	public void listeningConnectToServer() {
 
		try {
			connection.addConnectionListener(new ConnectionListener() { 

				@Override
				public void reconnectionSuccessful() {
					// TODO Auto-generated method stub
					android.os.Message msg = android.os.Message.obtain();
					msg.obj = true;
					isConnectServerhandler.sendMessage(msg);
				}

				@Override
				public void reconnectionFailed(Exception arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void reconnectingIn(int arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void connectionClosedOnError(Exception e) {
					// TODO Auto-generated method stub
					android.os.Message msg = android.os.Message.obtain();
					msg.obj = false;
					isConnectServerhandler.sendMessage(msg);

				}

				@Override
				public void connectionClosed() {
					// TODO Auto-generated method stub

				}
			});
		} catch (Throwable e) {
			e.printStackTrace();
		}
		 
	}
	
 
	
	/**
	 * 原因：the smack.providers file, usually in /META-INF folder in normal versions of smack, can't be loaded in Android because its jar packaging. So all the providers must be initialized by hand
	 * <br/>
	 * 参考网址：http://stackoverflow.com/questions/5910219/problem-using-usersearch-in-xmpp-with-asmack
	 * @param pm
	 */
	public void configure(ProviderManager pm) {

		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());

		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time",
					Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
			Log.w("TestClient",
					"Can't load class for org.jivesoftware.smackx.packet.Time");
		}

		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());

		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());

		// Chat State
		pm.addExtensionProvider("active",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("composing",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());

		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());

		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());

		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());

		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());

		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());

		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());

		// Delayed Delivery
		pm.addExtensionProvider("x", "jabber:x:delay",
				new DelayInformationProvider());

		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version",
					Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
		}

		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());

		// Offline Message Indicator
		pm.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());

		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());

		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());

		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());

		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());

		// FileTransfer
		pm.addIQProvider("si", "http://jabber.org/protocol/si",
				new StreamInitiationProvider());

		pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
				new BytestreamsProvider());

		// Privacy
		pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
		pm.addIQProvider("command", "http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider());
		pm.addExtensionProvider("malformed-action",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.MalformedActionError());
		pm.addExtensionProvider("bad-locale",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadLocaleError());
		pm.addExtensionProvider("bad-payload",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadPayloadError());
		pm.addExtensionProvider("bad-sessionid",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadSessionIDError());
		pm.addExtensionProvider("session-expired",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.SessionExpiredError());
	}
 
	
}
