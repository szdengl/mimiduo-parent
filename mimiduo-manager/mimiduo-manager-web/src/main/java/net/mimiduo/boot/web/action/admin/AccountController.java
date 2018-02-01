package net.mimiduo.boot.web.action.admin;

import java.util.Date;


import net.mimiduo.boot.common.domain.LogRscStatus;
import net.mimiduo.boot.common.domain.OperTypeStatus;
import net.mimiduo.boot.common.util.ResponseResult;
import net.mimiduo.boot.pojo.admin.OperationLog;
import net.mimiduo.boot.service.admin.OperationLogService;
import net.mimiduo.boot.web.action.CommonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Objects;
import com.google.common.base.Strings;


@Controller
@RequestMapping("/admin/account")
public class AccountController extends CommonController {

	@Autowired
	private OperationLogService operationLogService;

	@RequestMapping("/security")
	public String security() {
		return autoView("admin/account/security");
	}

	@RequestMapping(value = "/security/change_password", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult doChangePassword(@RequestParam("currentPassword") String currentPassword,
										   @RequestParam("newPassword") String newPassword, @RequestParam("newPassword") String comfirmPassword) {
		if (!this.getUserService().isCurrentUserPassword(currentPassword)) {
			return failureResult("当前密码输入有误");
		}
		if (!Objects.equal(newPassword, comfirmPassword)) {
			return failureResult("新密码输入不匹配");
		}
		if (Strings.isNullOrEmpty(newPassword) || newPassword.length() < 6 || newPassword.length() > 30) {
			return failureResult("新密码格式输入有误");
		}
		String operation = "用户：" + currentUser().getName() + "(ID:" + currentUser().getId() + ")执行修改密码操作！";
		OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(),
				OperTypeStatus.UPDATE.getText(), operation, LogRscStatus.COMMON.getValue());
		try {
			this.getUserService().changeUserPassword(this.forceCurrentUser().getId(), newPassword);
			operationLogService.save(openLog);
			return this.successResult();
		} catch (Exception e) {
			logger.error("doChangePassword", e);
			return this.exceptionAsResult(e);
		}
	}
}
