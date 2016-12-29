package com.lqr;

/**
 * @创建者 CSDN_LQR
 * @描述 用于RecyclerView条目的数据类
 */
public class DataBean {
    private String text;
    private int resId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
