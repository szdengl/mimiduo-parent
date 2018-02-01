package net.mimiduo.boot.pojo.data;

public class MobileAttribute {
      private String province;
      private String city;
      private int operator;

      public MobileAttribute(){}

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }
}
