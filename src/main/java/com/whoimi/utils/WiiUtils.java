package com.whoimi.utils;


import jakarta.servlet.http.HttpServletRequest;

/**
 * 工具类
 * @author whoimi
 */
public class WiiUtils {
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null
                && "XMLHttpRequest".equals(request.getHeader("X-Requested-With")));
    }
}
