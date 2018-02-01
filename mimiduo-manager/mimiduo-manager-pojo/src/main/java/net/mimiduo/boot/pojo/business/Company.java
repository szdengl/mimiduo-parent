package net.mimiduo.boot.pojo.business;



import net.mimiduo.boot.pojo.common.IdEntity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Entity
@Table(name = Company.TABLE_NAME)
@Cacheable
public class Company extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2223430909597146505L;

	protected static final String TABLE_NAME = "t_company";

	@Column(length = 20, nullable = false, unique = true)
	@NotNull
	@Size(max = 20, min = 1)
	private String name;

	private String address;

	private String email;

	private String phone;

	private String setting;

	@Column(name = "isstatus", nullable = false, length = 2)
	private int isstatus;

	public Company() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIsstatus() {
		return isstatus;
	}

	public void setIsstatus(int isstatus) {
		this.isstatus = isstatus;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSetting() {
		return setting;
	}

	public void setSetting(String setting) {
		this.setting = setting;
	}

}
