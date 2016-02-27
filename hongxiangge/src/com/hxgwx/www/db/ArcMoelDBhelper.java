package com.hxgwx.www.db;

import com.hxgwx.www.bean.SendARCBody;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DbUpgradeListener;

import android.content.Context;

public class ArcMoelDBhelper {

	private static DbUtils dbManager;

	private Context context;

	private static ArcMoelDBhelper db;

	private static String DBNAME = "com.hxgwx.bookmodel";

	public static int DBVERSION = 12;

	/**
	 * @return the dBVERSION
	 */
	public static int getDBVERSION() {
		return DBVERSION;
	}

	/**
	 * @param dBVERSION
	 *            the dBVERSION to set
	 */
	public static void setDBVERSION(int dBVERSION) {
		DBVERSION = dBVERSION;
	}

	/**
	 * @return the dBNAME
	 */
	public static String getDBNAME() {
		return DBNAME;
	}

	/**
	 * @param dBNAME
	 *            the dBNAME to set
	 */
	public static void setDBNAME(String dBNAME) {
		DBNAME = dBNAME;
	}

	/**
	 * 创建数据库
	 * 
	 * @param name
	 */
	public DbUtils createDb() {
		if (dbManager == null) {
			dbManager = DbUtils.create(context, DBNAME, DBVERSION, new DbUpgradeListener() {

				@Override
				public void onUpgrade(DbUtils arg0, int arg1, int arg2) {
					// TODO Auto-generated method stub
					if (arg1 != arg2) {
						// 在这更新表结构
						DbManager.uploadDB(arg0, SendARCBody.class);
					}
				}
			});
		}
		return dbManager;
		// TODO Auto-generated method stub

	}

	private ArcMoelDBhelper(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	/**
	 * @return the db
	 */
	public static ArcMoelDBhelper getInstance(Context context) {
		if (db == null) {
			db = new ArcMoelDBhelper(context);
		}
		return db;
	}

}
