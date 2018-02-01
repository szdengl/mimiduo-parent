package net.mimiduo.boot.web.action.admin;

import java.util.Date;


import net.mimiduo.boot.common.annotation.ActionMethod;
import net.mimiduo.boot.common.annotation.LogIgnore;
import net.mimiduo.boot.common.cache.event.Login;
import net.mimiduo.boot.common.exception.ExecuteException;
import net.mimiduo.boot.service.admin.DbRealm;
import net.mimiduo.boot.service.admin.UserService;
import net.mimiduo.boot.service.impl.admin.ShiroDbRealm;
import net.mimiduo.boot.web.action.CommonController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping(value = "/passport")
public class PassportController extends CommonController {

	private static final String SHIRO_LOGIN_FAILURE_KEY = "shiroLoginFailure";
	@Autowired
	private ApplicationContext ctx;

	@ActionMethod(serviceName = "转向登录页面")
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return autoView("passport/login");
	}

	@LogIgnore
	@ActionMethod(serviceName = "登录处理")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String doLogin(@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam(value = "rememberMe", required = false, defaultValue = "false") boolean rememberMe,
			ModelMap model) {
		Realm realm = ctx.getBean(Realm.class);
		if (!(realm instanceof DbRealm)) {
			throw new ExecuteException("认证机制不再是：" + DbRealm.class);
		}
		DbRealm dbRealm = (DbRealm) realm;
		DbRealm primitive = dbRealm.getAuthorizingRealm();
		try {
			Subject currentUser = SecurityUtils.getSubject();
			if (!currentUser.isAuthenticated()) {
				if (!(primitive instanceof ShiroDbRealm)) {
					ShiroDbRealm shiroDbRealm = new ShiroDbRealm(ctx.getBean(UserService.class));
					dbRealm.setAuthorizingRealm(shiroDbRealm);
					primitive = null;
				}
				UsernamePasswordToken token = new UsernamePasswordToken(username, password);
				if (rememberMe) {
					token.setRememberMe(true);
				}
				boolean ok = false;
				try {
					currentUser.login(token);
					ok = true;
					this.publishEvent(Login.TOPIC, new Login(username, new Date()));
				} catch (UnknownAccountException uae) {
					logger.warn("UnknownAccountException", uae);
					model.put(SHIRO_LOGIN_FAILURE_KEY, "您输入的用户名不存在");
				} catch (IncorrectCredentialsException ice) {
					logger.warn("IncorrectCredentialsException", ice);
					model.put(SHIRO_LOGIN_FAILURE_KEY, "您输入的用户名或密码不对。是否忘记密码了？");
				} catch (LockedAccountException lae) {
					logger.warn("LockedAccountException", lae);
					model.put(SHIRO_LOGIN_FAILURE_KEY, lae.getMessage() + "。有任何疑问请联系系统客服！");
				} catch (Exception e) {
					logger.warn("Exception", e);
					model.put(SHIRO_LOGIN_FAILURE_KEY, "系统当前无法处理您的登录请求，请重试。有任何疑问请联系系统客服！");
				}
				if (!ok) {
					return login();
				}
			}
			final SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(this.getRequest());
			if (savedRequest != null) {
				boolean noPollingURL = !savedRequest.getRequestUrl().contains("/_polling_");
				if (noPollingURL) {
					return "redirect:/admin";
				}
			}
			return "redirect:/";
		} finally {
			if (primitive != null) {
				dbRealm.setAuthorizingRealm(primitive);
			}
		}
	}

	@RequestMapping(value = "/unauthorized", method = RequestMethod.GET)
	public String unauthorized() {
		return autoView("passport/unauthorized");
	}

	@RequestMapping(value = "/dispatch")
	public String dispatch() {
		return "redirect:/";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register() {
		return autoView("passport/register");
	}
}
