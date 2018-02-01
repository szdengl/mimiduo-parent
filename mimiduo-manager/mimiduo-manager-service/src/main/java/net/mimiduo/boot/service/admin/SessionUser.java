package net.mimiduo.boot.service.admin;

import java.util.Date;

/**
 * 当前会话用户.
 * 
 */
public interface SessionUser {

    Long getId();

    String getLoginName();

    String getName();

    Date getLastLoginTime();
}
