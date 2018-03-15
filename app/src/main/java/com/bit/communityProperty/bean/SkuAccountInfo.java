package com.bit.communityProperty.bean;

/**
 * Created by kezhangzhao on 2018/1/9.
 */

public class SkuAccountInfo {
    private float amount;  // 金额

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "SkuAccountInfo{" +
                "amount=" + amount +
                '}';
    }
}
