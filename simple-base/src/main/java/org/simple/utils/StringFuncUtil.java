package org.simple.utils;

import cn.hutool.core.util.ObjectUtil;
import org.simple.dto.UserInfo;
import dm.jdbc.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 转化SQL中的{}参数
 *
 * @author yh_liu
 * @version 1.0
 * @since 2022/09/20
 */

public class StringFuncUtil {
    /**
     * 根据正则表达式获取文本中的变量名列表
     *
     * @param content 内容
     * @return 参数列表
     */
    public static List<String> getParams(String content) {
        String reg = "\\{([^}]*)}";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(content);
        List<String> result = new ArrayList<>();
        while (m.find()) {
            result.add(m.group(1));
        }
        return result;
    }

    /**
     * 根据正则表达式将文本中的变量使用实际的数据替换成无变量的文本
     *
     * @param content  替换的内容
     * @param data     原始数据
     * @param userInfo 用户信息
     * @return 替换后的内容
     */
    public static String replaceRegString(String content, Map<String, Object> data, UserInfo userInfo) {
        List<String> listParam = getParams(content);
        for (String key : listParam) {
            String value = "";
            switch (key) {
                case "userId":
                    value = userInfo.getUserId().toString();
                    break;
                case "orgId":
                    value = userInfo.getOrgId().toString();
                    break;
                default:
                    if (ObjectUtil.isNotNull(data.get(key))) {
                        value = data.get(key).toString();
                    }
                    break;
            }
            if (StringUtil.isNotEmpty(value)) {
                content = content.replaceAll("\\{" + key + "}", value);
            }
        }
        return content;
    }
}
