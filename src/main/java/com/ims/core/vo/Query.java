package com.ims.core.vo;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.constant.WebplusCons;

/**
 * 
 * 类名:com.ims.common.util.Query
 * 描述:
 * 编写者:陈骑元
 * 创建时间:2018年4月9日 下午10:59:00
 * 修改说明:
 */
public class Query<T> extends Page<T> {
    /**
	 * 分页查询条件
	 */
	private static final long serialVersionUID = 1L;
	
    public Query(Map<String,Object> params) {
        super(Integer.parseInt(params.getOrDefault(WebplusCons.PAGE, WebplusCons.DEFAULT_CURRENTPAGE).toString())
                , Integer.parseInt(params.getOrDefault(WebplusCons.LIMIT, WebplusCons.DEFAULT_PAGESIZE).toString()));

        params.remove(WebplusCons.PAGE);
        params.remove(WebplusCons.LIMIT);
      
    }
    
   
}
