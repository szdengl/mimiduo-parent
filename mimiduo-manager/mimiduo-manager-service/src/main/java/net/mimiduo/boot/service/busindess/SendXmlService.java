package net.mimiduo.boot.service.busindess;


import net.mimiduo.boot.pojo.business.Channel;
import net.mimiduo.boot.pojo.business.Sell;
import net.mimiduo.boot.pojo.business.Visit;

public interface SendXmlService {

    /**
     * 下发销量
     * @param visit
     * @return
     */
    String sendSell(Visit visit);

    /***
     * 下发通道
     * @param sell
     * @param channel
     * @param total
     * @return
     */
    String sendXml(Sell sell, Channel channel, int total);

    /***
     * 回复通道
     * @param channel
     * @param smsContent
     * @param jsonContent
     * @param next
     * @param sell
     * @return
     */
    String sendWapXml(Channel channel, String smsContent, String jsonContent, int next, Sell sell);
}
