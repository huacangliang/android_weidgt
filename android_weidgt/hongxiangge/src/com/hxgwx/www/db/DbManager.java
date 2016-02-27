package com.hxgwx.www.db;

import java.util.List;

import com.hxgwx.www.bean.SendARCBody;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

public class DbManager {
	/**
	 * Éý¼¶Êý¾Ý¿â
	 * 
	 * @param db
	 * @param t
	 */
	public static <T> void uploadDB(DbUtils db, Class<T> t) {
		List<T> oldData = null;
		try {
			oldData = db.findAll(t);
			db.dropTable(SendARCBody.class);
			if (oldData!=null) {
				db.saveAll(oldData);
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
