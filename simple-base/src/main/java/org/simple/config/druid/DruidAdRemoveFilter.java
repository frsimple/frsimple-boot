package org.simple.config.druid;

import com.alibaba.druid.util.Utils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 去掉druid底部广告
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/09/08
 */
public class DruidAdRemoveFilter extends OncePerRequestFilter {

    /**
     * common.js 的路径
     */
    private static final String COMMON_JS_ILE_PATH = "support/http/resources/js/common.js";

    /**
     * 过滤druid中的广告
     *
     * @param request  请求
     * @param response 返回
     * @param chain    过滤
     * @throws ServletException 请求异常
     * @throws IOException      io异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        chain.doFilter(request, response);
        // 重置缓冲区，响应头不会被重置
        response.resetBuffer();
        // 获取 common.js
        String text = Utils.readFromResource(COMMON_JS_ILE_PATH);
        // 正则替换 banner, 除去底部的广告信息
        text = text.replaceAll("<a.*?banner\"></a><br/>", "");
        text = text.replaceAll("powered.*?shrek.wang</a>", "");
        response.getWriter().write(text);
    }

}
