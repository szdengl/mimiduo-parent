package net.mimiduo.boot.service.impl.admin;


import net.mimiduo.boot.pojo.admin.User;
import net.mimiduo.boot.service.admin.UserService;
import org.springframework.data.domain.AuditorAware;
import org.springframework.transaction.annotation.Transactional;

public class AuditorAwareImpl implements AuditorAware<User> {

	private UserService userService;

	public AuditorAwareImpl(UserService userService) {
		this.userService = userService;
	}

	@Override
	@Transactional(readOnly = true)
	public User getCurrentAuditor() {
		return userService.getCurrentUser();
	}
}
