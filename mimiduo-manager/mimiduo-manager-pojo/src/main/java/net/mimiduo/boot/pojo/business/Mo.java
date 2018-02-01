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
@Table(name = Mo.TABLE_NAME)
public class Mo extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3457438864193540970L;

	protected static final String TABLE_NAME = "t_mo";

	private long companyId;

	private String mobile;

	private String spnumber;

	private String momsg;

	private String linkid;

	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date motime;

	private String report;

	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date retime;

	private String city;

	private String province;

	public Mo() {
	}

	public Mo(long companyId, String mobile, String spnumber, String momsg, String linkid, String report) {
		setCompanyId(companyId);
		setMobile(mobile);
		setSpnumber(spnumber);
		setMomsg(momsg);
		setLinkid(linkid);
		setReport(report);
		setMotime(new Date());
		setRetime(new Date());
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

	public String getMomsg() {
		return momsg;
	}

	public void setMomsg(String momsg) {
		this.momsg = momsg;
	}

	public String getLinkid() {
		return linkid;
	}

	public void setLinkid(String linkid) {
		this.linkid = linkid;
	}

	// 设定JSON序列化时的日期格式
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getMotime() {
		return motime;
	}

	public void setMotime(Date motime) {
		this.motime = motime;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	// 设定JSON序列化时的日期格式
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getRetime() {
		return retime;
	}

	public void setRetime(Date retime) {
		this.retime = retime;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

}
