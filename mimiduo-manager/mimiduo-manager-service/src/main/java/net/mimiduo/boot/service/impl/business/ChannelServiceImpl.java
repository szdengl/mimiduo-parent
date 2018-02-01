package net.mimiduo.boot.service.impl.business;

import net.mimiduo.boot.common.domain.VisitStatus;
import net.mimiduo.boot.common.exception.ExecuteException;
import net.mimiduo.boot.common.util.DateUtils;
import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.bussiness.ChannelDao;
import net.mimiduo.boot.pojo.business.*;
import net.mimiduo.boot.pojo.data.MobileAttribute;
import net.mimiduo.boot.service.busindess.*;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.SearchFilter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ChannelServiceImpl extends BaseServiceImpl<Channel, Long> implements ChannelService {
    protected final Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);
    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private SellLimitService sellLimitService;

    @Autowired
    private SendXmlService sendXmlService;

    @Autowired
    private ChargingService chargingService;

    @Autowired
    private PhoneSegmentService phoneSegmentService;

    @Autowired
    private SellAccessService sellAccessService;

    @Autowired
    private ChannelProvinceService channelProvinceService;

    @Autowired
    private CompanyService companyService;

    @Override
    @Transactional(rollbackFor = ExecuteException.class)
    public void updateChannel(Channel channel){
        this.save(channel);
        if(channel.getIsstatus().equals("1")){
            channelProvinceService.updateByChannelId(channel.getId());
        }
    }

    @Override
    @Transactional(rollbackFor = ExecuteException.class)
    public void updateChannelByCompanyId(long id) {
        List<Channel> channelList=this.findByCompanyId(Long.valueOf(id).intValue());
        channelList.forEach(e->{
            this.channelProvinceService.updateByChannelId(e.getId());
        });
        this.deleteChannelByCompanyId(id);
    }

    @Override
    @Transactional(rollbackFor = ExecuteException.class)
    public void deleteChannelByCompanyId(long id) {
        List<Channel> channelList=this.findByCompanyId(Long.valueOf(id).intValue());
        channelList.forEach(e->{
            this.channelProvinceService.deleteByChannelId(e.getId());
        });
        this.deleteChannelByCompanyId(id);

    }

    @Override
    @Transactional(rollbackFor = ExecuteException.class)
    public void deleteChannel(long id){
        this.delete(id);
        this.channelProvinceService.deleteByChannelId(id);
    }



    @Override
    public Page<Channel> findAllChannels(Collection<SearchFilter> searchFilters, Pageable pageable) {
        return channelDao.findAll(bySearchFilter(searchFilters, Channel.class), pageable);
    }

    @Override
    public List<Channel> findByCompanyId(int companyId) {
        final String istatus = "0";
        return this.channelDao.findByCompanyIdAndIsstatus(companyId, istatus);
    }

    @Override
    public List<Channel> findByServiceType(int serviceType) {
        return this.channelDao.findByServiceType(serviceType);
    }


    private Channel findByProvinceIdAndisNew(Sell sell, boolean isNew, StringBuilder log) {
        int hour = LocalTime.now().getHour(); //当前时间
        List<ChannelProvince> channelProvinceList = this.channelProvinceService.findByProvinceIdAndIsstatusAndStartimeLessThanEqualAndEndtimeGreaterThanEqual(sell.getProvince(), 0, hour, hour);
        Channel channel = null;
        Collections.sort(channelProvinceList, Comparator.comparing(ChannelProvince::getOrderId));
        if (channelProvinceList != null && channelProvinceList.size() > 0) {
            for (ChannelProvince e : channelProvinceList) {
                channel = this.findOne(e.getChannelId());
                if (channel != null
                        && bCompany(channel.getCompanyId())
                        && channel.getIsstatus().equals("0")
                        && channel.getOperator() == sell.getOperator()
                        && !channel.getShieldCity().contains(sell.getCity()
                )) {
                    boolean bLimit = sellLimitService.bChannelLimit(sell, channel.getId());
                    log.append("超过业务日月限(" + channel.getName() + ")");
                    if (isNew && !bLimit) {
                        break;
                    } else {
                        if (channel.getFeeType() != 3 && !bLimit) {
                            break;
                        }
                    }
                }
                channel = null;
            }
        }
        return channel;
    }

    private boolean bCompany(long companyId) {
        Company company = this.companyService.findOne(companyId);
        if (company != null) {
            return company.getIsstatus() == 0;
        }
        return false;
    }

