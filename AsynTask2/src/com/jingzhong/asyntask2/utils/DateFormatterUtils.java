package com.jingzhong.asyntask2.utils;

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

			return y + "-" + Utils.converoDate(m) + "-" + Utils.converoDate(d) + "\t " + Utils.converoDate(h) + ":"
					+ Utils.converoDate(ms) + ":" + Utils.converoDate(sc);
		}
		else if (s > month) {
			m = (int) (s % year / month);
			d = (int) (s % year % month / day);
			h = (int) (s % year % month % day / hh);
			ms = (int) (s % year % month % day % hh / mm);
			sc = (int) (s % year % month % day % hh % mm / ss);
			return "00-" + Utils.converoDate(m) + "-" + Utils.converoDate(d) + "\t " + Utils.converoDate(h) + ":"
					+ Utils.converoDate(ms) + ":" + Utils.converoDate(sc);
		}
		else if (s > day) {
			d = (int) (s % year % month / day);
			h = (int) (s % year % month % day / hh);
			ms = (int) (s % year % month % day % hh / mm);
			sc = (int) (s % year % month % day % hh % mm / ss);
			return "00-00-" + Utils.converoDate(d) + "\t " + Utils.converoDate(h) + ":" + Utils.converoDate(ms) + ":"
					+ Utils.converoDate(sc);
		}
		else {
			h = (int) (s % year % month % day / hh);
			ms = (int) (s % year % month % day % hh / mm);
			sc = (int) (s % year % month % day % hh % mm / ss);
			return Utils.converoDate(h) + ":" + Utils.converoDate(ms) + ":" + Utils.converoDate(sc);
		}

	}
}
