package com.jingzhong.asyntask2.utils;

import com.jingzhong.asyntask2.exception.Asyntask2Exception;

import android.util.SparseArray;
import android.view.View;

public class ViewHolderUtils {

	@SuppressWarnings("unchecked")
	public static <T> T getViewById(View root, int id) {
		if(root==null)
			new Asyntask2Exception("null point container");
		
		SparseArray<View> viewHolder;

		viewHolder = (SparseArray<View>) root.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			root.setTag(viewHolder);
			View chilrdView = root.findViewById(id);
			viewHolder.put(id, chilrdView);
			return (T) chilrdView;
		}
		View chilrdView = viewHolder.get(id);
		if (chilrdView == null) {
			chilrdView = root.findViewById(id);
			viewHolder.put(id, chilrdView);
		}
		return (T) chilrdView;

	}
}
