package net.mimiduo.boot.web.action;


import net.mimiduo.boot.pojo.admin.User;
import net.mimiduo.boot.service.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;



/**
 * 支持帐号相关功能的基类Controller.
 * 
 */
public abstract class CurrentUserableController extends EventSupportController {

	@Autowired
	private UserService userService;

	/**
	 * 获取当前会话用户.
	 */
	protected User currentUser() {
		return getUserService().getCurrentUser();
	}

	/**
	 * 获取当前登录用户(强制).
	 * <p>
	 * 如果当前用户未登录， 则抛出运行时异常.
	 */
	protected User forceCurrentUser() {
		User user = currentUser();
		if (user == null) {
			throw new RuntimeException("用户未登录");
		}
		return user;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
