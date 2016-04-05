package com.mlphoto.customerservices.ui;
 
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * 基类
 * @ClassName: BaseActivity
 * @author ZSS
 * @date 2016-3-23 PM
 */
public abstract class BaseActivity extends Activity {
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏  
		setContentView(initsetView());
 
		
		initView(getApplication(),savedInstanceState);
		initData();
		initEvent();
	}
	
	
	Toast mToast;

	public void ShowToast(final String text) {
		if (!TextUtils.isEmpty(text)) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (mToast == null) {
						mToast = Toast.makeText(getApplicationContext(), text,
								Toast.LENGTH_LONG);
					} else {
						mToast.setText(text);
					}
					mToast.show();
				}
			});
			
		}
	}
	
	public void ShowToast(final int resId) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mToast == null) {
					mToast = Toast.makeText(BaseActivity.this.getApplicationContext(), resId,
							Toast.LENGTH_LONG);
				} else {
					mToast.setText(resId);
				}
				mToast.show();
			}
		});
	}
	
	/** 打Log
	  * ShowLog
	  * @return void
	  * @throws
	  */
	public void ShowLog(String msg){
		Log.i("life",msg);
	}
	
	/** 隐藏软键盘
	  * hideSoftInputView
	  * @Title: hideSoftInputView
	  * @Description: TODO
	  * @param  
	  * @return void
	  * @throws
	  */
	public void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	
	public abstract int initsetView();
	public abstract void initView(Context context,Bundle savedInstanceState);
    public abstract void initData();
    public abstract void initEvent();
}
