package com.mlphoto.customerservice.db;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.xutils.DbManager;
import org.xutils.x;
import org.xutils.DbManager.DaoConfig;
import org.xutils.DbManager.DbUpgradeListener;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;

import com.mlphoto.customerservice.bean.ChatMessage;

import android.os.Environment;
import android.util.Log;

/**
 * �����¼���ݿ�
 * 
 * @ClassName: DbManagerUtil
 * @author ZSS
 * @date 2016-3-24 PM
 */
public class DbManagerUtil {

	private static DbManager dbManager;

	private DbManagerUtil() {

	}

	private static final DbManagerUtil single = new DbManagerUtil();

	public static DbManagerUtil getInstance() {

		return single;
	}

	public static void create(String userAccount) {

		DbManager.DaoConfig daoConfig = new DaoConfig()
				// ���ݿ������
				.setDbName(userAccount)
				// ���浽ָ��·��
				.setDbDir(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/mlphotoDB"))
				// ���ݿ�İ汾��
				.setDbVersion(1)
				// ���ݿ�汾���¼���
				.setDbUpgradeListener(new DbUpgradeListener() {
					@Override
					public void onUpgrade(DbManager arg0, int arg1, int arg2) {
						LogUtil.e("���ݿ�汾�����ˣ�");
						Log.i("dbManager", "++++++++���ݿ�汾������");
					}
				});
		dbManager = x.getDb(daoConfig);
 
	}

	public List<ChatMessage> queryMessages(int MsgPagerNum) {

		if (dbManager != null) {
			try {

				int pagerNum = (MsgPagerNum + 1) * 10;
				List<ChatMessage> list = new LinkedList<ChatMessage>();
				// ��  id �������� 
				list = dbManager.selector(ChatMessage.class).orderBy("id", true).limit(pagerNum).findAll();
				Collections.reverse(list);// ǰ��תһ����Ϣ��¼
				return list;
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}/*else{
			  throw new IllegalArgumentException("should getInstance().create to get dbManager,but you not !!");
		}*/
		return null;
	}
	
	public int queryChatTotalCount() {
		// TODO Auto-generated method stub
		if (dbManager != null) {
		  
			try {
				return (int) dbManager.selector(ChatMessage.class).count();
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}

	public void setMessage2Db(ChatMessage chatMessage){
		try {
			Log.i("MessageListener", "-+-+-+-+-+-+-+"+chatMessage.getContent());
			dbManager.saveOrUpdate(chatMessage);
  
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("MessageListener", "-e-e-e-e-e-e-e-e--e-e-e-e-"+e.toString());
		} 
	}
}
