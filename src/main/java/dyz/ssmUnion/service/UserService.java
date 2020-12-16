package dyz.ssmUnion.service;

import dyz.ssmUnion.daomain.PageBean;
import dyz.ssmUnion.daomain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author ：DYZ
 * @date ：Created By 2020/12/11 19:47
 */
public interface UserService {


    public void addUser(User user);

    public void delUser(int UserId);

    public void updateUser(User user);

    public User findUserById(Integer id);

    public void delSelectedUsers(String[] ids);

    public PageBean<User> findUsersByPage(int currentPage, Map<String, String[]> allParam);

    public Integer getTotalPage(Map<String,String[]> condition);
}
