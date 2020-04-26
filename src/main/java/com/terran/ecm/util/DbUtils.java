package com.terran.ecm.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * DBUtils使用
 * https://blog.csdn.net/simonforfuture/article/details/90480147
 * @throws Exception
 */
public class DbUtils {
    /**
     * 采用连接池
     * DruidDataSourceFactory
     * @param url
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public static DataSource setPoolDataSource(String connectPool,String driverClassName,
                                                String url,String username,String password) throws Exception{
        Connection connection = null;
        DataSource dataSource = null;
        if(connectPool.equals("druid")){
            Map<String,String> map = new HashMap<>();
            map.put("driverClassName",driverClassName);
            map.put("url",url);
            map.put("username",username);
            map.put("password",password);
                dataSource = DruidDataSourceFactory.createDataSource(map);
        }else if(connectPool.equals("hikari")){
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(driverClassName);
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }
    /**
     * 纯jdbc,无连接池,DriverManager
     * https://www.cnblogs.com/noteless/p/10319296.html
     */
    public static Connection setJDBCDataSource(String driverClassName,
                                                String url,String username,String password) throws Exception{
        Class.forName(driverClassName);
        return DriverManager.getConnection(url,username,password);
    }
    /**
     * 关闭资源统一代码
     * @param conn
     * @param st
     * @param rs
     */
    public static void closeAll(Connection conn, PreparedStatement st, ResultSet rs){
        try {
            if(conn != null) conn.close();
            if(st != null)st.close();
            if(rs != null)rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
