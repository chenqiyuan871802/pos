package com.ims.core.exception;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ims.core.constant.WebplusCons;
import com.ims.core.vo.R;



/**
 * 异常处理器
 * 
 */
@ControllerAdvice
@ResponseBody
public class WebplusExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 自定义异常
	 */
	@ExceptionHandler(WebplusException.class)
	public R handleBDException(WebplusException e) {
		R r = new R();
		r.put(WebplusCons.APPCODE_KEY, e.getAppcode());
		r.put(WebplusCons.APPCODE_KEY, e.getMessage());

		return r;
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public R handleDuplicateKeyException(DuplicateKeyException e) {
		logger.error(e.getMessage(), e);
		return R.error("SQL記録あります");
	}

	@ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
	public R noHandlerFoundException(org.springframework.web.servlet.NoHandlerFoundException e) {
		logger.error(e.getMessage(), e);
		return R.error("ページが見つかりません");
	}
	
	@ExceptionHandler(AuthorizationException.class)
	public R handleAuthorizationException(AuthorizationException e) {
		logger.error(e.getMessage(), e);
		return R.error("権限必要です");
	}
	/**
	 * 
	 * 简要说明：token超时
	 * 编写者：陈骑元
	 * 创建时间：2019年5月12日 下午6:27:24
	 * @param 说明
	 * @return 说明
	 */
	@ExceptionHandler(AuthenticationException.class)
	public R handleAuthenticationException(AuthenticationException e) {
		logger.error(e.getMessage(), e);
		return R.error("権限必要です");
	}
	/**
	 * 
	 * 简要说明：未授权异常
	 * 编写者：陈骑元
	 * 创建时间：2019年5月12日 下午3:30:43
	 * @param 说明
	 * @return 说明
	 */
	@ExceptionHandler(UnauthorizedException.class)
	public R handleUnauthorizedException(UnauthorizedException e) {
		logger.error(e.getMessage(), e);
		return R.error("権限エラ");
	}

	@ExceptionHandler(Exception.class)
	public R handleException(Exception e) {
		logger.error(e.getMessage(), e);
		return R.error("サーバーエラ");
	}
}
