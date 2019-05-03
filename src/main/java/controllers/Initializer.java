package controllers;

import exceptions.ProjectNotFound;
import models.data.bid.mapper.BidMapper;
import models.data.project.mapper.ProjectMapper;
import models.data.user.mapper.UserMapper;
import models.remoteServices.ProjectInitializer;
import models.remoteServices.SkillInitializer;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class Initializer {
    @PostConstruct
    public void init() {
        BidMapper.getInstance();
        SkillInitializer.initSkills();
        ProjectInitializer.initProjects();
        UserMapper.getInstance().addDefaultUser();
    }
}

