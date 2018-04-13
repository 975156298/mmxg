package com.shangdao.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
//import org.springframework.boot.autoconfigure.orm.jpa.
@Configuration
@ConditionalOnClass(name = "javax.persistence.EntityManagerFactory")
@ConditionalOnBean(name = "entityManagerFactory")
public class HibernateConfig {
    @Bean
    public SessionFactory getSessionFactory(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        EntityManagerFactory entityManagerFactory=entityManagerFactoryBean.getObject();
        if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        return entityManagerFactory.unwrap(SessionFactory.class);
    }
}
