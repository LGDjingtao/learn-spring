# Q01: Circular Dependency and How Spring Handles It

## Standard interview answer

Circular dependency means `Bean A -> Bean B` and `Bean B -> Bean A`.

Spring can resolve it in the **singleton + non-constructor injection** case by early exposure.
Spring usually cannot resolve it in the **constructor injection cycle** case, and it ends with `BeanCurrentlyInCreationException` (wrapped by `UnsatisfiedDependencyException` in many startup paths).

## Core mechanism you should explain

Spring singleton creation uses a three-level cache:

1. `singletonObjects`: fully initialized singleton beans.
2. `earlySingletonObjects`: early bean references.
3. `singletonFactories`: factories that can produce early references (including proxy-aware references).

Why level-3 cache matters:

- With AOP/proxy scenarios, Spring may need to expose a proxy-capable early reference, not a raw object.

## Practice code in this repo

- Main exercise classes:
  - `spring-first/src/main/java/org/example/q01/AService.java`
  - `spring-first/src/main/java/org/example/q01/BService.java`
  - `spring-first/src/main/java/org/example/q01/CService.java`
  - `spring-first/src/main/java/org/example/q01/DService.java`
  - `spring-first/src/main/java/org/example/q01/LazyAService.java`
  - `spring-first/src/main/java/org/example/q01/LazyBService.java`
- Verification tests:
  - `spring-first/src/test/java/org/example/AppTest.java`

Covered scenarios:

1. Setter cycle (`AService <-> BService`) can be resolved.
2. Constructor cycle (`CService <-> DService`) cannot be resolved.
3. `@Lazy` can break constructor-time cycle in one side dependency.

## TODO tasks for you

1. Run tests:

```bash
mvn -s .mvn/settings.xml test
```

2. In `AService` and `BService`, follow TODO comments:
- Convert setter/field style to constructor style, rerun tests, compare behavior.

3. In `CService` and `DService`:
- Change one side to setter injection and observe whether startup behavior changes.

4. In `AppTest` lazy scenario:
- Remove `@Lazy` and rerun tests to see the failure.

## Interview follow-up answers

1. Why Boot projects often fail on cycles by default?
- `spring.main.allow-circular-references` is disabled by default in Boot.

2. Why constructor cycle fails?
- Constructor injection requires full dependencies at creation time, so no early exposure window.

3. Best engineering choice?
- Prefer redesigning dependency direction instead of relying on circular-reference support.
