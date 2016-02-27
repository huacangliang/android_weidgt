package com.hongxiangge.image;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * 
 * @author dengzhijiang
 *
 */
public class MD5 {
	// MD5変換
	public static String Md5(String str) {
		if (str != null && !str.equals("")) {
			try {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
				byte[] md5Byte = md5.digest(str.getBytes("UTF8"));
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < md5Byte.length; i++) {
					sb.append(HEX[(int) (md5Byte[i] & 0xff) / 16]);
					sb.append(HEX[(int) (md5Byte[i] & 0xff) % 16]);
				}
				str = sb.toString();
			} catch (NoSuchAlgorithmException e) {
			} catch (Exception e) {
			}
		}
		return str;
	}
	
	public static String GetMd5(String FilePath)
	{
	    char hexdigits[] =
	    {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
	            'e', 'f'};
	    FileInputStream fis = null;
	    String sString;
	    char str[] = new char[16 * 2];
	    int k = 0;
	    try
	    {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        fis = new FileInputStream(FilePath);
	        byte[] buffer = new byte[2048];
	        int length = -1;
	        // long s = System.currentTimeMillis();
	        while ((length = fis.read(buffer)) != -1)
	        {
	            md.update(buffer, 0, length);
	        }
	        byte[] b = md.digest();

	        for (int i = 0; i < 16; i++)
	        {
	            byte byte0 = b[i];
	            str[k++] = hexdigits[byte0 >>> 4 & 0xf];
	            str[k++] = hexdigits[byte0 & 0xf];
	        }
	        fis.close();
	        sString = new String(str);

	        return sString;
	    }
	    catch (Exception ex)
	    {
	        ex.printStackTrace();
	        return null;
	    }
	}

	
}
