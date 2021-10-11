package com.ims.core.matatype;
/**
 * 
 * 类名:com.toonan.common.matatype.Tail
 * 描述:字典存储接口
 * 编写者:陈骑元
 * 创建时间:2018年12月17日 上午9:57:32
 * 修改说明:
 */
public interface Tail extends java.io.Serializable {
	/**
	 * 
	 * 简要说明：通过键获取值的接口
	 * 编写者：陈骑元
	 * 创建时间：2019年4月25日 下午2:37:12
	 * @param 说明
	 * @return 说明
	 */
	public Object get(String key);
	/**
	 * 
	 * 简要说明：通过键和值设置存储接口
	 * 编写者：陈骑元
	 * 创建时间：2019年4月25日 下午2:37:37
	 * @param 说明
	 * @return 说明
	 */
	public void set(String key,Object value);
}
