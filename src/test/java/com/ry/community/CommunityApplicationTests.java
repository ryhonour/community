package com.ry.community;

import com.ry.community.mapper.QuestionMaperCustom;
import com.ry.community.model.Question;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommunityApplicationTests {

    @Autowired
    QuestionMaperCustom questionMaperCustom;

    @Test
    public void contextLoads() {
        Question question = new Question();
        question.setId(18L);
        question.setTags("Spring Boot|Spring|java");
        List<Question> question1 = questionMaperCustom.selectQuestionListByTagsWithRegexp(question);
        for (Question question2 : question1) {
            System.out.println(question2.getId() + " - " + question2.getTitle() + " - " + question2.getTags() + " - " + question2.getDescription());
        }
    }

}
