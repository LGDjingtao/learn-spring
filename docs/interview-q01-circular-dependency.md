# Q01：什么是循环依赖，Spring 怎么解决循环依赖？

## 1. 面试标准回答

### 30 秒版本

循环依赖是指 Bean A 依赖 Bean B，同时 Bean B 又依赖 Bean A。  
Spring 对**单例 + 非构造器注入**场景，可以通过“提前暴露对象”解决；对**构造器循环依赖**通常无法解决，会抛出 `BeanCurrentlyInCreationException`。

### 3 分钟版本

Spring 解决循环依赖的核心是三级缓存：

1. `singletonObjects`：一级缓存，存放完全初始化完成的单例对象。
2. `earlySingletonObjects`：二级缓存，存放提前暴露的“早期对象”。
3. `singletonFactories`：三级缓存，存放创建早期对象的工厂（用于处理可能的代理对象）。

当 A 创建过程中需要 B，B 又需要 A 时：

1. A 实例化后，先把可以生成 A 早期引用的工厂放入三级缓存。
2. 创建 B 时发现需要 A，容器会从三级缓存拿到 A 的早期引用（必要时经过代理）。
3. B 完成后回填给 A，最后 A/B 都完成初始化并进入一级缓存。

所以它能解决的是“对象先实例化，再属性注入”的循环；而构造器注入要求对象在构造时就拿到完整依赖，无法提前暴露。

## 2. 你要掌握的高频追问

1. `@Transactional` / AOP 代理下为什么要三级缓存而不是二级？
- 因为三级缓存存的是工厂，可在早期引用阶段返回代理对象，避免原始对象与代理对象不一致。

2. Spring Boot 为什么很多时候看起来“不能循环依赖”？
- 因为 Boot 默认 `spring.main.allow-circular-references=false`（建议保持默认）；开启后只是在允许的注入模型下可尝试解析。

3. 为什么构造器循环依赖通常解不了？
- 构造器阶段必须拿到完整依赖，无法像 Setter/字段注入那样先暴露半成品对象。

## 3. 代码练习路径（本仓库）

代码位置：

- `spring-first/src/main/java/org/example/q01/`
- `spring-first/src/test/java/org/example/AppTest.java`

本题有 3 个场景：

1. `AService <-> BService`：Setter 注入循环依赖（可解）
2. `CService <-> DService`：构造器注入循环依赖（不可解）
3. `LazyAService <-> LazyBService`：通过 `@Lazy` 打破构造器循环

## 4. 你要做的 TODO（按顺序）

1. 运行测试，先看到基准结果：

```bash
mvn -Dmaven.repo.local=.m2 test
```

2. 打开 `AService` 和 `BService`，完成注释里的 TODO：
- 把 Setter/字段注入与构造器注入互相切换。
- 每次改动后运行测试，记录哪些场景会失败。

3. 在 `CService` / `DService` 上尝试“修复”：
- 只改一侧为 Setter 注入，验证是否由不可解变为可解。

4. 在 `AppTest#lazyProxyCanBreakConstructorCycle` 对应配置中去掉 `@Lazy`，再跑测试：
- 观察失败信息，写下为什么失败。

## 5. 面试表达模板（可背）

“循环依赖本质是 Bean 之间相互引用。Spring 在单例 + 非构造器注入时通过三级缓存解决：一级放成品，二级放早期对象，三级放对象工厂，必要时可在早期阶段返回代理对象。构造器循环依赖因为必须在构造时拿到完整依赖，通常无法解决，会抛出 `BeanCurrentlyInCreationException`。在 Boot 中默认不鼓励循环依赖，通常建议通过重构依赖关系来消除。”

