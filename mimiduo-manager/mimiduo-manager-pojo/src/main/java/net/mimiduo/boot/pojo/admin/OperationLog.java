package net.mimiduo.boot.pojo.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.mimiduo.boot.common.domain.LogRscStatus;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户操作日志.
 */
@Entity
@Table(name = OperationLog.TABLE_NAME)
public class OperationLog implements Serializable {
    private static final long serialVersionUID = -8186028012795265272L;
    public static final String TABLE_NAME = "t_oper_log";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    private String userName;

    private String userIp;

    private Date operTime;

    private String operType;

    private String operation;

    private int logRsc;

    @Transient
    private String logRscDisplay;

    public OperationLog() {
    }

    public OperationLog(Long userId, String userName, String userIp, Date operTime, String operType,
                        String operation, int logRsc) {
        this.userId = userId;
        this.userName = userName;
        this.userIp = userIp;
        this.operTime = operTime;
        this.operType = operType;
        this.operation = operation;
        this.logRsc = logRsc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    // 设定JSON序列化时的日期格式
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getLogRsc() {
        return logRsc;
    }

    public void setLogRsc(int logRsc) {
        this.logRsc = logRsc;
    }

    public String getLogRscDisplay() {
        if (logRsc == LogRscStatus.COMMON.getValue()) {
            return LogRscStatus.COMMON.getText();
        } else {
            return LogRscStatus.USER.getText();
        }
    }

    public void setLogRscDisplay(String logRscDisplay) {
        this.logRscDisplay = logRscDisplay;
    }

}
