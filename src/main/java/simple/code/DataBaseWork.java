package simple.code;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import simple.data.RequestValue;
import simple.data.RequestValueFolder;
import simple.data.ResponseBuildVersions;

import java.io.*;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;


public class DataBaseWork {

    private static final String URL = "jdbc:postgresql://localhost:5432/test";

    private static final String USER = "postgres";

    private static final String PASSWORD = "postgres";

    public void creadeNewRelease(String json)  {

        try{
            RequestValue requestValue = Serialize.deserialize(json);
            saveToFile(requestValue, json);
            saveToBD(requestValue);
        }
        catch (Exception e){

        }


    }
    public String getBuildVersion(){


        return "";
    }


    public static void main(String[] args) throws SQLException {

        /*String[] folders = new String[]{
                "C:/Work/9_java/UpData/Data/AutoCad/тест",
                "C:/Work/9_java/UpData/Data/AutoCad/тест/Зажим воздуховодов TDС 2F_1.xxx",
                "C:/Work/9_java/UpData/Data/AutoCad/тест/тест2/Зажим воздуховодов TDС 2F_2.xxx"
        };*/
/*
        String[] folders = new String[]{
                "C:/Work/9_java/UpData/Data/AutoCad/тест",
                "C:/Work/9_java/UpData/Data/AutoCad/Консоль TCA 41x42Dx2.0.png"
                //"C:/Work/9_java/UpData/Data/AutoCad/Руководство TERMOCLIP TOOLS.pdf"
        };
        */
        String[] folders = new String[]{
                "C:/Work/9_java/UpData/Data/AutoCad"
        };
        DataBaseWork dataBaseWork  = new DataBaseWork();
        dataBaseWork.saveToBD_Test(folders);
        System.out.println("Hello world!");
    }



    public void saveToBD_Test(String[] filesName) throws SQLException{
        if(filesName.length >0)
        {
            try(var connection = DriverManager.getConnection(URL, USER, PASSWORD)){
                connection.setAutoCommit(false);
                var idNewBuild = getMaxBuildVersion(connection);
                idNewBuild++;
                String relativePath = "";

                for (String fileName :
                        filesName) {
                    File file = new File(fileName);
                    if(file.isFile()){

                        insertRecord(connection, idNewBuild, relativePath, fileName);
                        saveToFile( "save file: ile.getName(): " + file.getName() + " fileName: " + fileName);
                    }
                    else if(file.isDirectory()){
                        saveToBD(connection, file, idNewBuild, relativePath);
                    }
                    else {

                    }
                }
            }
        }

    }

    private void saveToBD(RequestValue requestValue){
        for (RequestValueFolder requestValueFolder :
                requestValue.getMyArray()) {
            File file = new File(requestValueFolder.getCurrantUnit());
            if(file.isFile()){
                saveToFile(requestValueFolder.getCurrantUnit());
            }
            else if(file.isDirectory()){
                //saveToBD(file);
            }
            else {

            }
        }
    }

    private void saveToBD(Connection connection, File file, int idNewBuild, String relativePath){
        try{
            File[] files = file.listFiles();
            relativePath += "/" + file.getName();
            for (File fileStr: files) {
                if(fileStr.isFile()){
                    insertRecord(connection, idNewBuild, relativePath, fileStr.getPath());
                    saveToFile( "save file: fileStr.getPath(): " + fileStr.getPath() + " relativePath: " + relativePath);
                }
                else if(fileStr.isDirectory()){
                    saveToBD(connection, fileStr, idNewBuild, relativePath);
                    saveToFile( "go to directory: relativePath: " + relativePath);
                }
                else {
                    saveToFile("error");
                }
            }
        }
        catch(Exception e){
            saveToFile(e.toString());
        }

    }





