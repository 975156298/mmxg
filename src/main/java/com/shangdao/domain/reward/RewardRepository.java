package com.shangdao.domain.reward;

import com.shangdao.domain.BaseRepository;
import com.shangdao.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface RewardRepository extends BaseRepository<Reward> {

    Reward findRewardByUser(User user);

    @Query("select new map(u.id as uid,u.name as username,u.mobile as mobile,r.totalBP as totalBP,u.createdAt as createdAt) from Reward r JOIN r.user u where u.inviter.id= :inviter")
    Page<Map> findAllFriend(@Param("inviter") Integer user,Pageable pageable);
    @Query("select new map(u.id as uid,u.name as username,u.mobile as mobile,r.totalBP as totalBP,u.createdAt as createdAt) from Reward r JOIN r.user u on u=:user")
    Map getUserMap(@Param("user") User user);
}