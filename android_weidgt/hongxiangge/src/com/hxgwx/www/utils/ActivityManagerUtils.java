package com.hxgwx.www.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public interface ActivityManagerUtils {
	public static final List<Activity> ACTIVITYS = new ArrayList<Activity>();
	
	void add();
	void close();
	void closeAll();
}
