package joboonja;

import config.BidConfig;
import config.JoboonjaConfig;
import bid.Bid;
import org.json.JSONObject;
import project.Project;
import skill.Skill;
import user.User;

import java.util.ArrayList;
import java.util.HashMap;

public class Joboonja {
    private static ArrayList<Project> projects = new ArrayList<Project>();
    private static ArrayList<User> users = new ArrayList<User>();
    private static ArrayList<Bid> bids = new ArrayList<Bid>();

    public static void addNewProject(JSONObject projectInfo)
    {
        projects.add(new Project(projectInfo));
    }

    public static void registerNewUser(JSONObject userInfo)
    {
        users.add(new User(userInfo));
    }

    public static void addNewBid(JSONObject bidInfo) {
        try{
            User user = getUserByUsername(bidInfo.getString(BidConfig.BIDDING_USER));
            Project project = getProjectByProjectTitle(bidInfo.getString(BidConfig.PROJECT_TITLE));
            if(project.checkSatisfaction(user.getSkills(), bidInfo.getLong(BidConfig.BID_AMOUNT)))
                bids.add(new Bid(bidInfo));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    public static void auction(JSONObject projectIdentifier)
    {
        String projectTitle = projectIdentifier.getString(BidConfig.PROJECT_TITLE);
        User user = null;
        Project project = null;
        try {
            project = getProjectByProjectTitle(projectTitle);

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        int maxAuctionRate = 0;
        boolean firstVisited = false;
        User winner = null;

        for(Bid bid : bids)
        {
            if(!bid.getProjectTitle().equals(projectTitle))
                continue;
            try {
                user = getUserByUsername(bid.getBiddingUserName());
                int auctionRate = calcAuctionFormula(user, project, bid);
                if(!firstVisited)
                {
                    maxAuctionRate = auctionRate;
                    firstVisited = true;
                    winner = user;
                }
                else if(auctionRate > maxAuctionRate)
                {
                    maxAuctionRate = auctionRate;
                    winner = user;
                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        }

        System.out.println(JoboonjaConfig.WINNER_MSG(winner));
    }

    private static User getUserByUsername(String name) throws Exception
    {
        for (User user : users) {
            if (user.getUsername().equals(name))
                return user;
        }

        throw new Exception(JoboonjaConfig.USER_NOT_FOUND_ERROR);
    }

    private static Project getProjectByProjectTitle(String projectTitle) throws Exception
    {
        for (Project project : projects)
        {
            if (project.getTitle().equals(projectTitle))
                return project;
        }
        throw new Exception(JoboonjaConfig.PROJECT_NOT_FOUND_ERROR);
    }

    private static int calcAuctionFormula(User user, Project project, Bid bid)
    {
        HashMap<String, Skill> userSkills = user.getSkills();
        HashMap<String, Skill> projectSkills = project.getSkills();

        int auctionRate = 0;

        for(Skill projectSkill : projectSkills.values())
        {
            Skill userSkill = userSkills.get(projectSkill.getName());
            auctionRate += 10000 *  Math.pow(userSkill.getPoints() - projectSkill.getPoints(), 2);
        }
        auctionRate += (project.getBudget() - bid.getOffer());
        return auctionRate;
    }
}
