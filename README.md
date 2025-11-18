# 学生信息管理系统 - IoC和AOP示例

本项目使用Spring Boot实现了一个学生信息管理系统，演示了IoC（控制反转）和AOP（面向切面编程）技术的应用。

## 功能特性

### 1. IoC (控制反转)
- `StudentService` 使用 `@Service` 注解，由Spring IoC容器管理
- 依赖通过 `@Autowired` 自动注入
- 展示了Spring依赖注入的核心概念

### 2. AOP (面向切面编程)
- `LoggingAspect` 使用 `@Aspect` 和 `@Around` 注解
- 自动记录所有CRUD操作的调用日志
- 包含方法名、参数、返回值和异常信息
- 无需修改业务代码即可添加日志功能

### 3. CRUD操作
- `addStudent` - 添加学生
- `deleteStudent` - 删除学生
- `modifyStudent` - 修改学生信息
- `findStudent` - 查询学生
- `findAll` - 查询所有学生

### 4. 自定义异常处理
- `StudentNotFoundException` - 访问不存在的学生时抛出
- 提供友好的错误提示信息

## 项目结构

```
src/main/java/cn/edu/hzcu/yrx/demo/
├── model/
│   └── Student.java              # 学生实体类（sid, name, tele）
├── service/
│   └── StudentService.java       # 学生服务类（使用IoC）
├── aspect/
│   └── LoggingAspect.java        # AOP日志切面
├── exception/
│   └── StudentNotFoundException.java  # 自定义异常
└── Schoolwork7Application.java   # 主应用类

src/test/java/cn/edu/hzcu/yrx/demo/
├── Schoolwork7ApplicationTests.java           # 基础测试类
└── service/
    └── StudentServiceIntegrationTest.java     # 集成测试类
```

## 技术栈

- Java 17
- Spring Boot 3.5.7
- Spring AOP (AspectJ)
- JUnit 5
- Maven

## 运行项目

### 1. 构建项目
```bash
mvn clean compile
```

### 2. 运行测试
```bash
mvn test
```

### 3. 查看测试输出
测试运行时会在控制台输出AOP日志，例如：
```
=== AOP日志 === 调用方法: addStudent, 参数: [Student@...]
=== AOP日志 === 方法 addStudent 执行成功, 返回值: Student@...
```

## 测试说明

项目包含16个测试用例，覆盖以下场景：

### 基础功能测试 (Schoolwork7ApplicationTests)
- 添加学生
- 查询学生
- 修改学生
- 删除学生
- 查询所有学生
- 异常处理（学生不存在）

### 集成测试 (StudentServiceIntegrationTest)
- IoC依赖注入验证
- 完整CRUD流程演示
- AOP日志记录验证
- 自定义异常提示验证

## AOP日志示例

```
========== 测试添加学生 ==========
=== AOP日志 === 调用方法: addStudent, 参数: [Student@...]
=== AOP日志 === 方法 addStudent 执行成功, 返回值: Student@...
添加成功，学生ID: 1

========== 测试自定义错误提示 ==========
=== AOP日志 === 调用方法: findStudent, 参数: [999]
=== AOP日志 === 方法 findStudent 执行失败: Student with ID 999 not found
错误提示: Student with ID 999 not found
```

## 关键技术点

### IoC实现
```java
@Service
public class StudentService {
    // Spring自动管理此类的生命周期
}

@Autowired
private StudentService studentService;  // 自动注入
```

### AOP实现
```java
@Aspect
@Component
public class LoggingAspect {
    @Around("execution(* cn.edu.hzcu.yrx.demo.service.StudentService.*(..))")
    public Object logServiceMethodCall(ProceedingJoinPoint joinPoint) {
        // 方法调用前后的日志记录
    }
}
```

## 学习要点

1. **IoC容器**：理解Spring如何管理Bean的生命周期
2. **依赖注入**：掌握通过注解实现依赖注入
3. **AOP切面**：学习如何使用AOP实现横切关注点
4. **切点表达式**：理解execution表达式的使用
5. **异常处理**：实现自定义异常和友好的错误提示

## 作者

本项目为Spring Boot学习示例，演示IoC和AOP的实际应用。
