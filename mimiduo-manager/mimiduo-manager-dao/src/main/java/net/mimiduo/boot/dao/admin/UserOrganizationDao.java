package net.mimiduo.boot.dao.admin;

import java.util.List;

import net.mimiduo.boot.pojo.admin.Organization;
import net.mimiduo.boot.pojo.admin.User;
import net.mimiduo.boot.pojo.admin.UserOrganization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserOrganizationDao
		extends JpaRepository<UserOrganization, Long>, JpaSpecificationExecutor<UserOrganization> {

	String findUsersByOrganizationIdSQL = "select uo.user from UserOrganization uo where uo.user.isDeleted =0 and uo.user.loginName!='admin' and uo.organization.id = ?1";
	String findAllUsersByOrganizationCodeSQL = "select uo.user from UserOrganization uo where uo.organization.code like CONCAT(:code,'%') and uo.user.loginName!='admin'";

	@Query(UserOrganizationDao.findUsersByOrganizationIdSQL)
	Page<User> findUsersByOrganizationId(Long orgId, Pageable pageable);

	@Query(UserOrganizationDao.findUsersByOrganizationIdSQL)
	List<User> findUsersByOrganizationId(Long orgId);

	@Query("select uo.organization from UserOrganization uo where uo.user.id = ?1 ")
	List<Organization> findOrganizationsByUserId(Long userId);

	List<UserOrganization> findByUserIdAndOrganizationId(Long userId, Long orgId);

	List<UserOrganization> findByUserId(Long id);

	@Query("select u from User u where u.isDeleted = 0 and u.loginName!='admin' and u.id not in (select uo.user.id from UserOrganization uo)")
	Page<User> findUsersNotInOrganizations(Pageable pageable);

	@Query(findAllUsersByOrganizationCodeSQL)
	Page<User> findAllUsersByOrganizationCode(@Param("code") String code, Pageable pageable);

	@Query(findAllUsersByOrganizationCodeSQL)
	List<User> findAllUsersByOrganizationCode(@Param("code") String code);

	@Modifying
	@Query("update UserOrganization u set u.isDeleted = 1 where u.id = ?1")
	void deleteUserOrganization(Long id);
}
