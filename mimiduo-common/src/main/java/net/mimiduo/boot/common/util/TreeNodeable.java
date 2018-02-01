package net.mimiduo.boot.common.util;

import java.util.List;
import java.util.Map;

public interface TreeNodeable<T extends TreeNodeable<?>> {

    Long getId();

    String getText();

    T getParent();

    List<T> getChildren();

    Map<String, Object> getAttributes();

    String getIconCls();

    boolean isSelected();

    boolean isChecked();

    String getState();

    String getDescription();
}
