package com.store.common.util;

import java.math.BigDecimal;

/**
 * 功能：精确计算工具类
 * 备注：BigDecimal 转 Long时候，要使用 xxx.longValue()
 * @author sunpeng
 * @date 2018
 */
public class BigDecimalUtil {

	//默认除法运算精度
	private static final int DEF_DIV_SCALE = 2;

	//构造器私有，让这个类不能实例化
	private BigDecimalUtil()	{}

	/**
	 * 功能：提供精确的加法运算
	 * @param v1 被加数
	 * @param v2 加数
	 * @return 两个参数的和
	 */
	public static BigDecimal add(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).stripTrailingZeros();
	}

	/**
	 * 功能：提供精确的加法运算
	 * @param v1 被加数
	 * @param v2 加数
	 * @return 两个参数的和
	 */
	public static BigDecimal add(BigDecimal v1, BigDecimal v2 ) {
		return v1.add(v2).stripTrailingZeros();
	}

	/**
	 * 功能：提供精确的减法运算
	 * @param v1 被减数
	 * @param v2 减数
	 * @return 两个参数的差
	 */
	public static BigDecimal sub(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.subtract(b2).stripTrailingZeros();
	}

	/**
	 * 功能：提供精确的减法运算
	 * @param v1 被减数
	 * @param v2 减数
	 * @return 两个参数的差
	 */
	public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
		return v1.subtract(v2).stripTrailingZeros();
	}

	/**
	 * 功能：提供精确的乘法运算
     * 备注：会自动抹掉多余的0
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return 两个参数的积
	 */
	public static BigDecimal mul(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).stripTrailingZeros();
	}

    /**
     * 功能：提供精确的乘法运算
     * 备注：会自动抹掉多余的0
     * @param b1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static BigDecimal mul(BigDecimal b1, String v2) {
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).stripTrailingZeros();
    }

	/**
	 * 功能：提供精确的乘法运算
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return 两个参数的积
	 */
	public static BigDecimal mul(BigDecimal v1, Integer v2) {
		return v1.multiply(new BigDecimal(String.valueOf(v2))).stripTrailingZeros();
	}

	/**
	 * 功能：提供（相对）精确的除法运算，当发生除不尽的情况时，精确到小数点以后 DEF_DIV_SCALE 位的数字四舍五入
     * 备注：
     *      BigDecimal.ROUND_HALF_UP 四舍五入模式
     *      ROUND_HALF_DOWN          向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则为上舍入的舍入模式，即：五舍六入
     *      ROUND_HALF_EVEN          银行家算法：
     *                                  如果要舍弃部分左边是 奇数，则舍入行为与 ROUND_HALF_UP（即：四舍五入）相同
     *                                  如果要舍弃部分左边是 偶数，则舍入行为与 ROUND_HALF_DOWN 相同
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 两个参数的商
	 */
	public static BigDecimal div(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2 , DEF_DIV_SCALE , BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
	}

	/**
	 * 功能：判断是否等于0
	 * @param v1
	 * @return true：相等，false：不等
	 */
	public static boolean isZERO(BigDecimal v1) {
		return 0 == v1.compareTo(BigDecimal.ZERO) ? true : false;
	}

	/**
	 * 功能：判断两个数，是否相等
	 * @param v1
	 * @param v2
	 * @return true：相等，false：不等
	 */
	public static boolean isEqual(BigDecimal v1, BigDecimal v2) {
		return 0 == v1.compareTo(v2) ? true : false;
	}

	/**
	 * 功能：第一个数是否大于第二个数
	 * @param v1
	 * @param v2
	 * @return true  ：第一个数 大于 第二个数
	 *          false ：第一个数 小于 第二个数
	 */
	public static boolean isGreaterThan(BigDecimal v1, BigDecimal v2) {
		return 1 == v1.compareTo(v2) ? true : false;
	}

	/**
	 * 功能：第一个数是否小于于第二个数
	 * @param v1
	 * @param v2
	 * @return true  ：第一个数 小于 第二个数
	 *          false ：第一个数 大于 第二个数
	 */
	public static boolean isSmallThan(BigDecimal v1, BigDecimal v2) {
		return -1 == v1.compareTo(v2) ? true : false;
	}

	/*
	public static void main(String[] args) {
        BigDecimal b2 = new BigDecimal("100.01");
        System.out.println(Long.valueOf(b2 + ""));
        System.out.println(b2.longValue());
    }
    */

    /*
	public static void main(String[] args) {
        System.out.println("0.05 + 0.01 = " + (0.05 + 0.01));
        System.out.println("1.0 - 0.42 = "  + (1.0 - 0.42));
        System.out.println("4.015 * 100 = " + (4.015 * 100));
        System.out.println("123.3 / 100 = " + (123.3 / 100));

        System.out.println("======================");

		System.out.println("0.05 + 0.01 = " + Arith.add("0.05", "0.01"));
		System.out.println("1.0 - 0.42 = "  + Arith.sub("1.0", "0.42"));
        System.out.println("4.019 * 100 = " + Arith.mul("4.019", "100"));
        System.out.println("4.019 * 100 double value = " + Arith.mul("4.019", "100").doubleValue());
        System.out.println("4.019 * 100 int value    = " + Arith.mul("4.019", "100").intValue());
		System.out.println("123.3 / 100 = " + Arith.div("123.3", "100"));
		System.out.println("123.3 / 100 double value = " + Arith.div("123.3", "100").doubleValue());
		System.out.println("123.3 / 100 int    value = " + Arith.div("123.3", "100").intValue());

        System.out.println("----------------------");

        System.out.println("123.3 / 100 = " + Arith.div("1", "9"));
        System.out.println("123.3 / 100 = " + Arith.div("1", "9").doubleValue());

        System.out.println("+++++++++++++++++++++");

        BigDecimal b1 = new BigDecimal("0.01111");
        BigDecimal b2 = new BigDecimal("0.01110");
        System.out.println(Arith.isEqual(b1, b2));
        System.out.println(Arith.isGreaterThan(b1, b2));

        System.out.println("----------------=====================");
        System.out.println("4.015 * 1000 double value = " + Arith.mul("4.019", "1000").doubleValue());

        BigDecimal c = new BigDecimal("0001030.0110010000990988000").stripTrailingZeros();
        System.out.println(c);

        mul("100.0", "100");
    }
    */

    /*
    public static void main(String[] args) {
        // 四舍五入
        System.out.println("120.465  -- "+new BigDecimal("120.465").divide(BigDecimal.valueOf(1), 2, BigDecimal.ROUND_HALF_UP));
        System.out.println("120.465  -- " + new BigDecimal(new BigDecimal(120.465).toPlainString() + "").divide(BigDecimal.valueOf(1), 2, BigDecimal.ROUND_HALF_UP));
        System.out.println("120.4652 -- "+new BigDecimal("120.4652").divide(BigDecimal.valueOf(1), 2, BigDecimal.ROUND_HALF_UP));

        System.out.println("120.465  -- "+new BigDecimal("120.465").divide(new BigDecimal("1"), 2, BigDecimal.ROUND_HALF_UP));
        System.out.println("120.465  -- " + new BigDecimal("120.465").divide(new BigDecimal("1"), 2, BigDecimal.ROUND_HALF_UP));
        System.out.println("120.4652 -- "+new BigDecimal("120.4652").divide(new BigDecimal("1"), 2, BigDecimal.ROUND_HALF_UP));

        System.out.println("-----------------");
        System.out.println("向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则向相邻的偶数舍入。\n" +
                "如果舍弃部分左边的数字为奇数，则舍入行为与 ROUND_HALF_UP 相同;\n" +
                "如果为偶数，则舍入行为与 ROUND_HALF_DOWN 相同。\n" +
                "注意，在重复进行一系列计算时，此舍入模式可以将累加错误减到最小。\n" +
                "此舍入模式也称为“银行家舍入法”，主要在美国使用。四舍六入，五分两种情况。\n" +
                "如果前一位为奇数，则入位，否则舍去。\n" +
                "以下例子为保留小数点1位，那么这种舍入方式下的结果。");
        System.out.println("四舍六入，五分两种");

        System.out.println("120.465  -- "+new BigDecimal("120.465").divide(new BigDecimal("1"), 2, BigDecimal.ROUND_HALF_EVEN));
        System.out.println("120.485  -- "+new BigDecimal("120.485").divide(new BigDecimal("1"), 2, BigDecimal.ROUND_HALF_EVEN));
        System.out.println("120.445  -- "+new BigDecimal("120.445").divide(new BigDecimal("1"), 2, BigDecimal.ROUND_HALF_EVEN));
        System.out.println("120.4652 -- "+new BigDecimal("120.4652").divide(new BigDecimal("1"), 2, BigDecimal.ROUND_HALF_EVEN));

        System.out.println("--------------");

        System.out.println("120.4262 -- "+new BigDecimal("120.4262").divide(new BigDecimal("1"), 2, BigDecimal.ROUND_HALF_EVEN));
        System.out.println("120.4472 -- "+new BigDecimal("120.4472").divide(new BigDecimal("1"), 2, BigDecimal.ROUND_HALF_EVEN));
        System.out.println("120.4642 -- "+new BigDecimal("120.4642").divide(new BigDecimal("1"), 2, BigDecimal.ROUND_HALF_EVEN));
        System.out.println("120.4832 -- "+new BigDecimal("120.4832").divide(new BigDecimal("1"), 2, BigDecimal.ROUND_HALF_EVEN));
    }
    */

    /*
    public static void main(String[] args) {
        System.out.println(div("1070", "100").longValue());
        System.out.println(div("1013", "100").longValue());
        System.out.println(div("1014", "100").longValue());
        System.out.println(div("1015", "100").longValue());
        System.out.println(div("1016", "100").longValue());
        System.out.println(div("1017", "100").longValue());
    }
    */
}