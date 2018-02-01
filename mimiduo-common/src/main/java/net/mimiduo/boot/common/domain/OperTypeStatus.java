package net.mimiduo.boot.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 * @Title: OperationLogEnum.java
 * @Description: 操作类型
 * @version
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OperTypeStatus {

    NEW("新增"), DELETE("删除"), UPDATE("修改");

    OperTypeStatus(String text) {
        this.text = text;
    }

    private String text;

    public String getText() {
        return text;
    }
}