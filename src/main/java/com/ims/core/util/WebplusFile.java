package com.ims.core.util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 类名:com.toonan.util.WebplusFile
 * 描述:文件操作
 * 编写者:陈骑元
 * 创建时间:2019年4月25日 下午6:13:36
 * 修改说明:
 */
public final class WebplusFile {
	
	private static Logger logger = LoggerFactory.getLogger(WebplusFile.class);
	private static final int BUFFER = 1024*2;
	
	/**
	 * 
	 * 简要说明：获取文件类型
	 * 编写者：陈骑元
	 * 创建时间：2016年7月23日 上午9:45:40
	 * @param 说明
	 * @return 说明
	 */
	public static String getFileType(String fileName) { // 获取文件名后缀名
		if(WebplusUtil.isEmpty(fileName)){
			throw new RuntimeException("文件名称为空");
		}
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}
	/**
	 * 判断文件是否为图片<br>
	 * <br>
	 * 
	 * @param filename
	 *            文件名<br>
	 *            判断具体文件类型<br>
	 * @return 检查后的结果<br>
	 * @throws Exception
	 */
	public static boolean isPicture(String filename) {
		// 文件名称为空的场合
		if (WebplusUtil.isEmpty(filename)) {
			// 返回不和合法
			return false;
		}
		// 获得文件后缀名
		//String tmpName = getExtend(filename);
		String tmpName = filename;
		// 声明图片后缀名数组
		String imgeArray[][] = { { "bmp", "0" }, { "dib", "1" },
				{ "gif", "2" }, { "jfif", "3" }, { "jpe", "4" },
				{ "jpeg", "5" }, { "jpg", "6" }, { "png", "7" },
				{ "tif", "8" }, { "tiff", "9" }, { "ico", "10" } };
		// 遍历名称数组
		for (int i = 0; i < imgeArray.length; i++) {
			// 判断单个类型文件的场合
			if (imgeArray[i][0].equals(tmpName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * 简要说明：递归创建文件夹
	 * 编写者：陈骑元
	 * 创建时间：2016年9月6日 下午4:46:30
	 * @param 说明
	 * @return 说明
	 */
	public static boolean createFolder(String folderPath) {
		File folderFile = new File(folderPath); // 如果文件夹不存在 则创建文件夹
		if(folderFile.getParentFile().exists()){
			folderFile.mkdir();
		  }else{
			  createFolder(folderFile.getParentFile().getPath());
			  folderFile.mkdir();
		}
	   
		return true;
	}

	/**
	 * 如果文件夹不存在，则创建
	 * 
	 * @param file
	 *            文件
	 * @param folderPath文件夹路径
	 * @param fileName文件名称
	 * @return
	 */
	public static boolean buildFile(File file, String folderPath, String fileName) {
		boolean result = false;
		FileOutputStream fos = null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		long fileLen = file.length();
		try {
			File folderFile = new File(folderPath);
			if (!folderFile.exists() && !folderFile.isDirectory()) {
				folderFile.mkdir();
			}
			String outFilePath = folderPath + File.separator + fileName;
			fos = new FileOutputStream(outFilePath);
			if (fileLen == 0) {
				file.createNewFile();
			}
			fis = new FileInputStream(file); // 读取文件内容
			bis = new BufferedInputStream(fis); // 使用流读出
			byte[] buf = new byte[BUFFER];
			int len = 0;
			while ((len = bis.read(buf)) != -1) {
				fos.write(buf, 0, len); // 写入到压缩包
				fos.flush();
			}
			result = true;
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		} finally {

			try {
				if (fos != null) {
					fos.close();
				}
				if (bis != null) {
					bis.close();
				}
				if (fos != null) {
					fos.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				result = false;
				e.printStackTrace();

			}
		}

		return result;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param file_name
	 *            String 要检查文件的绝对路径
	 * @return boolean true存在，false不存在
	 */
	public static boolean isExist(String file_name) throws Exception {
		if (WebplusUtil.isEmpty(file_name))
			return false;

		try {
			File file = new File(file_name);
			return file.exists();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * 创建一个新文件
	 * 
	 * @param file_name
	 *            String 文件绝对路径
	 * @param isReplace
	 *            boolean 如果存在是否替换 true替换，false保留
	 * @return boolean 是否创建成功
	 */
	public static boolean createFile(String file_name, boolean isReplace) throws Exception {
		if (WebplusUtil.isEmpty(file_name))
			return false;

		try {
			File file = new File(file_name);
			// 如果替换的话，先删除再创建
			if (!file.exists()) {
				file.createNewFile();
			}
			// 如果替换的话，先删除再创建
			else if (isReplace) {
				file.delete();
				file.createNewFile();
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 *            String 要删除文件的绝对路径
	 * @return boolean
	 */
	public static boolean deleteFile(String filePath)  {
		if (WebplusUtil.isEmpty(filePath))
			return false;

		try {
			File file = new File(filePath);

			if (file.exists()) {
				return file.delete();
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * 重命名文件
	 * 
	 * @param des_file_name
	 *            String
	 * @throws Exception
	 * @return boolean
	 */
	public static boolean renameFile(String src_file_name, String des_file_name) throws Exception {
		if (WebplusUtil.isEmpty(des_file_name))
			return false;

		try {
			File file = new File(des_file_name);
			File src_file = new File(src_file_name);
			if (src_file.exists()) {
				return src_file.renameTo(file);
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * 文件拷贝
	 * 
	 * @param source_file
	 *            String 源文件绝对路径
	 * @param des_file
	 *            String 目标文件绝对路径
	 * @param isReplace
	 *            boolean 如目标文件存在是否替换
	 * @return boolean
	 */
	@SuppressWarnings({ "resource", "unused" })
	public static boolean copyFile(String source_file, String des_file, boolean isReplace) throws Exception {
		try {
			int bytesum = 0;
			int byteread = 0;

			// 如目标文件存在并且要替换的话，先删除再复制，否则不进行任何操作
			if (isExist(des_file)) {
				if (isReplace)
					deleteFile(des_file);
				else
					return true;
			}

			InputStream inStream = new FileInputStream(source_file);
			FileOutputStream fs = new FileOutputStream(des_file);
			byte[] buffer = new byte[1444];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
			}
			inStream.close();
			return true;
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (IOException ex1) {
			ex1.printStackTrace();
			throw ex1;
		}
	}

	/**
	 * 读取文件信息到字符数组中，文件中一行作为数组的一个元素。文件中每行长度不能超过60000字节
	 * 
	 * @param file_name
	 *            String 文件绝对路径
	 * @throws Exception
	 * @return String[] 文件内容
	 */
	@SuppressWarnings({ "rawtypes", "unused", "resource", "unchecked" })
	public static String[] readFile(String file_name) throws Exception {
		try {
			ArrayList list = new ArrayList();
			String oneLine = "";
			String temp = "";
			FileInputStream fileInputStream = new FileInputStream(new File(file_name));
			BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
			while ((oneLine = br.readLine()) != null) {
				// 略过空行
				if (oneLine.equals(""))
					continue;
				if (oneLine.length() > 60000)
					throw new Exception("文件中记录过长");
				list.add(oneLine);
			}
			if (list.size() == 0)
				return new String[0];

			String[] strData = new String[list.size()];
			String aRecord = "";
			for (int i = 0; i < list.size(); i++) {
				temp = (String) list.get(i);
				strData[i] = temp;
			}
			return strData;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.toString());
		}
	}

	/**
	 * 将文本内容写入到指定文件中
	 * 
	 * @param file_name
	 *            String 文件绝对路径
	 * @param content
	 *            String 要写入的内容
	 * @param isCreate
	 *            boolean 文件不存在是否创建
	 * @throws Exception
	 * @return boolean
	 */
	public static boolean writeFile(String file_name, String content, boolean isCreate) throws Exception {
		try {
			if (!isExist(file_name)) {
				if (!isCreate)
					return false;
				else
					createFile(file_name, true);
			}

			// 读文件内容,获取文件长度并定义数组
			File fl = new File(file_name);
			int flen = (int) fl.length();
			char file_content[] = new char[flen];

			// 读文件内容
			try {
				if (fl.canRead()) {
					FileReader fis = new FileReader(file_name);
					flen = fis.read(file_content);
					fis.close();
				}
			} catch (IOException e) {
				System.out.println("系统写日志时产生错误: " + e.toString());
			}

			// 写文件内容
			try {
				FileWriter pw = new FileWriter(file_name);

				pw.write(file_content, 0, flen);
				if (flen > 0)
					pw.write("\r\n");
				pw.write(content);

				pw.close();
			} catch (IOException e) {
				System.out.println("系统写日志时产生错误: " + e.toString());
			}

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.toString());
		}
	}
	
	/**
	 * 创建指定格式编码的文件
	 * 
	 * @param filePath文件路径
	 * @param encoding文件编码
	 * @param content文件内容
	 * @return
	 */
	public static boolean createCodeFile(String filePath, 
			String content,String encoding) {
		if(WebplusUtil.isEmpty(encoding)){
			encoding="UTF-8";
		}
		OutputStream out = null;
		try {
			File file = new File(filePath);
			if(!file.exists()){
				file.createNewFile();
			}
			out = new FileOutputStream(file,true);
			byte[] b = content.getBytes(encoding);
			out.write(b);
			out.flush();
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (out != null) { // 关闭输出流
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return  false;
	}

	/**
	 * 取得指定目录下的所有文件信息
	 * 
	 * @param path
	 *            String 路径名称
	 * @throws Exception
	 * @return String[]
	 */
	public static String[] getFiles(String path) throws Exception {
		try {
			File d = new File(path); // 建立当前目录中文件的File对象
			File list[] = d.listFiles(); // 取得代表目录中所有文件的File对象数组

			String[] files = new String[list.length];
			for (int i = 0; i < list.length; i++) {
				if (list[i].isFile()) {
					files[i] = list[i].getName();
				}
			}
			return files;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * 获取文件大小,单位为k
	 * 
	 * @param fileName
	 *            String
	 * @throws Exception
	 * @return double
	 */
	public static double getFileSize(String fileName) throws Exception {
		try {
			File file = new File(fileName);

			if (file.exists() && file.isFile()) {
				return file.length() / 1024;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return 0;
	}

	/**
	 * 读取到字节数组
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(String filePath) throws IOException {
		File f = new File(filePath);
		if (!f.exists()) {
			throw new FileNotFoundException(filePath);
		}
		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(f);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {

			}
			return byteBuffer.array();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Mapped File way MappedByteBuffer 可以在处理大文件时，提升性能
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArrayBig(String filePath) throws IOException {

		FileChannel fc = null;
		RandomAccessFile rf = null;
		try {
			rf = new RandomAccessFile(filePath, "r");
			fc = rf.getChannel();
			MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0, fc.size()).load();
			byte[] result = new byte[(int) fc.size()];
			if (byteBuffer.remaining() > 0) {
				byteBuffer.get(result, 0, byteBuffer.remaining());
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				rf.close();
				fc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @param response
	 * @param filePath
	 *            //文件完整路径(包括文件名和扩展名)
	 * @param fileName
	 *            //下载后看到的文件名
	 * @return 文件名
	 */
	public static void downloadFile(final HttpServletRequest request,final HttpServletResponse response, String filePath, String fileName) {

		OutputStream outputStream = null;
		try {
			byte[] data = toByteArray(filePath);
			if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {  //解决IE和火狐文件名兼容的问题
				fileName=new String( fileName.getBytes("utf-8"),"ISO-8859-1");//解决中文乱码
		      } else {
		    	   fileName=URLEncoder.encode( fileName,"UTF-8");;
		     }
			response.reset();
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			response.addHeader("Content-Length", "" + data.length);
			response.setContentType("application/octet-stream;charset=UTF-8");
			outputStream = new BufferedOutputStream(response.getOutputStream());
			outputStream.write(data);
			outputStream.flush();
			response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	/**
	 * @param response
	 * @param filePath
	 *            //文件完整路径(包括文件名和扩展名)
	 * @param fileName
	 *            //下载后看到的文件名
	 * @return 文件名
	 */
	public static void downloadFile(final HttpServletResponse response, String filePath, String fileName) {

		OutputStream outputStream = null;
		try {
			byte[] data = toByteArray(filePath);
			fileName = URLEncoder.encode(fileName, "UTF-8");
			response.reset();
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			response.addHeader("Content-Length", "" + data.length);
			response.setContentType("application/octet-stream;charset=UTF-8");
			outputStream = new BufferedOutputStream(response.getOutputStream());
			outputStream.write(data);
			outputStream.flush();
			response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	/**
	 * 
	 * 简要说明：通过流下载文件
	 * 编写者：陈骑元
	 * 创建时间：2018年12月20日 下午1:44:44
	 * @param 说明
	 * @return 说明
	 */
	public static boolean  downloadFile(final HttpServletResponse response,InputStream inputStream) {
	      if(WebplusUtil.isNotEmpty(inputStream)){
	    	  OutputStream os = null;
				try {
					
				
						// 文件以流的方式发送到客户端浏览器
						os = response.getOutputStream();
						byte[] buffer = new byte[8096];
						int byteread = 0;
						while ((byteread = inputStream.read(buffer, 0, BUFFER)) != -1) {
							os.write(buffer, 0, byteread);
							os.flush();
						}
						os.close();
						
						return true;

				} catch (Exception e) {
					logger.error("通过流下载文件出错："+e);
				} finally {
					if (inputStream != null) {
						try {
							inputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
		
	      }
	      logger.error("下载文件没有文件流返回");
	      return false;
		 
	
	}
	/**
	 * 
	 * 简要说明：通过文件路径把文件转成base64
	 * 编写者：陈骑元
	 * 创建时间：2019年1月10日 下午5:49:18
	 * @param 说明
	 * @return 说明
	 */
	public static String fromFileToBase64(String filePath){
		File file=new File(filePath);
		
		return fromFileToBase64(file);
	}
	/**
	 * 
	 * 简要说明：把文件转成base64
	 * 编写者：陈骑元
	 * 创建时间：2019年1月10日 下午5:49:51
	 * @param 说明
	 * @return 说明
	 */
    public static String fromFileToBase64(File file){
		if(!file.isFile()){
			logger.error("转换文件base64失败，文件不存在");
			return "";
		}
		FileInputStream fis = null;
		try {
			fis= new FileInputStream(file);
			byte[] buffer = new byte[(int)file.length()];
	        fis.read(buffer);
	        return new String(Base64.encodeBase64( buffer));
		} catch (Exception e) {
			logger.error("转换文件base64失败:"+e);
			
		}finally{
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
        return "";
       
		
	}
    /**
     * 
     * 简要说明：从网络文件中转base64为
     * 编写者：陈骑元
     * 创建时间：2019年1月10日 下午6:43:19
     * @param 说明
     * @return 说明
     */
    public static String formFileUrlToBase64(String fileUrl){
    	InputStream inputStream=null;
    	ByteArrayOutputStream bos = null;
		try {
			URL url = new URL(fileUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置超时间为3秒
			conn.setConnectTimeout(10 * 1000);
			// 防止屏蔽程序抓取而返回403错误
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			// 得到输入流
			inputStream= conn.getInputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			bos=new ByteArrayOutputStream();
			while ((len = inputStream.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			byte[] byteArray=bos.toByteArray();
			return new String(Base64.encodeBase64( byteArray));

		} catch (Exception e) {
			logger.info("从网络文件转Base64失败:"+e);
		}finally{
			if (bos!= null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
         return "";
		

    }
    /**
     * 
     * 简要说明：通过文件流下载文件
     * 编写者：陈骑元
     * 创建时间：2019年5月17日 下午6:46:29
     * @param 说明
     * @return 说明
     */
	public static void downloadFile(final HttpServletRequest request,final HttpServletResponse response, byte[] fileByteArray, String fileName) {

		OutputStream outputStream = null;
		try {
			
			if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {  //解决IE和火狐文件名兼容的问题
				fileName=new String( fileName.getBytes("utf-8"),"ISO-8859-1");//解决中文乱码
		      } else {
		    	   fileName=URLEncoder.encode( fileName,"UTF-8");;
		     }
			response.reset();
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			response.addHeader("Content-Length", "" + fileByteArray.length);
			response.setContentType("application/octet-stream;charset=UTF-8");
			outputStream = new BufferedOutputStream(response.getOutputStream());
			outputStream.write(fileByteArray);
			outputStream.flush();
			response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	/**
	 * 测试主应用
	 * 
	 * @param arg
	 *            String[]
	 */
	public static void main(String[] arg) {
		/*
		 * try { FileOperation file = new FileOperation();
		 * System.out.println("==" + String.valueOf(file.getFileSize("c:/aa")));
		 * } catch (Exception ex) { ex.printStackTrace(); }
		 */
	}
}
