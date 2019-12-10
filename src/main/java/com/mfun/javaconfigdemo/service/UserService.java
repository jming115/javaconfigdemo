package com.mfun.javaconfigdemo.service;

import com.mfun.javaconfigdemo.dao.UserDao;
import com.mfun.javaconfigdemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: JMing
 * @Date: 2019/11/26 17:05
 * @Description:
 */

@Service
public class UserService {
    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    // 调用方法前，先检索该缓存是否存在，如果存在，直接返回缓存中的值，
    // 如果不存在，调用方法后，将结果缓存起来
    @Cacheable(cacheNames="userCache", key="'user_' + #p0")
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    // 使用事务
    @Transactional(rollbackFor = Exception.class)
    // 调用方法后，将结果缓存起来
    @CachePut(cacheNames="userCache", key="'user_' + #p0.getId()")
    public User addUser(User user) throws Exception {
        int result = userDao.add(user);
        if(result <= 0) {
            throw new Exception("用户添加失败");
        }
        User newUser = getUserById(user.getId());
        return newUser;
    }

    // 使用事务
    @Transactional(rollbackFor = Exception.class)
    // 更新用户信息，同时更新缓存
    @CachePut(cacheNames="userCache", key="'user_' + #p0")
    public User updateUser(Long id, User user) throws Exception {
        int result = userDao.updateById(id, user);
        if(result <= 0) {
            throw new Exception("用户更新失败");
        }
        User newUser = getUserById(id);
        return newUser;
    }
}
