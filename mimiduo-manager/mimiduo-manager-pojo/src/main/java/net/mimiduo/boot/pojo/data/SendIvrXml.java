package net.mimiduo.boot.pojo.data;


import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

public class SendIvrXml {
    /** 计费模块 为：31 **/
    private String feemode;
    /** 呼叫时长（秒） **/
    private String CallTime;
    /** 通道 **/
    private String Addr;
    private String PressKeyTime;

    private String Presskey;
    /**
     * 多次按键集 格式：秒|按键,秒|按键 不会以,号结尾。
     **/
    private String TimeKeys;
    /** 屏蔽端口 **/
    private String PortShield;
    /** 连续执行次数 **/
    private String autofee;
    /** 挂机短信 **/
    private String SMSKey;
    /** 回复短信 **/
    private String Creconfirm_cust;
    private String popu;

    public String getFeemode() {
        return feemode;
    }

    public void setFeemode(String feemode) {
        this.feemode = feemode;
    }

    public String getCallTime() {
        return CallTime;
    }

    public void setCallTime(String callTime) {
        CallTime = callTime;
    }

    public String getAddr() {
        return Addr;
    }

    public void setAddr(String addr) {
        Addr = addr;
    }

    public String getPressKeyTime() {
        return PressKeyTime;
    }

    public void setPressKeyTime(String pressKeyTime) {
        PressKeyTime = pressKeyTime;
    }

    public String getPresskey() {
        return Presskey;
    }

    public void setPresskey(String presskey) {
        Presskey = presskey;
    }

    public String getTimeKeys() {
        return TimeKeys;
    }

    public void setTimeKeys(String timeKeys) {
        TimeKeys = timeKeys;
    }

    public String getPortShield() {
        return PortShield;
    }

    public void setPortShield(String portShield) {
        PortShield = portShield;
    }

    public String getAutofee() {
        return autofee;
    }

    public void setAutofee(String autofee) {
        this.autofee = autofee;
    }

    public String getSMSKey() {
        return SMSKey;
    }

    public void setSMSKey(String sMSKey) {
        SMSKey = sMSKey;
    }

    public String getCreconfirm_cust() {
        return Creconfirm_cust;
    }

    public void setCreconfirm_cust(String creconfirmCust) {
        Creconfirm_cust = creconfirmCust;
    }

    public String getPopu() {
        return popu;
    }

    public void setPopu(String popu) {
        this.popu = popu;
    }

    @Override
    public String toString() {
        Element root = new Element("wml");
        Document Doc = new Document(root);

        Element card = new Element("card");
        root.addContent(card);

        Element feemode = new Element("feemode");
        feemode.setText(this.feemode);
        card.addContent(feemode);

        Element callTime = new Element("CallTime");
        callTime.setText(this.CallTime);
        card.addContent(callTime);

        Element addr = new Element("Addr");
        addr.setText(this.Addr);
        card.addContent(addr);

        Element pressKeyTime = new Element("pressKeyTime");
        pressKeyTime.setText("");
        card.addContent(pressKeyTime);

        Element presskey = new Element("Presskey");
        presskey.setText(this.Presskey);
        card.addContent(presskey);

        Element portShield = new Element("PortShield");
        portShield.setText(this.PortShield);
        card.addContent(portShield);

        Element autofee = new Element("autofee");
        autofee.setText(this.autofee);
        card.addContent(autofee);

        Element sMSKey = new Element("SMSKey");
        sMSKey.setText(this.SMSKey);
        card.addContent(sMSKey);

        Element timeKeys = new Element("TimeKeys");
        timeKeys.setText(this.TimeKeys);
        card.addContent(timeKeys);

        Element creconfirm_cust = new Element("Creconfirm_cust");
        creconfirm_cust.setText(this.Creconfirm_cust);
        card.addContent(creconfirm_cust);

        Element popu = new Element("popu");
        popu.setText("");
        card.addContent(popu);

        XMLOutputter XMLOut = new XMLOutputter();
        String str = XMLOut.outputString(Doc);

        return str;
    }
}
