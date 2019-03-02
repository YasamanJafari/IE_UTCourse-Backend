package models.data.project;

import models.data.skill.UserSkill;

import java.util.HashMap;

public class Project {
    private String id;
    private String title;
    private String description;
    private String imageURL;
    private long budget;
    private HashMap<String, UserSkill> skills;
    private long deadline;
//    private List <Bid> bids;
//    private UserInfo winner;


    public Project(String id, String title, String description, String imageURL, long budget, HashMap<String, UserSkill> skills, long deadline)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.budget = budget;
        this.skills = skills;
        this.deadline = deadline;
    }

    public String getTitle()
    {
        return title;
    }

    public String getID() { return id; }

    public HashMap<String, UserSkill> getSkills() { return skills; }


    public long getBudget()
    {
        return budget;
    }

    public boolean checkBudgetSatisfaction(long bidAmount)
    {
        return budget >= bidAmount;
    }
    public boolean checkSkillSatisfaction(HashMap<String, UserSkill> userSkills)
    {
        if(userSkills.size() < skills.size())
            return false;

        for (UserSkill skill : skills.values()) {
            String skillName = skill.getName();
            if(!userSkills.containsKey(skillName))
                return false;
            if(userSkills.get(skillName).getPoints() < skill.getPoints())
                return false;
        }
        return true;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }
}