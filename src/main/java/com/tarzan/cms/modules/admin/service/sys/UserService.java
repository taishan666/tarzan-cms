package com.tarzan.cms.modules.admin.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.cms.auth.cache.SystemCache;
import com.tarzan.cms.auth.constant.AppConstant;
import com.tarzan.cms.auth.model.UserInfo;
import com.tarzan.cms.auth.model.UserOauth;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.modules.admin.mapper.sys.UserMapper;
import com.tarzan.cms.modules.admin.mapper.sys.UserRoleMapper;
import com.tarzan.cms.modules.admin.model.sys.User;
import com.tarzan.cms.modules.admin.model.sys.UserRole;
import com.tarzan.cms.modules.admin.vo.UserOnlineVo;
import com.tarzan.cms.shiro.redis.RedisCacheManager;
import com.tarzan.cms.shiro.redis.RedisSessionDAO;
import com.tarzan.cms.utils.Func;
import lombok.AllArgsConstructor;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class UserService extends ServiceImpl<UserMapper, User> {


    private final RedisSessionDAO redisSessionDAO;
    private final SessionManager sessionManager;
    private final RedisCacheManager redisCacheManager;
    private final UserRoleMapper userRoleMapper;
    private final RoleService roleService;

    private static UserOnlineVo getSessionBo(Session session) {
        //获取session登录信息。
        Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if (null == obj) {
            return null;
        }
        //确保是 SimplePrincipalCollection对象。
        if (obj instanceof SimplePrincipalCollection) {
            SimplePrincipalCollection spc = (SimplePrincipalCollection) obj;
            obj = spc.getPrimaryPrincipal();
            if (obj instanceof User) {
                User user = (User) obj;
                //存储session + user 综合信息
                UserOnlineVo userBo = new UserOnlineVo();
                //最后一次和系统交互的时间
                userBo.setLastAccess(session.getLastAccessTime());
                //主机的ip地址
                userBo.setHost(user.getLoginIpAddress());
                //session ID
                userBo.setSessionId(session.getId().toString());
                //最后登录时间
                userBo.setLastLoginTime(user.getLastLoginTime());
                //回话到期 ttl(ms)
                userBo.setTimeout(session.getTimeout());
                //session创建时间
                userBo.setStartTime(session.getStartTimestamp());
                //是否踢出
                userBo.setSessionStatus(false);
                /*用户名*/
                userBo.setUsername(user.getUsername());
                return userBo;
            }
        }
        return null;
    }

    public User selectByUsername(String username) {
        return baseMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username).eq(User::getStatus, CoreConst.STATUS_VALID));
    }

    public boolean register(User user) {
        return save(user);
    }

    public void updateLastLoginTime(User user) {
        Assert.notNull(user, "param: user is null");
        user.setLastLoginTime(new Date());
        updateById(user);
    }

    public IPage<User> selectUsers(User user, Integer pageNumber, Integer pageSize) {
        IPage<User> page = new Page<>(pageNumber, pageSize);
        page=page(page,Wrappers.<User>lambdaQuery()
                .like(StringUtils.isNotBlank(user.getUsername()),User::getUsername,user.getUsername())
                .like(StringUtils.isNotBlank(user.getEmail()),User::getEmail,user.getEmail())
                .like(StringUtils.isNotBlank(user.getPhone()),User::getPhone,user.getPhone())
                .eq(User::getStatus,CoreConst.STATUS_VALID)
                .orderByDesc(User::getCreateTime));
        if(CollectionUtils.isNotEmpty(page.getRecords())){
            List<Integer> userIds=page.getRecords().stream().map(User::getId).collect(Collectors.toList());
            Map<Integer,String> map=roleService.findRoleNameByUserIds(userIds);
            page.getRecords().forEach(e->{
                e.setRoleName(map.get(e.getId()));
            });
         }
        return page;
    }

    public User selectByUserId(Integer userId) {
        return getById(userId);
    }

    public boolean updateByUserId(User user) {
        Assert.notNull(user, "param: user is null");
        user.setUpdateTime(new Date());
        return updateById(user);
    }

    public boolean updateStatusBatch(List<Integer> userIds, Integer status) {
        return update(Wrappers.<User>lambdaUpdate().in(User::getId, userIds)
                .set(User::getStatus, status).set(User::getUpdateTime, new Date()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void addAssignRole(Integer userId, List<String> roleIds) {
        userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userId));
        for (String roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(Integer.valueOf(roleId));
            userRoleMapper.insert(userRole);
        }
    }

    public List<UserOnlineVo> selectOnlineUsers(UserOnlineVo userVo) {
        // 因为我们是用redis实现了shiro的session的Dao,而且是采用了shiro+redis这个插件
        // 所以从spring容器中获取redisSessionDAO
        // 来获取session列表.
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        Iterator<Session> it = sessions.iterator();
        List<UserOnlineVo> onlineUserList = new ArrayList<UserOnlineVo>();
        // 遍历session
        while (it.hasNext()) {
            // 这是shiro已经存入session的
            // 现在直接取就是了
            Session session = it.next();
            //标记为已提出的不加入在线列表
            if (session.getAttribute("kickOut") != null) {
                continue;
            }
            UserOnlineVo onlineUser = getSessionBo(session);
            if (onlineUser != null) {
                /*用户名搜索*/
                if (StringUtils.isNotBlank(userVo.getUsername())) {
                    if (onlineUser.getUsername().contains(userVo.getUsername())) {
                        onlineUserList.add(onlineUser);
                    }
                } else {
                    onlineUserList.add(onlineUser);
                }
            }
        }
        return onlineUserList;
    }

    public void kickOut(Serializable sessionId, String username) {
        getSessionBySessionId(sessionId).setAttribute("kickOut", true);
        //读取缓存,找到并从队列中移除
        Cache<String, Deque<Serializable>> cache = redisCacheManager.getCache(CoreConst.SHIRO_REDIS_CACHE_NAME);
        Deque<Serializable> deques = cache.get(username);
        for (Serializable deque : deques) {
            if (sessionId.equals(deque)) {
                deques.remove(deque);
                break;
            }
        }
        cache.put(username, deques);
    }

    private Session getSessionBySessionId(Serializable sessionId) {
        return sessionManager.getSession(new DefaultSessionKey(sessionId));
    }


    public UserInfo userInfo(Long userId) {
        User user = SystemCache.getUser(userId);
        return buildUserInfo(user);
    }

    public UserInfo userInfo(String tenantId, String account, String password) {
        Collection<User> users= SystemCache.getAllUser();
        User user = users.stream()
                .filter(e -> Func.equals(0l, tenantId) && (Func.equals(e.getUsername(), account)||Func.equals(e.getPhone(), account)) &&Func.equals(e.getPassword(), password) && Func.equals(e.getStatus(), AppConstant.YES))
                .findFirst().orElse(null);
        if (Objects.isNull(user)) {
            return null;
        }
        return buildUserInfo(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserInfo userInfo(UserOauth userOauth) {
     //   UserOauth uo = userOauthService.getOne(Wrappers.<UserOauth>query().lambda().eq(UserOauth::getUuid, userOauth.getUuid()).eq(UserOauth::getSource, userOauth.getSource()));
        UserInfo userInfo = new UserInfo();
/*        if (Func.isNotEmpty(uo) && Func.isNotEmpty(uo.getUserId())) {
            userInfo = this.userInfo(uo.getUserId());
            userInfo.setOauthId(Func.toStr(uo.getId()));
        } else {
            userInfo = new UserInfo();
            if (Func.isEmpty(uo)) {
                userOauthService.save(userOauth);
                userInfo.setOauthId(Func.toStr(userOauth.getId()));
            } else {
                userInfo.setOauthId(Func.toStr(uo.getId()));
            }
            User user = new User();
            user.setAccount(userOauth.getUsername());
            userInfo.setUser(user);
        }*/
        return userInfo;
    }

    /**
     * 方法描述: 构建用户信息
     *
     * @param user
     * @Return {@link UserInfo}
     * @throws
     */
    private UserInfo buildUserInfo(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUser(user);
        return userInfo;
    }


}
