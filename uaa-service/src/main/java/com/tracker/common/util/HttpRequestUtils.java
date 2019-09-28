package com.tracker.common.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * @author Martin Krumov
 */
public final class HttpRequestUtils {

    private static final Set<String> IP_HEADER_CANDIDATES = Set.of(
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    );

    private HttpRequestUtils() {
        throw new IllegalStateException("This is utility class");
    }

    public static String getClientIpAddress(HttpServletRequest request) {
        return IP_HEADER_CANDIDATES.stream()
                .filter(header -> isValidIpAddress(request.getHeader(header)))
                .findFirst()
                .map(ip -> ip.split(",")[0])
                .orElse(request.getRemoteAddr());
    }

    private static boolean isValidIpAddress(String ip) {
        return isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip);
    }
}
