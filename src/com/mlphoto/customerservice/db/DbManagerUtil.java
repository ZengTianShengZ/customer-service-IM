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
 * 聊天记录数据库
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
				// 数据库的名字
				.setDbName(userAccount)
				// 保存到指定路径
				.setDbDir(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/mlphotoDB"))
				// 数据库的版本号
				.setDbVersion(1)
				// 数据库版本更新监听
				.setDbUpgradeListener(new DbUpgradeListener() {
					@Override
					public void onUpgrade(DbManager arg0, int arg1, int arg2) {
						LogUtil.e("数据库版本更新了！");
						Log.i("dbManager", "++++++++数据库版本更新了");
					}
				});
		dbManager = x.getDb(daoConfig);
 
	}

	public List<ChatMessage> queryMessages(int MsgPagerNum) {

		if (dbManager != null) {
			try {

				int pagerNum = (MsgPagerNum + 1) * 10;
				List<ChatMessage> list = new LinkedList<ChatMessage>();
				// 以  id 升序排列 
				list = dbManager.selector(ChatMessage.class).orderBy("id", true).limit(pagerNum).findAll();
				Collections.reverse(list);// 前后反转一下消息记录
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
