package org.example.q01;

public class DService {

    private final CService cService;

    // TODO(exercise): 尝试把其中一侧改成 setter 注入，验证循环依赖是否可被解析。
    // TODO(exercise): Change one side to setter injection and compare results.
    public DService(CService cService) {
        this.cService = cService;
    }

    public CService getCService() {
        return cService;
    }
}
