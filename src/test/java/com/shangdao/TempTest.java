package com.shangdao;

import com.shangdao.util.CommonUtil;
import junit.framework.TestCase;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import sun.security.provider.MD5;

import java.util.UUID;

public class TempTest {
    @Test
    public void temp() {
        Integer integer =Integer.valueOf("1100");
        System.out.println(integer%100);
    }

    enum Type{
        A,
        B
    }
}
