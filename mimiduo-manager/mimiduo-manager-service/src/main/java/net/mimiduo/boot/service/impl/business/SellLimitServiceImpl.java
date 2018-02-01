package net.mimiduo.boot.service.impl.business;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.mimiduo.boot.common.domain.VisitStatus;
import net.mimiduo.boot.pojo.business.*;
import net.mimiduo.boot.service.busindess.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static net.mimiduo.boot.common.util.DateUtils.LocalDateToDate;


@Service
public class SellLimitServiceImpl implements SellLimitService {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ChargingService chargingService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ChannelProvinceService channelProvinceService;

    /***
     * 所属的项目是否超限
     * @param sell
     * @return
     */
    private int bProjectLimit(Sell sell) {
        int bResult=0;
        Client client=this.clientService.findByCode(sell.getClientId());
        if (client == null || (client != null && !client.getIsstatus().equals("0"))) {
            return VisitStatus.clientClose.getValue();
        }
        Project project=this.projectService.findByClientIdAndCode(sell.getClientId(),sell.getProjectId());
        if (project == null || (project != null && !project.getIsstatus().equals("0"))) {
            return VisitStatus.projectClose.getValue();
        }
        LocalDate star = LocalDate.now().plusMonths(-1);
        LocalDate end =LocalDate.now().plusDays(1);
        long fee=this.chargingService.findByMobileAndStatusAndCreateDateBetween(sell.getMobile(),1,  LocalDateToDate(star),LocalDateToDate(end));
        if(fee>=project.getLimitCost()){
            return VisitStatus.projectLimit.getValue();
        }
        return bResult;
    }

    /***
     * 所属的业务是否超限
     * @param sell
     * @return
     */
    @Override
    public boolean bChannelLimit(Sell sell,long channelId) {
        boolean bResult=false;
        Channel channel=this.channelService.findOne(channelId);
        ChannelProvince channelProvince=this.channelProvinceService.findByProvinceIdAndChannelId(sell.getProvince(),channelId);
        LocalDate star = LocalDate.now().plusMonths(-1);
        LocalDate end =LocalDate.now().plusDays(1);
        long monthLimit=this.chargingService.countByMobileAndChannelIdAndCreateDateBetween(sell.getMobile(),channelId,LocalDateToDate(star),LocalDateToDate(end));
        if (monthLimit >= channelProvince.getMonthLimit()) {
            return true;
        }
        long dayLimit=this.chargingService.countByMobileAndChannelIdAndCreateDateBetween(sell.getMobile(),channelId,LocalDateToDate(LocalDate.now()),LocalDateToDate(end));
        if (dayLimit >= channelProvince.getDayLimit()) {
            return true;
        }
        long IvrDayCount=this.chargingService.countByMobileAndFeeTypeAndCreateDateBetween(sell.getMobile(),2,LocalDateToDate(LocalDate.now()),LocalDateToDate(end));
        if (channel.getFeeType() == 2 && IvrDayCount > 5) {
            return true;
        }
        return bResult;
    }



    @Override
    public String  findBySell(Sell sell, Visit visit, Charging charging, StringBuilder log, boolean isNew){
        String result="";
        int isProjectLimit = bProjectLimit(sell);
        if (isProjectLimit != 0) {
            visit.setStatus(isProjectLimit);
            log.append(VisitStatus.getName(isProjectLimit) + "\r\n");
        } else {
            result=channelService.findBySellAndIsNew(sell,isNew,charging,visit,log);
        }
        Map<Integer,Object> map=new HashMap<Integer,Object>();
        map.put(1,result);
        map.put(2,visit);
        map.put(3,charging);
        map.put(4,log);
        return JSON.toJSONString(map);
    }
}
