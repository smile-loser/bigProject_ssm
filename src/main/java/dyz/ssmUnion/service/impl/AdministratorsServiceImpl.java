package dyz.ssmUnion.service.impl;

import dyz.ssmUnion.dao.AdministratorsDao;
import dyz.ssmUnion.daomain.Administrators;
import dyz.ssmUnion.service.AdministratorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author ：DYZ
 * @date ：Created By 2020/12/11 19:48
 */
@Service("administratorsService")
public class AdministratorsServiceImpl implements AdministratorsService {


    @Autowired
    private AdministratorsDao ad;

    @Override
    public Administrators findIt(Administrators loginUser) {
        Administrators it=null;
        try {
            it = ad.findIt(loginUser);
        } catch (Exception e) {
            System.out.println("账户不存在");
            it=null;
        }
        return it;
    }
}
