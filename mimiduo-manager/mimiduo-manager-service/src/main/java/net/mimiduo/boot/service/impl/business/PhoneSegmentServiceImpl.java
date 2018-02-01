package net.mimiduo.boot.service.impl.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import net.mimiduo.boot.common.util.TrimNumberUtils;
import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.bussiness.PhoneSegmentDao;
import net.mimiduo.boot.pojo.business.PhoneSegment;
import net.mimiduo.boot.pojo.data.MobileAttribute;
import net.mimiduo.boot.service.busindess.PhoneSegmentService;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhoneSegmentServiceImpl extends BaseServiceImpl<PhoneSegment,String> implements PhoneSegmentService {

    @Autowired
    private PhoneSegmentDao phoneSegmentDao;

    @Override
    public BaseDao<PhoneSegment, String> getDao() {
        return this.phoneSegmentDao;
    }

    private final String haoUrl = " http://apis.haoservice.com/mobile";

    @Override
    public MobileAttribute setSell(String mobile) {
        MobileAttribute mobileAttribute = null;
        Optional<PhoneSegment> phoneSegmentOpt = Optional.fromNullable(this.findOne(mobile.substring(0, 7)));
        if (phoneSegmentOpt.isPresent()) {
            PhoneSegment phoneSegment = phoneSegmentOpt.get();
            mobileAttribute = new MobileAttribute();
            mobileAttribute.setProvince(phoneSegment.getProvince());
            mobileAttribute.setCity(phoneSegment.getCity());
            mobileAttribute.setOperator(phoneSegment.getIsp());
        } else {
            if (savePhoneSegment(mobile)) {
                setSell(mobile);
            }
        }
        return mobileAttribute;
    }

    private boolean savePhoneSegment(String mobile) {
        boolean bResult = false;
        String httpArg = "phone=" + mobile;
        String result = TrimNumberUtils.thirdApi(haoUrl, httpArg, "GET");
        JSONObject object = JSON.parseObject(result);
        if (object.getIntValue("error_code") == 0) {
            JSONObject jsonObject = (JSONObject) object.getJSONObject("result");
            PhoneSegment phoneSegment = new PhoneSegment();
            phoneSegment.setCity(jsonObject.get("city").toString());
            phoneSegment.setProvince(jsonObject.get("province").toString());
            phoneSegment.setZip(jsonObject.get("zip").toString());
            phoneSegment.setPhone(mobile.substring(0, 7));
            phoneSegment.setCode(jsonObject.get("areacode").toString());
            phoneSegment.setPrefix(mobile.substring(0, 3));
            phoneSegment.setTypes(jsonObject.get("card").toString());
            String company = jsonObject.get("company").toString();
            if (company.contains("移动")) {
                phoneSegment.setIsp(4);
            } else if (company.contains("联通")) {
                phoneSegment.setIsp(5);
            } else if (company.contains("电信")) {
                phoneSegment.setIsp(6);
            }
            this.save(phoneSegment);
            bResult = true;
        }
        return bResult;
    }

}
