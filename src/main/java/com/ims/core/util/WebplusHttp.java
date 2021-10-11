package com.ims.core.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.apache.commons.codec.Charsets;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ims.core.matatype.Dto;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * 类名:com.ims.common.core.asset.HttpRequestProxy
 * 描述:
 * 编写者:陈骑元
 * 创建时间:2017年4月29日 下午4:01:20
 * 修改说明:
 */
public class WebplusHttp {

	/**
	 * 连接超时
	 */
	private static int connectTimeOut = 120000;

	/**
	 * 读取数据超时
	 */
	private static int readTimeOut = 120000;

	/**
	 * 请求编码
	 */
	public static String requestEncoding = "UTF-8";

	private static Log logger = LogFactory.getLog(WebplusHttp.class);
	
	private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36";

	private static final MediaType CONTENT_TYPE_FORM = MediaType.parse("application/x-www-form-urlencoded");
	private static HttpClient httpClient = null;  
    private static final String CHARSET = "UTF-8";  
	private WebplusHttp() {
	}
	public static synchronized HttpClient getSaveHttpClient(){  
        if(httpClient == null){  
        	HttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
        	HttpConnectionManagerParams managerParams = new HttpConnectionManagerParams();
        	managerParams.setConnectionTimeout(30000);
        	managerParams.setMaxTotalConnections(50);
        	managerParams.setDefaultMaxConnectionsPerHost(30);
        	managerParams.setSoTimeout(20000);
        	manager.setParams(managerParams);
        	httpClient = new HttpClient(manager);
        	httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,CHARSET);
        }  
        return httpClient;  
    }
	/**
	 * 
	 * 简要说明：默认使用UTF-8请求
	 * 编写者：陈骑元
	 * 创建时间：2017年4月29日 下午5:12:04
	 * @param 说明
	 * @return 说明
	 */
	public static String doGet(String reqUrl,Dto parameters){
		
		return doGet(reqUrl,parameters,requestEncoding);
	}
	/**
	 * <pre>
	 * 发送带参数的GET的HTTP请求
	 * </pre>
	 * 
	 * @param reqUrl
	 *            HTTP请求URL
	 * @param parameters
	 *            参数映射表
	 * @return HTTP响应的字符串
	 */
	public static String doGet(String reqUrl, Dto  parameters,
			String recvEncoding) {
		HttpURLConnection url_con = null;
		String responseContent = null;
		try {
			url_con = doHttpGetConnection(reqUrl,parameters);

			InputStream in = url_con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in,
					recvEncoding));
			String tempLine = rd.readLine();
			StringBuffer temp = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				temp.append(tempLine);
				temp.append(crlf);
				tempLine = rd.readLine();
			}
			responseContent = temp.toString();
			rd.close();
			in.close();
		} catch (IOException e) {
			logger.error("网络故障", e);
		} finally {
			if (url_con != null) {
				url_con.disconnect();
			}
		}

		return responseContent;
	}

	private static HttpURLConnection doHttpGetConnection(String reqUrl,
			Dto  parameters) throws IOException {
		HttpURLConnection url_con = null;
		String params = getDtoParam(parameters,WebplusHttp.requestEncoding);
		
		URL url = new URL(reqUrl);
		url_con = (HttpURLConnection) url.openConnection();
		url_con.setRequestMethod("GET");
		System.setProperty("sun.net.client.defaultConnectTimeout",
				String.valueOf(WebplusHttp.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
		System.setProperty("sun.net.client.defaultReadTimeout",
				String.valueOf(WebplusHttp.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
		
		url_con.setDoOutput(true);
		byte[] b = params.toString().getBytes();
		url_con.getOutputStream().write(b, 0, b.length);
		url_con.getOutputStream().flush();
		url_con.getOutputStream().close();

		return url_con;
	}

	/**
	 * <pre>
	 * 发送不带参数的GET的HTTP请求
	 * </pre>
	 * 
	 * @param reqUrl
	 *            HTTP请求URL
	 * @return HTTP响应的字符串
	 */
	public static String doGet(String reqUrl, String recvEncoding) {
		HttpURLConnection url_con = null;
		String responseContent = null;
		try {
			StringBuffer params = new StringBuffer();
			String queryUrl = reqUrl;
			int paramIndex = reqUrl.indexOf("?");

			if (paramIndex > 0) {
				queryUrl = reqUrl.substring(0, paramIndex);
				String parameters = reqUrl.substring(paramIndex + 1,
						reqUrl.length());
				String[] paramArray = parameters.split("&");
				for (int i = 0; i < paramArray.length; i++) {
					String string = paramArray[i];
					int index = string.indexOf("=");
					if (index > 0) {
						String parameter = string.substring(0, index);
						String value = string.substring(index + 1,
								string.length());
						params.append(parameter);
						params.append("=");
						params.append(URLEncoder.encode(value,
								WebplusHttp.requestEncoding));
						params.append("&");
					}
				}

				params = params.deleteCharAt(params.length() - 1);
			}

			URL url = new URL(queryUrl);
			url_con = (HttpURLConnection) url.openConnection();
			url_con.setRequestMethod("GET");
			System.setProperty("sun.net.client.defaultConnectTimeout",
					String.valueOf(WebplusHttp.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
			System.setProperty("sun.net.client.defaultReadTimeout",
					String.valueOf(WebplusHttp.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
			
			url_con.setDoOutput(true);
			byte[] b = params.toString().getBytes();
			url_con.getOutputStream().write(b, 0, b.length);
			url_con.getOutputStream().flush();
			url_con.getOutputStream().close();
			InputStream in = url_con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in,
					recvEncoding));
			String tempLine = rd.readLine();
			StringBuffer temp = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				temp.append(tempLine);
				temp.append(crlf);
				tempLine = rd.readLine();
			}
			responseContent = temp.toString();
			rd.close();
			in.close();
		} catch (IOException e) {
			logger.error("网络故障", e);
		} finally {
			if (url_con != null) {
				url_con.disconnect();
			}
		}

		return responseContent;
	} 
	
	/**
	 * 
	 * 简要说明：发送带参数的HTTP请求
	 * 编写者：陈骑元
	 * 创建时间：2017年8月3日 下午2:00:59
	 * @param 说明
	 * @return 说明
	 */
	public static String doPost(String reqUrl,Dto parameters){
		
		return doPost(reqUrl,parameters,WebplusHttp.requestEncoding);
	}
	/**
	 * <pre>
	 * 发送带参数的POST的HTTP请求
	 * </pre>
	 * 
	 * @param reqUrl
	 *            HTTP请求URL
	 * @param parameters
	 *            参数映射表
	 * @return HTTP响应的字符串
	 */
	public static String doPost(String reqUrl, Dto  parameters,
			String recvEncoding) {
		HttpURLConnection url_con = null;
		String responseContent = null;
		try {
			String params = getDtoParam(parameters,WebplusHttp.requestEncoding);
			URL url = new URL(reqUrl);
			url_con = (HttpURLConnection) url.openConnection();
			url_con.setRequestMethod("POST");
			System.setProperty("sun.net.client.defaultConnectTimeout",
					String.valueOf(WebplusHttp.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
			System.setProperty("sun.net.client.defaultReadTimeout",
					String.valueOf(WebplusHttp.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
			
			url_con.setDoOutput(true);
			byte[] b = params.toString().getBytes();
			url_con.getOutputStream().write(b, 0, b.length);
			url_con.getOutputStream().flush();
			url_con.getOutputStream().close();

			InputStream in = url_con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in,
					recvEncoding));
			String tempLine = rd.readLine();
			StringBuffer tempStr = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				tempStr.append(tempLine);
				tempStr.append(crlf);
				tempLine = rd.readLine();
			}
			responseContent = tempStr.toString();
			rd.close();
			in.close();
		} catch (IOException e) {
			logger.error("网络故障", e);
		} finally {
			if (url_con != null) {
				url_con.disconnect();
			}
		}
		return responseContent;
	}
	
	
	/**
	 * <pre>
	 * 发送带参数的POST的HTTP请求
	 * </pre>
	 * 
	 * @param reqUrl
	 *            HTTP请求URL
	 * @param parameters
	 *            参数映射表
	 * @return HTTP响应的字符串
	 */
	public static String doJsonPost(String reqUrl, Dto  parameters,
			String jsonData) {
		HttpURLConnection url_con = null;
		String responseContent = null;
		try {
			String params = getDtoParam(parameters,WebplusHttp.requestEncoding);

			URL url=null;
            if(WebplusUtil.isNotEmpty(params)) {
            	if(reqUrl.indexOf("?")>-1) {
            		url= new URL(reqUrl+"&"+params);
            	}else {
            		url= new URL(reqUrl+"?"+params);
            	}
            }else {
            	url= new URL(reqUrl);
            }
			url_con = (HttpURLConnection) url.openConnection();
			url_con.setRequestMethod("POST");
			System.setProperty("sun.net.client.defaultConnectTimeout",
					String.valueOf(WebplusHttp.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
			System.setProperty("sun.net.client.defaultReadTimeout",
					String.valueOf(WebplusHttp.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
			url_con.setDoOutput(true);
			url_con.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式  
			url_con.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式  
			
			byte[] b = jsonData.toString().getBytes(WebplusHttp.requestEncoding);
			url_con.getOutputStream().write(b, 0, b.length);
			url_con.getOutputStream().flush();
			url_con.getOutputStream().close();

			InputStream in = url_con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in,
					WebplusHttp.requestEncoding));
			String tempLine = rd.readLine();
			StringBuffer tempStr = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				tempStr.append(tempLine);
				tempStr.append(crlf);
				tempLine = rd.readLine();
			}
			responseContent = tempStr.toString();
			rd.close();
			in.close();
		} catch (IOException e) {
			logger.error("网络故障", e);
		} finally {
			if (url_con != null) {
				url_con.disconnect();
			}
		}
		return responseContent;
	}

	/**
	 * 
	 * @param reqUrl
	 * @param parameters
	 * @param recvEncoding
	 * @param fileIn
	 *            文件流
	 * @return
	 */
	public static String uploadMedia(String reqUrl, Dto  parameters,
			String recvEncoding, InputStream fileIn, String fileName,
			String contentType) {
		HttpURLConnection url_con = null;
		String responseContent = null;
		try {
			// 设置边界
			String BOUNDARY = "----------" + System.currentTimeMillis();
			String params = getDtoParam(parameters,WebplusHttp.requestEncoding);

			URL urlObj = new URL(reqUrl + "&" + params.toString());
			// 连接
			url_con = (HttpURLConnection) urlObj.openConnection();
			/**
			 * 设置关键值
			 */
			url_con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
			url_con.setDoInput(true);
			url_con.setDoOutput(true);
			url_con.setUseCaches(false); // post方式不能使用缓存

			// 设置请求头信息
			url_con.setRequestProperty("Connection", "Keep-Alive");
			url_con.setRequestProperty("Charset",recvEncoding);

			// 设置边界
			url_con.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			// 请求正文信息

			// 第一部分：
			StringBuilder sb = new StringBuilder();
			sb.append("--"); // 必须多两道线
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"file\";filename=\""
					+ fileName + "\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");

			byte[] head = sb.toString().getBytes(recvEncoding);

			// 获得输出流
			OutputStream out = new DataOutputStream(url_con.getOutputStream());
			// 输出表头
			out.write(head);
			// 文件正文部分
			// 把文件已流文件的方式 推入到url中
			DataInputStream in = new DataInputStream(fileIn);
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();

			// 结尾部分
			byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes(recvEncoding);// 定义最后数据分隔线

			out.write(foot);
			out.flush();
			out.close();

			InputStream iddn = url_con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(iddn,
					recvEncoding));
			String tempLine = rd.readLine();
			StringBuffer tempStr = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				tempStr.append(tempLine);
				tempStr.append(crlf);
				tempLine = rd.readLine();
			}
			responseContent = tempStr.toString();
			rd.close();
		} catch (IOException e) {
			logger.error("网络故障", e);
		} finally {
			if (url_con != null) {
				url_con.disconnect();
			}
		}
		return responseContent;
	}
	/**
	 * 
	 * 简要说明：
	 * 编写者：陈骑元
	 * 创建时间：2017年11月6日 上午9:59:02
	 * @param 说明
	 * @return 说明
	 */
	public static String post(String url, String params) {
		RequestBody body = RequestBody.create(CONTENT_TYPE_FORM, params);
		Request request = new Request.Builder().url(url).post(body).build();
		return exec(request);
	}
   /**
    * 
    * 简要说明：
    * 编写者：陈骑元
    * 创建时间：2017年11月6日 上午9:59:10
    * @param 说明
    * @return 说明
    */
	private static String exec(okhttp3.Request request) {
		try {
			okhttp3.Response response = new OkHttpClient().newCall(request).execute();
			if (!response.isSuccessful())
				throw new RuntimeException("Unexpected code " + response);
			return response.body().string();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 简要说明：发送SSL的post请求
	 * 编写者：陈骑元
	 * 创建时间：2017年11月2日 下午4:34:56
	 * @param 说明
	 * @return 说明
	 */
	public static String postSSL(String url, String data, String certPath, String certPass) {
		HttpsURLConnection conn = null;
		OutputStream out = null;
		InputStream inputStream = null;
		BufferedReader reader = null;
		try {
			KeyStore clientStore = KeyStore.getInstance("PKCS12");
			clientStore.load(new FileInputStream(certPath), certPass.toCharArray());
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(clientStore, certPass.toCharArray());
			KeyManager[] kms = kmf.getKeyManagers();
			SSLContext sslContext = SSLContext.getInstance("TLSv1");

			sslContext.init(kms, null, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			URL _url = new URL(url);
			conn = (HttpsURLConnection) _url.openConnection();

			conn.setConnectTimeout(25000);
			conn.setReadTimeout(25000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);

			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
			conn.connect();

			out = conn.getOutputStream();
			out.write(data.getBytes(Charsets.toCharset(requestEncoding)));
			out.flush();

			inputStream = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream, Charsets.toCharset(requestEncoding)));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(inputStream);
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
	
	
	/**
	 * 
	 * 简要说明：发送POSt请求
	 * 编写者：陈骑元
	 * 创建时间：2019年8月5日 下午3:42:10
	 * @param 说明
	 * @return 说明
	 */
	public static final String sendPost(String url,Dto paramDto) {
		String result = "";
		HttpClient client = WebplusHttp.getSaveHttpClient();
		PostMethod postMethod = new PostMethod(url);
		try {
			for(Map.Entry<String,Object> entry:paramDto.entrySet()){    
			    postMethod.addParameter(entry.getKey(), entry.getValue()+"");
			}   
			int statusCode = client.executeMethod(postMethod);
			if (statusCode == 200){
				result = postMethod.getResponseBodyAsString();
			}
			
		} catch (Exception e) {
			logger.error(e);
		} finally {
			postMethod.releaseConnection();
		}
		return result;
	}
	/**
	 * 
	 * 简要说明：
	 * 编写者：陈骑元
	 * 创建时间：2019年8月5日 下午3:41:51
	 * @param 说明
	 * @return 说明
	 */
	public static final String sendGet(String url,Dto paramDto,Dto headerDto) {
		String result = "";
		HttpClient client = WebplusHttp.getSaveHttpClient();
		GetMethod getMethod = new GetMethod(url);
		try {
			
			String paramStr = getDtoParam(paramDto, requestEncoding);
			if (url.indexOf("?") > -1) {
				url += "&" + paramStr;
			} else {
				url += "?" + paramStr;
			}
			if(WebplusUtil.isNotEmpty(headerDto)){
				for(String key:headerDto.keySet()){
					String value=headerDto.getString(key);
					getMethod.setRequestHeader(key,value);
				}
			}
			int statusCode = client.executeMethod(getMethod);
			if (statusCode == 200){
				result = getMethod.getResponseBodyAsString();
			}
			
		} catch (Exception e) {
			logger.error(e);
		} finally {
			getMethod.releaseConnection();
		}
		return result;
	}
	/**
	 * 将参数转换成string
	 * @param paramDto
	 * @param requestEncoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String getDtoParam(Dto paramDto,String requestEncoding) throws IOException{
		StringBuffer params = new StringBuffer();
		// 设置边界
		for (String key:paramDto.keySet()) {
			String value=paramDto.getString(key);
			params.append(key);
			params.append("=");
			params.append(URLEncoder.encode(value,requestEncoding));
			params.append("&");
		}

		if (params.length() > 0) {
			params = params.deleteCharAt(params.length() - 1);
		}
		
		return params.toString();
	}
	

	/**
	 * @return 连接超时(毫秒)
	 * @see com.WebplusHttp.common.web.HttpRequestProxy#connectTimeOut
	 */
	public static int getConnectTimeOut() {
		return WebplusHttp.connectTimeOut;
	}

	/**
	 * @return 读取数据超时(毫秒)
	 * @see com.WebplusHttp.common.web.HttpRequestProxy#readTimeOut
	 */
	public static int getReadTimeOut() {
		return WebplusHttp.readTimeOut;
	}

	/**
	 * @return 请求编码
	 * @see com.WebplusHttp.common.web.HttpRequestProxy#requestEncoding
	 */
	public static String getRequestEncoding() {
		return requestEncoding;
	}

	/**
	 * @param connectTimeOut
	 *            连接超时(毫秒)
	 * @see com.WebplusHttp.common.web.HttpRequestProxy#connectTimeOut
	 */
	public static void setConnectTimeOut(int connectTimeOut) {
		WebplusHttp.connectTimeOut = connectTimeOut;
	}

	/**
	 * @param readTimeOut
	 *            读取数据超时(毫秒)
	 * @see com.WebplusHttp.common.web.HttpRequestProxy#readTimeOut
	 */
	public static void setReadTimeOut(int readTimeOut) {
		WebplusHttp.readTimeOut = readTimeOut;
	}

	/**
	 * @param requestEncoding
	 *            请求编码
	 * @see com.WebplusHttp.common.web.HttpRequestProxy#requestEncoding
	 */
	public static void setRequestEncoding(String requestEncoding) {
		WebplusHttp.requestEncoding = requestEncoding;
	}

}
