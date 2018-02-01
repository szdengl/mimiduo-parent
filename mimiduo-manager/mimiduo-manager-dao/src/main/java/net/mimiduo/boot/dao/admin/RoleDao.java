package net.mimiduo.boot.dao.admin;

import java.util.List;

import net.mimiduo.boot.pojo.admin.Role;
import net.mimiduo.boot.pojo.admin.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RoleDao extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

	String UsersByPrivilegePrefixSQl = "select distinct u.* from t_privilege p, t_role_privilege rp , t_role r, t_user_role ur, t_user u where p.id=rp.privilege_id and rp.role_id = r.id and r.id = ur.role_id and ur.user_id = u.id ";

	Role findByName(String name);

	Role findRoleByNameAndRealm(String name, String realm);

	@Query(value = UsersByPrivilegePrefixSQl + " and p.target = ?1", nativeQuery = true)
	List<User> findUsersByPrivilege(String target);

	@Query(value = UsersByPrivilegePrefixSQl + " and p.target = ?1 and p.method = ?2", nativeQuery = true)
	List<User> findUsersByPrivilege(String target, String method);

	@Query(value = UsersByPrivilegePrefixSQl
			+ " and p.target = ?1 and p.method = ?2 and scope = ?3", nativeQuery = true)
	List<User> findUsersByPrivilege(String target, String method, String scope);

	@Query("select r from Role r join r.privileges p where p.target= :target")
	List<Role> findByPerm(@Param("target") String target);

	@Query("select r from Role r join r.privileges p where p.target= :target and p.method = :method")
	List<Role> findByPerm(@Param("target") String target, @Param("method") String method);

	@Query("select r from Role r join r.privileges p where p.target= :target and p.method = :method and p.scope = :scope")
	List<Role> findByPerm(@Param("target") String target, @Param("method") String method, @Param("scope") String scope);

	@Query("select ur.user from  UserRole ur where ur.role.id= :roleId")
	List<User> findUsersByRoleId(@Param("roleId") Long roleId);

	@Modifying
	@Query("update Role u set u.isDeleted = 1 where u.id = ?1")
	void deleteRole(Long id);
}
