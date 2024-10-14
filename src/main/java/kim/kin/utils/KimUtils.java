package kim.kin.utils;


import jakarta.servlet.http.HttpServletRequest;

/**
 * 工具类
 * @author choky
 */
public class KimUtils {
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null
                && "XMLHttpRequest".equals(request.getHeader("X-Requested-With")));
    }
}
