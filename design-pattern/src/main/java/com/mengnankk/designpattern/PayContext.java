package com.mengnankk.designpattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PayContext {
    @Autowired
    private Map<String,PayStrategy> paymap;

    public void dopay(String type){
        PayStrategy strategy = paymap.get(type);
        if (strategy!=null) strategy.pay();
    }
}
 interface PayStrategy{
    void pay();
}
@Component("aliPay")
 class AliPay implements PayStrategy {
    public void pay() {
        System.out.println("支付宝支付");
    }
}
@Component("wechatPay")
 class WeChatPay implements PayStrategy {
    public void pay() { System.out.println("微信支付"); }
}


