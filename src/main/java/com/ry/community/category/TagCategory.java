package com.ry.community.category;

import com.ry.community.dto.TagDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 14:38 2019/9/27
 * @Version 1.0
 */
public class TagCategory {
    /**
     * 获取全部的标签以及其分类
     */
    public static List<TagDTO> get() {
        ArrayList<TagDTO> tagList = new ArrayList<>();
        TagDTO language = new TagDTO();
        language.setTagCategoryName("编程语言");
        language.setTagCategoryEnName("language");
        language.setTagList(Arrays.asList("javascript", "php", "css", "html", "html5", "java", "node.js", "python", "c++", "c", "golang", "typescript", "shell", "swift", "c#", "sass", "ruby", "bash", "less", "asp.net", "lua", "scala", "coffeescript", "actionscript", "rust", "erlang", "perl"));
        tagList.add(language);

        TagDTO frame = new TagDTO();
        frame.setTagCategoryName("平台框架");
        frame.setTagCategoryEnName("frame");
        frame.setTagList(Arrays.asList("laravel", "spring", "express", "django", "flask", "yii", "tornado", "koa", "struts"));
        tagList.add(frame);

        TagDTO server = new TagDTO();
        server.setTagCategoryName("服务器");
        server.setTagCategoryEnName("server");
        server.setTagList(Arrays.asList("linux", "nginx", "docker", "apache", "ubuntu", "centos", "tomcat", "unix", "hadoop", "windows-server"));
        tagList.add(server);

        TagDTO db = new TagDTO();
        db.setTagCategoryName("数据库");
        db.setTagCategoryEnName("db");
        db.setTagList(Arrays.asList("mysql", "redis", "mongodb", "sql", "oracle", "nosql", "memcached", "sqlserver", "postgresql", "sqlite"));
        tagList.add(db);

        TagDTO tool = new TagDTO();
        tool.setTagCategoryName("开发工具");
        tool.setTagCategoryEnName("tool");
        tool.setTagList(Arrays.asList("it", "ithub", "isual-studio-code", "im", "ublime-text", "code", "ntellij-idea", "clipse", "aven", "de", "vn", "tom", "macs", "extmate", "g"));
        tagList.add(tool);
        return tagList;
    }
}
