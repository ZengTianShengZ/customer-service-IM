package om.mlphoto.customerservices.interfaces;

/** 消息表的监听回调
 * @ClassName: XListView
 * @Description: TODO
 * @author ZSS
 * @date 2016-3-23 PM
 */

public abstract interface OnReceiveListener {

	//public abstract void onSuccess(BmobMsg msg);

	public abstract void onFailure(int code, String arg1);

}
     