package net.mimiduo.boot.web.action.bussines;



import net.mimiduo.boot.pojo.business.Attribute;
import net.mimiduo.boot.pojo.business.Province;
import net.mimiduo.boot.service.busindess.AttributeService;
import net.mimiduo.boot.service.busindess.ProvinceService;
import net.mimiduo.boot.web.action.CommonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/admin/common/data")
public class ApiController extends CommonController {

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private AttributeService attributeService;

    @RequestMapping("/attributes/list")
    public List<Attribute> Attributes(@RequestParam(value = "type", required = false) int type) {
        return attributeService.findByType(type);
    }

    @RequestMapping("/province/list")
    public List<Province> Provinces() {
        List<Province> list=provinceService.findAll();
        return list;
    }





}
