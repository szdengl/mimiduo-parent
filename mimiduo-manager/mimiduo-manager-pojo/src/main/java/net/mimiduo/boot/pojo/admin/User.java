package net.mimiduo.boot.pojo.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;;
import net.mimiduo.boot.common.domain.UserStatus;
import net.mimiduo.boot.pojo.SessionUser;
import net.mimiduo.boot.pojo.common.IdEntity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 用户.
 * 
 */
@Entity
@Table(name = User.TABLE_NAME)
public class User extends IdEntity implements SessionUser {

	public static final String TABLE_NAME = "t_user";

	private static final long serialVersionUID = -1374885727020836249L;

	// 登录名.
	@Column(length = 50, unique = true)
	@NotBlank
	@Size(max = 50, min = 2)
	private String loginName;

	// 姓名.
	@Column(length = 200)
	@Size(max = 200)
	@NotBlank
	private String name;

	// 原始密码.
	// (不保存到数据库)
	@Transient
	private String plainPassword;

	// Hash后的密码.
	private String password;

	// Hash用的salt值.
	private String salt;

	// 注册时间.
	@Temporal(TemporalType.TIMESTAMP)
	private Date registerDate;

	// 状态.
	// @see UserStatus
	private Long status;

	// 性别.
	// 男 | 女
	@Column(length = 20)
	@Size(max = 20)
	private String gender;

	// 最近一次登录时间.
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLoginTime;

	@Column
	@NotNull
	private int isDeleted;

	public User() {
	}

	@Override
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// 不持久化到数据库，也不显示在Restful接口的属性.
	@JsonIgnore
	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JsonIgnore
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	// 设定JSON序列化时的日期格式
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public boolean isActive() {
		return Objects.equal(this.status, UserStatus.active.getValue());
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
