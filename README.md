# 简介

本项目基于 Spring AOP 实现，通过注解的方式简化了锁的使用，并对锁的使用方式进行了统一管理，而且还可与 Spring 的 `@Transaction` 一起使用，默认提供了单机锁和分布式锁实现。

# 快速开始

可以参考本项目中的 `easy-lock-demo`。

## 引入依赖

```xml
<dependency>
    <groupId>io.github.maskvvv</groupId>
    <artifactId>easy-lock-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 启用 Easy Lock

在 spring boot 项目的启动类上添加 `@EnableEasyLock` 注解以启用 Easy Lock。

```java
@EnableEasyLock
@SpringBootApplication
public class EasyLockDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasyLockDemoApplication.class, args);
    }
}
```

## 编辑配置文件（可选）

```properties
# lock key 前缀
easy-lock.prefix=demo
# 默认的锁执行器
easy-lock.default-lock-processor-bean-name=defaultLockProcessor
# 锁的过期事件
easy-lock.lease-time=30000
```

## 编写 KeyConvert （可选）

用来生成需要加锁的 key。

```java
@Component
public class DemoConvert implements KeyConvert {
    @Override
    public String getKey(Object... params) {
        DemoParam param = (DemoParam) params[0];
        return param.getId();
    }
}
```

## 在需要加锁的方法上添加注解

```java
@EasyLock(keyConvert = DemoConvert.class, spEl = "{{#body.name}}-{{#body.age}}-{{#userid}}")
public String addUser(DemoParam param, DemoBody body) {
    System.out.println("addUser do something.....");
    System.out.println("param: " + param);
    System.out.println("body: " + body);
    return "success";
}
```

# 相关概念

## Easy Lock 可配置的属性

用来配置所执行的默认参数，全部参数如下：

```properties
# lock key 前缀，默认为空
easy-lock.prefix=demo
#lock key 分割符，默认为 ":"
easy-lock.key-separator=:
# 默认的锁执行器的 bean name，默认为 defaultLockProcessor 单机锁
easy-lock.default-lock-processor-bean-name=defaultLockProcessor
# 锁的过期事件，默认为 -1
easy-lock.lease-time=30000
```

## @EasyLock

用来标识需要加锁的方法。

- prefix：lock key 前缀，默认为 properties 中的值，用来组成 lock key
- keyConvert：keyConvert 的实现类，用来组成 lock key
- spEL：文本内容，用来组成 lock key， `{{}}` 内可以使用 spEL 表达式，当前可使用的变量有 **目标方法的参数** 和 **IOC 容器中的 bean**，后面说怎么拓展
- keySeparator：上面 3 个值的分隔符，用来组成 lock key
- leaseTime：锁的过期时间，默认为 properties 中的值
- lockProcessor：锁的执行器，默认为 properties 中的值

## KeyConvert

通过实现 KeyConvert 接口，并在注解中指定以生成 lock key 的一部分，框架会将 @EasyLock 注解标注的方法参数传递给实现类，可选，例子如下。

```java
@Component
public class DemoConvert implements KeyConvert {
    @Override
    public String getKey(Object... params) {
        DemoParam param = (DemoParam) params[0];
        return param.getId();
    }
}
```

## EasyLockEvaluationContextPostProcessor

目前框架中 spEL 表达式中可使用的变量有 **目标方法的参数** 和 **IOC 容器中的 bean**，如果你想注入自己的变量，只需要实现 `EasyLockEvaluationContextPostProcessor` 接口并注入到容器中即可。

举个例子，我想注入当前访问用户的 userid。

```java
@Component
public class DemoEasyLockEvaluationContextPostProcessor implements EasyLockEvaluationContextPostProcessor {
    @Override
    public void postProcess(StandardEvaluationContext context) {
        context.setVariable("userid", "user111");
    }
}
```

这样我就可以直接通过 `{{#userid}}` 使用该变量了。

## LockProcessor

锁执行器用来对目标方法进行加锁，框架默认提供单机锁。你可以通过实现 `LockProcessor` 接口用来定制自己的加锁方式，如下所示。

```java
@Component
public class DemoLockProcessor implements LockProcessor {

    @Override
    public Object proceed(MethodInvocation invocation, String key, String leaseTime) throws Throwable {
        
        // 加锁
        
        Object proceed = invocation.proceed();
        
        // 解锁
        return proceed;
    }
}
```

这样你就可以在 **配置文件中** 和 **@EasyLock** 直接使用该锁执行器了。

# easy-lock-redisson 分布式锁实现

由于 Easy Lock 只提供了单机锁的实现，所以这里提供了基于 Redisson 的分布式锁实现，你可以直接使用，使用方式如下。

## 引入依赖

```xml
<dependency>
    <groupId>io.github.maskvvv</groupId>
    <artifactId>easy-lock-redisson</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 编写配置文件

```properties
# redisson lock processor
# 启用
easy-lock-redisson.enable=true
# redis 地址
easy-lock-redisson.single-server.address=redis://${myserver}:6380
# redis 密码
easy-lock-redisson.single-server.password=1234567788
```

这样你就可以在 **配置文件中** 和 **@EasyLock** 直接使用该锁执行器了。
