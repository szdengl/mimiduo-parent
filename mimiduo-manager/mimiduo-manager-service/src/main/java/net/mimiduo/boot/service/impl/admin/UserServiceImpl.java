package net.mimiduo.boot.service.impl.admin;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.admin.*;
import net.mimiduo.boot.pojo.admin.*;
import net.mimiduo.boot.service.admin.OrganizationService;
import net.mimiduo.boot.service.admin.SessionUser;
import net.mimiduo.boot.service.admin.UserService;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Clock;
import org.springside.modules.utils.Encodes;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


import net.mimiduo.boot.common.domain.UserStatus;



@Service
@Transactional(readOnly = true)
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;

	@Autowired
	private UserDao userDao;

	@Override
	public BaseDao<User, Long> getDao() {
		return this.userDao;
	}

	private PrivilegeDao privilegeDao;

	private RoleDao roleDao;

	private UserRoleDao userRoleDao;

	private UserOrganizationDao userOrganizationDao;

	private Clock clock = Clock.DEFAULT;

	private OrganizationService organizationService;

	@Override
	public User findUserByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
	}

	@Override
	public User findUserByLoginID(String loginID) {
		return userDao.findUserByLoginID(loginID);
	}

	@Override
	public List<User> getAllUser() {
		return (List<User>) userDao.findAll();
	}

	@Override
	public User getUser(Long id) {
		return userDao.findOne(id);
	}

	@Override
	@Transactional
	public void registerUser(User user) {
		entryptPassword(user);
		user.setRegisterDate(clock.getCurrentDate());

		userDao.save(user);
	}

	@Override
	@Transactional
	public void updateUser(User user) {
		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			entryptPassword(user);
		}
		userDao.save(user);
	}

	@Override
	@Transactional
	public void deleteUser(Long id) {
		if (isSupervisor(id)) {
			logger.warn("操作员{}尝试删除超级管理员用户", getCurrentUser().getLoginName());
			throw new ServiceException("不能删除超级管理员用户");
		}
		userDao.deleteUser(id);
	}

	/**
	 * 判断是否超级管理员.
	 */
	@Override
	public boolean isSupervisor(Long id) {
		return id == 1;
	}

	/**
	 * 取出Shiro中的当前用户.
	 */
	@Override
	public SessionUser getCurrentSessionUser() {
		try {
			final Subject subject = SecurityUtils.getSubject();
			if (subject == null) {
				return null;
			}
			return (SessionUser) subject.getPrincipal();
		} catch (Exception e) {
			logger.warn("getCurrentSessionUser: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * 取出Shiro中的当前用户.
	 */
	@Override
	public User getCurrentUser() {
		final SessionUser sessionUser = getCurrentSessionUser();
		if (sessionUser == null) {
			return null;
		}
		if (!(sessionUser instanceof User)) {
			throw new RuntimeException("getCurrentUser, sessionUser not instanceof User");
		}
		return (User) sessionUser;
	}

	@Override
	public boolean isCurrentUserPassword(String plainPassword) {
		final User user = this.getCurrentUser();
		if (user == null) {
			return false;
		}

		final String entryptPassword = entryptPassword(plainPassword, Encodes.decodeHex(user.getSalt()));
		return Objects.equal(user.getPassword(), entryptPassword);
	}

	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash.
	 */
	private void entryptPassword(User user) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		user.setSalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(Charsets.UTF_8), salt, HASH_INTERATIONS);
		user.setPassword(Encodes.encodeHex(hashPassword));
	}

	private String entryptPassword(String plainPassword, byte[] salt) {
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(Charsets.UTF_8), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(hashPassword);
	}

	@Transactional
	@Override
	public void assignRolesToUser(User user, Role... roles) {
		Preconditions.checkNotNull(user);
		Preconditions.checkArgument(roles != null && roles.length > 0);

		for (Role role : roles) {
			UserRole userRole = new UserRole(user, role);
			userRoleDao.save(userRole);
		}
	}

	@Override
	public List<Role> getUserAllRoles(User user) {
		// 直接的.
		List<Role> immedRoles = userRoleDao.findAllRolesByUserId(user.getId());

		List<Role> result = Lists.newArrayList(immedRoles);
		for (Role role : immedRoles) {
			List<Role> roleParents = getRoleParents(role.getParent(), Lists.<Role> newArrayList());
			result.addAll(roleParents);
		}

		return Lists.newArrayList(Sets.newHashSet(result));
	}

	// 获取
	private List<Role> getRoleParents(Role role, List<Role> result) {
		if (role == null) {
			return result;
		}

		result.add(role);
		return getRoleParents(role.getParent(), result);
	}

	@Override
	@Transactional
	public void createRole(Role role) {
		roleDao.save(role);
	}

	@Override
	public Role findRoleByName(String name) {
		return roleDao.findByName(name);
	}

	public static void main(String[] args) {
		User user = new User();
		user.setPlainPassword("12345678");
		new UserServiceImpl().entryptPassword(user);
		System.out.println(
				String.format("plainPassword: 12345678, password:%s, salt: %s", user.getPassword(), user.getSalt()));
	}

	@Override
	public Role findRoleByNameAndRealm(String name, String realm) {
		return roleDao.findRoleByNameAndRealm(name, realm);
	}

	@Override
	@Transactional
	public void createPrivilege(Privilege privilege) {
		privilegeDao.save(privilege);
	}

	@Override
	public Privilege findPrivilegeByTargetAndMethod(String target, String method) {
		return privilegeDao.findByTargetAndMethod(target, method);
	}

	@Override
	public Privilege findPrivilegeByTargetAndMethodAndScope(String target, String method, String scope) {
		return privilegeDao.findByTargetAndMethodAndScope(target, method, scope);
	}

	@Override
	public List<Privilege> getUserAllPrivileges(User user) {
		List<Role> roles = this.getUserAllRoles(user);
		List<Privilege> result = Lists.newArrayList();

		for (Role role : roles) {
			result.addAll(role.getPrivileges());
		}
		return Lists.newArrayList(Sets.newHashSet(result));
	}

	@Override
	public Page<User> findAllUsers(Collection<SearchFilter> searchFilters, Pageable pageable) {
		Specifications<User> spec = Specifications.where(bySearchFilter(searchFilters, User.class));
		Specification<User> specification = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = Lists.newArrayList();
				Path<String> loginNameExp = root.get("loginName");
				predicates.add(cb.notEqual(loginNameExp, "admin"));
				if (predicates.size() > 0) {
					return cb.and(predicates.toArray(new Predicate[predicates.size()]));
				}
				return cb.conjunction();
			}
		};
		spec = spec.and(Specifications.where(specification));
		return userDao.findAll(spec, pageable);
	}

	@Override
	public Privilege findPrivilegeByName(String name) {
		return privilegeDao.findByName(name);
	}

	@Override
	@Transactional
	public void addPrivilegesToRole(Role role, Privilege... privileges) {
		Preconditions.checkNotNull(role);
		Preconditions.checkArgument(privileges != null && privileges.length > 0);

		for (Privilege p : privileges) {
			role.addPrivilege(p);
		}
		roleDao.save(role);
	}

	@Override
	public List<Privilege> getAllPrivileges() {
		return Lists.newArrayList(privilegeDao.findAll());
	}

	@Override
	public User findUserById(Long id) {
		return userDao.findOne(id);
	}

	@Override
	public Privilege findPrivilegeById(Long id) {
		return privilegeDao.findOne(id);
	}

	@Override
	@Transactional
	public void updatePrivilege(Privilege privilege) {
		privilegeDao.save(privilege);
	}

	@Override
	public Page<Privilege> findAllPrivileges(Pageable pageable) {
		return privilegeDao.findAll(pageable);
	}

	@Override
	@Transactional
	public void deletePrivilege(Long id) {
		privilegeDao.deletePrivileges(id);
	}

	@Override
	public Page<Privilege> findAllPrivileges(Collection<SearchFilter> filters, Pageable pageable) {
		return privilegeDao.findAll(DynamicSpecifications.bySearchFilter(filters, Privilege.class), pageable);
	}

	@Override
	public Role findRoleById(Long id) {
		return roleDao.findOne(id);
	}

	@Override
	@Transactional
	public void updateRole(Role role) {
		roleDao.save(role);
	}

	@Override
	@Transactional
	public void deleteRole(Long id) {
		roleDao.deleteRole(id);
	}

	@Override
	public Page<Role> findAllRoles(Collection<SearchFilter> searchFilters, Pageable pageable) {
		return roleDao.findAll(DynamicSpecifications.bySearchFilter(searchFilters, Role.class), pageable);
	}

	@Override
	@Transactional
	public void removePrivilegeFromRole(Long roleId, Long privilegeId) {
		Role role = this.findRoleById(roleId);
		if (role == null) {
			throw new ServiceException("id为" + roleId + "的角色不存在");
		}
		Privilege privilege = this.findPrivilegeById(privilegeId);
		if (privilege == null) {
			throw new ServiceException("id为" + privilegeId + "权限不存在");
		}

		role.removePrivilege(privilege);

		roleDao.save(role);
	}

	@Override
	@Transactional
	public void addPrivilegeToRole(final Long roleId, final Long privilegeId) {
		Role role = this.findRoleById(roleId);
		if (role == null) {
			throw new ServiceException("id为" + roleId + "的角色不存在");
		}

		boolean exists = Iterables.any(role.getPrivileges(), new com.google.common.base.Predicate<Privilege>() {
			@Override
			public boolean apply(Privilege input) {
				return input.getId().equals(privilegeId);
			}
		});

		if (exists) {
			throw new ServiceException("该角色已存在要添加的权限");
		}

		Privilege privilege = this.findPrivilegeById(privilegeId);
		if (privilege == null) {
			throw new ServiceException("id为" + privilegeId + "权限不存在");
		}

		role.addPrivilege(privilege);
		roleDao.save(role);
	}

	@Override
	public Iterable<Role> findAllRoles() {
		return roleDao.findAll();
	}

	@Override
	@Transactional
	public void changeUserStatus(Long id) {
		User user = this.findUserById(id);
		if (user == null) {
			throw new ServiceException("id为" + id + "用户不存在");
		}

		if (Objects.equal(user.getStatus(), UserStatus.active.getValue())) {
			user.setStatus(UserStatus.disabled.getValue());
		} else {
			user.setStatus(UserStatus.active.getValue());
		}

		userDao.save(user);
	}

	@Override
	public Iterable<Role> getUserRoles(Long userId) {
		return userRoleDao.findAllRolesByUserId(userId);
	}

	@Override
	@Transactional
	public void removeRoleFromUser(Long userId, Long roleId) {
		User user = this.findUserById(userId);
		if (user == null) {
			throw new ServiceException("id为" + userId + "用户不存在");
		}
		Role role = this.findRoleById(roleId);
		if (role == null) {
			throw new ServiceException("id为" + roleId + "的角色不存在");
		}

		UserRole userRole = userRoleDao.findByUserIdAndRoleId(userId, roleId);

		userRoleDao.delete(userRole);
	}

	@Override
	@Transactional
	public void addRoleToUser(final Long userId, final Long roleId) {
		User user = this.findUserById(userId);
		if (user == null) {
			throw new ServiceException("id为" + userId + "用户不存在");
		}

		boolean exists = Iterables.any(this.getUserRoles(userId), new com.google.common.base.Predicate<Role>() {
			@Override
			public boolean apply(Role input) {
				return input.getId().equals(roleId);
			}
		});

		if (exists) {
			throw new ServiceException("该角色已经添加");
		}

		Role role = this.findRoleById(roleId);
		if (role == null) {
			throw new ServiceException("id为" + roleId + "的角色不存在");
		}

		UserRole userRole = new UserRole(user, role);
		userRoleDao.save(userRole);
	}

	@Override
	public Page<User> findUsersByOrganization(Long orgId, Pageable pageable) {
		return userOrganizationDao.findUsersByOrganizationId(orgId, pageable);
	}

	@Override
	public Page<User> findAllUsersByOrganization(Long orgId, Pageable pageable) {
		Organization org = organizationService.findOrganizationById(orgId);
		Preconditions.checkNotNull(org);

		return userOrganizationDao.findAllUsersByOrganizationCode(org.getCode(), pageable);
	}

	@Override
	public List<User> findUsersByOrganization(Long orgId) {
		return userOrganizationDao.findUsersByOrganizationId(orgId);
	}

	@Override
	public List<User> findAllUsersByOrganization(Long orgId) {
		Organization org = organizationService.findOrganizationById(orgId);
		Preconditions.checkNotNull(org);

		return userOrganizationDao.findAllUsersByOrganizationCode(org.getCode());
	}

	@Override
	@Transactional
	public void registerUserAndAddToOrganization(User user, Long orgId) {
		this.registerUser(user);
		this.addUserToOrganization(orgId, user.getId());
	}

	@Override
	@Transactional
	public void addUserToOrganization(Long orgId, Long userId) {
		Preconditions.checkNotNull(orgId, "机构Id不能为空");
		Preconditions.checkNotNull(userId, "用户Id不能为空");

		Organization org = organizationService.findOrganizationById(orgId);
		Preconditions.checkNotNull(org, "机构不存在");

		User user = this.findUserById(userId);
		Preconditions.checkNotNull(user, "用户不存在");

		UserOrganization uo = new UserOrganization(user, org);
		userOrganizationDao.save(uo);
	}

	@Override
	@Transactional
	public void updateUserAndChangeOrganization(User user, Long orgId) {
		this.updateUser(user);
		List<UserOrganization> uos = userOrganizationDao.findByUserId(user.getId());

		userOrganizationDao.delete(uos);

		if (orgId != null && orgId > 0) {
			this.addUserToOrganization(orgId, user.getId());
		}
	}

	@Override
	public Long getUserOrganizationId(Long userId) {
		Organization org = getUserOrganization(userId);
		if (org == null) {
			return null;
		}
		return org.getId();
	}

	@Override
	public Organization getUserOrganization(Long userId) {
		List<Organization> orgs = userOrganizationDao.findOrganizationsByUserId(userId);
		if (orgs.isEmpty()) {
			return null;
		}
		return orgs.get(0);
	}

	@Override
	public Page<User> findAllUsersNotInOrganizations(Pageable pageable) {
		return userOrganizationDao.findUsersNotInOrganizations(pageable);
	}

	@Override
	@Transactional
	public void changeUserPassword(Long id, String newPassword) {
		User user = this.findUserById(id);
		if (user == null) {
			throw new ServiceException("id为" + id + "用户不存在");
		}
		user.setPlainPassword(newPassword);
		this.updateUser(user);
	}

	@Override
	public List<Role> getAllRolesByPerm(String perm) {
		List<Role> roles = getRolesByPerm(perm);
		List<Role> ret = Lists.newArrayList();
		flattenRoles(roles, ret);
		return Lists.newArrayList(Sets.newLinkedHashSet(ret));
	}

	private void flattenRoles(List<Role> roles, List<Role> result) {
		for (Role role : roles) {
			result.add(role);
			flattenRoles(role.getChildren(), result);
		}
	}

	@Override
	public List<Role> getRolesByPerm(String perm) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(perm));

		String[] ps = perm.split(":");
		if (ps.length == 1) {
			return (roleDao.findByPerm(ps[0]));
		} else if (ps.length == 2) {
			return (roleDao.findByPerm(ps[0], ps[1]));
		} else {
			return (roleDao.findByPerm(ps[0], ps[1], ps[2]));
		}
	}

	@Override
	public List<User> getUsersByRole(Role role) {
		return roleDao.findUsersByRoleId(role.getId());
	}

	@Override
	public List<User> getUsersByPerms(String... perms) {
		if (perms == null) {
			return ImmutableList.of();
		}

		List<List<User>> lusers = Lists.newArrayList();
		for (String perm : perms) {
			// 找到具有此权限的所有(包括所有下级角色)角色
			List<Role> roles = getAllRolesByPerm(perm);

			// 将找到角色对应的用户集合叠加
			List<User> roleUsers = Lists.newArrayList();
			for (Role role : roles) {
				roleUsers.addAll(getUsersByRole(role));
			}

			lusers.add(roleUsers);
		}
		// 对上一个步骤的集合取交集
		Set<User> us = Sets.newHashSet(lusers.get(0));
		for (int i = 1; i < lusers.size(); i++) {
			us = Sets.intersection(us, Sets.newHashSet(lusers.get(i)));
		}

		return Lists.newArrayList(us);
	}

	@Override
	public List<User> getUsersByOrganizationAndPerm(Long orgId, String perm) {
		// TODO 性能优化
		List<User> users = this.getUsersByPerms(perm);
		List<User> result = Lists.newArrayList();
		for (User user : users) {
			if (Objects.equal(this.getUserOrganizationId(user.getId()), orgId)) {
				result.add(user);
			}
		}
		return result;
	}

	@Override
	public List<Privilege> findPrivilegesByRole(Long roleId, Sort sort) {
		return privilegeDao.findPrivilegesById(roleId, sort);
	}

	// @Async
	@Transactional
	@Override
	// TODO 支持异步
	public Future<Boolean> updateUserLastLoginTime(String loginID, Date time) {
		User user = this.findUserByLoginID(loginID);
		if (user == null) {
			return new AsyncResult<Boolean>(false);
		}

		user.setLastLoginTime(time);
		userDao.save(user);

		return new AsyncResult<Boolean>(true);
	}

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setPrivilegeDao(PrivilegeDao privilegeDao) {
		this.privilegeDao = privilegeDao;
	}

	@Autowired
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	@Autowired
	public void setUserRoleDao(UserRoleDao userRoleDao) {
		this.userRoleDao = userRoleDao;
	}

	@Autowired
	public void setUserOrganizationDao(UserOrganizationDao userOrganizationDao) {
		this.userOrganizationDao = userOrganizationDao;
	}

	public void setClock(Clock clock) {
		this.clock = clock;
	}

	@Autowired
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}
