package org.example;

import org.example.q01.AService;
import org.example.q01.BService;
import org.example.q01.CService;
import org.example.q01.DService;
import org.example.q01.LazyAService;
import org.example.q01.LazyBService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppTest {

    @Test
    void setterCycleCanBeResolvedWithCircularReferencesEnabled() {
        try (var context = new SpringApplicationBuilder(App.class)
                .properties("spring.main.web-application-type=none")
                .properties("spring.main.allow-circular-references=true")
                .run()) {
            AService aService = context.getBean(AService.class);
            BService bService = context.getBean(BService.class);
            assertSame(bService, aService.getBService());
            assertSame(aService, bService.getAService());
        }
    }

    @Test
    void constructorCycleCannotBeResolved() {
        UnsatisfiedDependencyException ex = assertThrows(UnsatisfiedDependencyException.class, () -> {
            try (var context = new AnnotationConfigApplicationContext(ConstructorCycleConfig.class)) {
                context.getBean(CService.class);
            }
        });
        assertNotNull(ex.getMostSpecificCause());
        assertSame(BeanCurrentlyInCreationException.class, ex.getMostSpecificCause().getClass());
    }

    @Test
    void lazyProxyCanBreakConstructorCycle() {
        try (var context = new AnnotationConfigApplicationContext(LazyCycleConfig.class)) {
            LazyAService lazyAService = context.getBean(LazyAService.class);
            LazyBService lazyBService = context.getBean(LazyBService.class);
            assertNotNull(lazyAService.getLazyBService());
            assertSame(lazyAService, lazyBService.getLazyAService());
        }
    }

    @Configuration
    static class ConstructorCycleConfig {

        @Bean
        CService cService(DService dService) {
            return new CService(dService);
        }

        @Bean
        DService dService(CService cService) {
            return new DService(cService);
        }
    }

    @Configuration
    static class LazyCycleConfig {

        @Bean
        LazyAService lazyAService(@Lazy LazyBService lazyBService) {
            return new LazyAService(lazyBService);
        }

        @Bean
        LazyBService lazyBService(LazyAService lazyAService) {
            return new LazyBService(lazyAService);
        }
    }
}
