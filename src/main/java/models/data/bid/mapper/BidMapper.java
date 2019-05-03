package models.data.bid.mapper;

import config.DatabaseErrorsConfig;
import exceptions.AlreadyBid;
import exceptions.InvalidBidRequirements;
import exceptions.ProjectNotFound;
import exceptions.UserNotFound;
import models.data.bid.Bid;
import models.data.mapper.Mapper;
import models.data.project.Project;
import models.data.project.mapper.ProjectMapper;
import models.data.user.User;
import models.data.user.mapper.UserMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BidMapper extends Mapper<Bid, String> {
    private static BidMapper ourInstance = new BidMapper();
    private ArrayList<Bid> bids;

    public static BidMapper getInstance() {
        return ourInstance;
    }

    private BidMapper() {
        bids = new ArrayList<Bid>();
        try {
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(DatabaseErrorsConfig.canNotCreateTable("Bid"));
        }
    }

    private boolean hasAlreadyBid(Bid newBid) {
        ArrayList<Bid> bids = this.getBidsOfProject(newBid.getProjectID());
        for(Bid bid : bids) {
            if(bid.getBiddingUserName().equals(newBid.getBiddingUserName()))
                return true;
        }
        return false;
    }

    private boolean isValidToAdd(Bid newBid) throws UserNotFound, ProjectNotFound {
        ProjectMapper projectMapper = ProjectMapper.getInstance();
        UserMapper userMapper = UserMapper.getInstance();

        User user = userMapper.getUserById(newBid.getBiddingUserName());
        Project project = projectMapper.getProjectByProjectID(newBid.getProjectID());

        return project.checkSkillSatisfaction(user.getSkills()) && project.checkBudgetSatisfaction(newBid.getOffer());
    }
    public void addNewBid(Bid newBid) throws AlreadyBid, InvalidBidRequirements, UserNotFound, ProjectNotFound {
        if(hasAlreadyBid(newBid))
            throw new AlreadyBid();
        if(!isValidToAdd(newBid))
            throw new InvalidBidRequirements();
        bids.add(newBid);
    }
    public ArrayList<Bid> getBidsOfProject(String projectID)
    {
        ArrayList<Bid> bidsOfProject = new ArrayList<Bid>();
        for(Bid bid : bids)
        {
            if(bid.getProjectID().equals(projectID))
                bidsOfProject.add(bid);
        }
        return bidsOfProject;
    }
    public boolean isEmpty()
    {
        return bids.size() == 0;
    }

    @Override
    protected String getFindStatement() {
        return null;
    }

    @Override
    protected Bid convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return null;
    }


    @Override
    protected ArrayList<String> getCreateTableStatement() {
        ArrayList<String> statements = new ArrayList<>();
        statements.add ("CREATE TABLE IF NOT EXISTS Bid(" +
                "    offer BIGINT," +
                "    userId CHAR(20) NOT NULL," +
                "    pid CHAR(20) NOT NULL," +
                "    FOREIGN KEY(userId) REFERENCES JoboonjaUser ON DELETE CASCADE," +
                "    FOREIGN KEY(pid) REFERENCES Project ON DELETE CASCADE," +
                "    PRIMARY KEY(offer, userId, pid)" +
                ");");
        return statements;
    }

    @Override
    public String getInsertStatement() {
        return null;
    }

    @Override
    public void fillInsertStatement(PreparedStatement stmt, Bid object) throws SQLException {

    }
}
