package com.shangdao.domain.config;

import com.shangdao.domain.BaseEntity;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "config")
@Cacheable
public class Config extends BaseEntity {
    private static final long serialVersionUID = 725161360936618453L;

    private String strkey;
    private String strvalue;

    public String getStrkey() {
        return strkey;
    }

    public void setStrkey(String strkey) {
        this.strkey = strkey;
    }

    public String getStrvalue() {
        return strvalue;
    }

    public void setStrvalue(String strvalue) {
        this.strvalue = strvalue;
    }
}
