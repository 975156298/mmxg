package com.shangdao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangdao.domain.config.Config;
import com.shangdao.domain.config.ConfigLog;
import com.shangdao.domain.config.ConfigLogRepository;
import com.shangdao.domain.config.ConfigRepository;
import com.shangdao.util.ConfigEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
public class ConfigService {

    @Autowired
    ConfigRepository configRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ConfigLogRepository configLogRepository;
    public Config createOrUpdate(String key, String value) {
        Assert.hasText(key, "配置项名称不能为空");
        Assert.hasText(value, "配置项的值不能为空");
        Config config = configRepository.findConfigByStrkey(key);
        if (config == null) {
            config = new Config();
            config.setStrkey(key);
        }

        String diffStr = diff(config.getStrvalue(),value);

        ConfigLog log =new ConfigLog();
        log.setConfig(config);
        log.setDiff(diffStr);
        configLogRepository.save(log);


        config.setStrvalue(value);

        configRepository.save(config);
        return config;
    }

    @PostConstruct
    public void postConstruct() {
        createByKeyAndValue(ConfigEnum.POUNDAGE.toString(), "0.00");//初始化一个手续费
        createByKeyAndValue(ConfigEnum.CURRENT_PRICE.toString(), "1.00");//即时价格
        createByKeyAndValue(ConfigEnum.EXTRACTION_SPEED.toString(),"0.001");//开采速度
        createByKeyAndValue(ConfigEnum.FREEZE_MULTIPLE.toString(),"5");//冻结倍数
        createByKeyAndValue(ConfigEnum.FIRST_BONUS.toString(),"0.10");//第一层提成
        createByKeyAndValue(ConfigEnum.SECOND_BONUS.toString(),"0.60");//第二层提成
    }

    private void createByKeyAndValue(String key, String value) {
        Config config = configRepository.findConfigByStrkey(key);//即时价格
        if (config == null) {
            config = new Config();
            config.setStrkey(key);
            config.setStrvalue(value);
            configRepository.save(config);
        }
    }

    public BigDecimal getCurrentPrice()
    {
        Config currentPrice = configRepository.findConfigByStrkey(ConfigEnum.CURRENT_PRICE.toString());//当前价格
        Assert.notNull(currentPrice, "当前价格读取失败，请联系工作人员");
        try {
            double price = Double.valueOf(currentPrice.getStrvalue());
            return BigDecimal.valueOf(price);

        }catch (NumberFormatException e){
            throw new RuntimeException("当前价格配置错误，请联系工作人员");
        }

    }

    public BigDecimal getPoundage(){
        Config configPoundage = configRepository.findConfigByStrkey(ConfigEnum.POUNDAGE.toString());//手续费
        Assert.notNull(configPoundage, "手续费读取失败，请联系工作人员");
        try {
            double price = Double.valueOf(configPoundage.getStrvalue());
            return BigDecimal.valueOf(price);

        }catch (NumberFormatException e){
            throw new RuntimeException("当前手续费配置错误，请联系工作人员");
        }
    }
    public BigDecimal getFreezeMultiple()
    {
        Config config = configRepository.findConfigByStrkey(ConfigEnum.FREEZE_MULTIPLE.toString());//手续费
        Assert.notNull(config, "冻结倍数读取失败，请联系工作人员");
        try {
            double price = Double.valueOf(config.getStrvalue());
            return BigDecimal.valueOf(price);

        }catch (NumberFormatException e){
            throw new RuntimeException("当前冻结倍数配置错误，请联系工作人员");
        }
    }

    public BigDecimal getFirstBonus()
    {
        Config config = configRepository.findConfigByStrkey(ConfigEnum.FIRST_BONUS.toString());
        Assert.notNull(config, "第一层提成读取失败，请联系工作人员");
        try {
            double price = Double.valueOf(config.getStrvalue());
            return BigDecimal.valueOf(price);

        }catch (NumberFormatException e){
            throw new RuntimeException("当前第一层提成配置错误，请联系工作人员");
        }
    }
    public BigDecimal getSecondBonus()
    {
        Config config = configRepository.findConfigByStrkey(ConfigEnum.SECOND_BONUS.toString());
        Assert.notNull(config, "第二层提成读取失败，请联系工作人员");
        try {
            double price = Double.valueOf(config.getStrvalue());
            return BigDecimal.valueOf(price);

        }catch (NumberFormatException e){
            throw new RuntimeException("当前第二层提成配置错误，请联系工作人员");
        }
    }

    private String diff(String  oldValue,String newValue){
        return "{" + "\"oldValue\":\"" + oldValue + "\"," +
                "\"newValue\":\"" + newValue + "\"}";
    }
}
