package om.mlphoto.customerservices.interfaces;

/** ��Ϣ��ļ����ص�
 * @ClassName: XListView
 * @Description: TODO
 * @author ZSS
 * @date 2016-3-23 PM
 */

public abstract interface OnReceiveListener {

	//public abstract void onSuccess(BmobMsg msg);

	public abstract void onFailure(int code, String arg1);

}
     