package com.ims.core.util;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 
 * 类名:com.toonan.util.WebplusQrcode
 * 描述:二维码工具类
 * 编写者:陈骑元
 * 创建时间:2019年4月25日 下午8:28:09
 * 修改说明:
 */
public class WebplusQrcode {
	
	private static final String CHARSET = "utf-8";
	private static final String FORMAT = "PNG";
	// 二维码尺寸
	private static final int QRCODE_SIZE = 300;
	// LOGO宽度
	private static final int LOGO_WIDTH = 60;
	// LOGO高度
	private static final int LOGO_HEIGHT = 60;
	/**
	 * 生成二维码(内嵌LOGO)
	 * 二维码文件名随机，文件名可能会有重复
	 * 
	 * @param content
	 *            内容
	 * @param logoPath
	 *            LOGO地址
	 * @param destPath
	 *            存放目录
	 * @param needCompress
	 *            是否压缩LOGO
	 * @throws Exception
	 */
	public static String createQrcodeImage(String content,String destPath,String fileName) {
		
		try {
			BufferedImage image = WebplusQrcode.createQrcodeImage(content, "",false);
			mkdirs(destPath);
			ImageIO.write(image, FORMAT, new File(destPath + "/" + fileName));
			return fileName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
    /**
     * 
     * 简要说明： 创建二维码图片
     * 编写者：陈骑元
     * 创建时间：2019年4月25日 下午8:28:58
     * @param 说明 content 二维码内容 logoPath 图片路径 needCompress是否需要压缩
     * @return 说明
     */
	private static BufferedImage createQrcodeImage(String content, String logoPath, boolean needCompress)  {
		
		
		try {
			Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
			hints.put(EncodeHintType.MARGIN, 1);
			BitMatrix bitMatrix= new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE,
					hints);
			int width = bitMatrix.getWidth();
			int height = bitMatrix.getHeight();
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
				}
			}
			if (logoPath == null || "".equals(logoPath)) {
				return image;
			}
			// 插入图片
			insertImage(image, logoPath, needCompress);
			return image;
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return null;
	}
 
