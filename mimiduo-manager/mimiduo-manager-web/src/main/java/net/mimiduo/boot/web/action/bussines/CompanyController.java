package net.mimiduo.boot.web.action.bussines;


import com.google.common.base.Optional;
import net.mimiduo.boot.common.domain.UserStatus;

import net.mimiduo.boot.common.util.ResponseResult;
import net.mimiduo.boot.common.util.ValueTexts;
import net.mimiduo.boot.pojo.business.Company;
import net.mimiduo.boot.service.busindess.CompanyService;
import net.mimiduo.boot.util.EasyUIPage;
import net.mimiduo.boot.web.action.CommonController;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.mapper.JsonMapper;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/companys")
public class CompanyController extends CommonController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private Mapper mapper;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(ModelMap model) {
        model.put("statusJSON", JsonMapper.nonEmptyMapper().toJson(userStatusAsMap()));
        return "business/company/index";
    }

    private Map<Long, String> userStatusAsMap() {
        return ValueTexts.asMap(Arrays.asList(UserStatus.values()));
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult save(@Valid Company company, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.failureResult("输入有误!");
        }
        try {
            this.companyService.save(company);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("操作有误", e);
            return this.failureResult("保存有误!" + e.getMessage());
        }
        return this.successResult("成功了");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult update(@Valid Company company, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.failureResult("输入有误!");
        }
        logger.debug("company:{}", ToStringBuilder.reflectionToString(company));
        Optional<Company> companyOpt = Optional.fromNullable(this.companyService.findOne(company.getId()));
        if (companyOpt.isPresent()) {
            try {
                Company entity = companyOpt.get();
                mapper.map(company, entity);
                this.companyService.updateCompany(entity);
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
            this.companyService.deleteCompany(id);
            return this.successResult("删除成功了");
        } catch (Exception e) {
            logger.warn("操作有误", e);
            return this.failureResult("删除有误!" + e.getMessage());
        }
    }

    @RestController
    @RequestMapping("/admin/companys/data")
    static class DataController extends CommonController {

        @Autowired
        private CompanyService companyService;

        private static final int isstatus = 0;

        @RequestMapping("/list")
        @ResponseBody
        public EasyUIPage<Company> listData() {
            Page<Company> page = this.companyService.findAllCompany(this.getSearchFilters(),
                    this.getPageRequestFromEasyUIDatagrid());
            return this.toEasyUIPage(page);
        }

        @RequestMapping("/select")
        @ResponseBody
        public List<Company> companys_select_status() {
            List<Company> companyList = this.companyService.findCompanyByIsstatus(isstatus);
            Company company = new Company();
            company.setName("全部");
            company.setId((long) -1);
            company.setIsstatus(0);
            companyList.add(0, company);
            return companyList;
        }
    }
}
