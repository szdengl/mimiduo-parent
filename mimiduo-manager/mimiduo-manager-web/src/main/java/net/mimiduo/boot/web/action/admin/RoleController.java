package net.mimiduo.boot.web.action.admin;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import net.mimiduo.boot.common.domain.LogRscStatus;
import net.mimiduo.boot.common.domain.OperTypeStatus;
import net.mimiduo.boot.common.domain.PrivilegeTypeStatus;
import net.mimiduo.boot.common.exception.ServiceException;
import net.mimiduo.boot.common.util.*;
import net.mimiduo.boot.pojo.admin.OperationLog;
import net.mimiduo.boot.pojo.admin.Privilege;
import net.mimiduo.boot.pojo.admin.Role;
import net.mimiduo.boot.service.admin.OperationLogService;
import net.mimiduo.boot.service.admin.UserService;
import net.mimiduo.boot.util.EasyUIPage;
import net.mimiduo.boot.util.ErrorValidate;
import net.mimiduo.boot.web.action.CommonController;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.JsonMapper;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;



@Controller
@RequestMapping("/admin/roles")
public class RoleController extends CommonController {


	@Autowired(required = true)
	private UserService userService;

	@Autowired
	private OperationLogService operationLogService;

	@RequestMapping("")
	public String index(ModelMap model) {
		model.put("rolesJSON", JsonMapper.nonEmptyMapper().toJson(rolesAsMap()));
		model.put("typesJSON", JsonMapper.nonEmptyMapper().toJson(PrivilegeTypeStatus.asMap()));
		return autoView("admin/roles/index");
	}

