package net.mimiduo.boot.service.impl.admin;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.admin.MenuDao;
import net.mimiduo.boot.pojo.admin.Menu;
import net.mimiduo.boot.service.admin.MenuService;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.SearchFilter;

import com.google.common.base.Strings;



@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu, Long> implements MenuService {
	
	@Autowired
	private MenuDao menuDao;

	@Override
	public BaseDao<Menu, Long> getDao() {
		return this.menuDao;
	}
	
	@Override
	@Transactional
	public void createMenu(Menu menu) {
		menuDao.save(menu);
	}

	@Override
	public Menu findMenuById(Long id) {
		return menuDao.findOne(id);
	}

	@Override
	@Transactional
	public void deleteMenu(Long id) {
		menuDao.deleteMenu(id);
	}

	@Override
	public Page<Menu> findAllTopMenus(Collection<SearchFilter> searchFilters, Pageable pageable) {
		Specifications<Menu> spec = Specifications.where(bySearchFilter(searchFilters, Menu.class))
				.and(builtinSpecs.<Menu> parentIsNull());
		return menuDao.findAll(spec, pageable);
	}

	@Override
	public List<Menu> findAllTopMenus() {
		Sort sort = new Sort(new Order(Direction.ASC, "sort"), new Order(Direction.ASC, "id"));
		return menuDao.findByParentIsNull(sort);
	}

	@Override
	public void filterMenusByCurrentUserPerms(List<Menu> menus) {
		if (menus == null || menus.isEmpty()) {
			return;
		}
		Iterator<Menu> iter = menus.iterator();
		while (iter.hasNext()) {
			Menu menu = iter.next();
			String menuPerm = menu.getPerm();
			boolean noPerm = !Strings.isNullOrEmpty(menuPerm)
					&& !SecurityUtils.getSubject().isPermitted(menu.getPerm());
			boolean isDeleted = false;
			if (menu.getIsDeleted() == 1 || menu.getIsActived() == 0) {
				isDeleted = true;
			}
			if (noPerm || isDeleted) {
				iter.remove();
			} else {
				filterMenusByCurrentUserPerms(menu.getChildren());
			}
		}
	}

	@Autowired
	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}

	@Override
	public List<Menu> findAllTopMenusByPid(Long id) {
		return menuDao.findByParentId(id);
	}
}
