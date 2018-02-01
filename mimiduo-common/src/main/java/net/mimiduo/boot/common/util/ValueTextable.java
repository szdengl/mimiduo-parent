package net.mimiduo.boot.common.util;

public interface ValueTextable<T> {

    T getValue();

    String getText();

    String getDescription();

    boolean isSelected();
}
