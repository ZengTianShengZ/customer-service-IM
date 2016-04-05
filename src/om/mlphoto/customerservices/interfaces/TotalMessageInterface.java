package om.mlphoto.customerservices.interfaces;


/**
 * 来自服务器的总消息监听接口 ，当用户不在聊天界面的情况，监听后台的消息
 * 
 * @ClassName: XmppTool
 * @Description: TODO
 * @author ZSS
 * @date 2016-3-28 AM
 */

public interface TotalMessageInterface {

	public void getTotalOnlineMessage(String msg);
	
}
