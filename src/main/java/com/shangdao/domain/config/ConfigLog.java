package com.shangdao.domain.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.domain.BaseEntity;
import com.shangdao.domain.user.User;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;

@Entity
@Table(name = "config_log")
public class ConfigLog extends BaseEntity {
    private static final long serialVersionUID = -306353718406743082L;
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_user")
    @JsonIgnore
    private User createdUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Config config;
    private String diff;

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }
}
