package net.mimiduo.boot.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.mimiduo.boot.common.util.ValueTextable;
import net.mimiduo.boot.common.util.ValueTexts;

import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 *
 * @Title: OperationLogEnum.java
 * @Description: 日志来源
 * @version
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public  enum LogRscStatus implements ValueTextable<Integer> {

    COMMON(0, "公共平台"), USER(1, "用户");

    LogRscStatus(Integer value, String text) {
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

    private static Map<Integer, String> map = ValueTexts.asMap(newArrayList(LogRscStatus.values()));

    public static Map<Integer, String> asMap() {
        return map;
    }
}
