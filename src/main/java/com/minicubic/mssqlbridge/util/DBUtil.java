package com.minicubic.mssqlbridge.util;

import com.google.gson.Gson;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DBUtil {

    public String resultSetToJson(String query, Object[] params) throws Exception {
        ConnectionUtil util = new ConnectionUtil();
        Connection connection = util.getConnection();
        List<Map<String, Object>> listOfMaps = null;

        try {
            QueryRunner queryRunner = new QueryRunner();
            if ( params == null ) {
                listOfMaps = queryRunner.query(connection, query, new MapListHandler());
            } else {
                listOfMaps = queryRunner.query(connection, query, new MapListHandler(), params);
            }
            
        } catch (SQLException se) {
            throw new RuntimeException("Couldn't query the database.", se);
        } finally {
            DbUtils.closeQuietly(connection);
        }
        return new Gson().toJson(listOfMaps);
    }
    
    public void executeQuery(String query, Object[] params) throws Exception {
        ConnectionUtil util = new ConnectionUtil();
        Connection connection = util.getConnection();

        try {
            QueryRunner queryRunner = new QueryRunner();
            if ( params == null ) {
                queryRunner.update(connection, query);
            } else {
                queryRunner.update(connection, query, params);
            }
            
        } catch (SQLException se) {
            throw new RuntimeException("Couldn't query the database.", se);
        } finally {
            DbUtils.closeQuietly(connection);
        }
    }
}