    private static void saveToFile (RequestValue requestValue, String json){
        try(FileWriter writer = new FileWriter("simple2.txt", false))
        {
            writer.write(json);
            writer.append('\r');
            writer.append('\n');

            for (RequestValueFolder requestValueFolder :
                    requestValue.getMyArray()) {
                try{
                    File file = new File(requestValueFolder.getCurrantUnit());
                    if(file.isFile()){
                        System.out.println("File:" );
                        writer.write("File:");
                        writer.append('\r');
                        writer.append('\n');
                    }
                    else if(file.isDirectory()){
                        System.out.println("Directory: " );
                        writer.write("Directory:");
                        writer.append('\r');
                        writer.append('\n');

                    }
                    else{
                        System.out.println("error URL:");
                        writer.write("error URL:");
                        writer.append('\r');
                        writer.append('\n');
                    }
                }
                catch(Exception e){

                }
                System.out.println(requestValueFolder.getCurrantUnit());
                writer.write(requestValueFolder.getCurrantUnit());
                writer.append('\r');
                writer.append('\n');

            }
            //writer.append('\n');
            writer.write(requestValue.getName());

            // запись по символам

            //writer.append('E');

            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    private static void saveToFile (String str){
        try(FileWriter writer = new FileWriter("File1.txt", true))
        {
            writer.append('\r');
            writer.append('\n');
            writer.write(str);

            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    private void insertRecord(Connection connection, int idNewBuild, String relativePath, String Name) throws SQLException{


        byte[] fileArray = null;

        try(FileInputStream fin=new FileInputStream(Name))
        {
            File file = new File(Name);
            relativePath += "/" + file.getName();
            fileArray = fin.readAllBytes();
            System.out.println("fileArray.length: "+ fileArray.length);
            int idFile = insertRecordFile(connection, fileArray);
            insertRecordFileBuild(connection, idNewBuild, idFile, relativePath);

        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }


    }
    private int insertRecordFile(Connection connection, byte[] fileArray) throws SQLException {



        long checkSum = checkAmount(fileArray);

        int id = searchIdFile(connection, checkSum);
        if(id != 0){
            return id;
        }
        else {
            try(var pst = connection.prepareStatement("insert into files(id, value, chack_amount) values (?, ?, ?)")) {
                id = getMaxIdFiles(connection);
                id++;
                var savePoint = connection.setSavepoint("savePointName");
                pst.setInt(1, id);
                pst.setBytes(2, fileArray);
                pst.setLong(3, checkSum);
                try{
                    var rowCount = pst.executeUpdate();
                    connection.commit();
                    System.out.println("inserted rowCount: " + rowCount);
                } catch(SQLException ex){
                    connection.rollback(savePoint);
                    System.out.println("error " + ex.getMessage());
                }
            }
        }


        return id;
    }

    private int searchIdFile(Connection connection, long chackAmount) throws SQLException {
        try(var pst = connection.prepareStatement("select id from files where chack_amount = ?")){
            pst.setLong(1, chackAmount);
            try(var rs = pst.executeQuery()){
                if(rs.next()){
                    int id = rs.getInt(1);
                    System.out.println("files id where chackAmount old: " + id);
                    return id;
                }
            }
        }
        return 0;
    }

    private int insertRecordFileBuild(Connection connection, int idNewBuild  , int idFile, String relativePath) throws SQLException {

        int id = getMaxIdBuild(connection);
        id++;
        try(var pst = connection.prepareStatement("insert into builds(id, build_version, file_id, file_path) values (?, ?, ?, ?)")) {
            var savePoint = connection.setSavepoint("savePointName");
            pst.setInt(1, id);
            pst.setInt(2, idNewBuild);
            pst.setInt(3, idFile);
            pst.setString(4, relativePath);
            try{
                var rowCount = pst.executeUpdate();
                connection.commit();
                System.out.println("inserted rowCount: " + rowCount);
            } catch(SQLException ex){
                connection.rollback(savePoint);
                System.out.println("error " + ex.getMessage());
            }
        }
        return id;
    }

    private void insertRecord(Connection connection, int id, byte[] fileArray) throws SQLException {
        try(var pst = connection.prepareStatement("insert into testBytea(id, value) values (?, ?)")) {
            var savePoint = connection.setSavepoint("savePointName");
            pst.setInt(1, id);
            pst.setBytes(2, fileArray);
            try{
                var rowCount = pst.executeUpdate();
                connection.commit();
                System.out.println("inserted rowCount: " + rowCount);
            } catch(SQLException ex){
                connection.rollback(savePoint);
                System.out.println("error " + ex.getMessage());
            }
        }
    }

    private byte[] selectRecord(Connection connection, int id) throws SQLException {
        byte[] fileArray2 = null;
        try(var pst = connection.prepareStatement("select value from testBytea where id = ?")){
            pst.setInt(1, id);

            try(var rs = pst. executeQuery()){
                if(rs.next()){
                    fileArray2 = rs.getBytes("value");
                    System.out.println("fileArray2.length: "+ fileArray2.length);
                }
            }
        }
        return fileArray2;
    }

    private int getMaxIdBuild(Connection connection) throws SQLException {
        try(var pst = connection.prepareStatement("select max(id) from builds")){
            //pst.setString(1, nameTable);
            try(var rs = pst.executeQuery()){
                if(rs.next()){
                    int id = rs.getInt(1);
                    System.out.println("builds max(id): " + id);
                    return id;
                }
            }
        }
        return 0;
    }

    private int getMaxBuildVersion(Connection connection) throws SQLException {
        try(var pst = connection.prepareStatement("select max(build_version) from builds")){
            try(var rs = pst.executeQuery()){
                if(rs.next()){
                    int id = rs.getInt(1);
                    System.out.println("builds max(build_version): " + id);
                    return id;
                }
            }
        }
        return 0;
    }

    private int getMaxIdFiles(Connection connection) throws SQLException {
        try(var pst = connection.prepareStatement("select max(id) from files")){
            //pst.setString(1, nameTable);
            try(var rs = pst.executeQuery()){
                if(rs.next()){
                    int id = rs.getInt(1);
                    System.out.println("files max(id): " + id);
                    return id;
                }
            }
        }
        return 0;
    }

    private long checkAmount(byte[] bytes){
        long chack_amount = 0;
        for(byte byteI: bytes){
            chack_amount += byteI;
        }
        return chack_amount;
    }

}
