package net.mimiduo.boot.web.action.admin;

import java.util.List;

import net.mimiduo.boot.pojo.admin.Menu;
import net.mimiduo.boot.service.admin.MenuService;
import net.mimiduo.boot.web.action.CommonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminController extends CommonController {

	@Autowired
	private MenuService menuService;

	private final String DEFAULT_INIT_PASSWORD = "123450";

	//@ActionMethod(serviceName = "转向主页")
	@RequestMapping("")
	public String index(ModelMap model) {
		// model.put("currentUserOrg",this.getAccoutService().getUserOrganization(this.forceCurrentUser().getId()));
		return autoView("admin/index");
	}

	//@ActionMethod(serviceName = "获取菜单")
	@RequestMapping(value = "/data/channels.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Menu> channels() {
		List<Menu> ret = menuService.findAllTopMenus();
		//menuService.filterMenusByCurrentUserPerms(ret);
		return ret;
	}

	@RequestMapping(value = "/check_should_change_password")
	@ResponseBody
	public boolean checkShoulChangePassword() {
		return this.getUserService().isCurrentUserPassword(DEFAULT_INIT_PASSWORD);
	}
}
