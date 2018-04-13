package com.shangdao.service;

import com.shangdao.domain.user.User;
import com.shangdao.domain.user.UserRepository;
import com.shangdao.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Optional;

public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    InviteService inviteService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AccountService accountService;
    @Autowired
    BPService bpService;
    @Autowired
    RewardService rewardService;

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        User user = userRepository.findUserByMobile(mobile);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return user;
    }

    @PostConstruct
    public void postConstruct() {
        String mobile = "12300000001";
        User user1 = userRepository.findUserByMobile(mobile);
        if (user1 == null) {
            user1 = register("testAdmin", "abc135462", mobile,null);
            user1.setType(User.Type.ADMIN);
            userRepository.save(user1);
        }
        String mobile2 = "12300000002";
        User user2 = userRepository.findUserByMobile(mobile2);
        if (user2 == null) {
            user2 = register("testUser", "1a2b34c56", mobile2,user1.getInviteCode());
            user2.setType(User.Type.NORMAL);
            user2.setInviter(user1);
            userRepository.save(user2);
        }

    }

    public boolean frontLogin(String mobile, String password) {
        Assert.hasText(mobile, "手机号不能为空");
        Assert.hasText(password, "密码不能为空");

        User user = userRepository.findUserByMobile(mobile);
        Assert.notNull(user, "用户不存在");

        if (user.getStatus().equals(User.Status.DISABLE)) {
            return false;
        }
        boolean matchResult = passwordEncoder.matches(password, user.getPassword());
        if (matchResult) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        }
        return false;
    }

    @Transactional
    public User register(String username, String password, String mobile,String inviteCode) {
        Assert.hasText(username, "用户名不能为空");
        Assert.hasText(password, "密码不能为空");
        Assert.hasText(mobile, "手机号不能为空");
        User user = userRepository.findUserByMobile(mobile);
        if (user != null) {
            throw new RuntimeException("手机号已申请过");
        }
        if (!mobile.matches("^1[\\d]{10}$")) {
            throw new RuntimeException("请输入正确的手机号");
        }
        user = new User();
        user.setName(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setMobile(mobile);
        user.setType(User.Type.NORMAL);
        user.setStatus(User.Status.NORMAL);
        user.setWalletAddress(CommonUtil.md5Encode(mobile));
        user.setInviteCode(CommonUtil.generateInviteCode());
        user =userRepository.save(user);
        if(inviteCode!=null) {
            User inviter = userRepository.findUserByInviteCode(inviteCode);
            if (inviter == null) {
                throw new RuntimeException("邀请码不存在");
            }
            user.setInviter(inviter);
            inviter.setInviteCode(CommonUtil.generateInviteCode());//重置邀请人密码
            userRepository.save(inviter);

            inviteService.createInviteBy(inviter,user);
        }
        userRepository.save(user);

        accountService.createByUser(user);

        bpService.createByUser(user);

        rewardService.findRewardByUser(user);

        return user;
    }

    public boolean setSecondPassword(String secondPassword) {
        Assert.hasText(secondPassword, "二级密码不能为空");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setSecondPassword(passwordEncoder.encode(secondPassword));
        userRepository.save(user);
        return true;
    }

    public boolean verifySecondPassword(String spwd, User user) {

        User flushUser=userRepository.findUserByWalletAddress(user.getWalletAddress());
        Assert.hasText(spwd, "请输入二级密码");
        Assert.hasText(flushUser.getSecondPassword(), "请设置二级密码");
        boolean result =  passwordEncoder.matches(spwd, flushUser.getSecondPassword());
        if(result){
            return true;
        }
        throw new RuntimeException("二级密码验证失败");
    }

    public void setAvatar(String avatarUrl) {
        Assert.hasText(avatarUrl,"头像url不能为空");
        User user = userRepository.findUserByMobile(CommonUtil.getPrincipal().getMobile());
        user.setAvatar(avatarUrl);
        userRepository.save(user);
    }

    public void resetPassword(Integer id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(!optionalUser.isPresent()){
            throw new RuntimeException("用户不存在");
        }

        User user =optionalUser.get();
        user.setPassword(passwordEncoder.encode("123456"));
        userRepository.save(user);
    }
    public void updatePassword(String oldPassWord,String newPassword){
        User user=userRepository.findUserByMobile(CommonUtil.getPrincipal().getMobile());
        if(!passwordEncoder.matches(oldPassWord,user.getPassword())){
            throw new RuntimeException("原先密码错误");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);


    }
}
