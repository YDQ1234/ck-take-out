package fun.cyhgraph.utils;

import java.math.BigDecimal;

public class ValidatePriceUtil {
    /**
     * 完整校验价格是否合法
     *
     * @param price        待校验价格
     * @param maxScale     允许的最大小数位数（如 2 代表最多保留角分）
     * @param maxAllowPrice 允许的最大金额（防止异常超大金额）
     * @return 校验通过返回 null，不通过返回错误提示信息
     */
    public static String validatePrice(BigDecimal price, int maxScale, BigDecimal maxAllowPrice) {
        String mesg= "";
        // 1. 非空校验
        if (price == null) {
            return "价格不能为空";
        }

        // 2. 正数校验（价格必须 > 0）
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            return "价格必须大于0";
        }

        // 3. 小数位数校验（超过指定位数不合法）
        if (price.scale() > maxScale) {
            return "价格最多允许保留" + maxScale + "位小数";
        }

        // 4. 最大金额限制
        if (maxAllowPrice != null && price.compareTo(maxAllowPrice) > 0) {
            return "价格不能超过" + maxAllowPrice;
        }

        // 全部校验通过
        return mesg;
    }
}
