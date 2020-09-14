package com.store.common.util.cent;

import com.store.common.util.BigDecimalUtil;
import com.store.common.util.StringUtil;

import java.math.BigDecimal;

public class CentTranslateUtil {
    /**
     * 功能：分 转 元，会：保留两位小数
     * @param num
     * @param centFlag true：保留 分位，不保留 分为
     * @return
     *  1、传入 null 或 ""，返回 null
     *  2、传入 0，返回0
     *  3、其他均会保留小数点后两位
     *      如：1 返回 0.01         -1 返回 -0.01
     *          10 返回 0.10        -10 返回 -0.10
     *          100 返回 1.00       -100 返回 -1.00
     *          1000 返回 10.00     -1000 返回 -10.00
     *  4、+1、+10 禁止此中形式入参
     */
    public static String cent2Yuan(String num, boolean centFlag) {
        if (StringUtil.isEmpty(num)) {
            return null;
        }
        if (num.equals("0")) {
            return "0";
        }
        String sign = "";
        String result = null;
        //处理负号
        if (num.matches("^-.*")) {
            num = num.substring(1);
            sign = "-";
        }
        if (num.length() >= 3) {
            result = num.substring(0, num.length() - 2) + "." + num.substring(num.length() - 2);
        } else if (num.length() == 2){
            result = "0."+num;
        } else if (num.length() == 1){
            result = "0.0"+num;
        }
        if (!centFlag) {
            result = result.substring(0, result.length() - 1);
            if (BigDecimalUtil.isZERO(new BigDecimal(result))) {
                return "0";
            }
        }
        return sign + result;
    }

    /**
     * 功能：元 转 分
     * @param num 入参小数点后最多带两位形式
     * @return
     *  1 返回 100        +1 返回 +100      -1 返回 -100
     *  10 返回 1000      +10 返回 +1000    -10 返回 -1000
     *  0.1 返回 010      +0.1 返回 +010    -0.1 返回 -010
     *  0.01 返回 001     +0.01 返回 +001   -0.01 返回 -001
     */
    public static String expandTimes100(String num) {
        if (StringUtil.isEmpty(num)) {
            return null;
        }
        String sign = "";
        String result = null;
        //处理负号
        if (num.matches("^-.*")) {
            num = num.substring(1);
            sign = "-";
        }
        int i = num.indexOf(".");
        if ("0".equals(num)) {//为0
            sign = "";
            result = "0";
        } else if (i == -1) {//非小数
            result = num + "00";
        } else if (num.substring(i + 1).length() == 0) {//有个小数点
            result = num.substring(0, i) + "00";
        } else if (num.substring(i + 1).length() == 1) {//1位小数
            result = num.substring(0, i) +  num.substring(i + 1) + "0";
        } else if (num.substring(i + 1).length() == 2) {//两位小数
            result = num.substring(0, i) +  num.substring(i + 1);
        } else {//两位以上小数
            result = num.substring(0, i) + num.substring(i + 1, i + 3);
        }
        return sign + result;
    }
}