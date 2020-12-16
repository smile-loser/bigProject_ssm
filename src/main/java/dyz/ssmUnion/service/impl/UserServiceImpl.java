package dyz.ssmUnion.service.impl;

import dyz.ssmUnion.dao.UserDao;
import dyz.ssmUnion.daomain.PageBean;
import dyz.ssmUnion.daomain.User;
import dyz.ssmUnion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：DYZ
 * @date ：Created By 2020/12/11 19:48
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    @Override
    public void addUser(User user) {
        userDao.addUser(user);
    }

    @Override
    public void delUser(int userId) {
        userDao.delUser(userId);
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    public User findUserById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public void delSelectedUsers(String[] ids) {
        for(String id:ids)
        {
            userDao.delUser(Integer.parseInt(id));
        }
    }

    /**
     * 分页查询，可能进行的条件查询再分页查询,就是先查出来那一页是多少到多少
     * @return
     */
    @Override
    public PageBean<User> findUsersByPage(int currentPage, Map<String, String[]> allParam) {

        //1.创建空的PageBean对象
        PageBean<User> pb=new PageBean<User>();
        int rows=pb.getRows();
        //2.设置参数

        if(currentPage==0)
        {
            currentPage=1;
        }

        pb.setCurrentPage(currentPage);



        /*获得地址邮箱姓名，注意可能为空*/
        Map<String,String> condition=new HashMap<String,String>();
        String name=allParam.get("name")[0]!=null&&allParam.get("name")[0]!=""?allParam.get("name")[0]:null;
        String email=allParam.get("email")[0]!=null&&allParam.get("email")[0]!=""?allParam.get("email")[0]:null;
        String address=allParam.get("address")[0]!=null&&allParam.get("address")[0]!=""?allParam.get("address")[0]:null;

        condition.put("name",name);
        condition.put("email",email);
        condition.put("address",address);
        int totalCount=0;
        //3.用dao查询总记录数
        try{
            totalCount= userDao.findTotalCount(name,email,address);

        }catch (Exception e)
        {
            System.out.println("计算数目出现问题");
            e.printStackTrace();
        }

        System.out.println("总数量是"+totalCount);
        pb.setTotalCount(totalCount);



        //5.计算总页码
        int totalPage=(totalCount%rows)==0?totalCount/rows:(totalCount/rows)+1;

        pb.setTotalPage(totalPage);

        //4.调用dao查询这一页要 展示的List集合
        //计算开始的记录索引,


        int start=(currentPage-1)*rows;
        List<User> list=null;


        try{

            list=userDao.findByPage(start,rows,name,address,email);
        }catch (Exception e)
        {
            System.out.println("List出现问题");
            e.printStackTrace();
        }
        pb.setList(list);


        return pb;
    }


    /**
     * 这个方法在增加用户才会用到，直接跳转最后一页，所以address,name,email为null无所谓
     * @param allParam
     * @return
     */
    @Override
    public Integer getTotalPage(Map<String, String[]> allParam) {
        int row=5;

        int totalCount = userDao.findTotalCount(null,null,null);
        //该展示的总页数

        return (totalCount%row==0)?totalCount/row:(totalCount/row+1);
    }
}
