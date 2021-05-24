package kim.kin.kklog;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author choky
 */
@Component
@Aspect
public class LogAspect {
    private static final String UNKNOWN = "unknown";
    ThreadLocal<Long> currentTime = new ThreadLocal<>();
    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(kim.kin.kklog.KkLog)")
    public void logPointcut() {
    }

    /**
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        currentTime.set(System.currentTimeMillis());
        result = joinPoint.proceed();
        currentTime.remove();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        List<Object> collect = Arrays.stream(joinPoint.getArgs()).collect(Collectors.toList());
        Enumeration<String> headerNames = request.getHeaderNames();
/*        logger.error("header:------------------------------------------------");
        headerNames.asIterator().forEachRemaining(s -> {
            String header = request.getHeader(s);
            logger.info(s + " :" + header);
        });
        logger.error("header:------------------------------------------------");*/
        logger.info("ip:" + acquireIp(request) + " args:" + collect + " joinPoint:" + joinPoint);
        return result;
    }

    /**
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        logger.error(e.toString());
        currentTime.remove();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        logger.error("ip:" + acquireIp(request) + " joinPoint:" + joinPoint);
    }

    /**
     * acquire request ip
     */
    public static String acquireIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String comma = ",";
        List<String> localIps = Arrays.asList("127.0.0.1", "0:0:0:0:0:0:0:1", "localhost", "localhost.localdomain", "localhost4", "localhost4.localdomain4", "::1", "localhost6", "localhost6.localdomain6");
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if (localIps.contains(ip)) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                ip = "UNKNOWN_IP";
            }
        }
        return ip;
    }
}
