package net.mimiduo.boot.common.domain;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.mimiduo.boot.common.util.ValueTextable;
import net.mimiduo.boot.common.util.ValueTexts;


/**
 * 权限类型.
 * 
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PrivilegeTypeStatus implements ValueTextable<String> {

	Application("Application", "应用"), Business("Business", "业务"), Menu("Menu", "菜单");

	PrivilegeTypeStatus(String value, String text) {
		this.value = value;
		this.text = text;
	}

	private String value;
	private String text;
	private boolean selected;
	private String description;

	@Override
	public String getValue() {
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

	private static Map<String, String> map = ValueTexts.asMap(newArrayList(PrivilegeTypeStatus.values()));

	public static Map<String, String> asMap() {
		return map;
	}
}
