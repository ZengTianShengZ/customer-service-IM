package om.mlphoto.customerservices.interfaces;


/** ¼���仯����
 * @ClassName: OnRecordChangeListener
 * @Description: TODO
 * @author ZSS
 * @date 2016-3-24 AM
 */
public interface OnRecordChangeListener {
	
	/** �����ı仯
	  * @Title: onChange
	  * @Description: TODO
	  * @param  value����ǰ����ֵ 
	  * @return 
	  * @throws
	  */
	public abstract void onVolumnChanged(int value);
		
	/** ¼��ʱ�����
	  * @Title: onTimeChanged
	  * @Description: TODO
	  * @param  value ��ǰ¼��ʱ��
	  * @param  localPath ¼���ļ���ַ--���ڵ�¼��1���ӵ�ʱ��Ĭ�Ͼͷ�����Ϣ
	  * @return 
	  * @throws
	  */
	public abstract void onTimeChanged(int value,String localPath);
}	
