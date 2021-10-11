package com.ims.core.exception;

/**
 * 自定义异常

 */
public class WebplusException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String appmsg;
    private int appcode = 500;
    
    public WebplusException(String appmsg) {
		super(appmsg);
		this.appmsg = appmsg;
	}
	
	public WebplusException(String appmsg, Throwable e) {
		super(appmsg, e);
		this.appmsg = appmsg;
	}
	
	public WebplusException(String appmsg, int appcode) {
		super(appmsg);
		this.appmsg = appmsg;
		this.appcode = appcode;
	}
	
	public WebplusException(String msg, int code, Throwable e) {
		super(msg, e);
		this.appmsg = msg;
		this.appcode = code;
	}

	public String getAppmsg() {
		return appmsg;
	}

	public void setAppmsg(String appmsg) {
		this.appmsg = appmsg;
	}

	public int getAppcode() {
		return appcode;
	}

	public void setAppcode(int appcode) {
		this.appcode = appcode;
	}

	
	
	
}