	private Map<Long, String> rolesAsMap() {
		return ValueTexts
				.asMap(Iterables.transform(userService.findAllRoles(), new Function<Role, ValueTextable<Long>>() {
					@Override
					public ValueTextable<Long> apply(Role role) {
						return new ValueText<>(role.getId(), role.getName());
					}
				}));
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult save(@Valid Role role, BindingResult result) {
		if (result.hasErrors()) {
			String message = ErrorValidate.convertErrorMessage(result);
			return ResponseResult.failure(message).message(result.toString());
		}
		try {
			String operation = "用户：" + currentUser().getName() + "(ID:" + currentUser().getId() + ")执行新增角色操作！";
			OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(),
					OperTypeStatus.NEW.getText(), operation, LogRscStatus.COMMON.getValue());

			preMethods(role);
			userService.createRole(role);
			operationLogService.save(openLog);
			return ResponseResult.success();
		} catch (Exception e) {
			logger.error("save", e);
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				return ResponseResult.failure("保存出错， 确认是否填入已经存在的角色名?");
			}
			return exceptionAsResult(e);
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult update(@RequestParam("id") Long id, @Valid Role role, BindingResult result) {
		if (result.hasErrors()) {
			String message = ErrorValidate.convertErrorMessage(result);
			return ResponseResult.failure(message).message(result.toString());
		}

		Role entity = userService.findRoleById(id);
		if (entity == null) {
			return ResponseResult.failure("角色不存在");
		}

		try {
			String operation = "用户：" + currentUser().getName() + "(ID:" + currentUser().getId() + ")执行修改角色操作！";
			OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(),
					OperTypeStatus.UPDATE.getText(), operation, LogRscStatus.COMMON.getValue());

			preMethods(role);
			if (role.getParent() != null && Objects.equal(entity.getId(), role.getParent().getId())) {
				return ResponseResult.failure("上一级角色不能设置为自身");
			}
			// mapper.map(role, entity);
			entity.setName(role.getName());
			entity.setDescription(role.getDescription());
			entity.setRealm(role.getRealm());
			entity.setIsActived(role.getIsActived());
			userService.updateRole(entity);
			operationLogService.save(openLog);
			return ResponseResult.success();
		} catch (Exception e) {
			logger.error("updateRole", e);
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				return ResponseResult.failure("保存出错， 确认是否填入已经存在的角色名?");
			}
			return ResponseResult.failure("更新有误").message(result.toString());
		}
	}

	private void preMethods(Role role) {
		if (role.getParent() != null && role.getParent().getId() != null && role.getParent().getId() != 0) {
			role.setParent(userService.findRoleById(role.getParent().getId()));
		} else {
			role.setParent(null);
		}
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult delete(@RequestParam("id") Long id) {
		Role role = userService.findRoleById(id);
		if (role == null) {
			return ResponseResult.failure("角色不存在");
		}

		try {
			String operation = "用户：" + currentUser().getName() + "(ID:" + currentUser().getId() + ")执行删除角色操作！";
			OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(),
					OperTypeStatus.DELETE.getText(), operation, LogRscStatus.COMMON.getValue());

			userService.deleteRole(id);
			operationLogService.save(openLog);
			return ResponseResult.success();
		} catch (Exception e) {
			logger.error("deleteUser", e);
			return ResponseResult.failure("删除有误").message(e.toString());
		}
	}

	@RequestMapping(value = "/privileges/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult deleteRolePrivilege(@RequestParam("roleId") Long roleId,
			@RequestParam("privilegeId") Long privilegeId) {
		try {
			String operation = "用户：" + currentUser().getName() + "(ID:" + currentUser().getId() + ")执行移除角色权限操作！";
			OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(),
					OperTypeStatus.DELETE.getText(), operation, LogRscStatus.COMMON.getValue());

			userService.removePrivilegeFromRole(roleId, privilegeId);
			operationLogService.save(openLog);
			return ResponseResult.success();
		} catch (Exception e) {
			logger.error("deleteUser", e);
			return ResponseResult.failure("移除有误").message(e.toString());
		}
	}

	@RequestMapping(value = "/privileges", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult addRolePrivilege(@RequestParam("roleId") Long roleId,
			@RequestParam("privilegeIds") String privilegeIds) {
		try {
			String operation = "用户：" + currentUser().getName() + "(ID:" + currentUser().getId() + ")执行添加角色权限操作！";
			OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(),
					OperTypeStatus.UPDATE.getText(), operation, LogRscStatus.COMMON.getValue());

			Iterable<String> ids = Splitter.on(",").split(privilegeIds);
			for (String idStr : ids) {
				long privilegeId = NumberUtils.toLong(idStr, -1);
				if (privilegeId != -1) {
					userService.addPrivilegeToRole(roleId, privilegeId);
				}
			}
			operationLogService.save(openLog);
			return ResponseResult.success();
		} catch (Exception e) {
			logger.error("addRolePrivilege", e);
			if (e instanceof ServiceException) {
				ServiceException es = (ServiceException) e;
				return ResponseResult.failure("添加有误:" + es.getMessage()).message(e.toString());
			}
			return ResponseResult.failure("添加有误").message(e.toString());
		}
	}

	@RestController
	@RequestMapping("/admin/roles/data")
	static class DataController extends CommonController {

		@Autowired
		private UserService userService;

		@RequestMapping("/roles.json")
		public EasyUIPage<Role> list() {
			Page<Role> data = userService.findAllRoles(this.getSearchFilters(),
					this.getPageRequestFromEasyUIDatagridWithInitSort(this.getIdAscSort()));

			return toEasyUIPage(data);
		}

		@RequestMapping("/roles_nopage.json")
		public Iterable<Role> listNopage() {
			List<Role> roles = Lists.newArrayList(userService.findAllRoles());
			Role empty = new Role();
			empty.setName("无");

			roles.add(empty);
			return roles;
		}

		@RequestMapping("/role/privileges.json")
		public List<Privilege> rolePrivileges(@RequestParam("id") Long id) {
			return userService.findPrivilegesByRole(id, getSortFromEasyUIDatagrid());
		}

		@RequestMapping("/role/all_privileges.json")
		public Collection<TreeNode> roleAllPrivileges(@RequestParam("id") Long id) {
			Role role = userService.findRoleById(id);
			return getRolePrivilegesTree(role);
		}

		private Collection<TreeNode> getRolePrivilegesTree(Role role) {
			Map<Long, TreeNode> result = Maps.newLinkedHashMap();
			getRolePrivilegesTreeRec(role, result);
			return result.values();
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
