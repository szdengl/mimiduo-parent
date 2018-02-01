package net.mimiduo.boot.web.action.bussines;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import net.mimiduo.boot.common.domain.UserStatus;

import net.mimiduo.boot.common.util.ResponseResult;
import net.mimiduo.boot.common.util.ValueTexts;
import net.mimiduo.boot.pojo.business.Client;
import net.mimiduo.boot.pojo.business.Project;
import net.mimiduo.boot.service.busindess.ClientService;
import net.mimiduo.boot.service.busindess.ProjectService;
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
@RequestMapping("/admin/projects")
public class ProjectController extends CommonController {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ClientService clientService;

	@Autowired
	private Mapper mapper;
	
	private final String isstatus="0";

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.put("statusJSON", JsonMapper.nonEmptyMapper().toJson(userStatusAsMap()));
		model.put("clientJSON", JsonMapper.nonEmptyMapper().toJson(clientAsMap()));
		return "business/project/index";
	}

	private Map<Long, String> userStatusAsMap() {
		return ValueTexts.asMap(Arrays.asList(UserStatus.values()));
	}

	
	private List<Client> clientAsMap(){
		List<Client> list=clientService.findByIsstatus(isstatus);
		return list;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult save(@Valid Project projectProject, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return this.failureResult("输入有误!");
		}
		try {
			this.projectService.save(projectProject);
		} catch (Exception e) {
			logger.error("操作有误", e);
			return this.failureResult("保存有误!" + e.getMessage());
		}
		return this.successResult("成功了");
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult update(@Valid Project project, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return this.failureResult("输入有误!");
		}
		logger.debug("project:{}", ToStringBuilder.reflectionToString(project));
		Optional<Project> projectOpt =Optional.fromNullable(this.projectService.findOne(project.getId()));
		if (projectOpt.isPresent()) {
			try {
				Project entity = projectOpt.get();
				mapper.map(project, entity);
				this.projectService.save(entity);
			} catch (Exception e) {
				logger.error("操作有误", e);
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
			this.projectService.delete(id);
			return this.successResult("delete成功了");
		} catch (Exception e) {
			logger.error("操作有误", e);
			return this.failureResult("刪除有误!" + e.getMessage());
		}
	}
	
	@RestController
	@RequestMapping("/admin/projects/data")
	static class DataController extends CommonController {
	
		@Autowired
		private ProjectService projectService;
		
		@RequestMapping("/list")
		@ResponseBody
		public EasyUIPage<Project> listData() {
			Page<Project> page = this.projectService.findAllTopProjects(this.getSearchFilters(),
					this.getPageRequestFromEasyUIDatagridWithInitSort(this.getIdAscSort()));
			return this.toEasyUIPage(page);
		}
		
	}
}
