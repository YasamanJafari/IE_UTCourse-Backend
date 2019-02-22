package services.project;

import config.ProjectServiceConfig;
import project.Project;
import project.ProjectRepo;


import javax.xml.ws.spi.http.HttpExchange;
import java.io.IOException;
import java.util.ArrayList;

public class ProjectService {
    public static String getAllProjectsHtml() throws Exception {
        ProjectRepo projectRepo = ProjectRepo.getInstance();
        ArrayList<Project> projects =  projectRepo.getProjectsForUser(ProjectServiceConfig.USER_ID);

        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Projects</title>\n" +
                "    <style>\n" +
                "        table {\n" +
                "            text-align: center;\n" +
                "            margin: 0 auto;\n" +
                "        }\n" +
                "        td {\n" +
                "            min-width: 300px;\n" +
                "            margin: 5px 5px 5px 5px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <table>\n";
        for(Project project : projects)
        {
            html += "        <tr>\n" +
                    "            <th>id</th>\n" +
                    "            <th>title</th>\n" +
                    "            <th>budget</th>\n" +
                    "        </tr>\n";
        }
        html +=  "    </table>\n" +
                "</body>\n" +
                "</html>";
        return html;

    }
    public static String getProjectByIdHtml(String projectId)
    {
        return "";
    }
}
