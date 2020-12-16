package dyz.ssmUnion.dao;



import dyz.ssmUnion.daomain.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *用户操作的dao
 */
@Repository
public interface UserDao {


    /**
     * 因为user的id
     * 根据提交信息创建用户
     * @param user
     */
    @Update("insert into user(name,gender,age,address,qq,email) values(#{name},#{gender},#{age},#{address},#{qq},#{email})")
    public void addUser(User user);

    /**
     * 根据ID删除
     * @param userId
     */
    @Delete("delete from user where id=#{userId}")
    public void delUser(int userId);

    /**
     * 更改用户信息
     * @param user
     */
    @Update("update user set name=#{name},gender=#{gender},age=#{age},qq=#{qq},email=#{email} where id=#{id} ")
    public void updateUser(User user);


    /**
     * 根据Id找到那个用户，来修改对象
     * @param id
     * @return
     */
    @Select("select * from user where id=#{id}")
    public User findById(Integer id);

    /**
     * 分页操作中实现统计所有记录个数,或根据条件来统计·
     *  like #{username} 和 '%${value}%'，这两种方式的实现效果是一样的，但执行的语句是不一样的
     *  参数为Map,map中则是#{key,jdbcType=VARCHAR}
     *  基本数据类型：包含int,String,Date等。基本数据类型作为传参，只能传入一个。通过#{参数名} 即可获取传入的值
     * 复杂数据类型：包含JAVA实体类、Map。通过#{属性名}
     * @return
     */
    @Select("<script> select count(*) from user  "+
                "  <where>   " +
                    "  <if test=' name!=null '>   " +
                        "   and   name like  '%${name}%'  " +
                    " </if>     " +
                    "  <if test=' address!=null  '>  " +
                        "    and  address like  '%${address}%' " +
                    "  </if>      " +
                    "  <if test=' email!=null '>    " +
                        " and    email like  '%${email}%'   " +
                    "  </if>   " +
                "  </where>    "+
            "  </script>")
    public int findTotalCount(@Param("name") String name,@Param("email")String email,@Param("address")String address);



    /**
     * 在分页操作中找到要展示那一页的所有User信息
     * @param start
     * @param rows
     * @return
     */
    @Select("<script>  select * from user  " +
            " <where>    " +
            " <if test=' name!=null  '>    " +
            "   and   name like  CONCAT('%',#{name},'%') " +
            " </if>   " +
            " <if test=' address!=null '>    " +
            "  and    address like  CONCAT('%',#{address},'%')  " +
            " </if>   " +
            " <if test=' email!=null '>    " +
            "  and   email like  CONCAT('%',#{email},'%')    " +
            " </if>  " +
            " </where>    "+
            "limit #{start},#{rows} "+
            " </script> ")
    public List<User> findByPage(@Param("start") Integer start, @Param("rows")Integer rows,@Param("name") String  name, @Param("address")String address,@Param("email") String email);
}
