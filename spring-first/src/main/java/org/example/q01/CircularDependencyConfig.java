package org.example.q01;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {AService.class, BService.class})
public class CircularDependencyConfig {
    // TODO(exercise): 把 A/B 改为构造器注入后，对比容器行为。
}
