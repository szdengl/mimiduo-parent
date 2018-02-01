package net.mimiduo.boot.web.action.admin;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import net.mimiduo.boot.common.domain.LogRscStatus;
import net.mimiduo.boot.common.domain.OperTypeStatus;
import net.mimiduo.boot.common.util.ResponseResult;
import net.mimiduo.boot.common.util.ValueText;
import net.mimiduo.boot.common.util.ValueTextable;
import net.mimiduo.boot.common.util.ValueTexts;
import net.mimiduo.boot.pojo.admin.OperationLog;
import net.mimiduo.boot.pojo.admin.Organization;
import net.mimiduo.boot.pojo.admin.User;
import net.mimiduo.boot.service.admin.OperationLogService;
import net.mimiduo.boot.service.admin.OrganizationService;
import net.mimiduo.boot.service.admin.UserService;
import net.mimiduo.boot.util.EasyUIPage;
import net.mimiduo.boot.util.ErrorValidate;
import net.mimiduo.boot.web.action.CommonController;
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
import com.google.common.collect.Iterables;


@Controller
@RequestMapping("/admin/organizations")
public class OrganizationController extends CommonController {

	@Autowired(required = true)
	private OrganizationService organizationService;

	@Autowired
	private UserService accountService;

	@Autowired
	private OperationLogService operationLogService;

	@RequestMapping("")
	public String index(ModelMap model) {
		model.put("orgsJSON", JsonMapper.nonEmptyMapper().toJson(organizationAsMap()));
		return autoView("admin/organizations/index");
	}

	private Map<Long, String> organizationAsMap() {
		return ValueTexts.asMap(Iterables.transform(organizationService.findAllOrganizations(),
				new Function<Organization, ValueTextable<Long>>() {
					@Override
					public ValueTextable<Long> apply(Organization organization) {
						return new ValueText<>(organization.getId(), organization.getName());
					}
				}));
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult save(Organization organization, BindingResult result) {
		if (result.hasErrors()) {
			String message = ErrorValidate.convertErrorMessage(result);
			return ResponseResult.failure(message).message(result.toString());
		}
		if (organizationService.existsOrganization(organization)) {
			return ResponseResult.failure("该机构名称已经存在");
		}
		try {
			String operation = "用户：" + currentUser().getName() + "(ID:" + currentUser().getId() + ")执行新增组织机构操作！";
			OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(),
					OperTypeStatus.NEW.getText(), operation, LogRscStatus.COMMON.getValue());

			preMethods(organization);

			organizationService.createOrganization(organization);
			operationLogService.save(openLog);
			return ResponseResult.success();
		} catch (Exception e) {
			logger.error("save", e);
			return exceptionAsResult(e);
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult update(@RequestParam("id") Long id, @Valid Organization organization, BindingResult result) {
		if (result.hasErrors()) {
			String message = ErrorValidate.convertErrorMessage(result);
			return ResponseResult.failure(message).message(result.toString());
		}

		Organization entity = organizationService.findOrganizationById(id);
		if (entity == null) {
			return ResponseResult.failure("机构不存在");
		}
		if (entity.getChildren() != null && !entity.getChildren().isEmpty() && organization.getIsActived() == 0
				&& checkChildren(entity.getChildren())) {
			return ResponseResult.failure("该机构下存在子机构，请先移除");
		}
		if (organizationService.existsOrganization(organization)) {
			return ResponseResult.failure("该机构名称已经存在");
		}
		List<User> userList = accountService.findUsersByOrganization(id);
		if (userList != null && !userList.isEmpty() && organization.getIsActived() == 0) {
			return ResponseResult.failure("该机构下存在用户，不能修改为无效机构");
		}
		try {
			String operation = "用户：" + currentUser().getName() + "(ID:" + currentUser().getId() + ")执行修改组织机构操作！";
			OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(),
					OperTypeStatus.UPDATE.getText(), operation, LogRscStatus.COMMON.getValue());

			preMethods(organization);

			if (organization.getParent() != null && Objects.equal(entity.getId(), organization.getParent().getId())) {
				return ResponseResult.failure("上一级机构不能设置为自身");
			}
			// mapper.map(organization, entity);
			entity.setName(organization.getName());
			entity.setDescription(organization.getDescription());
			entity.setParent(organization.getParent());
			entity.setContactPhone(organization.getContactPhone());
			entity.setContactUser(organization.getContactUser());
			entity.setIsActived(organization.getIsActived());
			organizationService.updateOrganization(entity);
			operationLogService.save(openLog);
			return ResponseResult.success();
		} catch (Exception e) {
			logger.error("updateOrganization", e);
			return ResponseResult.failure("更新有误").message(result.toString());
		}
	}

	private void preMethods(Organization organization) {
		if (organization.getParent() != null && organization.getParent().getId() != null
				&& organization.getParent().getId() != 0) {
			organization.setParent(organizationService.findOrganizationById(organization.getParent().getId()));
		} else {
			organization.setParent(null);
		}
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult delete(@RequestParam("id") Long id) {
		Organization organization = organizationService.findOrganizationById(id);
		if (organization == null) {
			return ResponseResult.failure("机构不存在");
		}
		if (organization.getChildren() != null && !organization.getChildren().isEmpty()
				&& checkChildren(organization.getChildren())) {
			return ResponseResult.failure("该机构下存在子机构，请先移除");
		}
		List<User> userList = accountService.findUsersByOrganization(id);
		if (userList != null && !userList.isEmpty()) {
			return ResponseResult.failure("该机构下存在用户，请先移除");
		}
		try {
			String operation = "用户：" + currentUser().getName() + "(ID:" + currentUser().getId() + ")执行删除组织机构操作！";
			OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(),
					OperTypeStatus.DELETE.getText(), operation, LogRscStatus.COMMON.getValue());

			organizationService.deleteOrganization(id);
			operationLogService.save(openLog);
			return ResponseResult.success();
		} catch (Exception e) {
			logger.error("deleteUser", e);
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				return ResponseResult.failure("删除有误， 确认是否此组织机构已经具有下一级的数据? 请清空关联数据再删除此组织机构。");
			}
			return ResponseResult.failure("删除有误").message(e.toString());
		}
	}

