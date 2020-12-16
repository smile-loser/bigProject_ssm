package dyz.ssmUnion.dao;


import dyz.ssmUnion.daomain.Administrators;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorsDao {

    /**
     * 查询用户
     * @param loginUser
     * @return
     */
    @Select("select * from Administrators where username=#{username}")
    public Administrators findIt(Administrators loginUser);


}
