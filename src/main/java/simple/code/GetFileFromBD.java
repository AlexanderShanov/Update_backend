package simple.code;

import simple.dao.pool;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GetFileFromBD {

    private static final String URL = "jdbc:postgresql://localhost:5432/test";

    private static final String USER = "postgres";

    private static final String PASSWORD = "postgres";

    public static void main(String[] args) throws SQLException {
        GetFileFromBD getFileFromBD = new GetFileFromBD();


        byte[] bytes = getFileFromBD.selectRecord(3);

        System.out.println(bytes.length);
        System.out.println("Hello world!");
    }





    public byte[] selectRecord( int id)  {
        byte[] fileArray2 = null;
        try{

            var connection = pool.getConnection();
            try(var pst = connection.prepareStatement("select value from files where id = ?")){
                pst.setInt(1, id);
                try(var rs = pst. executeQuery()){
                    if(rs.next()){
                        fileArray2 = rs.getBytes("value");
                        System.out.println("fileArray2.length: "+ fileArray2.length);
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

        return fileArray2;
    }
}
