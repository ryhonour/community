## Hello社区项目
### 前言
此社区是根据B站码匠老师做的一个关于Spring Boot 项目实战的视频所搭建的
视频ID（av65117012），此社区是作者在学习过程中的一个产物，非常适合在学习
Spring Boot时拿来练手。

## 资料

> [Spring 文档](https://spring.io/projects/spring-framework)
>
> [目标社区](www.zykcoderman.xyz)
>
> [Bootstrap 文档](https://v3.bootcss.com/getting-started/) 
>
> [Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)
>
> [Spring Boot Doc](https://docs.spring.io/spring-boot/docs/2.1.7.RELEASE/reference/html/)
>
> [Lombok](https://projectlombok.org/features/all)
>
> [thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)
>
> [MyBatis Generator](http://www.mybatis.org/generator/)
>
> [MyBatis Pagehelper](https://github.com/pagehelper/Mybatis-PageHelper) 
> [js date format工具 moment.js](http://momentjs.cn/)

## 工具
>[visual paradigm](https://www.visual-paradigm.com/cn/)
>
>[Apache Commons Lang，String的工具包](https://mvnrepository.com/artifact/org.apache.commons/commons-lang3)

## 笔记
### 1. Spring Boot + Mybatis业务逻辑：  
Controller --> service接口 --> serviceImpl --> dao接口 --> daoImpl --> mapper -->db
### 2. *Github授权登录，获取登录信息*
### 3. *使用MySQL数据库*
```properties
URL = jdbc:mysql://localhost:3306/community?useSSL=true&characterEncoding=utf-8&serverTimezone=GMT
driver = com.mysql.cj.jdbc.Driver
```
### 4. *集成MyBatis*
- [mybatis-spring-boot-autoconfigure](http://www.mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)
- 添加相关Maven文件:
```
<!-- spring boot web-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<!-- spring boot and mybatis-->       
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.1.0</version>
</dependency>
<!-- -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.16</version>
</dependency>
```
- application.properties添加相关配置
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/community?useSSL=true&characterEncoding=utf-8&serverTimezone=GMT
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
#mybatis配置数据库表名下划线转驼峰
mybatis.configuration.map-underscore-to-camel-case=true
```
- 在mapper类上添加注解
```java
@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) " +
            "values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);
}
```
- 使用的时候当做普通的类注入即可，在注入时提示该对象没有被注入到容器中时，只需要在报错的地方alt+回车 在inspection中选择忽视这个错误即可。
```
@Autowired
private UserMapper userMapper;
```
### 5. *添加flyway数据库管理工具*
- 添加相关Maven文件
```mxml
<build>
    <plugins>
        <plugin>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-maven-plugin</artifactId>
            <version>5.2.4</version>
            <configuration>
                <url>jdbc:mysql://localhost:3306/community?serverTimezone=GMT</url>
                <user>root</user>
                <password>root</password>
            </configuration>
            <dependencies>
                <dependency>
                    <groupId>mysql</groupId>
                    <artifactId>mysql-connector-java</artifactId>
                    <version>8.0.16</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
```
- 创建文件夹src/main/resources/db/migration
- 创建第一个migration文件src/main/resources/db/migration/V1__Create_user_table.sql
```sql
create table user
(
    id           int auto_increment
        primary key,
    account_id   varchar(100) null,
    name         varchar(50)  null,
    token        char(50)     null,
    gmt_create   bigint       null,
    gmt_modified bigint       null
);
```
- 执行flyway来迁移数据库
```$xslt
mvn flyway:migrate
```
- 之后对数据库进行修改时只需要在migration的路径下创建相应的SQL文件，并执行Flyway即可。
### 6. 提问页面以及功能的编写
- 简单的实现持久化登录  
①把代表用户信息的token存放在Cookie中  
```text
response.addCookie(new Cookie("token",token));
```
②在application.properties中设置Cookie的过期时间，以秒为单位。
```properties
server.servlet.session.cookie.max-age=7776000
```
③在打开首页时先判断Cookie中是否有token，且数据库user表中是否有该用户，若存在则保存在Session中
```java
@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        @CookieValue(value = "token",required = false) String token){
        //判断Cookie中是否存在token
        if(token != null && token != ""){
            //判断数据库中是否存在该token对应的user
            User user = userMapper.findByToken(token);
            if(user != null){
                //把该user保存在Session中
                request.getSession().setAttribute("user",user);
            }
        }
        return "index";
    }
}
```
### 7. Lombok的使用
- 导入Maven文件
```mxml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.8</version>
    <scope>provided</scope>
</dependency>
```
- [Lombok插件下载](https://plugins.jetbrains.com/plugin/6317-lombok/versions)
- @Data注解：根据参数自动生成get、set方法
```java
@Data
public class User {
    private int id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
}
```
### 8. 首页展示功能编写  
- 加入Service层，多个mapper的混合逻辑，在Service中执行，然后在Controller中只需要调用Service中返回的值即可。
- 前端循环显示：  
```
th:each  -->  用于循环  
th:src   -->  用于循环中取地址类型的值
```
```html
<div class="media" th:each="questionDTO : ${questionDTOList}">
    <div class="media-left">
        <a href="#">
            <img class="media-object img-rounded" th:src="${questionDTO.user.avatarUrl}">
        </a>
    </div>
    <div class="media-body">
        <h4 class="media-heading"><td th:text="${questionDTO.title}"/></h4>
        <span class="text-desc">
            <span th:text="${questionDTO.commentCount}"></span>
             个回复 •
            <span th:text="${questionDTO.viewCount}"></span>
             次浏览 •
            <!--格式化时间-->
            <span th:text="${#dates.format(questionDTO.gmtCreate,'dd/MMM/yyyy HH:mm')}"></span>
        </span>
    </div>
</div>
```
### 9. MyBatis Generator
- 导入Maven文件
```text
<project ...>
     ...
     <build>
        ...
        <plugins>
            ...
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.7</version>
                <dependencies>
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>8.0.16</version>
                    </dependency>
                </dependencies>
            </plugin>
            ...
        </plugins>
        ...
     </build>
     ...
  </project>
```
- 配置文件的默认路径为：src/main/resources/generatorConfig.xml
- generatorConfig.xml配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <context id="MySLQ8_Context" targetRuntime="MyBatis3">
        <!-- 分页插件-->
        <plugin type="org.mybatis.generator.plugins.RowBoundsPlugin">
        </plugin>

        <commentGenerator>
            <!--去除注释-->
            <property name="suppressAllComments" value="false"/>
            <!--注释中去除日期注释-->
            <property name="suppressDate" value="true"/>
            <!--注释中添加数据库字段备注注释-->
            <property name="addRemarkComments" value="true"/>
        </commentGenerator>

        <!--数据库连接的信息：驱动类、连接地址、用户名、密码 -->
        <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/community"
                        userId="root"
                        password="root">
            <!--MySQL 8.x 需要指定服务器的时区-->
            <property name="serverTimezone" value="UTC"/>
            <!--MySQL 不支持 schema 或者 catalog 所以需要添加这个-->
            <!--参考 : http://www.mybatis.org/generator/usage/mysql.html-->
            <property name="nullCatalogMeansCurrent" value="true"/>
            <!-- MySQL8默认启用 SSL ,不关闭会有警告-->
            <property name="useSSL" value="false"/>
        </jdbcConnection>

        <!-- 默认false，把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer，为 true时把JDBC DECIMAL 和
            NUMERIC 类型解析为java.math.BigDecimal -->
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>

        <!-- targetProject:生成Model类的位置 -->
        <javaModelGenerator targetPackage="com.ry.community.model" targetProject="src/main/java">
            <!-- 从数据库返回的值被清理前后的空格 -->
            <property name="trimStrings" value="true"/>
            <property name="enableSubPackages" value="true"/>
        </javaModelGenerator>

        <!-- targetProject:mapper映射文件生成的位置 -->
        <sqlMapGenerator targetPackage="mapper" targetProject="src/main/resources">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>

        <!-- targetPackage：mapper接口生成的位置 -->
        <javaClientGenerator type="XMLMAPPER" targetPackage="com.ry.community.mapper"
                             targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
        </javaClientGenerator>

        <!-- 指定数据库表 -->
        <table schema="community" tableName="user" domainObjectName="User">
        </table>
        <table schema="community" tableName="question" domainObjectName="Question">
        </table>

    </context>
</generatorConfiguration>
```
- maven运行命令：
```bash 
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```
- selectByExample与selectByExampleWithBLOBs区别：

  selectByExample在返回时，不包括text类型的大字段，selectByExampleWithBLOBs返回全部类型的值。
  
- 排序加分页

```text
QuestionExample questionExample = new uestionExample();
//setOrderByClause为查询时order by 后面所接的字段
questionExample.setOrderByClause("gmt_create DESC");
//offset为分页偏移量，size为每页的大小
RowBounds rowBounds = new RowBounds(offset, size);
//selectByExampleWithRowbounds为Mybatis Generator的分页方法
List<Question> questionList = uestionMapper.selectByExampleWithRowbounds(questionExample, rowBounds);
```

- ds
### 10. 异常处理

**（分为两种: 一种是Spring MVC可以拦截(程序上下文中的异常)，另一种是 无法拦截的异常**

1. Spring MVC可以拦截的:@ControllerAdvice与@ExceptionHandler(Exception.class)来实现拦截

```java
@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable ex, Model model) {
        if (ex instanceof CustomizeException) {
            model.addAttribute("message", ex.getMessage());
        } else {
            model.addAttribute("message", "服务器太热了，请稍后再访问...");
        }
        return new ModelAndView("error");
    }
}
```
2. Spring MVC无法拦截的:

```java
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomizeErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request, Model model) {
        HttpStatus status = getStatus(request);
        if (status.is4xxClientError()) {
            model.addAttribute("message", "请求有错，要不换个姿势？");
        }
        if (status.is5xxServerError()) {
            model.addAttribute("message", "服务器冒烟了，请稍后再试");
        }
        return new ModelAndView("error");
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
```

3. 自定义一个CustomizeException异常类继承RuntimeException，方便做一些程序内的异常处理

```java
public class CustomizeException extends RuntimeException {
    private String message;
    private Integer code;

    //CustomizeErrorCode为自定义的异常信息接口，封装了code与message
    public CustomizeException(CustomizeErrorCode customizeErrorCode) {
        this.message = customizeErrorCode.getMessage();
        this.code = customizeErrorCode.getCode();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

}
```

4. CustomizeErrorCode接口类

```
public interface CustomizeErrorCode {
    Integer getCode();
    String getMessage();
}
```

5. 实现了CustomizeErrorCode接口的枚举类，用于存放自定义的Code与Message

```java
public enum CustomizeErrorCodeImpl implements CustomizeErrorCode {
    /**
     * 异常Message的枚举
     */
    QUESTION_NOT_FIND(2001, "您找的问题不存在或已被删除..."),
    USER_NOT_FIND(2002, "当前操作未登录，请先登录！"),
    TARGET_PARAM_NOT_FIND(2003, "未选中任何问题或回复..."),
    SYS_ERROR(2004, "服务器太热了，请稍后再访问..."),
    TYPE_TARGET_ERROR(2005, "评论类型不存在或错误..."),
    REPLY_COMMENT_NOT_FIND(2006, "您回复的评论不存在..."),
    REPLY_QUESTION_NOT_FIND(2007, "您回复的问题不存在..."),
    COMMENT_IS_EMPTY(2008, "回复内容不能为空..."),
    COMMENT_NOT_FIND(2009, "该评论不存在或已被删除...");


    private String message;
    private Integer code;

    CustomizeErrorCodeImpl(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

}
```





### 11. 事务

#### Spring的事务机制

所有的数据访问技术都有事务处理机制，这些技术提供了 API 用于开启事务、提交事务来完成数据操作，或者在发生错误时回滚数据。

Spring 的事务机制是用统一的机制来处理不同数据访问技术的事务处理，Spring 的事务机制提供了一个 PlatformTransactionManager 接口，不同的数据访问技术的事务使用不同的接口实现，如下表：

| 数据访问技术 | 实现                         |
| :----------- | :--------------------------- |
| JDBC         | DataSourceTransactionManager |
| JPA          | JPATransactionManager        |
| Hibernate    | HibernateTransactionManager  |
| JDO          | JdoTransactionManager        |
| 分布式事务   | JtaTransactionManager        |

#### 声明式事务

Spring 支持声明式事务，即使用注解来选择需要使用事务的方法，他使用 @Transactional 注解在方法上表明该方法需要事务支持。被注解的方法在被调用时，Spring 开启一个新的事务，当方法无异常运行结束后，Spring 会提交这个事务。如：

```text
@Transactional
public void saveStudent(Student student){
        // 数据库操作
}
```

Spring 提供一个 @EnableTranscationManagement 注解在配置类上来开启声明式事务的支持。使用了 @EnableTranscationManagement 后，Spring 容器会自动扫描注解 @Transactional 的方法与类。@EnableTranscationManagement 的使用方式如下：

```java
@Configuration
@EnableTranscationManagement 
public class AppConfig{
}
```

#### 注解行为

@Transactional 有如下表所示的属性来定制事务行为。

| 属性                   | 含义                                                         |
| :--------------------- | :----------------------------------------------------------- |
| propagation            | 事务传播行为                                                 |
| isolation              | 事务隔离级别                                                 |
| readOnly               | 事务的读写性，boolean型                                      |
| timeout                | 超时时间，int型，以秒为单位。                                |
| rollbackFor            | 一组异常类，遇到时回滚。（rollbackFor={SQLException.class}） |
| rollbackForCalssName   | 一组异常类名，遇到回滚，类型为 string[]                      |
| noRollbackFor          | 一组异常类，遇到不回滚                                       |
| norollbackForCalssName | 一组异常类名，遇到时不回滚。                                 |

#### 类级别使用 @Transactional

@Transactional 不仅可以注解在方法上，还可以注解在类上。注解在类上时意味着此类的所有 public 方法都是开启事务的。如果类级别和方法级别同时使用了 @Transactional 注解，则使用在类级别的注解会重载方法级别的注解。

#### SpringBoot的事务支持

1. **自动开启注解事务的支持**

   SpringBoot 专门用于配置事务的类为 org.springframework.boot.autoconfigure.transcation.TransactionAutoConfiguration，此配置类依赖于 JpaBaseConfiguration 和 DataSourceTransactionManagerAutoConfiguration。
   而在 DataSourceTransactionManagerAutoConfiguration 配置里还开启了对声明式事务的支持

2. 所以在 SpringBoot 中，无须显式开启使用 @EnableTransactionManagement 注解。
#### 实际代码

以下代码意思为：只要Insert抛出运行时异常的时候Spring事务就会自动回滚

```text
@ResponseBody
@Transactional(rollbackFor = RuntimeException.class)
public void insert(Comment comment) {
    if (comment.getParentId() == null || comment.getParentId() == 0) {
        throw new CustomizeException(CustomizeErrorCodeImpl.TARGET_PARAM_NOT_FIND);
    }
    //回复评论
    Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
    if (dbComment == null) {
        //CustomizeException为自定义的一个异常继承RuntimeException类
        throw new CustomizeException(CustomizeErrorCodeImpl.REPLY_COMMENT_NOT_FIND);
    }
    commentMapper.insert(comment);
    commentMaperCustom.incComment(comment.getParentId());
    //创建通知
    createNotify(comment, dbComment.getCommentator(), 		                             NotificationTypeEnum.REPLY_COMMENT, dbComment.getContent());
}
```

### 12. 拦截

1. 自定义一个拦截类，实现HandlerInterceptor接口

```java
@Service
public class SessionInterceptor implements HandlerInterceptor {
    
    //在实际的处理程序之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //相应的处理程序
        //返回true为继续执行之后的程序
        return true;
    }

    //在实际的处理程序之后执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    //在完成完整的请求之后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
```

2. 自定义一个WebConfig类并实现WebMvcConfigurer接口

```java
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //几种添加拦截路径的方法
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
        //registry.addInterceptor(new LocaleChangeInterceptor());
        //registry.addInterceptor(new ThemeChangeInterceptor()).addPathPatterns("/**").excludePathPatterns("/admin/**");
        //registry.addInterceptor(new SecurityInterceptor()).addPathPatterns("/secure/*");
    }
}
```

 


