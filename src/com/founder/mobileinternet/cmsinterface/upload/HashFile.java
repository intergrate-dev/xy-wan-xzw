package com.founder.mobileinternet.cmsinterface.upload;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;

import javax.imageio.ImageIO;

public class HashFile
{
	public static char[] hexChar =
	{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static void main(String[] args) throws Exception
	{
		long begin = System.currentTimeMillis();

		// File big = new File("E:\\Downloads\\cls96y1l.udd");

		String md5 = MD5("接口服务结构图.jpg");

		long end = System.currentTimeMillis();
		System.out.println("md5:" + md5 + " time:" + ((end - begin) / 1000) + "s");
	}

	public static String getFileNameMD5(File file) throws Exception
	{
		String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());

		return MD5(file.getName() + file.length()) + "." + suffix;
	}

	public static String getFileMD5(BufferedImage image) throws Exception
	{
		return getFileHash(image, "md5");
	}
	
	public static String getFileHash(BufferedImage image, String hashType) throws Exception
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", os);
		InputStream fis = new ByteArrayInputStream(os.toByteArray());
		
		byte[] buffer = new byte[1024];
		MessageDigest md = MessageDigest.getInstance(hashType);
		int numRead = 0;
		while ((numRead = fis.read(buffer)) > 0)
		{
			md.update(buffer, 0, numRead);
		}
		fis.close();
		return toHexString(md.digest());
	}

	public static String toHexString(byte[] b)
	{
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++)
		{
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	private static String MD5(String md5)
	{
		try
		{
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes(Charset.forName("UTF8")));
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i)
			{
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e)
		{
		}
		return null;
	}
}
