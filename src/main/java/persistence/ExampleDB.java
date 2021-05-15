package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ExampleDB {

    public static void testDB(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:SQLite/test.db");
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS TESTDATA (uid integer PRIMARY KEY, name text);");
            stmt.execute("INSERT INTO TESTDATA(uid, name) VALUES(1, \"David\")");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
