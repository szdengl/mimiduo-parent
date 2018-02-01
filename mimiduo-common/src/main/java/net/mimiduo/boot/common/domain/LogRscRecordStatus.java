package net.mimiduo.boot.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.mimiduo.boot.common.util.ValueTextable;
import net.mimiduo.boot.common.util.ValueTexts;

import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 *
 * @Title: OperationLogEnum.java
 * @Description: 是否记录日志：NOTRECORD为不记录， RECORD为记录日志
 * @version
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum LogRscRecordStatus implements ValueTextable<Integer> {
    NOTRECORD(0, "不记录"), RECORD(1, "记录");

    LogRscRecordStatus(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    private Integer value;
    private String text;
    private boolean selected;
    private String description;

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    private static Map<Integer, String> map = ValueTexts.asMap(newArrayList(LogRscRecordStatus.values()));

    public static Map<Integer, String> asMap() {
        return map;
    }
}
