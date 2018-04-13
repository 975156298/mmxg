package com.shangdao.util;

public abstract class RegexUtil {

    public static boolean isMobile(String mobile){
        if(mobile==null || mobile.isEmpty()){
            return false;
        }

        return mobile.matches("^1[\\d]{10}$");
    }
    public static boolean isInviteCode(String code){
        if(code==null || code.isEmpty()){
            return false;
        }
        return code.matches("([\\d]||[A-Z]){15}");
    }
}
