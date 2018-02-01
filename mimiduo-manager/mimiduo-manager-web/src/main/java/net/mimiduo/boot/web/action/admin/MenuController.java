package net.mimiduo.boot.web.action.admin;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;


import net.mimiduo.boot.common.domain.LogRscStatus;
import net.mimiduo.boot.common.domain.OperTypeStatus;
import net.mimiduo.boot.common.util.ResponseResult;
import net.mimiduo.boot.pojo.admin.Menu;
import net.mimiduo.boot.pojo.admin.OperationLog;
import net.mimiduo.boot.pojo.admin.Privilege;
import net.mimiduo.boot.service.admin.MenuService;
import net.mimiduo.boot.service.admin.OperationLogService;
import net.mimiduo.boot.service.admin.UserService;
import net.mimiduo.boot.util.EasyUIPage;
import net.mimiduo.boot.util.ErrorValidate;
import net.mimiduo.boot.web.action.CommonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Objects;

@Controller
@RequestMapping("/admin/menus")
public class MenuController extends CommonController {


    @Autowired
    private MenuService menuService;

    @Autowired
    private OperationLogService operationLogService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return autoView("admin/menus/index");
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult save(@Valid Menu menu, BindingResult result) {

        if (result.hasErrors()) {
            String message = ErrorValidate.convertErrorMessage(result);
            return ResponseResult.failure(message).message(result.toString());
        }
        String operation = "用户：" + currentUser().getName() + "(ID:" + currentUser().getId() + ")执行新增菜单操作！";
        OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(),
                OperTypeStatus.NEW.getText(), operation, LogRscStatus.COMMON.getValue());
        try {
            preMethods(menu);
            menuService.createMenu(menu);
            operationLogService.save(openLog);
            return ResponseResult.success();
        } catch (Exception e) {
            logger.error("save", e);
            return exceptionAsResult(e);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult update(@RequestParam("id") Long id, @Valid Menu menu, BindingResult result) {
        if (result.hasErrors()) {
            String message = ErrorValidate.convertErrorMessage(result);
            return ResponseResult.failure(message).message(result.toString());
        }

        Menu entity = menuService.findMenuById(id);
        if (entity == null) {
            return ResponseResult.failure("菜单不存在");
        }

        try {

            String operation = "用户：" + currentUser().getName() + "(ID:" + currentUser().getId() + ")执行修改菜单操作！";
            OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(),
                    OperTypeStatus.NEW.getText(), operation, LogRscStatus.COMMON.getValue());

            preMethods(menu);

            if (menu.getParent() != null && Objects.equal(entity.getId(), menu.getParent().getId())) {
                return ResponseResult.failure("上一级菜单不能设置为自身");
            }
            // mapper.map(menu, entity);
            entity.setName(menu.getName());
            entity.setUrl(menu.getUrl());
            entity.setIconCls(menu.getIconCls());
            entity.setPerm(menu.getPerm());
            entity.setDescription(menu.getDescription());
            entity.setParent(menu.getParent());
            entity.setSort(menu.getSort());
            entity.setIsActived(menu.getIsActived());
            menuService.createMenu(entity);
            operationLogService.save(openLog);
            return ResponseResult.success();
        } catch (Exception e) {
            logger.error("updateMenu", e);
            return ResponseResult.failure("更新有误").message(result.toString());
        }
    }

    private void preMethods(Menu menu) {
        if (menu.getParent() != null && menu.getParent().getId() != null && menu.getParent().getId() != 0) {
            menu.setParent(menuService.findMenuById(menu.getParent().getId()));
        } else {
            menu.setParent(null);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult delete(@RequestParam("id") Long id) {
        Menu menu = menuService.findMenuById(id);
        if (menu == null) {
            return ResponseResult.failure("菜单不存在");
        }
        try {

            String operation = "用户：" + currentUser().getName() + "(ID:" + currentUser().getId() + ")执行删除菜单操作！";
            OperationLog openLog = new OperationLog(currentUser().getId(), currentUser().getName(), "", new Date(),
                    OperTypeStatus.DELETE.getText(), operation, LogRscStatus.COMMON.getValue());

            menuService.deleteMenu(id);
            operationLogService.save(openLog);
            return ResponseResult.success();
        } catch (Exception e) {
            logger.error("deleteUser", e);
            return ResponseResult.failure("删除有误").message(e.toString());
        }
    }

    @RestController
    @RequestMapping("/admin/menus/data")
    static class DataController extends CommonController {

        @Autowired
        private MenuService menuService;
        @Autowired(required = true)
        private UserService accountService;

        @RequestMapping("/menus.json")
        public EasyUIPage<Menu> page() {
            Page<Menu> data = menuService.findAllTopMenus(this.getSearchFilters(),
                    this.getPageRequestFromEasyUIDatagridWithInitSort(this.getIdAscSort()));
            filterMenu(data.getContent(), false);
            return toEasyUIPage(data);
        }

        @RequestMapping("/menus_nopage.json")
        public List<Menu> list() {
            List<Menu> list = menuService.findAllTopMenus();
            filterMenu(list, true);
            return list;
        }

        @RequestMapping("/menus_nopageList.json")
        public List<Menu> menuList(@RequestParam("id") Long id) {
            List<Menu> list = menuService.findAllTopMenus();
            filterList(list, id);
            return list;
        }

        @RequestMapping("/test")
        public List<Privilege> getPermissions() {
            List<Privilege> privileges = accountService.getAllPrivileges();
            return privileges;
        }

        private List<Menu> filterList(List<Menu> menuList, Long id) {
            boolean braekFlag = false;
            for (Menu menu : menuList) {
                if (menu.getId().equals(id) || menu.getIsActived() == 0 || menu.getIsDeleted() == 1) {
                    int index = menuList.indexOf(menu);
                    menuList.remove(index);
                    braekFlag = true;
                    break;
                } else {
                    filterList(menu.getChildren(), id);
                }
            }
            if (braekFlag) {
                braekFlag = false;
                filterList(menuList, id);
            }
            return menuList;
        }

        /*
         * activeFlag ：true表示需要过滤非active状态的，false表示不需要过滤
         */
        private List<Menu> filterMenu(List<Menu> menuList, boolean activeFlag) {
            boolean braekFlag = false;

            for (Menu menu : menuList) {
                if ((activeFlag && (menu.getIsActived() == 0 || menu.getIsDeleted() == 1))
                        || (!activeFlag && menu.getIsDeleted() == 1)) {
                    int index = menuList.indexOf(menu);
                    menuList.remove(index);
                    braekFlag = true;
                    break;
                } else {
                    filterMenu(menu.getChildren(), activeFlag);
                }
            }
            if (braekFlag) {
                braekFlag = false;
                filterMenu(menuList, activeFlag);
            }
            return menuList;
        }
    }

}
