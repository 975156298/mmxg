package com.shangdao.domain.statistic;

import com.shangdao.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "statistic")
public class Statistic extends BaseEntity {
    private static final long serialVersionUID = -6052845972950259144L;
    @Column(name = "key_name")
    private String key;
    @Column(name = "key_value")
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
