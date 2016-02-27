package com.hxgwx.www.test;

import android.test.AndroidTestCase;

import com.jingzhong.asyntask2.utils.Utils;

public class Test extends AndroidTestCase {
	private static final String Tag="Test";
	
	public void test() throws Exception{
	Utils.getMyUUID();
	}
}
