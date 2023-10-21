package simple.code;

import simple.data.ChangeVersion;
import simple.data.ChangeVersionInfoFiles;
import simple.data.InfoFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListUpdataFiles {

    private static final String URL = "jdbc:postgresql://localhost:5432/test";

    private static final String USER = "postgres";

    private static final String PASSWORD = "postgres";

    private ChangeVersion changeVersion;


    public static void main(String[] args) throws SQLException {
        ChangeVersion changeVersion = new ChangeVersion("0", "5");

        ListUpdataFiles listUpdataFiles = new ListUpdataFiles(changeVersion);
        String json = listUpdataFiles.getJsonListUpdataFilesFromBD();
    }
    
    public ListUpdataFiles(ChangeVersion changeVersion){
        this.changeVersion = changeVersion;
        try(FileWriter writer = new FileWriter("ChangeVersion.txt", false))
        {
            // запись всей строки
            String text = changeVersion.toString() ;
            writer.write(text);

            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    public String getJsonListUpdataFilesTest() {

        InfoFile[] filesAdd = new InfoFile[]{new InfoFile(1, "qwerty1.txt"),new InfoFile(2, "qwerty2.txt") };
        InfoFile[] filesDelete = new InfoFile[]{new InfoFile(3, "qwerty3.txt"),new InfoFile(4, "qwerty4.txt") };

        ChangeVersionInfoFiles changeVersionInfoFiles = new ChangeVersionInfoFiles(filesAdd, filesDelete);
        String json = "";

        try{
            json = Serialize.serialize(changeVersionInfoFiles);
        }
        catch(Exception e){
            json = e.toString();
        }
        return json;
    }

    public String getJsonListUpdataFilesFromBD() throws SQLException{
        String json = "";
        if(changeVersion.getIdNewVersion() != null && changeVersion.getIdNewVersion() != null)
        {
            try(var connection = DriverManager.getConnection(URL, USER, PASSWORD)){
                connection.setAutoCommit(false);
                InfoFile[] filesAdd = getArrayInfoFilesExcept(changeVersion.getIdCurrentVersion(), changeVersion.getIdNewVersion(), connection);
                InfoFile[] filesDelete = getArrayInfoFilesExcept(changeVersion.getIdNewVersion(), changeVersion.getIdCurrentVersion(), connection);
                ChangeVersionInfoFiles changeVersionInfoFiles = new ChangeVersionInfoFiles(filesAdd, filesDelete);
                try{
                    json = Serialize.serialize(changeVersionInfoFiles);
                }
                catch(Exception e){
                    json = e.toString();
                }
            }
        }


        return json;
    }

    private InfoFile[] getArrayInfoFilesExcept(String idCurrent, String idNew, Connection connection) throws SQLException{
        List<InfoFile> builds = new ArrayList<>();
        try(var pst = connection.prepareStatement(
                "select file_id, file_path from builds where build_version = ? " +
                        "except " +
                        "select file_id, file_path from builds where build_version = ?")) {
            var savePoint = connection.setSavepoint("savePointName");
            int idNewInt = Integer.parseInt(idNew);
            int idCurrentInt = Integer.parseInt(idCurrent);
            pst.setInt(1, idNewInt);
            pst.setInt(2, idCurrentInt);
            try(var rs = pst.executeQuery()){
                while(rs.next()){
                    Integer id = rs.getInt(1);
                    String path = rs.getString(2);
                    System.out.println("get id: " + id + " path: " + path);
                    builds.add(new InfoFile(id, path));
                }
            }
        }

        InfoFile[] builds1 =  builds.toArray(new InfoFile[builds.size()]);

        return builds1;
    }


}
