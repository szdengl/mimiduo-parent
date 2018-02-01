package net.mimiduo.boot.common.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import net.mimiduo.boot.common.util.ValueTextable;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ActionStatus implements ValueTextable<Long> {

    active(0L, "激活"), disabled(1L, "禁用"),test(2L,"测试");

    ActionStatus(Long value, String text) {
        this.value = value;
        this.text = text;
    }

    private Long value;
    private String text;
    private boolean selected;
    private String description;

    @Override
    public Long getValue() {
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
}
