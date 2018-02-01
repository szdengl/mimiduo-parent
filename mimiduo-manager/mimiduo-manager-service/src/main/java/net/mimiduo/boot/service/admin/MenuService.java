package net.mimiduo.boot.service.admin;

import java.util.Collection;
import java.util.List;

import net.mimiduo.boot.pojo.admin.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springside.modules.persistence.SearchFilter;



public interface MenuService {

	void createMenu(Menu menu);

	Menu findMenuById(Long id);

	void deleteMenu(Long id);

	Page<Menu> findAllTopMenus(Collection<SearchFilter> searchFilters, Pageable pageable);

	List<Menu> findAllTopMenus();

	List<Menu> findAllTopMenusByPid(Long id);

	void filterMenusByCurrentUserPerms(List<Menu> menus);
}
