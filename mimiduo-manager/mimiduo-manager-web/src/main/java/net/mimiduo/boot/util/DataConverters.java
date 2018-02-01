package net.mimiduo.boot.util;

import org.springframework.data.domain.Page;

public final class DataConverters {

	public static <T> EasyUIPage<T> pageToEasyUI(Page<T> page) {
		return new EasyUIPage<T>(page.getTotalElements(), page.getContent());
	}

	public static <T> BootstrapUIPage<T> pageToBootstrapUI(Page<T> page) {
		return new BootstrapUIPage<T>(page.getTotalElements(), page.getContent());
	}
}
