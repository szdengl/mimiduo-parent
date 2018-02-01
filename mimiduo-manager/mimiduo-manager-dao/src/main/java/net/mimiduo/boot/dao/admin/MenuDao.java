package net.mimiduo.boot.dao.admin;

import java.util.List;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.pojo.admin.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface MenuDao extends PagingAndSortingRepository<Menu, Long>, JpaRepository<Menu, Long>,
		JpaSpecificationExecutor<Menu>, BaseDao<Menu, Long> {

	List<Menu> findByParentIsNull();

	List<Menu> findByParentId(Long id);

	List<Menu> findByParentIsNull(Sort sort);

	@Modifying
	@Query("update Menu u set u.isDeleted = 1 where u.id = ?1")
	void deleteMenu(Long id);
}
