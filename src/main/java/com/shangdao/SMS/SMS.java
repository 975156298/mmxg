package com.shangdao.SMS;

import javax.servlet.http.HttpSession;

public interface SMS {
    void send(String mobile, String content);
    boolean verify(String code,String mobile);
}
