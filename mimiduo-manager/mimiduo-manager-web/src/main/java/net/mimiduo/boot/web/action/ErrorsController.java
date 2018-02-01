package net.mimiduo.boot.web.action;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorsController extends BaseController {

    @RequestMapping("/404")
    public String _404() {
        return autoView("common/404");
    }

    @RequestMapping("/500")
    public String _500(HttpServletRequest request, ModelMap model) {
        // TODO 异常提醒机制
        model.put("exception", request.getAttribute(javax.servlet.RequestDispatcher.ERROR_EXCEPTION));
        return autoView("common/500");
    }

    @RequestMapping("/public/test_500")
    @ResponseBody
    public String test_500() {
        throw new RuntimeException("错误了");
    }
}
