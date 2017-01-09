package com.minicubic.mssqlbridge.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xergio
 */
public class ConnectionUtil {

    private static final String DB = "jdbc:sqlserver://10.57.1.26:1433;databaseName=courier_system;user=minicubic_user;password=M1n1.Cub1c";
    private static final String USER_ID = "minicubic_user";
    private static final String PASSWORD = "M1n1.Cub1c";
    private final Connection conn;

    public ConnectionUtil() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        conn = DriverManager.getConnection(DB, USER_ID, PASSWORD);
    }

    public Connection getConnection() {
        return conn;
    }

    public void test() {
        Statement statement;
        ResultSet rs;
        
        String queryString = "select * from sysobjects where type='u'";
        
        try {

            statement = conn.createStatement();
            rs = statement.executeQuery(queryString);

            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (Exception ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
