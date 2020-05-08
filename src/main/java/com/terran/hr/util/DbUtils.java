package com.terran.hr.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DBUtils使用
 * DbUtils参考
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
     * 采用jdbc，封装查询jdbc
     * @param driverClassName
     * @param url
     * @param username
     * @param password
     * @param sql
     * @param params
     * @return
     * @throws Exception
     */
    public static List<Map<String,Object>> getDataFormJdbc(String driverClassName,String url,String username,
                                    String password,String sql,Object...params){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection connection = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            Class.forName(driverClassName);
            connection = DriverManager.getConnection(url,username,password);
            pstmt = connection.prepareStatement(sql);
            if(params != null){
                for (int i = 0;i < params.length ; i ++) {
                    pstmt.setObject(i + 1,params[i]);
                }
            }
            rs = pstmt.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                Map<String, Object> rowData = new HashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i).toLowerCase(), rs.getString(i));
                }
                list.add(rowData);
            }
        }catch(Exception e){
                e.printStackTrace();
        }finally{
            closeAll(connection,pstmt,rs);
        }
        return list;
    }
    public static Connection getConnection(String driverClassName,String url,String username,
                                    String password) throws Exception{
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
