package org.example.q01;

public class LazyBService {

    private final LazyAService lazyAService;

    public LazyBService(LazyAService lazyAService) {
        this.lazyAService = lazyAService;
    }

    public LazyAService getLazyAService() {
        return lazyAService;
    }
}
