## Hello社区项目

## 资料
>[Spring 文档](https://spring.io/guides/)   
>[目标社区](https://elasticsearch.cn/)   
>[Bootstrap 文档](https://v3.bootcss.com/getting-started/)  
>[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)   
>[Spring Doc](https://docs.spring.io/spring-boot/docs/2.1.7.RELEASE/reference/html/)
>
## 工具
>[visual paradigm](https://www.visual-paradigm.com/cn/)

## 笔记
1. Github授权登录，获取登录信息
2. 使用MySQL数据库
    ```
    URL = jdbc:mysql://localhost:3306/community?useSSL=true&characterEncoding=utf-8&serverTimezone=GMT
    driver = com.mysql.cj.jdbc.Driver
    ```
3. 集成MyBatis
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
    ```
    spring.datasource.url=jdbc:mysql://localhost:3306/community?useSSL=true&characterEncoding=utf-8&serverTimezone=GMT
    spring.datasource.username=root
    spring.datasource.password=root
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver 
    ```
    - 在mapper类上添加注解
    ```$xslt
    @Mapper
    @Component("userMapper")
    public interface UserMapper {
        @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) " +
                "values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
        void insert(User user);
    }
    ```
    - 使用的时候当做普通的类注入即可
    ```$xslt
    @Autowired
    private UserMapper userMapper;
    ```
    
    
## IDEA快捷键
- CTRL + ALT + o : 自动移除多余的包  
- SHIFT + F6 : 重命名   
- SHIFT + 回车 : 无论光标在哪里换到下一行  
- CTRL + SHIFT + N : 全局搜索