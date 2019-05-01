package models.data.project.mapper;

import config.ProjectConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import exceptions.ProjectNotFound;
import exceptions.UserNotFound;
import models.data.connectionPool.ConnectionPool;
import models.data.mapper.Mapper;
import models.data.project.Project;
import models.data.skill.UserSkill;
import models.data.user.User;
import models.data.user.mapper.UserMapper;

public class ProjectMapper extends Mapper<Project, String> implements IProjectMapper{
    private static ProjectMapper ourInstance = new ProjectMapper();
    private ArrayList<Project> projects ;

    public static ProjectMapper getInstance() {
        return ourInstance;
    }

    private ProjectMapper() {
        projects = new ArrayList<Project>();
        try {
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Project getProjectByProjectID(String projectID) throws ProjectNotFound
    {
        Project project = null;
        try {
            project = find(projectID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(project == null)
            throw new ProjectNotFound();
        return project;
    }
    public Project getProjectByIDForUser(String projectID, String userID) throws Exception
    {
        Project project = getProjectByProjectID(projectID);
        User user = UserMapper.getInstance().getUserById(userID);
        if(!project.checkSkillSatisfaction(user.getSkills()))
            throw new Exception(ProjectConfig.USER_CANNOT_SATISFY_PROJECT);
        return project;
    }

    public void addNewProject(Project newProject)
    {
        for (Project project : projects)
            if (project.getTitle().equals(project.getTitle())){
                System.out.println(ProjectConfig.PROJECT_TITLE_ALREADY_EXISTS_ERROR);
                return;
            }
        projects.add(newProject);
    }
    public void addNewProjects(ArrayList<Project> newProjects) throws SQLException
    {
        projects.addAll(newProjects);
        for (Project newProject:
             projects) {
            insert(newProject);
        }
    }

    public ArrayList<Project> getProjectsForUser(String userId) throws UserNotFound {
        ArrayList<Project> projectsForUser = new ArrayList<Project>();
        User user = UserMapper.getInstance().getUserById(userId);
        for(Project project : projects)
        {
            if(project.checkSkillSatisfaction(user.getSkills()))
            {
                projectsForUser.add(project);
            }
        }
        return projectsForUser;
    }
    @Override
    protected ArrayList<String> getCreateTableStatement() {
        ArrayList<String> statements = new ArrayList<>();
        statements.add("CREATE TABLE IF NOT EXISTS Project(" +
                "    creationDate BIGINT," +
                "    pid CHAR(20)," +
                "    title CHAR(20)," +
                "    imageUrl TEXT," +
                "    projectDescription TEXT," +
                "    budget BIGINT," +
                "    deadline BIGINT," +
                "    PRIMARY KEY(pid)" +
                ");");
        statements.add("CREATE TABLE IF NOT EXISTS ProjectRequires(" +
                "    points INTEGER," +
                "    name CHAR(20)," +
                "    pid CHAR(20)," +
                "    FOREIGN KEY(name) REFERENCES Skill ON DELETE CASCADE," +
                "    FOREIGN KEY(pid) REFERENCES Project ON DELETE CASCADE," +
                "    PRIMARY KEY(points, name, pid)" +
                ");");
        return statements;
    }

    @Override
    protected String getFindStatement() {
        return "SELECT *" +
                "FROM Project , ProjectRequires " +
                "WHERE Project.pid = ? AND Project.pid = ProjectRequires.pid" +
                ";";
    }

    @Override
    protected Project convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        long creationDate = rs.getLong(1);
        String pid = rs.getString(2);
        String title = rs.getString(3);
        String imageUrl = rs.getString(4);
        String description = rs.getString(5);
        long budget = rs.getLong(6);
        long deadline = rs.getLong(7);
        HashMap<String, UserSkill> skills = new HashMap<>();
        do {
            skills.put(rs.getString(9), new UserSkill(rs.getString(9), rs.getInt(8)));
        }while(rs.next());
        return new Project(pid, title, description, imageUrl, budget, skills, deadline, creationDate);
    }

    @Override
    protected String getDeleteStatement() {
        return null;
    }



    private String getInsertStatement() {
        return "INSERT INTO Project ( creationDate, pid, title, imageUrl, projectDescription," +
                "budget, deadline) " +
                "VALUES(? , ?, ?, ?, ?, ?, ?) ";
    }

    private void fillInsertStatement(PreparedStatement stmt, Project project) throws SQLException {
        stmt.setLong(1, project.getCreationDate());
        stmt.setString(2, project.getID());
        stmt.setString(3, project.getTitle());
        stmt.setString(4, project.getImageURL());
        stmt.setString(5, project.getDescription());
        stmt.setLong(6, project.getBudget());
        stmt.setLong(7, project.getDeadline());
    }

    private String getRequireInsertStatement() {
        return "INSERT INTO ProjectRequires(points, name, pid)" +
                "VALUES(?, ?, ?)";
    }

    private void fillInsertRequireStatement(PreparedStatement stmt, UserSkill userSkill, Project project) throws SQLException{
        stmt.setInt(1, userSkill.getPoints());
        stmt.setString(2, userSkill.getName());
        stmt.setString(3, project.getID());
    }
    @Override
    public void insert(Project project) throws SQLException {
//        TODO : after adding find
        if(find(project.getID()) != null)
            return;
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement pStmt = con.prepareStatement(getInsertStatement());
             PreparedStatement rStmt = con.prepareStatement(getRequireInsertStatement())
        ) {
            try {
                fillInsertStatement(pStmt, project);
                pStmt.execute();

                con.setAutoCommit(false);
                for(UserSkill userSkill : project.getSkills().values())
                {
                    fillInsertRequireStatement(rStmt, userSkill, project);
                    rStmt.addBatch();
                }
                rStmt.executeBatch();
                con.commit();

            } catch (SQLException ex) {
                System.out.println("error in Mapper.deleteByID query.");
                throw ex;
            }
        }
    }
}
