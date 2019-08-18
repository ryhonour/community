## Hello社区项目

## 资料
>[Spring 文档](https://spring.io/guides/)   
>[目标社区](https://elasticsearch.cn/)   
>[Bootstrap 文档](https://v3.bootcss.com/getting-started/)  
>[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)   
>[Spring Doc](https://docs.spring.io/spring-boot/docs/2.1.7.RELEASE/reference/html/)  
>[Lombok](https://projectlombok.org/features/all)
## 工具
>[visual paradigm](https://www.visual-paradigm.com/cn/)

## 笔记
1. *Github授权登录，获取登录信息*
2. *使用MySQL数据库*
    ```properties
    URL = jdbc:mysql://localhost:3306/community?useSSL=true&characterEncoding=utf-8&serverTimezone=GMT
    driver = com.mysql.cj.jdbc.Driver
    ```
3. *集成MyBatis*
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
    ```
    - 在mapper类上添加注解
    ```java
    @Mapper
    @Component("userMapper")
    public interface UserMapper {
        @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) " +
                "values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
        void insert(User user);
    }
    ```
    - 使用的时候当做普通的类注入即可
    ```
    @Autowired
    private UserMapper userMapper;
    ```
4. *添加flyway数据库管理工具*
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
5. 提问页面以及功能的编写
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
6. Lombok的使用
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
    
## IDEA快捷键
- CTRL + ALT + o : 自动移除多余的包  
- SHIFT + F6 : 重命名   
- SHIFT + 回车 : 无论光标在哪里换到下一行  
- CTRL + SHIFT + N : 全局搜索
- CTRL + P : 方法内参数的提示
- ALT + 回车 : 自动生成返回类型及参数