package net.mimiduo.boot.dao.admin;

import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.pojo.admin.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;



public interface UserDao extends PagingAndSortingRepository<User, Long>, JpaRepository<User, Long>,
		JpaSpecificationExecutor<User>, BaseDao<User, Long> {
	User findByLoginName(String loginName);

	@Query("select u from User u where u.loginName = :loginID")
	User findUserByLoginID(@Param("loginID") String loginID);

	@Modifying
	@Query("update User u set u.isDeleted = 1 where u.id = ?1")
	void deleteUser(Long id);

}
