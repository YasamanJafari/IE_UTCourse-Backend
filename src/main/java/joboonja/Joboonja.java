package joboonja;

import config.BidConfig;
import config.JoboonjaConfig;
import bid.Bid;
import org.json.JSONObject;
import project.Project;
import user.User;

import java.util.ArrayList;

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
    public static void addNewBid(JSONObject bidInfo) { bids.add(new Bid(bidInfo)); }
    public static void auction(JSONObject projectIdentifier)
    {
        String projectTitle = projectIdentifier.getString(BidConfig.PROJECT_TITLE);
        String bidingUser = projectIdentifier.getString(BidConfig.BIDDING_USER);
        int maxFormula;
        boolean firstVisited = false;
        for(int i = 0; i < bids.size() ; i++)
        {
            try
            {
                User user =  getUserByUsername(bidingUser);
                Project project = getProjectByProjectTitle(projectTitle);
                if(!project.satisfy(user.getSkills()))
                    continue;
                if(!firstVisited)
                {
                    maxFormula =
                }

            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
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
}
