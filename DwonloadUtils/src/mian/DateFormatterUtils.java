package mian;

import java.text.DecimalFormat;

import com.jingzhong.asyntask2.utils.Utils;

public class DateFormatterUtils {

	public static String formatterToTime(long s) {
		int year = 1000 * 60 * 60 * 24 * 30 * 12;
		int month = 1000 * 60 * 60 * 24 * 30;
		int day = 1000 * 60 * 60 * 24;
		int hh = 1000 * 60 * 60;
		int mm = 1000 * 60;
		int ss = 1000;
		int y;
		int m;
		int d;
		int h;
		int ms;
		int sc;
		if (s > year) {
			y = (int) (s / year);
			m = (int) (s % year / month);
			d = (int) (s % year % month / day);
			h = (int) (s % year % month % day / hh);
			ms = (int) (s % year % month % day % hh / mm);
			sc = (int) (s % year % month % day % hh % mm / ss);

			return y + "-" + converoDate(m) + "-" + converoDate(d) + "\t " + converoDate(h) + ":"
					+ converoDate(ms) + ":" + converoDate(sc);
		}
		else if (s > month) {
			m = (int) (s % year / month);
			d = (int) (s % year % month / day);
			h = (int) (s % year % month % day / hh);
			ms = (int) (s % year % month % day % hh / mm);
			sc = (int) (s % year % month % day % hh % mm / ss);
			return "00-" + converoDate(m) + "-" + converoDate(d) + "\t " + converoDate(h) + ":"
					+ converoDate(ms) + ":" + converoDate(sc);
		}
		else if (s > day) {
			d = (int) (s % year % month / day);
			h = (int) (s % year % month % day / hh);
			ms = (int) (s % year % month % day % hh / mm);
			sc = (int) (s % year % month % day % hh % mm / ss);
			return "00-00-" + converoDate(d) + "\t " + converoDate(h) + ":" + converoDate(ms) + ":"
					+ converoDate(sc);
		}
		else {
			h = (int) (s % year % month % day / hh);
			ms = (int) (s % year % month % day % hh / mm);
			sc = (int) (s % year % month % day % hh % mm / ss);
			return converoDate(h) + ":" + converoDate(ms) + ":" + converoDate(sc);
		}

	}
	
	public static String retain2(
			double f) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(f);
	}
	
	/** 
     * 返回byte的数据大小对应的文本 
     * @param size 
     * @return 
     */  
    public static String getDataSize(long size){  
        DecimalFormat formater = new DecimalFormat("####.00");  
        if(size<1024){  
            return size+"bytes";  
        }else if(size<1024*1024){  
            float kbsize = size/1024f;    
            return formater.format(kbsize)+"KB";  
        }else if(size<1024*1024*1024){  
            float mbsize = size/1024f/1024f;    
            return formater.format(mbsize)+"MB";  
        }else if(size<1024*1024*1024*1024){  
            float gbsize = size/1024f/1024f/1024f;    
            return formater.format(gbsize)+"GB";  
        }else{  
            return "size: error";  
        }  
        
    }
	
	/**
	 * 将小时分钟秒数格式化，只格式其中一个，例如1小时格式化为01小时
	 * @param date
	 * @return
	 */
	public static String converoDate(
			int date) {
		if (date < 0) {
			date = 0;
		}
		String time = date >= 10 ? date + "" : "0" + date;
		return time;

	}
}
