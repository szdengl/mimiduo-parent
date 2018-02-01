package net.mimiduo.boot.service.admin;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * ${DESCRIPTION}
 *
 * @author:LingDeng
 * @create 2018-01-25 18:31
 **/
public interface DbRealm {
    /**
     * 设置认证对象要用到的认证方案
     * @param realm
     */
    void setAuthorizingRealm(DbRealm realm);
    /**
     * 获取当前认证方案
     * @return
     */
    DbRealm getAuthorizingRealm();
    /**
     * 认证回调函数,登录时调用.
     * @param authcToken
     * @return
     * @throws AuthenticationException
     */
    AuthenticationInfo authenticationInfo(AuthenticationToken authcToken) throws AuthenticationException;

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     *
     * @param principals
     * @return
     */
    AuthorizationInfo authorizationInfo(PrincipalCollection principals);
}
