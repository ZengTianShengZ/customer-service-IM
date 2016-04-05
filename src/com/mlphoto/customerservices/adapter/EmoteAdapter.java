package com.mlphoto.customerservices.adapter;

import java.util.List;

import com.mlphoto.customerservice.bean.FaceText;
import com.mlphoto.customerservices.ui.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class EmoteAdapter extends BaseArrayListAdapter {

	public EmoteAdapter(Context context, List<FaceText> datas) {
		super(context, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_face_text, null);
			holder = new ViewHolder();
			holder.mIvImage = (ImageView) convertView
					.findViewById(R.id.v_face_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		FaceText faceText = (FaceText) getItem(position);
		String key = faceText.text.substring(1); //  ½«\\ue056 ½ØÈ¡Îª \ue056
		Drawable drawable =mContext.getResources().getDrawable(mContext.getResources().getIdentifier(key, "drawable", mContext.getPackageName()));
		holder.mIvImage.setImageDrawable(drawable);
		return convertView;
	}   

	class ViewHolder {
		ImageView mIvImage;
	}
}
