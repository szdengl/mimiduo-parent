package net.mimiduo.boot.web.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping("/")
	public String index() {
		// FIXME： 具体应用需修改此处
		return "redirect:/admin";
	}
}
