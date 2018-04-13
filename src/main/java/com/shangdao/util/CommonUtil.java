package com.shangdao.util;

import com.shangdao.domain.user.User;
import com.shangdao.domain.user.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class CommonUtil {

    public static User getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null){
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return ((User) principal);
//            return userRepository.findUserByMobile(((User) principal).getMobile());
        }
        return null;
    }
    public static String generateInviteCode()
    {
        return RandomStringUtils.random(6, "ABCDEFGHIJKLMNOPQESTUVWXYZ0123456789");
    }
    public static String md5Encode(String password) {

        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static BigDecimal strConvertToBigDecimal(String value) {
        Assert.hasText(value, "转换的字符串不能为空");
        try {

            double number = Double.valueOf(value);
            return BigDecimal.valueOf(number);

        } catch (NumberFormatException e) {
            throw new RuntimeException("字符串不能转换为数字");
        }

    }

    public static Integer strConvertToInteger(String value) {
        Assert.hasText(value, "转换的字符串不能为空");
        try {

            return Integer.valueOf(value);

        } catch (NumberFormatException e) {
            throw new RuntimeException("字符串不能转换为数字");
        }

    }

    public static boolean isLogin() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User;
    }
}
