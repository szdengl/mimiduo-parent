package net.mimiduo.boot.dao.admin;

import java.util.List;

import net.mimiduo.boot.pojo.admin.Role;
import net.mimiduo.boot.pojo.admin.UserRole;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;



public interface UserRoleDao extends PagingAndSortingRepository<UserRole, Long>, JpaSpecificationExecutor<UserRole> {

	@Query("select ur.role from UserRole ur where ur.user.id = ?1 ")
	List<Role> findAllRolesByUserId(Long id);

	UserRole findByUserIdAndRoleId(Long userId, Long roleId);

	@Modifying
	@Query("update UserRole u set u.isDeleted = 1 where u.id = ?1")
	void deleteUserRole(Long id);
}