	private boolean checkChildren(List<Organization> orgList) {
		for (Organization organization : orgList) {
			if (organization.getIsDeleted() == 0) {
				return true;
			}
		}
		return false;
	}

	@RestController
	@RequestMapping("/admin/organizations/data")
	static class DataController extends CommonController {

		@Autowired
		private OrganizationService organizationService;

		@RequestMapping("/organizations.json")
		public EasyUIPage<Organization> page() {
			Page<Organization> data = organizationService.findAllTopOrganizations(this.getSearchFilters(),
					this.getPageRequestFromEasyUIDatagridWithInitSort(this.getIdAscSort()));
			filterOrgActive(data.getContent());
			return toEasyUIPage(data);
		}

		@RequestMapping("/organizations_nopage.json")
		public List<Organization> list(@RequestParam("id") Long id) {
			List<Organization> list = organizationService.findAllTopOrganizations();
			filterList(list, id);
			return list;
		}

		@RequestMapping("/organizationsList_nopage.json")
		public List<Organization> organizationlist() {
			List<Organization> list = organizationService.findAllTopOrganizations();
			filterOrg(list);
			return list;
		}

		private List<Organization> filterList(List<Organization> orgList, Long id) {
			boolean braekFlag = false;
			for (Organization organization : orgList) {
				if (organization.getId() == id || organization.getIsActived() == 0
						|| organization.getIsDeleted() == 1) {
					int index = orgList.indexOf(organization);
					orgList.remove(index);
					braekFlag = true;
					break;
				} else {
					filterList(organization.getChildren(), id);
				}
			}
			if (braekFlag) {
				braekFlag = false;
				filterList(orgList, id);
			}
			return orgList;
		}

		private List<Organization> filterOrg(List<Organization> orgList) {
			boolean braekFlag = false;
			for (Organization organization : orgList) {
				if (organization.getIsActived() == 0 || organization.getIsDeleted() == 1) {
					int index = orgList.indexOf(organization);
					orgList.remove(index);
					braekFlag = true;
					break;
				} else {
					filterOrg(organization.getChildren());
				}
			}
			if (braekFlag) {
				braekFlag = false;
				filterOrg(orgList);
			}
			return orgList;
		}

		private List<Organization> filterOrgActive(List<Organization> orgList) {
			boolean braekFlag = false;
			for (Organization organization : orgList) {
				if (organization.getIsDeleted() == 1) {
					int index = orgList.indexOf(organization);
					orgList.remove(index);
					braekFlag = true;
					break;
				} else {
					filterOrgActive(organization.getChildren());
				}
			}
			if (braekFlag) {
				braekFlag = false;
				filterOrgActive(orgList);
			}
			return orgList;
		}

	}
}
