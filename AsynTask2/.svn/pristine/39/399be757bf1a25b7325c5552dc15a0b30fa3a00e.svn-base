package com.jingzhong.asyntask2.utils;

import java.io.File;
import java.io.FilenameFilter;
/**
 * 
 * @author dengzhijiang
 * 
 */
public class FileNameFilterUtils implements FilenameFilter {
	public String reg = "";

	@Override
	public boolean accept(File dir, String filename) {
		if (filename.matches(reg)) {
			return true;
		}
		return false;
	}
}
