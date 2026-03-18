package org.example.q01;

public class CService {

    private final DService dService;

    // TODO(exercise): 这里是构造器循环依赖的一侧，保持构造器注入并观察失败原因。
    // TODO(exercise): Keep constructor injection here to observe failure first.
    public CService(DService dService) {
        this.dService = dService;
    }

    public DService getDService() {
        return dService;
    }
}
