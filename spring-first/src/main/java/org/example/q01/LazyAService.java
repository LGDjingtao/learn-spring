package org.example.q01;

public class LazyAService {

    private final LazyBService lazyBService;

    public LazyAService(LazyBService lazyBService) {
        this.lazyBService = lazyBService;
    }

    public LazyBService getLazyBService() {
        return lazyBService;
    }
}
