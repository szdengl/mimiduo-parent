package net.mimiduo.boot.web.action.bussines;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;


import net.mimiduo.boot.common.domain.UserStatus;

import net.mimiduo.boot.common.util.ResponseResult;
import net.mimiduo.boot.common.util.ValueTexts;
import net.mimiduo.boot.pojo.admin.User;
import net.mimiduo.boot.pojo.business.Client;
import net.mimiduo.boot.service.admin.UserService;
import net.mimiduo.boot.service.busindess.ClientService;
import net.mimiduo.boot.util.EasyUIPage;
import net.mimiduo.boot.web.action.CommonController;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.dozer.Mapper;
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

import com.google.common.base.Optional;

@Controller
@RequestMapping("/admin/clients")
public class ClientController extends CommonController {

	private final long OrgId = 1L;

	@Autowired
	private ClientService clientService;

	@Autowired
	private UserService userService;

	@Autowired
	private Mapper mapper;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.put("statusJSON", JsonMapper.nonEmptyMapper().toJson(userStatusAsMap()));
		model.put("userJSON", JsonMapper.nonEmptyMapper().toJson(usersForOrgId()));
		return "business/client/index";
	}

	private Map<Long, String> userStatusAsMap() {
		return ValueTexts.asMap(Arrays.asList(UserStatus.values()));
	}

	private List<User> usersForOrgId() {
		List<User> data = userService.findAllUsersByOrganization(OrgId);
		return data;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult save(@Valid Client client, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return this.failureResult("输入有误!");
		}
		try {
			this.clientService.save(client);
			return this.successResult("成功了");
		} catch (Exception e) {
			logger.warn("操作有误", e);
			return this.failureResult("保存有误!" + e.getMessage());
		}
		
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult update(@Valid Client client, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return this.failureResult("输入有误!");
		}
		logger.debug("client:{}", ToStringBuilder.reflectionToString(client));
		Optional<Client> clientOpt = Optional.fromNullable(this.clientService.findOne(client.getId()));
		if (clientOpt.isPresent()) {
			try {
				Client entity = clientOpt.get();
				mapper.map(client, entity);
				this.clientService.save(client);
			} catch (Exception e) {
				logger.warn("操作有误", e);
				return this.failureResult("保存有误!" + e.getMessage());
			}
		} else {
			return this.failureResult("记录不存在");
		}

		return this.successResult("成功了");
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult delete(@RequestParam("id") Long id) {
		try {
			this.clientService.delete(id);
			return this.successResult("刪除成功了");
		} catch (Exception e) {
			logger.warn("操作有误", e);
			return this.failureResult("刪除有误!" + e.getMessage());
		}
	}

	@RestController
	@RequestMapping("/admin/clients/data")
	static class DataController extends CommonController {

		@Autowired
		private ClientService clientService;

		@RequestMapping("/list")
		@ResponseBody
		public EasyUIPage<Client> loadAllTopClients() {
			Page<Client> page = this.clientService.findAllTopClients(this.getSearchFilters(),
					this.getPageRequestFromEasyUIDatagrid());
			return this.toEasyUIPage(page);
		}

	}

}
