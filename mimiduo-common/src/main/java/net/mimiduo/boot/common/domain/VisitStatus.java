package net.mimiduo.boot.common.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import net.mimiduo.boot.common.util.ValueTextable;

/***
 * 用户访问结果状态
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum  VisitStatus implements ValueTextable<Integer> {
    sendXml(0,"下发通道"), success(1,"计费成功"), projectLimit(2,"项目超限"),noChannel(3,"没有通道可用"),channelLimit(4,"业务超限"), black(5,"黑名单"),clientClose(6,"客户关闭"),projectClose(7,"项目关闭");
    VisitStatus(Integer value, String text) {
		this.value = value;
		this.text = text;
    }

    // 普通方法
    public static String getName(Integer value) {
        for (VisitStatus visitStatus : VisitStatus.values()) {
            if (visitStatus.getValue().equals(value)) {
                return visitStatus.text;
            }
        }
        return null;
    }

    private Integer value;
    private String text;
    private boolean selected;
    private String description;

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }
}
