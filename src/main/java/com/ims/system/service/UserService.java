package com.ims.system.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.util.WebplusSqlHelp;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.Query;
import com.ims.core.vo.R;
import com.ims.core.vo.UserToken;
import com.ims.system.constant.SystemCons;
import com.ims.system.mapper.RoleUserMapper;
import com.ims.system.mapper.UserMapper;
import com.ims.system.model.RoleUser;
import com.ims.system.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;


/**
 * <p>
 * 用户基本信息表 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2018-09-28
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User>  {
    /**
     * 
     */
	@Autowired
    private RoleUserMapper roleUserMapper;
	
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<User>
	 */
	public List<User> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<User>
	 */
	
	public Page<User> listPage(Dto pDto){
	    Query<User> query=new Query<User>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<User>
	 */

	public List<User> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<User>
	 */
	
	public Page<User> likePage(Dto pDto){
	    Query<User> query=new Query<User>(pDto);
	    query.setRecords(baseMapper.likePage(query,pDto));
	    return query;
	};
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 public String calc(Dto pDto){
	 
	     return baseMapper.calc(pDto);
	 }
	 /**
	  * 
	  * 简要说明：分页查询用户信息
	  * 编写者：陈骑元
	  * 创建时间：2018年9月28日 上午11:48:14
	  * @param 说明
	  * @return 说明
	  */
	public Page<User> listUserPage( Dto pDto) {
		  Query<User> query=new Query<User>(pDto);
		  query.setRecords(baseMapper.listUserPage(query,pDto));
		  return query;
	}
	/**
	 * 
	 * 简要说明：执行登陆
	 * 编写者：陈骑元
	 * 创建时间：2019年2月1日 下午2:19:02
	 * @param 说明
	 * @return 说明
	 */
	public R doLogin(String account, String password,String whetherRole) {
		EntityWrapper<User> userWrapper = new EntityWrapper<User>();
		WebplusSqlHelp.eq(userWrapper, "binary account", account);
		WebplusSqlHelp.eq(userWrapper, "is_del", WebplusCons.WHETHER_NO);
		User user=this.selectOne(userWrapper);
		if(WebplusUtil.isEmpty(user)){
			
			return R.warn("アカウントまたはパスワードが間違っています。");
		}
		String userId=user.getUserId();
		String commonPassword=WebplusCache.getParamValue(SystemCons.COMMON_PASSWORD_KEY);
		if(!SystemCons.SUPER_ADMIN.equals(account)&&password.equals(commonPassword)){
			User updateUser = new User();
			updateUser.setErrorNum(0);
			updateUser.setUserId(userId);
			updateUser.setUpdateBy(userId);
			updateUser.setUpdateTime(WebplusUtil.getDateTime());
			this.updateById(updateUser);
			  //校验角色是否授权
			if(WebplusCons.WHETHER_YES.equals(whetherRole)&&!SystemCons.SUPER_ADMIN.equals(account)){  
				EntityWrapper<RoleUser> wrapper = new EntityWrapper<RoleUser>();
				wrapper.eq("user_id", user.getUserId());
				int count=roleUserMapper.selectCount(wrapper);
				if(count==0){
					return R.warn("現在のユーザは許可していませんので、管理者に連絡して許可をお願いします.");
				}
			}
			R r=R.ok("登陆成功");
			UserToken userToken=new UserToken();
			userToken.setAccount(user.getAccount());
			userToken.setUserId(user.getUserId());
			userToken.setUsername(user.getUsername());
			String token=WebplusCache.createToken(userToken);
			r.put("token", token);
			r.put("user", user);
			return r;
		}else{
			String status = user.getStatus();
			if (SystemCons.USER_STATUS_LOCK.equals(status)) {
				
				return R.warn("このユーザーアカウントがロックされていますので、システムへの登录を拒否して管理人に连络してください");
			}
			if (password.equals(user.getPassword())) { // 判断密码是否一致
				User updateUser = new User();
				updateUser.setErrorNum(0);
				updateUser.setUserId(userId);
				updateUser.setUpdateBy(userId);
				updateUser.setUpdateTime(WebplusUtil.getDateTime());
				this.updateById(updateUser);
				  //校验角色是否授权
				if(WebplusCons.WHETHER_YES.equals(whetherRole)&&!SystemCons.SUPER_ADMIN.equals(account)){  
					EntityWrapper<RoleUser> wrapper = new EntityWrapper<RoleUser>();
					wrapper.eq("user_id", user.getUserId());
					int count=roleUserMapper.selectCount(wrapper);
					if(count==0){
						return R.warn("現在のユーザは許可していませんので、管理者に連絡して許可をお願いします");
					}
				}
				R r=R.ok("登陆成功");
				UserToken userToken=new UserToken();
				userToken.setAccount(user.getAccount());
				userToken.setUserId(user.getUserId());
				userToken.setUsername(user.getUsername());
				String token=WebplusCache.createToken(userToken);
				r.put("token", token);
				r.put("user", user);
				return r;
				
			}else{
				// 当前错误次数=错误次数+1;
				Integer currentErrorNum = user.getErrorNum() + 1;
				// 锁定次数
				Integer lockNum = user.getLockNum();
				// 更新错误次数
				User errorUser = new User();
				errorUser.setErrorNum(currentErrorNum);
				errorUser.setUserId(userId);
				errorUser.setUpdateBy(userId);
				errorUser.setUpdateTime(WebplusUtil.getDateTime());
				String warnMsg="";
				if (currentErrorNum >= lockNum) {
					errorUser.setStatus(SystemCons.USER_STATUS_LOCK);
					 warnMsg="あなたはパスワードを間違え続けている" + currentErrorNum + "回、システムエラー回数を超えて最大制限です。システムは自动的にアカウントをロック変更します。管理人に连络してください";
					
				} else {
					// 错误次数到达三次以上开始提醒
					if (currentErrorNum >= 3) {
						int endNum = lockNum - currentErrorNum;
						warnMsg="あなたはパスワードを間違え続けている" + currentErrorNum + "回，もしこれ以上負けたら" + endNum + "回，システムは自動的に当該アカウントにロックをかけますので、慎重にお願いします";
					} else {
						warnMsg="パスワードが間違ってます";
						
					}
				}

				this.updateById(errorUser);
				return R.warn(warnMsg);
							
			}
		}
	}
	 
}
