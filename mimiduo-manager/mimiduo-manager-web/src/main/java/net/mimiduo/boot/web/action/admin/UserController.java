package net.mimiduo.boot.web.action.admin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import net.mimiduo.boot.common.domain.LogRscStatus;
import net.mimiduo.boot.common.domain.OperTypeStatus;
import net.mimiduo.boot.common.domain.PrivilegeTypeStatus;
import net.mimiduo.boot.common.domain.UserStatus;
import net.mimiduo.boot.common.util.ResponseResult;
import net.mimiduo.boot.common.util.TreeNode;
import net.mimiduo.boot.common.util.ValueTextable;
import net.mimiduo.boot.common.util.ValueTexts;
import net.mimiduo.boot.pojo.admin.OperationLog;
import net.mimiduo.boot.pojo.admin.Privilege;
import net.mimiduo.boot.pojo.admin.Role;
import net.mimiduo.boot.pojo.admin.User;
import net.mimiduo.boot.service.admin.OperationLogService;
import net.mimiduo.boot.service.admin.UserService;
import net.mimiduo.boot.util.EasyUIPage;
import net.mimiduo.boot.util.ErrorValidate;
import net.mimiduo.boot.web.action.CommonController;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.JsonMapper;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


@Controller
@RequestMapping("/admin/users")
public class UserController extends CommonController {

	@Autowired
	private Mapper mapper;

	@Autowired(required = true)
	private UserService userService;

	@Autowired
	private OperationLogService operationLogService;

	@RequestMapping("")
	public String index(ModelMap model) {
		model.put("statusJSON", JsonMapper.nonEmptyMapper().toJson(userStatusAsMap()));
		model.put("typesJSON", JsonMapper.nonEmptyMapper().toJson(PrivilegeTypeStatus.asMap()));
		return autoView("admin/users/index");
	}

