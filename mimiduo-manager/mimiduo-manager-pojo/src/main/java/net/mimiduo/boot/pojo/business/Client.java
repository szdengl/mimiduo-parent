package net.mimiduo.boot.pojo.business;

import net.mimiduo.boot.pojo.common.IdEntity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.io.Serializable;

@Entity
@Table(name = Client.TABLE_NAME, uniqueConstraints = @UniqueConstraint(columnNames = { "busUserId", "code",
		"account" }) )
@Cacheable
public class Client extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1567135193592015401L;

	protected static final String TABLE_NAME = "t_client";

	@Column(length = 5, nullable = false, unique = true)
	@NotNull
	@Size(max = 30, min = 1)
	private String code;

	@Column(length = 20, nullable = false)
	@NotNull
	@Size(max = 20, min = 1)
	private String name;

	@Column(length = 20, nullable = false, unique = true)
	@NotNull
	@Size(max = 20, min = 1)
	private String account;

	@Column(length = 20, nullable = false)
	@NotNull
	@Size(max = 20, min = 6)
	private String password;

	@Column(name = "separate", nullable = false, precision = 5, scale = 4)
	private Float separate;

	@Column(name = "isstatus", nullable = false, length = 2)
	private String isstatus;

	private long busUserId; // 商务人员编号

	public Client() {
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Float getSeparate() {
		return separate * 100;
	}

	public void setSeparate(Float separate) {
		this.separate = (float) (separate * 0.01);
	}

	public String getIsstatus() {
		return isstatus;
	}

	public void setIsstatus(String isstatus) {
		this.isstatus = isstatus;
	}

	public long getBusUserId() {
		return busUserId;
	}

	public void setBusUserId(long busUserId) {
		this.busUserId = busUserId;
	}

}