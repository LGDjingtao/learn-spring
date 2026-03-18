package org.example.q01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BService {

    private AService aService;

    // TODO(exercise): 尝试删除 @Autowired 并改用字段注入，对比差异。
    @Autowired
    public void setAService(AService aService) {
        this.aService = aService;
    }

    public AService getAService() {
        return aService;
    }
}
