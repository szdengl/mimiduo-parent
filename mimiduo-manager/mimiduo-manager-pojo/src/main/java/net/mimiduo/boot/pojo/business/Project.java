package net.mimiduo.boot.pojo.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.mimiduo.boot.pojo.common.IdEntity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = Project.TABLE_NAME)
@Cacheable
public class Project extends IdEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 969370568613702496L;

	protected static final String TABLE_NAME = "t_project";

	private String clientId;

	@Column(length = 2, nullable = false, unique = true)
	@NotNull
	@Size(max = 30, min = 2)
	private String code;

	@Column(length = 20, nullable = false)
	@NotNull
	@Size(max = 20, min = 1)
	private String name;

	private String batch;

	@Column(name = "separate", nullable = false, precision = 5, scale = 4)
	private Float separate;

	private long limitCost;

	@Column(name = "isstatus", nullable = false, length = 2)
	private String isstatus;

	@Temporal(TemporalType.TIMESTAMP)
	private Date failureDate;

	private String limitMonth;

	public Project() {
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
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

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public long getLimitCost() {
		return limitCost;
	}

	public void setLimitCost(long limitCost) {
		this.limitCost = limitCost;
	}

	public String getIsstatus() {
		return isstatus;
	}

	public void setIsstatus(String isstatus) {
		this.isstatus = isstatus;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	public Date getFailureDate() {
		return failureDate;
	}

	public void setFailureDate(Date failureDate) {
		this.failureDate = failureDate;
	}

	public Float getSeparate() {
		return separate * 100;
	}

	public void setSeparate(Float separate) {
		this.separate = (float) (separate * 0.01);
	}

	public String getLimitMonth() {
		return limitMonth;
	}

	public void setLimitMonth(String limitMonth) {
		this.limitMonth = limitMonth;
	}

}
