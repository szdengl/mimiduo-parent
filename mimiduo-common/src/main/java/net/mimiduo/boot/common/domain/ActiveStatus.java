package net.mimiduo.boot.common.domain;

/**
 * ${DESCRIPTION}
 *
 * @author:LingDeng
 * @create 2018-01-25 11:29
 **/
import static com.google.common.collect.Lists.newArrayList;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.mimiduo.boot.common.util.ValueTextable;
import net.mimiduo.boot.common.util.ValueTexts;

import java.util.Map;

/**
 * 数据记录是否有效状态
 *
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum  ActiveStatus implements ValueTextable<Integer> {

    UNACTIVE(0, "无效"), ACTIVE(1, "有效");

    ActiveStatus(Integer value, String text) {
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

    private static Map<Integer, String> map = ValueTexts.asMap(newArrayList(ActiveStatus.values()));

    public static Map<Integer, String> asMap() {
        return map;
    }

}
