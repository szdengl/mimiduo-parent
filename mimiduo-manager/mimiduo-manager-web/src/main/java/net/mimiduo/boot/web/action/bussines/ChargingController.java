package net.mimiduo.boot.web.action.bussines;


import net.mimiduo.boot.pojo.view.ChargingView;
import net.mimiduo.boot.service.busindess.ChannelProvinceService;
import net.mimiduo.boot.service.data.ChannelChargingService;
import net.mimiduo.boot.web.action.CommonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * ${DESCRIPTION}
 *
 * @author:LingDeng
 * @create 2017-12-25 18:24
 **/

@Controller
@RequestMapping("/admin/charging")
public class ChargingController  extends CommonController {
    @Autowired
    private ChannelChargingService channelChargingService;
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(ModelMap model) {
        return "business/charging/daysummary";
    }

    @RestController
    @RequestMapping("/admin/charging/data")
    static class ChargingDataController extends CommonController {

        @Autowired
        private ChannelChargingService channelChargingService;

        @Autowired
        private ChannelProvinceService channelProvinceService;

        @RequestMapping(value = "/dayList", method = RequestMethod.POST)
        public List<ChargingView> dayList(@RequestParam(value = "operator", required = false) int operator) {
            return this.channelChargingService.findByOperator(operator);
        }

        @RequestMapping(value = "/channelList", method = RequestMethod.POST)
        public List<ChargingView> channelList(@RequestParam(value = "operator", required = false) int operator, @RequestParam(value="province",required=false) String province){
            List<ChargingView>  chargingViewList=this.channelChargingService.findByOperator(operator);
            chargingViewList=chargingViewList.stream().filter(chargingView -> province.equals(province)).collect(Collectors.toList());
            return chargingViewList;
        }
    }
}
