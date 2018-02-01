package net.mimiduo.boot.pojo.business;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.mimiduo.boot.pojo.common.IdEntity;
import org.springframework.data.annotation.CreatedDate;
import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name = Ivr.TABLE_NAME)
public class Ivr extends IdEntity implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = -8767941697205267730L;
	/**
	 * 
	 */

	protected static final String TABLE_NAME = "t_ivr";

	private long companyId;

	private String mobile;

	private String spnumber;
	
	private int diff;
	
	private String linkId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date starTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createtime;

	public Ivr() {
	}

	public Ivr(long companyId, String mobile, String spnumber, Date starTime, Date endTime) {
		setCompanyId(companyId);
		setMobile(mobile);
		setSpnumber(spnumber);
		setStarTime(starTime);
		setEndTime(endTime);
		setDiff((int)(endTime.getTime()-starTime.getTime())/60000);
	}

	public Ivr(long companyId, String mobile, String spnumber, int diff,String linkId) {
		setCompanyId(companyId);
		setMobile(mobile);
		setSpnumber(spnumber);
		setDiff(diff);
		setLinkId(linkId);
	}
	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSpnumber() {
		return spnumber;
	}

	public void setSpnumber(String spnumber) {
		this.spnumber = spnumber;
	}

	// 设定JSON序列化时的日期格式
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getStarTime() {
		return starTime;
	}

	public void setStarTime(Date starTime) {
		this.starTime = starTime;
	}

	// 设定JSON序列化时的日期格式
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	// 设定JSON序列化时的日期格式
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public int getDiff() {
		return diff;
	}

	public void setDiff(int diff) {
		this.diff = diff;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	
	

}
