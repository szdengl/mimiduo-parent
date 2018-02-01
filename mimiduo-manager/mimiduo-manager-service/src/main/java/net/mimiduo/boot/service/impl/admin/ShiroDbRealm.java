package net.mimiduo.boot.service.impl.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import net.mimiduo.boot.common.annotation.ShiroRealm;
import net.mimiduo.boot.pojo.admin.Privilege;
import net.mimiduo.boot.pojo.admin.Role;
import net.mimiduo.boot.pojo.admin.User;
import net.mimiduo.boot.service.admin.DbRealm;
import net.mimiduo.boot.service.admin.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.utils.Encodes;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;




public class ShiroDbRealm extends AuthorizingRealm implements DbRealm {

	private static Logger LOGGER = LoggerFactory.getLogger(ShiroDbRealm.class);
	
	private static String ADMIN = "admin";

	private UserService userService;

	@Autowired
	private BeanFactory beanFactory;

	private DbRealm realm;
	
	public ShiroDbRealm(UserService userService) {
		this.userService = userService;
		this.realm = this;
	}
	
	

	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		return realm.authenticationInfo(authcToken);
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		// return realm.authorizationInfo(principals);
		// 填入当前用户的角色和权限信息
		Object target = principals.getPrimaryPrincipal();
		ShiroRealm realm = target.getClass().getAnnotation(ShiroRealm.class);
		if (realm != null) {
			DbRealm r = (DbRealm) beanFactory.getBean(realm.value());
			return r.authorizationInfo(principals);
		} else {
			final User user = (User) principals.getPrimaryPrincipal();
			final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			final Collection<String> roles = getRoleList(user);
			final Collection<String> perms = getPermissions(user);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("User: {}, roles: {}, perms:{}",
						user.getLoginName(), Arrays.toString(roles.toArray()),
						Arrays.toString(perms.toArray()));
			}
			info.addRoles(getRoleList(user));
			info.addStringPermissions(perms);
			return info;
		}
	}

	/**
	 * 设定Password校验的Hash算法与迭代次数.
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		LOGGER.info("initCredentialsMatcher");

		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(
				UserServiceImpl.HASH_ALGORITHM);
		matcher.setHashIterations(UserServiceImpl.HASH_INTERATIONS);

		setCredentialsMatcher(matcher);
	}

	public void setAccountService(UserService userService) {
		this.userService = userService;
	}

	private Collection<String> getRoleList(User user) {
		List<Role> roles = userService.getUserAllRoles(user);
		Function<Role, String> mapper = new Function<Role, String>() {
			@Override
			public String apply(Role input) {
				if (Strings.isNullOrEmpty(input.getRealm())) {
					return input.getName();
				}
				return input.getRealm() + ":" + input.getName();
			}
		};
		return Lists.newArrayList(Iterables.transform(roles, mapper));
	}

	private Collection<String> getPermissions(User user) {
		List<Privilege> privileges = new ArrayList<Privilege>();
		if(ADMIN.equals(user.getLoginName())){
			Privilege privilege = new Privilege();
			privilege.setTarget("*");
			privilege.setMethod("*");
			privilege.setScope("*");
			privileges.add(privilege);
		}else{
			privileges = userService.getUserAllPrivileges(user);
		}
		Function<Privilege, String> mapper = new Function<Privilege, String>() {
			@Override
			public String apply(Privilege input) {
				return String.format("%s:%s:%s",
						emptyOrElse(input.getTarget(), "*"),
						emptyOrElse(input.getMethod(), "*"),
						emptyOrElse(input.getScope(), "*"));
			}

			private String emptyOrElse(String t, String e) {
				return (Strings.isNullOrEmpty(t) ? e : t);
			}
		};
		
		// 参考：https://shiro.apache.org/permissions.html
		// http://runtime.blog.51cto.com/7711135/1288690
		return Lists.newArrayList(Iterables.transform(privileges, mapper));
	}

	@Override
	public void setAuthorizingRealm(DbRealm realm) {
		this.realm = realm;
	}

	@Override
	public AuthenticationInfo authenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		// 通过登录名找到用户对象
		final User user = userService.findUserByLoginID(token.getUsername());
		if (user != null && !user.isActive()) {
			throw new LockedAccountException("此用户已经被禁用！");
		}
		if (user != null && user.getIsDeleted() == 1) {
			throw new UnknownAccountException("用户不存在");
		}
		if (user != null && user.isActive()) {
			byte[] salt = Encodes.decodeHex(user.getSalt());
			return new SimpleAuthenticationInfo(user, user.getPassword(),
					ByteSource.Util.bytes(salt), getName());
		}

		return null;
	}

	@Override
	public AuthorizationInfo authorizationInfo(PrincipalCollection principals) {
		// 填入当前用户的角色和权限信息
		Object target = principals.getPrimaryPrincipal();
		ShiroRealm realm = target.getClass().getAnnotation(ShiroRealm.class);
		if (realm != null) {
			DbRealm r = (DbRealm) beanFactory.getBean(realm.value());
			return r.authorizationInfo(principals);
		} else {
			final User user = (User) principals.getPrimaryPrincipal();
			final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			final Collection<String> roles = getRoleList(user);
			final Collection<String> perms = getPermissions(user);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("User: {}, roles: {}, perms:{}",
						user.getLoginName(), Arrays.toString(roles.toArray()),
						Arrays.toString(perms.toArray()));
			}
			info.addRoles(getRoleList(user));
			info.addStringPermissions(perms);
			return info;
		}
	}

	@Override
	public DbRealm getAuthorizingRealm() {
		return this.realm;
	}
}
