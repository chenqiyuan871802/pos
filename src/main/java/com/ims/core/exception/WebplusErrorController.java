package com.ims.core.exception;


import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ims.core.vo.R;

@Controller
public class WebplusErrorController implements ErrorController {
	private static final String ERROR_PATH = "/error";

	@RequestMapping(value = ERROR_PATH)
	@ResponseBody
	public R handleError() {
		
		return R.error("リクエストエラ");
	}

	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return ERROR_PATH;
	}

}