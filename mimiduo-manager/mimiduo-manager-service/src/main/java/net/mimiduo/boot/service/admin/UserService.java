package net.mimiduo.boot.service.admin;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;


import net.mimiduo.boot.pojo.admin.Organization;
import net.mimiduo.boot.pojo.admin.Privilege;
import net.mimiduo.boot.pojo.admin.Role;
import net.mimiduo.boot.pojo.admin.User;
import net.mimiduo.boot.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springside.modules.persistence.SearchFilter;



public interface UserService extends BaseService<User, Long> {

    /**
     * 通过登录名查询用户.
     * 
     * @param loginname
     *            登录名
     */
    User findUserByLoginName(String loginname);

    /**
     * 通过登录标识查询用户.
     * 
     * @param loginID
     *            登录名、注册邮箱或手机号
     */
    User findUserByLoginID(String loginID);

    /**
     * 是当前用户的密码?
     * 
     * @param plainPassword
     *            未加密前的密码.
     */
    boolean isCurrentUserPassword(String plainPassword);

    /**
     * 获取当前会话用户信息.
     */
    SessionUser getCurrentSessionUser();

    /**
     * 获取当前会话用户.
     */
    User getCurrentUser();

    /**
     * 是超级管理员(系统第一个用户)?
     * 
     * @param id
     *            用户id
     */
    boolean isSupervisor(Long id);

    /**
     * 更新用户最近一次登录的时间.
     * 
     */
    Future<Boolean> updateUserLastLoginTime(String loginId, Date time);

    /**
     * 删除用户.
     */
    void deleteUser(Long id);

    /**
     * 更新用户.
     */
    void updateUser(User user);

    /**
     * 注册用户.
     */
    void registerUser(User user);

    /**
     * 获取用户.
     * 
     * @param id
     *            用户id
     */
    User getUser(Long id);

    /**
     * 获取所有用户.
     */
    List<User> getAllUser();

    /**
     * 分配角色到用户.
     * 
     */
    void assignRolesToUser(User user, Role... roles);

    /**
     * 获取用户所有角色的列表.
     * 
     */
    List<Role> getUserAllRoles(User user);

    /**
     * 创建角色.
     * 
     */
    void createRole(Role role);

    /**
     * 通过name查找角色.
     * 
     */
    Role findRoleByName(String name);

    /**
     * 通过name和realm查找角色.
     * 
     */
    Role findRoleByNameAndRealm(String name, String realm);

    /**
     * 创建权限.
     */
    void createPrivilege(Privilege privilege);

    /**
     * 通过名称查找权限.
     */
    Privilege findPrivilegeByName(String name);

    /**
     * 通过资源对象和方法找权限.
     */
    Privilege findPrivilegeByTargetAndMethod(String target, String method);

    /**
     * 通过资源对象、方法和数据范围找权限.
     */
    Privilege findPrivilegeByTargetAndMethodAndScope(String target, String method, String scope);

    /**
     * 查找用户所有的权限.
     */
    List<Privilege> getUserAllPrivileges(User user);

    /**
     * 查找所有用户.
     */
    Page<User> findAllUsers(Collection<SearchFilter> searchFilters, Pageable pageable);

    /**
     * 将权限添加给角色.
     */
    void addPrivilegesToRole(Role role, Privilege... privileges);

    /**
     * 获取所有权限列表.
     */
    List<Privilege> getAllPrivileges();

    /**
     * 通过Id找用户.
     */
    User findUserById(Long id);

    /**
     * 通过Id找权限.
     */
    Privilege findPrivilegeById(Long id);

    /**
     * 更新权限.
     */
    void updatePrivilege(Privilege privilege);

    /**
     * 查找所有的权限.
     */
    Page<Privilege> findAllPrivileges(Pageable pageable);

    /**
     * 删除权限.
     */
    void deletePrivilege(Long id);

    /**
     * 通过过滤条件查找所有的权限.
     */
    Page<Privilege> findAllPrivileges(Collection<SearchFilter> filters, Pageable pageable);

    /**
     * 通过Id找角色.
     */
    Role findRoleById(Long id);

    /**
     * 更新角色.
     */
    void updateRole(Role entity);

    /**
     * 删除角色.
     */
    void deleteRole(Long id);

    /**
     * 通过过滤条件查所有的角色.
     */
    Page<Role> findAllRoles(Collection<SearchFilter> searchFilters, Pageable pageable);

    /**
     * 从角色中移除权限.
     */
    void removePrivilegeFromRole(Long roleId, Long privilegeId);

    /**
     * 将权限分配给角色.
     */
    void addPrivilegeToRole(Long roleId, Long privilegeId);

    /**
     * 查找所有的角色列表.
     */
    Iterable<Role> findAllRoles();

    /**
     * 改变用户状态.
     */
    void changeUserStatus(Long id);

    /**
     * 获取用户的直接管理的角色.
     */
    Iterable<Role> getUserRoles(Long userId);

    /**
     * 将角色分配给用户.
     */
    void addRoleToUser(Long userId, Long roleId);

    /**
     * 取消分配用户的某个角色.
     */
    void removeRoleFromUser(Long userId, Long roleId);

    /**
     * 通过组织机构查找其直接关联的所有用户.
     */
    Page<User> findUsersByOrganization(Long orgId, Pageable pageable);

    /**
     * 通过组织机构查找其直接关联的所有用户.
     */
    List<User> findUsersByOrganization(Long orgId);

    /**
     * 通过组织机构查找其关联所有用户（包括各个下属部门的）.
     */
    Page<User> findAllUsersByOrganization(Long orgId, Pageable pageable);

    /**
     * 通过组织机构查找其关联所有用户（包括各个下属部门的）.
     */
    List<User> findAllUsersByOrganization(Long orgId);

    /**
     * 注册用户和并分配组织机构.
     */
    void registerUserAndAddToOrganization(User user, Long orgId);

    /**
     * 将用户添关联到某个组织机构.
     */
    void addUserToOrganization(Long orgId, Long userId);

    /**
     * 获取用户关联的组织机构Id.
     */
    Long getUserOrganizationId(Long userId);

    /**
     * 获取用户管理的组织机构.
     */
    Organization getUserOrganization(Long userId);

    /**
     * 更新用户信息和修改其关联的组织机构.
     */
    void updateUserAndChangeOrganization(User user, Long orgId);

    /**
     * 获取未关联组织机构的所有用户.
     */
    Page<User> findAllUsersNotInOrganizations(Pageable pageable);

    /**
     * 修改用户密码.
     */
    void changeUserPassword(Long id, String newPassword);

    /**
     * 获取具有某权限集的所有用户.
     */
    List<User> getUsersByPerms(String... perms);

    /**
     * 根据部门ID获取某部门下直接关联的具备某一权限的所有用户.
     */
    List<User> getUsersByOrganizationAndPerm(Long orgId, String perm);

    /**
     * 获取具有某权限的所有角色（包括继承相关的）.
     */
    List<Role> getAllRolesByPerm(String perm);

    /**
     * 获取具有某权限的直接管理所有角色.
     */
    List<Role> getRolesByPerm(String perm);

    /**
     * 获取拥有某个角色的所有用户.
     */
    List<User> getUsersByRole(Role role);

    /**
     * 获取角色直属的权限列表.
     */
    List<Privilege> findPrivilegesByRole(Long roleId, Sort sort);
}
