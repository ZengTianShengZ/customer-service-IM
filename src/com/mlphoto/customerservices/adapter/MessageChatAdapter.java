package com.mlphoto.customerservices.adapter;

import java.util.List;

import com.mlphoto.customerservice.bean.ChatMessage;
import com.mlphoto.customerservice.util.Config;
import com.mlphoto.customerservice.util.FaceTextUtils;
import com.mlphoto.customerservices.ui.R;

import android.content.Context;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 聊天适配器
 * @ClassName: MessageChatAdapter
 * @Description: TODO
 * @author ZSS
 * @date 2016-3-24 AM
 */
public class MessageChatAdapter extends BaseListAdapter<ChatMessage>{

	 
	/**
	 * 消息发送状态：成功
	 */
	private final int STATUS_SEND_SUCCESS=1;
	/**
	 * 消息发送状态：失败
	 */
	private final int STATUS_SEND_FAIL=2;
	
	public MessageChatAdapter(Context context, List<ChatMessage> list) {
		super(context, list);
		 
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {

		final ChatMessage item = list.get(position);
		if (convertView == null) {
			convertView = createViewByType(position);
		}
		ImageView iv_avatar = ViewHolder.get(convertView, R.id.iv_avatar);
		final ImageView iv_fail_resend = ViewHolder.get(convertView, R.id.iv_fail_resend);//失败重发
		final TextView tv_send_status = ViewHolder.get(convertView, R.id.tv_send_status);//发送状态
		TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
		TextView tv_message = ViewHolder.get(convertView, R.id.tv_message);
		
		// 加载用户 或是客服头像 才是
		if(item.getMsgType() == Config.TYPE_RECEIVER_TXT){ 
			iv_avatar.setImageResource(R.drawable.touxiang);
		}else{
			iv_avatar.setImageResource(R.drawable.head);
		}
		 
		
		tv_time.setText(item.getMsgTime());
		System.out.println(list.get(position).getContent());
		SpannableString spannableString = FaceTextUtils
				.toSpannableString(mContext, item.getContent());
		tv_message.setText(spannableString);
		
		//状态描述
	/*	if(item.getMsgType() == TYPE_SEND_TXT ){
			
	 		if(item.getStatus()== STATUS_SEND_SUCCESS){//发送成功
		 		iv_fail_resend.setVisibility(View.GONE);
				tv_send_status.setVisibility(View.VISIBLE);
				tv_send_status.setText("已发送");
			}else{
				iv_fail_resend.setVisibility(View.VISIBLE);
				tv_send_status.setVisibility(View.GONE);
				tv_send_status.setText("发送失败");
			}
		}*/
		 
		 Log.i("getMsgType", "+++++"+position+"++++++"+list.get(position).getMsgType());
		
		return convertView ;
	}
	
	@Override
	public int getItemViewType(int position) {
		ChatMessage msg = list.get(position);
		 
		return msg.getMsgType() == Config.TYPE_RECEIVER_TXT ? 0 : 1 ;
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	/**
	 * 返回发送或是接收 的 布局
	 * @param position
	 * @return View
	 */
	private View createViewByType(int position) {
		Log.i("position", "++++position++"+position);
		return getItemViewType(position) == 0 ? 
				mInflater.inflate(R.layout.item_chat_received_message, null) 
				:
				mInflater.inflate(R.layout.item_chat_sent_message, null);
	}
}