//	@Override
//	public List<Channel> findByProvinceIdAndisNew(Sell sell,boolean isNew){
//		Specification<Channel> spec = new Specification<Channel>() {
//			@Override
//			public Predicate toPredicate(Root<Channel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//				List<Predicate> predicate = Lists.newArrayList();
//
//				int hour= LocalTime.now().getHour(); //当前时间
//				Subquery<ChannelProvince> channelProvinceSubquery = criteriaQuery.subquery(ChannelProvince.class);
//				Root<ChannelProvince>  channelProvinceRoot=channelProvinceSubquery.from(ChannelProvince.class);
//				Path<Long> channelProvinceByChannelIdExp=channelProvinceRoot.get("channelId");
//				Path<Long> chanelBychannelIdExp=root.get("id");
//				Path<Integer> channelProvinceByStatusExp=channelProvinceRoot.get("isstatus");
//				Path<String> channelProvinceByProvinceIdExp=channelProvinceRoot.get("provinceId");
//				Path<Integer> channelProvinceByStartimeExp=channelProvinceRoot.get("startime");
//				Path<Integer> channelProvinceByEndtimeExp=channelProvinceRoot.get("endtime");
//				predicate.add(criteriaBuilder.equal(channelProvinceByChannelIdExp, chanelBychannelIdExp));
//				predicate.add(criteriaBuilder.equal(channelProvinceByProvinceIdExp, sell.getProvince()));
//				predicate.add(criteriaBuilder.equal(channelProvinceByStatusExp,0));
//				predicate.add(criteriaBuilder.le(channelProvinceByStartimeExp,hour));
//				predicate.add(criteriaBuilder.ge(channelProvinceByEndtimeExp,hour));
//				channelProvinceSubquery.where(predicate.toArray(new Predicate[predicate.size()])).select(channelProvinceRoot);
//
//				predicate.clear();
//				Subquery<Company> companySubquery=criteriaQuery.subquery(Company.class);
//				Root<Company> companyRoot=companySubquery.from(Company.class);
//				Path<Long> channelByCompanyIdExp=root.get("companyId");
//				Path<Long> companyByIdExp=companyRoot.get("id");
//				Path<Integer> companyByStatusExp=companyRoot.get("isstatus");
//				predicate.add(criteriaBuilder.equal(channelByCompanyIdExp,companyByIdExp));
//				predicate.add(criteriaBuilder.equal(companyByStatusExp,0));
//				companySubquery.where(predicate.toArray(new Predicate[predicate.size()])).select(companyRoot);
//
//				predicate.clear();
//				Path<String> channelByStatusExp=root.get("isstatus");
//				Path<Integer> channelByOperatorExp=root.get("operator");
//				Path<String>  channelByShieldCityExp=root.get("shieldCity");
//				Path<Integer> channelByFeeTypeExp=root.get("feeType");
//
//				predicate.add(criteriaBuilder.exists(channelProvinceSubquery));
//				predicate.add(criteriaBuilder.exists(companySubquery));
//				predicate.add(criteriaBuilder.equal(channelByOperatorExp,sell.getOperator()));
//				if(sell.getClientId().equals("MIMIDtest")){  //测试版
//					predicate.add(criteriaBuilder.equal(channelByStatusExp,"2"));
//				}
//				else{   //激活版
//					predicate.add(criteriaBuilder.equal(channelByStatusExp,"0"));
//				}
//				if(!isNew){  //旧版
//					predicate.add(criteriaBuilder.notEqual(channelByFeeTypeExp,3));
//				}
//				predicate.add(criteriaBuilder.notLike(channelByShieldCityExp,sell.getCity()));
//				Predicate[] pre = new Predicate[predicate.size()];
//				return criteriaQuery.where(criteriaBuilder.and(predicate.toArray(pre))).getRestriction();
//			}
//		};
//		return this.channelDao.findAll(spec);
//	}


    @Override
    public BaseDao<Channel, Long> getDao() {
        return channelDao;
    }

    @Override
    public List<Channel> findByCompanyIdAndCmdAndSpnumber(long companyId, String cmd, String spnumber) {
        return this.channelDao.findByCompanyIdAndCmdAndSpnumber(companyId, cmd, spnumber);
    }

    @Override
    public List<Channel> findByCompanyIdAndSpnumberAndFeeType(long companyId, String spnumber, int feeType) {
        return this.channelDao.findByCompanyIdAndSpnumberAndFeeType(companyId, spnumber, feeType);
    }


    @Override
    public String findBySellAndIsNew(Sell sell, Boolean isNew, Charging charging, Visit visit, StringBuilder log) {
        String result = "";
        Channel channel = findByProvinceIdAndisNew(sell, isNew,log);
        if (channel != null) {
            visit.setStatus(VisitStatus.sendXml.getValue());
            charging.setFee(channel.getFee());
            charging.setChannelId(channel.getId());
            charging.setCompanyId(channel.getCompanyId());
            charging.setFeeType(channel.getFeeType());
            result = sendXmlService.sendXml(sell, channel, 1);
            log.append("通道内容>>>" + result + "\r\n");
        } else {
            visit.setStatus(VisitStatus.noChannel.getValue());
            log.append(VisitStatus.noChannel.getText() + "\r\n");
        }
        return result;
    }

    @Override
    public void updateRecord(String mobile, String spnumber, String cmd, long companyId, int number, int feeType) {
        List<Channel> channelList = null;
        if (feeType != 2) {
            channelList = this.findByCompanyIdAndCmdAndSpnumber(companyId, cmd, spnumber);
        } else {
            channelList = this.findByCompanyIdAndSpnumberAndFeeType(companyId, spnumber, 2);
        }
        if (channelList != null && channelList.size() > 0) {
            Channel channel = channelList.get(0);
            List<Charging> chargingList = this.chargingService.findByCompanyIdAndChannelIdAndMobileAndStatusAndCreateDateBetween(companyId,
                    channel.getId(), mobile, VisitStatus.sendXml.getValue(), DateUtils.LocalDateToDate(LocalDate.now()), DateUtils.LocalDateToDate(LocalDate.now().plusDays(1)));
            if (chargingList == null || chargingList.size() < 1) {
                MobileAttribute mobileAttribute = phoneSegmentService.setSell(mobile);
                chargingList = this.chargingService.findByCompanyIdAndChannelIdAndProvinceAndCityAndStatusAndCreateDateBetween(companyId,
                        channel.getId(), mobileAttribute.getProvince(), mobileAttribute.getCity(), VisitStatus.sendXml.getValue(), DateUtils.LocalDateToDate(LocalDate.now()), DateUtils.LocalDateToDate(LocalDate.now().plusDays(1)));
                if (chargingList == null || chargingList.size() < 1) {
                    chargingList = this.chargingService.findByCompanyIdAndChannelIdAndProvinceAndStatusAndCreateDateBetween(companyId,
                            channel.getId(), mobileAttribute.getProvince(), VisitStatus.sendXml.getValue(), DateUtils.LocalDateToDate(LocalDate.now()), DateUtils.LocalDateToDate(LocalDate.now().plusDays(1)));
                }
            }
            if (channelList != null && chargingList.size() > 0) {
                sellAccessService.updateVisitRecord(chargingList.get(0), number, channel.getFee());
            }
        }


    }

}
