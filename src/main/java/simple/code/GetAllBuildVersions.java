package simple.code;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import simple.dao.pool;
import simple.data.ResponseBuildVersions;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GetAllBuildVersions {

    private static final String URL = "jdbc:postgresql://localhost:5432/updater";

    private static final String USER = "postgres";

    private static final String PASSWORD = "postgres";

    public static void main(String[] args) throws SQLException {
        GetAllBuildVersions getAllBuildVersions = new GetAllBuildVersions();
        String strings = getAllBuildVersions.getBuildVersions();
        System.out.println("Json: " + strings);
        System.out.println("Hello world!");
    }

    public String getBuildVersions(){
        String json = "{}";
        System.out.println("3");
        String[] buildVersions = getDataBaseBuildVersion();

        ResponseBuildVersions responseBuildVersions = new ResponseBuildVersions(buildVersions);
        System.out.println("6");
        json = serializeBuildVersions(responseBuildVersions);
        System.out.println("7");

        return json;
    }

    private String[] getDataBaseBuildVersion()   {
        String[] builds1 = null;
        long time1 = System.currentTimeMillis();
        Connection connection = null;
        try {

            connection = pool.getConnection();
            long time2 = System.currentTimeMillis();
            //LoggerSyslog.logger.warn("Get all name version: open connection pool database, time {}", time2 - time1);
            System.out.println("Get all name version: open connection pool database, time " + (time2 - time1) );
        }
        catch (Exception e) {
            long time3 = System.currentTimeMillis();
            //LoggerSyslog.logger.error("Get all name version: error open connection, time {}", time3 - time1);
            System.out.println("Get all name version: error open connection, time " + (time3 - time1));
            //LoggerSyslog.logger.error(e.toString());
            return null;
        }

        long time4 = System.currentTimeMillis();
        if(connection != null){
            try{
                List<String> builds = new ArrayList<>();

                try (var pst = connection.prepareStatement("select distinct build_version from builds order by build_version ")) {
                    try (var rs = pst.executeQuery()) {
                        while (rs.next()) {
                            Integer id = rs.getInt(1);
                            //System.out.println("builds build_version: " + id);
                            builds.add(id.toString());
                        }
                    }
                }
                builds1 =  builds.toArray(new String[builds.size()]);
                long time5 = System.currentTimeMillis();
                //LoggerSyslog.logger.warn("Get all name version: List received from database, time {}", time5 - time4);
                System.out.println("Get all name version: List received from database, time " + (time5 - time4));
            }
            catch (SQLException e){
                long time6 = System.currentTimeMillis();
                //LoggerSyslog.logger.error("Get all name version: error request form database, time {}", time6 - time4);
                System.out.println("Get all name version: error request form database, time "  + (time6 - time4));
                //LoggerSyslog.logger.error(e.toString());
                System.out.println(e.toString());
            }
        }

        return builds1;
    }

    private static String serializeBuildVersions(ResponseBuildVersions responseBuildVersions){
        String json = "";
        try{
            ObjectWriter objectWriter = new ObjectMapper().writer();
            json = objectWriter.writeValueAsString(responseBuildVersions);
        }
        catch (JsonProcessingException e){
            System.out.println(e.toString());
        }
        return json;
    }

}