	private Map<Long, String> userStatusAsMap() {
		return ValueTexts.asMap(Arrays.asList(UserStatus.values()));
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult saveUser(@RequestParam(value = "orgId", required = false) Long orgId, @Valid User user,
								   BindingResult result) {
		if (result.hasErrors()) {
			String message = ErrorValidate.convertErrorMessage(result);
			return ResponseResult.failure(message).message(result.toString());
		}
		try {
			String operation = "用户：" + currentUser().getName() + "(ID:" + currentUser().getId() + ")执行新增用户操作！";
			OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(),
					OperTypeStatus.NEW.getText(), operation, LogRscStatus.COMMON.getValue());

			prepareUser(user);

			if (orgId != null && orgId != 0) {
				userService.registerUserAndAddToOrganization(user, orgId);
			} else {
				userService.registerUser(user);
			}
			operationLogService.save(openLog);
			return ResponseResult.success();
		} catch (Exception e) {
			logger.error("saveUser", e);
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				return ResponseResult.failure("保存出错， 确认是否填入已经存在的登录名、邮箱或手机号?");
			}
			return exceptionAsResult(e);
		}
	}

	private void prepareUser(User user) {
		// if (Objects.equal(user.getEmail(), "")) {
		// user.setEmail(null);
		// }
		//
		// if (Objects.equal(user.getMobilePhone(), "")) {
		// user.setMobilePhone(null);
		// }
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult updateUser(@RequestParam("id") Long id, @RequestParam(value = "orgId") Long orgId,
			@Valid User user, BindingResult result) {
		if (result.hasErrors()) {
			String message = ErrorValidate.convertErrorMessage(result);
			return ResponseResult.failure(message).message(result.toString());
		}

		User entity = userService.findUserById(id);
		if (entity == null) {
			return ResponseResult.failure("用户不存在");
		}

		try {
			String operation = "用户：" + currentUser().getName() + "(ID:" + currentUser().getId() + ")执行修改用户操作！";
			OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(),
					OperTypeStatus.UPDATE.getText(), operation, LogRscStatus.COMMON.getValue());

			prepareUser(user);
			mapper.map(user, entity);
			// entity.setEmail(user.getEmail());
			// entity.setGender(user.getGender());
			// entity.setLoginName(user.getLoginName());
			// entity.setMobilePhone(user.getMobilePhone());
			// entity.setName(user.getName());
			// entity.setRemark(user.getRemark());
			// entity.setPosition(user.getPosition());
			// entity.setBirthday(user.getBirthday());
			// entity.setStatus(user.getStatus());
			boolean shouldChangeOrg = !Objects.equal(userService.getUserOrganizationId(entity.getId()), orgId);
			if (shouldChangeOrg) {
				userService.updateUserAndChangeOrganization(entity, orgId);
			} else {
				userService.updateUser(entity);
			}
			operationLogService.save(openLog);
			return ResponseResult.success();
		} catch (Exception e) {
			logger.error("updateUser", e);
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				return ResponseResult.failure("保存出错， 确认是否填入已经存在的登录名、邮箱或手机号?");
			}
			return ResponseResult.failure("更新有误").message(result.toString());
		}
	}

	@RequestMapping(value = "/change_status", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult changeUserStatus(@RequestParam(value = "id", required = true) Long id) {
		if (userService.isSupervisor(id)) {
			return ResponseResult.failure("不能修改超级管理员的状态");
		}

		try {
			userService.changeUserStatus(id);
			return ResponseResult.success();
		} catch (Exception e) {
			logger.error("changeUserStatus", e);
			return this.exceptionAsResult(e);
		}
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult deleteUser(@RequestParam("id") Long id) {
		User user = userService.findUserById(id);
		if (user == null) {
			return ResponseResult.failure("用户不存在");
		}

		if (userService.isSupervisor(id)) {
			return ResponseResult.failure("不能删除超级管理员");
		}

		try {
			String operation = "用户：" + currentUser().getName() + "(ID:" + currentUser().getId() + ")执行修改用户操作！";
			OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(),
					OperTypeStatus.DELETE.getText(), operation, LogRscStatus.COMMON.getValue());

			userService.deleteUser(id);
			operationLogService.save(openLog);
			return ResponseResult.success();
		} catch (Exception e) {
			logger.error("deleteUser", e);
			return ResponseResult.failure("删除有误").message(e.toString());
		}
	}

	@RequestMapping(value = "/roles/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult deleteUserRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId) {
		try {
			String operation = "用户：" + currentUser().getName() + "(ID:" + currentUser().getId() + ")执行修改用户角色操作！";
			OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(),
					OperTypeStatus.UPDATE.getText(), operation, LogRscStatus.COMMON.getValue());

			userService.removeRoleFromUser(userId, roleId);
			operationLogService.save(openLog);
			return ResponseResult.success();
		} catch (Exception e) {
			logger.error("deleteUser", e);
			return ResponseResult.failure("移除有误").message(e.toString());
		}
	}

	@RequestMapping(value = "/roles", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult addUserRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId) {
		try {
			String operation = "用户：" + currentUser().getName() + "(ID:" + currentUser().getId() + ")执行添加用户角色操作！";
			OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(),
					OperTypeStatus.UPDATE.getText(), operation, LogRscStatus.COMMON.getValue());

			userService.addRoleToUser(userId, roleId);
			operationLogService.save(openLog);
			return ResponseResult.success();
		} catch (Exception e) {
			logger.error("deleteUser", e);
			return ResponseResult.failure("添加有误").message(e.toString());
		}
	}

	@RequestMapping(value = "/change_password", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult changePassword(@RequestParam("id") Long id,
			@RequestParam(value = "newPassword") String newPassword) {
		if (Strings.isNullOrEmpty(newPassword)) {
			return ResponseResult.failure("输入有误");
		}

		if (userService.isSupervisor(id) && !userService.isSupervisor(this.forceCurrentUser().getId())) {
			return ResponseResult.failure("只有超级管理员能修改自己的密码");
		}

		try {
			userService.changeUserPassword(id, newPassword);
			return ResponseResult.success();
		} catch (Exception e) {
			logger.error("changePassword", e);
			return this.exceptionAsResult(e);
		}
	}

	@RestController
	@RequestMapping("/admin/users/data")
	static class UserDataController extends CommonController {

		@Autowired
		private UserService userService;

		@RequestMapping("/users.json")
		public EasyUIPage<User> users(@RequestParam(value = "orgId", required = false) Long orgId) {
			final PageRequest pageable = this.getPageRequestFromEasyUIDatagridWithInitSort(this.getIdAscSort());
			Page<User> data;
			if (orgId == null) {
				data = userService.findAllUsers(this.getSearchFilters(), pageable);
			} else if (orgId == 0) {
				data = userService.findAllUsersNotInOrganizations(pageable);
			} else {
				data = userService.findUsersByOrganization(orgId, pageable);
			}

			return toEasyUIPage(data);
		}

		@RequestMapping("/user/roles.json")
		public Iterable<Role> userRoles(@RequestParam("id") Long id) {
			return userService.getUserRoles(id);
		}

		@RequestMapping("/status.json")
		public List<? extends ValueTextable<Long>> userStatus() {
			return Lists.newArrayList(UserStatus.values());
		}

		@RequestMapping("/roles_for_select.json")
		public EasyUIPage<Role> rolesForSelect() {
			Page<Role> data = userService.findAllRoles(this.getSearchFilters(),
					this.getPageRequestFromEasyUIDatagridWithInitSort(this.getIdAscSort()));

			return toEasyUIPage(data);
		}

		@RequestMapping("/user/privileges.json")
		public List<Privilege> userAllPrivileges(@RequestParam(value = "id", required = false) Long id) {
			if (id == null) {
				return ImmutableList.of();
			}
			return userService.getUserAllPrivileges(userService.findUserById(id));
		}

		@RequestMapping("/user/all_privileges.json")
		public Collection<TreeNode> userRoleAllPrivileges(@RequestParam("id") Long id) {
			Map<Long, TreeNode> result = Maps.newLinkedHashMap();
			for (Role role : userService.getUserRoles(id)) {
				getRolePrivilegesTree(role, result);
			}
			return result.values();
		}

		@RequestMapping("/user/org.json")
		public ResponseResult userOrg(@RequestParam("id") Long id) {
			return ResponseResult.success().data(this.userService.getUserOrganization(id));
		}

		private Map<Long, TreeNode> getRolePrivilegesTree(Role role, Map<Long, TreeNode> result) {
			getRolePrivilegesTreeRec(role, result);
			return result;
		}

		private void getRolePrivilegesTreeRec(Role role, Map<Long, TreeNode> result) {
			if (role == null) {
				return;
			}

			TreeNode tn = new TreeNode(role.getId(), role.getName());
			tn.setIconCls("icon-window");
			for (Privilege p : role.getPrivileges()) {
				TreeNode ptn = new TreeNode(p.getId(), p.getName());
				ptn.setIconCls("icon-gears");
				tn.getChildren().add(ptn);
			}
			result.put(tn.getId(), tn);
			getRolePrivilegesTreeRec(role.getParent(), result);
		}
	}
}
