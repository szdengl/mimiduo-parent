package net.mimiduo.boot.service.impl.business;

import com.google.common.base.Optional;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.bussiness.ProvinceDao;
import net.mimiduo.boot.pojo.business.Province;
import net.mimiduo.boot.service.busindess.ProvinceService;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProvinceServiceImpl extends BaseServiceImpl<Province, Long> implements ProvinceService {

    @Autowired
    private ProvinceDao provinceDao;

    @Override
    public BaseDao<Province, Long> getDao() {
        return provinceDao;
    }

    @Override
    public boolean findByName(String name) {
        boolean bResult=false;
        if(StringUtils.isNotEmpty(name)&&!name.trim().equals("")){
            Province province=this.provinceDao.findByName(name);
            if(Optional.fromNullable(province).isPresent()) {
                bResult=true;
            }
        }
        return bResult;
    }

}