	/**
	 * 插入LOGO
	 * 
	 * @param source
	 *            二维码图片
	 * @param logoPath
	 *            LOGO图片地址
	 * @param needCompress
	 *            是否压缩
	 * @throws Exception
	 */
	private static boolean  insertImage(BufferedImage source, String logoPath, boolean needCompress)  {
	
	
		try {
			File file = new File(logoPath);
			if (!file.exists()) {
				
				return false;
			}
			Image src = ImageIO.read(new File(logoPath));
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			if (needCompress) { // 压缩LOGO
				if (width > LOGO_WIDTH) {
					width = LOGO_WIDTH;
				}
				if (height > LOGO_HEIGHT) {
					height = LOGO_HEIGHT;
				}
				Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
				BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(image, 0, 0, null); // 绘制缩小后的图
				g.dispose();
				src = image;
			}
			// 插入LOGO
			Graphics2D graph = source.createGraphics();
			int x = (QRCODE_SIZE - width) / 2;
			int y = (QRCODE_SIZE - height) / 2;
			graph.drawImage(src, x, y, width, height, null);
			Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
			graph.setStroke(new BasicStroke(3f));
			graph.draw(shape);
			graph.dispose();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
 
	/**
	 * 生成二维码(内嵌LOGO)
	 * 二维码文件名随机，文件名可能会有重复
	 * 
	 * @param content
	 *            内容
	 * @param logoPath
	 *            LOGO地址
	 * @param destPath
	 *            存放目录
	 * @param needCompress
	 *            是否压缩LOGO
	 * @throws Exception
	 */
	public static String encode(String content, String logoPath, String destPath, boolean needCompress) {
		
		try {
			BufferedImage image = WebplusQrcode.createQrcodeImage(content, logoPath, needCompress);
			mkdirs(destPath);
			String fileName = WebplusUtil.createFileId("Q")+ "." + FORMAT.toLowerCase();
			ImageIO.write(image, FORMAT, new File(destPath + "/" + fileName));
			return fileName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 生成二维码(内嵌LOGO)
	 * 调用者指定二维码文件名
	 * 
	 * @param content
	 *            内容
	 * @param logoPath
	 *            LOGO地址
	 * @param destPath
	 *            存放目录
	 * @param fileName
	 *            二维码文件名
	 * @param needCompress
	 *            是否压缩LOGO
	 * @throws Exception
	 */
	public static String encode(String content, String logoPath, String destPath, String fileName, boolean needCompress) {
		
		try {
			BufferedImage image = WebplusQrcode.createQrcodeImage(content, logoPath, needCompress);
			mkdirs(destPath);
			fileName = fileName.substring(0, fileName.indexOf(".")>0?fileName.indexOf("."):fileName.length()) 
					+ "." + FORMAT.toLowerCase();
			ImageIO.write(image, FORMAT, new File(destPath + "/" + fileName));
			return fileName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}
 
	/**
	 * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．
	 * (mkdir如果父目录不存在则会抛出异常)
	 * @param destPath
	 *            存放目录
	 */
	public static void mkdirs(String destPath) {
		File file = new File(destPath);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
	}
 
	/**
	 * 生成二维码(内嵌LOGO)
	 * 
	 * @param content
	 *            内容
	 * @param logoPath
	 *            LOGO地址
	 * @param destPath
	 *            存储地址
	 * @throws Exception
	 */
	public static String encode(String content, String logoPath, String destPath) {
		return WebplusQrcode.encode(content, logoPath, destPath, false);
	}
 
	/**
	 * 生成二维码
	 * 
	 * @param content
	 *            内容
	 * @param destPath
	 *            存储地址
	 * @param needCompress
	 *            是否压缩LOGO
	 * @throws Exception
	 */
	public static String encode(String content, String destPath, boolean needCompress) {
		return WebplusQrcode.encode(content, null, destPath, needCompress);
	}
 
	/**
	 * 生成二维码
	 * 
	 * @param content
	 *            内容
	 * @param destPath
	 *            存储地址
	 * @throws Exception
	 */
	public static String encode(String content, String destPath) {
		return WebplusQrcode.encode(content, null, destPath, false);
	}
 
	/**
	 * 生成二维码(内嵌LOGO)
	 * 
	 * @param content
	 *            内容
	 * @param logoPath
	 *            LOGO地址
	 * @param output
	 *            输出流
	 * @param needCompress
	 *            是否压缩LOGO
	 * @throws Exception
	 */
	public static void encode(String content, String logoPath, OutputStream output, boolean needCompress)
			throws Exception {
		BufferedImage image =WebplusQrcode.createQrcodeImage(content, logoPath, needCompress);
		ImageIO.write(image, FORMAT, output);
	}
 
	/**
	 * 生成二维码
	 * 
	 * @param content
	 *            内容
	 * @param output
	 *            输出流
	 * @throws Exception
	 */
	public static void encode(String content, OutputStream output) throws Exception {
		WebplusQrcode.encode(content, null, output, false);
	}
 
	/**
	 * 解析二维码
	 * 
	 * @param file
	 *            二维码图片
	 * @return
	 * @throws Exception
	 */
	public static String decode(File file)  {
		try {
			BufferedImage image = ImageIO.read(file);
			if (image == null) {
				return null;
			}
			BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			Result result;
			Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
			hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
			result = new MultiFormatReader().decode(bitmap, hints);
			String resultStr = result.getText();
			return resultStr;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
	}
 
	/**
	 * 解析二维码
	 * 
	 * @param path
	 *            二维码图片地址
	 * @return
	 * @throws Exception
	 */
	public static String decode(String path) {
		return WebplusQrcode.decode(new File(path));
	}
 
	
	
	public static void main(String[] args) throws Exception {
		String text = "http://bs-test.digitalgd.com.cn/ping/rating-dynamic/?msg=hallGZlhry-100-2178165492611262273-11440105340250217W4442106041002201910090007-11440105340250217W4442106041002";
		//不含Logo
		//QRCodeUtil.encode(text, null, "e:\\", true);
		//含Logo，不指定二维码图片名
		//QRCodeUtil.encode(text, "e:\\csdn.jpg", "e:\\", true);
		//含Logo，指定二维码图片名
		WebplusQrcode.encode(text, "", "e:\\", "qrcode", true);
	}


}
