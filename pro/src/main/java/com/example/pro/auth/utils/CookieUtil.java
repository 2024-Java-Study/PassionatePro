package com.example.pro.auth.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void addCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(false);
        cookie.setDomain("localhost");
        cookie.setAttribute("SameSite", "None");
        cookie.setMaxAge(300);

        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }
//
//    public static String serialize(Object obj) {
//        return Base64.getUrlEncoder()
//                .encodeToString(SerializationUtils.serialize(obj));
//    }
//
//    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
//        return cls.cast(
//                SerializationUtils.deserialize(
//                        Base64.getUrlDecoder().decode(cookie.getValue())
//                )
//        );
//    }
}
