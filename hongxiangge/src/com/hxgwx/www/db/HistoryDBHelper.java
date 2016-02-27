package com.hxgwx.www.db;

import android.content.Context;

import com.hxgwx.www.bean.ArticModelBase;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DbUpgradeListener;


public class HistoryDBHelper {
	private static DbUtils dbManager;
	
	private Context context;
	
	private static HistoryDBHelper db;
	
	private HistoryDBHelper(Context context){
		this.context=context;
	}
	
	private static String DBNAME="com.hxgwx.history";
	
	/**
	 * @return the dBNAME
	 */
	public static String getDBNAME() {
		return DBNAME;
	}

	/**
	 * @param dBNAME the dBNAME to set
	 */
	public static void setDBNAME(String dBNAME) {
		DBNAME = dBNAME;
	}

	public static int DBVERSION=12;

	/**
	 * @return the dBVERSION
	 */
	public static int getDBVERSION() {
		return DBVERSION;
	}

	/**
	 * @param dBVERSION the dBVERSION to set
	 */
	public static void setDBVERSION(int dBVERSION) {
		DBVERSION = dBVERSION;
	}

	/**
	 * 创建数据库
	 * 
	 * @param name
	 */
	public DbUtils createDb() {
		if (dbManager == null) {
			dbManager = DbUtils.create(context, DBNAME,
					DBVERSION, new DbUpgradeListener() {

						@Override
						public void onUpgrade(DbUtils arg0, int arg1, int arg2) {
							// TODO Auto-generated method stub
							if (arg1 != arg2) {
								// 在这更新表结构
								DbManager.uploadDB(arg0, ArticModelBase.class);
							}
						}
					});
		}
		return dbManager;
		// TODO Auto-generated method stub

	}

	/**
	 * @return the db
	 */
	public static HistoryDBHelper getInstance(Context context) {
		if(db==null){
			db=new HistoryDBHelper(context);
		}
		return db;
	}

}
