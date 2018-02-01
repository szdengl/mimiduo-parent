package net.mimiduo.boot.web.action.admin;

import java.util.Date;

import javax.validation.Valid;

import net.mimiduo.boot.common.domain.LogRscStatus;
import net.mimiduo.boot.common.domain.OperTypeStatus;
import net.mimiduo.boot.common.domain.PrivilegeMethodStatus;
import net.mimiduo.boot.common.domain.PrivilegeTypeStatus;
import net.mimiduo.boot.common.util.ResponseResult;
import net.mimiduo.boot.pojo.admin.OperationLog;
import net.mimiduo.boot.pojo.admin.Privilege;
import net.mimiduo.boot.service.admin.OperationLogService;
import net.mimiduo.boot.service.admin.UserService;
import net.mimiduo.boot.util.EasyUIPage;
import net.mimiduo.boot.util.ErrorValidate;
import net.mimiduo.boot.web.action.CommonController;
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
import com.google.common.base.Strings;


@Controller
@RequestMapping("/admin/privileges")
public class PrivilegeController extends CommonController {

    @Autowired
    private Mapper mapper;

    @Autowired(required = true)
    private UserService accountService;
    
    @Autowired
    private OperationLogService operationLogService;

    @RequestMapping("")
    public String index(ModelMap model) {
        model.put("typesJSON", JsonMapper.nonEmptyMapper().toJson(PrivilegeTypeStatus.asMap()));
        return autoView("admin/privileges/index");
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult save(@Valid Privilege privilege, BindingResult result) {
        if (result.hasErrors()) {
        	String message = ErrorValidate.convertErrorMessage(result);
            return ResponseResult.failure(message).message(result.toString());
        }
        try {
        	String operation = "用户："+currentUser().getName()+"(ID:"+currentUser().getId()+")执行新增权限操作！";
        	OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(), OperTypeStatus.NEW.getText(), operation, LogRscStatus.COMMON.getValue());
        	
            preMethods(privilege);
            accountService.createPrivilege(privilege);
            operationLogService.save(openLog);
            return ResponseResult.success();
        } catch (Exception e) {
            logger.error("save", e);
            if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                return ResponseResult.failure("保存出错， 确认是否填入已经存在的权限名?");
            }
            return exceptionAsResult(e);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult update(@RequestParam("id") Long id, @Valid Privilege privilege, BindingResult result) {
        if (result.hasErrors()) {
        	String message = ErrorValidate.convertErrorMessage(result);
            return ResponseResult.failure(message).message(result.toString());
        }

        Privilege entity = accountService.findPrivilegeById(id);
        if (entity == null) {
            return ResponseResult.failure("权限不存在");
        }

        if (entity.isReadOnly()) {
            return ResponseResult.failure("只读的权限不能通过web端进行修改！");
        }

        try {
        	String operation = "用户："+currentUser().getName()+"(ID:"+currentUser().getId()+")执行修改权限操作！";
        	OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(), OperTypeStatus.UPDATE.getText(), operation, LogRscStatus.COMMON.getValue());
        	
            preMethods(privilege);
            mapper.map(privilege, entity);
//            entity.setIsActived(privilege.getIsActived());
            accountService.updatePrivilege(entity);
            operationLogService.save(openLog);
            return ResponseResult.success();
        } catch (Exception e) {
            logger.error("update Privilege", e);
            if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                return ResponseResult.failure("保存出错， 确认是否填入已经存在的权限名?");
            }
            return ResponseResult.failure("更新有误").message(result.toString());
        }
    }

    private void preMethods(Privilege privilege) {
        if (!Strings.isNullOrEmpty(privilege.getMethod())
                && privilege.getMethod().contains(PrivilegeMethodStatus.all.toString())) {
            privilege.setMethod(PrivilegeMethodStatus.all.toString());
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult delete(@RequestParam("id") Long id) {
        Privilege privilege = accountService.findPrivilegeById(id);
        if (privilege == null) {
            return ResponseResult.failure("权限不存在");
        }

        if (privilege.isReadOnly()) {
            return ResponseResult.failure("只读的权限不能通过web端进行修改！");
        }

        try {
        	String operation = "用户："+currentUser().getName()+"(ID:"+currentUser().getId()+")执行删除权限操作！";
        	OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(), OperTypeStatus.DELETE.getText(), operation, LogRscStatus.COMMON.getValue());
        	
            accountService.deletePrivilege(id);
            
            operationLogService.save(openLog);
            return ResponseResult.success();
        } catch (Exception e) {
            logger.error("deleteUser", e);
            if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                return ResponseResult.failure("该权限已经分配到角色, 不能直接删除。确定要删除， 请先解除与对应角色的关系！").message(e.toString());
            }
            return ResponseResult.failure("删除有误").message(e.toString());
        }
    }

    @RestController
    @RequestMapping("/admin/privileges/data")
    static class DataController extends CommonController {

        @Autowired
        private UserService accountService;

        @RequestMapping("/privileges.json")
        public EasyUIPage<Privilege> list() {
            Page<Privilege> data = accountService.findAllPrivileges(this.getSearchFilters(),
                    this.getPageRequestFromEasyUIDatagridWithInitSort(this.getIdAscSort()));

            return toEasyUIPage(data);
        }

        @RequestMapping("/methods.json")
        public PrivilegeMethodStatus[] methods() {
            return PrivilegeMethodStatus.values();
        }

        @RequestMapping("/types.json")
        public PrivilegeTypeStatus[] types() {
            return PrivilegeTypeStatus.values();
        }
    }
}
