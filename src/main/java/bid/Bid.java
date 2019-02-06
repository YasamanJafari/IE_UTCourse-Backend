package bid;

import config.BidConfig;
import config.UserConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import project.Project;
import skill.Skill;
import user.User;

import java.util.ArrayList;

public class Bid {
    private String biddingUserName;
    private String projectTitle;
    private int bidAmount;

    public Bid(JSONObject bidInfo){
        biddingUserName = bidInfo.getString(BidConfig.BIDDING_USER);
        projectTitle = bidInfo.getString(BidConfig.PROJECT_TITLE);
        bidAmount = bidInfo.getInt(BidConfig.BID_AMOUNT);
    }
}