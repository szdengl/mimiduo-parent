package net.mimiduo.boot.web.action.bussines;


import net.mimiduo.boot.common.domain.ActionStatus;
import net.mimiduo.boot.common.domain.UserStatus;

import net.mimiduo.boot.common.util.ResponseResult;
import net.mimiduo.boot.common.util.ValueTexts;
import net.mimiduo.boot.pojo.business.Attribute;
import net.mimiduo.boot.pojo.business.Channel;
import net.mimiduo.boot.pojo.business.ChannelProvince;
import net.mimiduo.boot.pojo.business.Company;
import net.mimiduo.boot.service.busindess.AttributeService;
import net.mimiduo.boot.service.busindess.ChannelProvinceService;
import net.mimiduo.boot.service.busindess.ChannelService;
import net.mimiduo.boot.service.busindess.CompanyService;
import net.mimiduo.boot.util.EasyUIPage;
import net.mimiduo.boot.web.action.CommonController;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.mapper.JsonMapper;
import com.google.common.base.Optional;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/channels")
public class ChannelController extends CommonController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private AttributeService attributeService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ChannelProvinceService channelProvinceService;



    @Autowired
    private Mapper mapper;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(ModelMap model) {
        model.put("statusJSON", JsonMapper.nonEmptyMapper().toJson(userStatusAsMap()));
        model.put("attributeJSON", JsonMapper.nonEmptyMapper().toJson(attributeList()));
        model.put("actionJSON",JsonMapper.nonEmptyMapper().toJson(actionStatusAsMap()));
        model.put("companyJSON",JsonMapper.nonEmptyMapper().toJson(companyList()));
        return "business/channel/index";
    }

    private Map<Long, String> userStatusAsMap() {
        return ValueTexts.asMap(Arrays.asList(UserStatus.values()));
    }

    private List<Attribute> attributeList()  { return  attributeService.findAll();}

    private Map<Long,String> actionStatusAsMap() {return ValueTexts.asMap(Arrays.asList(ActionStatus.values()));}

    private List<Company> companyList() { return companyService.findAll();}

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult save(@Valid Channel channel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.failureResult("输入有误!");
        }
        try {
            this.channelService.save(channel);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("操作有误", e);
            return this.failureResult("保存有误!" + e.getMessage());
        }
        return this.successResult("成功了");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult update(@RequestParam("id") Long id, Channel channel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.failureResult("输入有误!");
        }
        logger.debug("channel:{}", ToStringBuilder.reflectionToString(channel));
        Optional<Channel> channelOpt =Optional.fromNullable(this.channelService.findOne(id));
        if (channelOpt.isPresent()) {
            try {
                Channel entity = channelOpt.get();
                mapper.map(channel, entity);
                entity.setOpenProvince(channelOpt.get().getOpenProvince());
                this.channelService.updateChannel(entity);
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
            this.channelService.deleteChannel(id);
            return this.successResult("delete成功了");
        } catch (Exception e) {
            logger.warn("操作有误", e);
            return this.failureResult("xx有误!" + e.getMessage());
        }
    }

    @RequestMapping(value = "/province/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult province_save(@Valid ChannelProvince channelProvince, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.failureResult("输入有误!");
        }
        try {
            String openProvince=channelProvince.getProvinceId();
            List<String> list = Arrays.asList(openProvince.split(","));
            List<ChannelProvince> channelProvinceList=new ArrayList<ChannelProvince>();
            if(!list.isEmpty()) {
                for (String e : list) {
                    if (channelProvinceService.findByProvinceIdAndChannelId(e,
                            channelProvince.getChannelId()) == null) {
                        ChannelProvince s = new ChannelProvince();
                        s = (ChannelProvince) channelProvince.clone();
                        s.setProvinceId(e);
                        channelProvinceList.add(s);
                    }
                }
            }
            this.channelProvinceService.save(channelProvinceList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("操作有误", e);
            return this.failureResult("保存有误!" + e.getMessage());
        }
        return this.successResult("成功了");
    }

    @RequestMapping(value = "/province/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult province_update(@RequestParam("id") Long id, ChannelProvince channelProvince,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.failureResult("输入有误!");
        }
        logger.debug("channelProvince:{}", ToStringBuilder.reflectionToString(channelProvince));
        Optional<ChannelProvince> channelProvinceOpt =Optional.fromNullable(this.channelProvinceService.findOne(id));
        if (channelProvinceOpt.isPresent()) {
            try {
                ChannelProvince entity = channelProvinceOpt.get();
                channelProvince.setProvinceId(entity.getProvinceId());
                mapper.map(channelProvince, entity);
                this.channelProvinceService.save(entity);
            } catch (Exception e) {
                logger.warn("操作有误", e);
                return this.failureResult("保存有误!" + e.getMessage());
            }
        } else {
            return this.failureResult("记录不存在");
        }
        return this.successResult("成功了");
    }

    @RequestMapping(value = "/province/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult province_delete(@RequestParam("id") Long id) {
        try {
            this.channelProvinceService.delete(id);
            return this.successResult("delete成功了");
        } catch (Exception e) {
            logger.warn("操作有误", e);
            return this.failureResult("删除有误!" + e.getMessage());
        }
    }

    @RequestMapping(value = "/province/order", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult province_order(@RequestParam("channelId") Long id,@RequestParam("province") String province,@RequestParam("orderId") int orderId) {
        Optional<ChannelProvince> channelProvinceOpt =Optional.fromNullable(this.channelProvinceService.findByProvinceIdAndChannelId(province,id));
        if (channelProvinceOpt.isPresent()) {
            try {
                ChannelProvince entity = channelProvinceOpt.get();
                entity.setOrderId(orderId);
                this.channelProvinceService.save(entity);
            } catch (Exception e) {
                logger.warn("操作有误", e);
                return this.failureResult("保存有误!" + e.getMessage());
            }
        } else {
            return this.failureResult("记录不存在");
        }
        return this.successResult("保存成功了");
    }

    @RestController
    @RequestMapping("/admin/channels/data")
    static class ChannelDataController extends CommonController {
        @Autowired
        private ChannelService channelService;

        @Autowired
        private ChannelProvinceService channelProvinceService;

        @RequestMapping("/list")
        public EasyUIPage<Channel> Channels() {
            Page<Channel> page = this.channelService.findAllChannels(this.getSearchFilters(),
                    this.getPageRequestFromEasyUIDatagridWithInitSort(this.getIdAscSort()));
            return this.toEasyUIPage(page);
        }

        @RequestMapping("/province/list")
        public List<ChannelProvince> ChannelProvinces(@RequestParam(value = "channelId", required = false) long channelId){
            return channelProvinceService.findByChannelId(channelId);
        }



    }
}
