package com.zll.program.ui.home;

/**
 * 商户端工作台 订单状态
 */
public enum HomeTypeEnum {

    NEW("新订单", 1), ING("进行中", 2), ALL("全部订单", 0);

    private String title;
    private int orderType;

    HomeTypeEnum(String title, int orderType) {
        this.title = title;
        this.orderType = orderType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public static int getOrderType(String title) {
        for (HomeTypeEnum c : HomeTypeEnum.values()) {
            if (c.getTitle().equals(title)) {
                return c.getOrderType();
            }
        }
        return ALL.getOrderType();
    }

    public static String getTitle(int orderType) {
        for (HomeTypeEnum c : HomeTypeEnum.values()) {
            if (c.getOrderType() == orderType) {
                return c.getTitle();
            }
        }
        return ALL.getTitle();
    }
}
