package com.mlphoto.customerservices.ui;

 

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.XMPPException;
import org.xutils.x;

import com.mlphoto.customerservice.service.ClientConServer;
import com.mlphoto.customerservice.service.UserOperateService;
import com.mlphoto.customerservice.util.CommonUtils;
import com.mlphoto.customerservice.util.Config;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
 

/**
 * @author Javen
 * 2016-1-8
 */
public class MyApplicaction extends Application {
 
 
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
   
         
        
	}
	// 共享变量  
	private Handler handler = null;  
	  
	// set方法  
	public void setHandler(Handler handler) {  
	this.handler = handler;  
	}  
	  
	// get方法  
	public Handler getHandler() {  
	return handler;  
	}  
 
}
