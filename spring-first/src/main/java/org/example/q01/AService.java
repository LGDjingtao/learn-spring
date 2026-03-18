package org.example.q01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AService {

    private BService bService;

    // TODO(exercise): 改成构造器注入，再运行测试观察是否还能通过。
    @Autowired
    public void setBService(BService bService) {
        this.bService = bService;
    }

    public BService getBService() {
        return bService;
    }
}
