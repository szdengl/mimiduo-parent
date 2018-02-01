package net.mimiduo.boot.web.action.bussines;

import net.mimiduo.boot.pojo.business.Visit;
import net.mimiduo.boot.service.busindess.VisitService;
import net.mimiduo.boot.util.EasyUIPage;
import net.mimiduo.boot.web.action.CommonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * ${DESCRIPTION}
 *
 * @author:LingDeng
 * @create 2017-12-27 12:09
 **/
@Controller
@RequestMapping("/admin/query")
public class DataController extends CommonController {

    @RequestMapping(value = "/visit", method = RequestMethod.GET)
    public String visit(ModelMap model) {
        return "business/data/visit";
    }

    @RequestMapping(value = "/charge", method = RequestMethod.GET)
    public String charge(ModelMap model) {
        return "business/data/charge";
    }

    @RequestMapping(value = "/sell", method = RequestMethod.GET)
    public String sell(ModelMap model) {
        return "business/data/sell";
    }

    @RequestMapping(value = "/mo", method = RequestMethod.GET)
    public String mo(ModelMap model) {
        return "business/data/mo";
    }

    @RequestMapping(value = "/ivr", method = RequestMethod.GET)
    public String ivr(ModelMap model) {
        return "business/data/ivr";
    }



    @RestController
    @RequestMapping("/admin/query/data")
    static class ChannelDataController extends CommonController {
        @Autowired
        private VisitService visitService;

        @RequestMapping("/visit/list")
        public EasyUIPage<Visit> visitEasyUIPage() {
            Page<Visit> page = this.visitService.findAllVisit(this.getSearchFilters(),
                    this.getPageRequestFromEasyUIDatagridWithInitSort(this.getIdAscSort()));
            return this.toEasyUIPage(page);
        }




    }


}
