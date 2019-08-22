## Hello社区项目

## 资料
>[Spring 文档](https://spring.io/guides/)   
>[目标社区](https://elasticsearch.cn/)   
>[Bootstrap 文档](https://v3.bootcss.com/getting-started/)  
>[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)   
>[Spring Boot Doc](https://docs.spring.io/spring-boot/docs/2.1.7.RELEASE/reference/html/)  
>[Lombok](https://projectlombok.org/features/all)  
>[thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)  
>[MyBatis Generator](http://www.mybatis.org/generator/)  
>[MyBatis Pagehelper](https://github.com/pagehelper/Mybatis-PageHelper)
## 工具
>[visual paradigm](https://www.visual-paradigm.com/cn/)

## 笔记
1. Spring Boot + Mybatis业务逻辑：  
    Controller --> service接口 --> serviceImpl --> dao接口 --> daoImpl --> mapper -->db
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
    - 使用的时候当做普通的类注入即可，在注入时提示该对象没有被注入到容器中时，只需要在报错的地方
    alt+回车 在inspection中选择忽视这个错误即可。
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
 7. 首页展示功能编写  
    - 加入Service层，多个mapper的混合逻辑，在Service中执行，然后在
 Controller中只需要调用Service中返回的值即可。
    - 前端循环显示：  
    th:each  -->  用于循环  
    th:src   -->  用于循环中取地址类型的值
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
 1. MyBatis Generator
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
    - maven运行命令：
    ```bash 
    mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
    ```
    - 配置文件的默认路径为：src/main/resources/generatorConfig.xml
## IDEA快捷键
- CTRL + ALT + o : 自动移除多余的包  
- SHIFT + F6 : 重命名   
- SHIFT + 回车 : 无论光标在哪里换到下一行  
- CTRL + SHIFT + N : 全局搜索
- CTRL + P : 方法内参数的提示
- ALT + 回车 : 自动生成返回类型及参数
- CTRL + E : 最近打开的文件
- CTRL + ALT + 左方向键 : 回到上一次编辑的位置