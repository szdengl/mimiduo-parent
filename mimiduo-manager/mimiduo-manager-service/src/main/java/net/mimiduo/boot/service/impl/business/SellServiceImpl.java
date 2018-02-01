package net.mimiduo.boot.service.impl.business;



import net.mimiduo.boot.common.util.TrimNumberUtils;
import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.bussiness.SellDao;
import net.mimiduo.boot.pojo.business.Sell;
import net.mimiduo.boot.pojo.business.SellAccess;
import net.mimiduo.boot.pojo.data.MobileAttribute;
import net.mimiduo.boot.service.busindess.PhoneSegmentService;
import net.mimiduo.boot.service.busindess.ProvinceService;
import net.mimiduo.boot.service.busindess.SellAccessService;
import net.mimiduo.boot.service.busindess.SellService;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SellServiceImpl extends BaseServiceImpl<Sell,Long> implements SellService {

    @Autowired
    private SellDao sellDao;

    @Autowired
    private SellAccessService sellAccessService;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private PhoneSegmentService phoneSegmentService;

    @Override
    public BaseDao<Sell, Long> getDao() {
        return this.sellDao;
    }

    @Override
    public Sell findByMobile(String mobile) {
        return  sellDao.findByMobile(mobile);
    }

    @Override
    public Sell findByImsi(String imsi) {
        return sellDao.findByImsi(imsi);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSellAndSellAccess(Sell sell, SellAccess sellAccess) {
        this.save(sell);
        this.sellAccessService.save(sellAccess);
    }

    @Override
    public Sell updateSellBySegment(Sell sell) {
        //处理手机号码、省份和城市
        if (!TrimNumberUtils.isMobile(sell.getMobile())) {
            sell.setMobile(TrimNumberUtils.trimTelNum(sell.getMobile()));
        }
        boolean isProvince=provinceService.findByName(sell.getProvince());
        if (!isProvince) {
            MobileAttribute mobileAttribute = this.phoneSegmentService.setSell(sell.getMobile());
            sell.setProvince(mobileAttribute.getProvince());
            sell.setCity(mobileAttribute.getCity());
            sell.setOperator(mobileAttribute.getOperator());
            this.save(sell);
        }
        return sell;
    }


}
