package com.terran.utilTest;

import com.terran.ecm.util.DbUtils;
import com.terran.ecm.util.XmlUtil;
import com.terran.scheduled.api.model.SysJobConfig;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class XmlTest {
    //ekp
    public static String driverCLass1 = "oracle.jdbc.driver.OracleDriver";
    public static String url1 = "jdbc:oracle:thin:@172.30.30.30:1521:ekp";
    public static String username1 = "ekp";
    public static String password1 = "abcABC123";
    public static String pool1 = "hikari";

    //本地库
    public static String driverCLass = "oracle.jdbc.driver.OracleDriver";
    public static String url = "jdbc:postgresql://192.168.76.144:5432/postgres";
    public static String username = "postgres";
    public static String password = "123456";
    public static String pool = "hikari";

    //本地数据库
    @Test
    public void test1() throws Exception{
        SysJobConfig sys = new SysJobConfig();
        sys.setRemark("123");
        sys.setJobId(12);
        sys.setCreateTime(new Date());
        sys.setName("cccc");
        System.out.println(XmlUtil.objectXmlEncoder(sys));
    }
    public static void main(String[] args) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            String sql = "select extend_data_xml from km_review_main where fd_id= ? ";
            connection = DbUtils.setPoolDataSource("druid",driverCLass1, url1, username1, password1).getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, "160b62201f035d9cef43af94a2da7329");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1));
                Map<String, Object> map = XmlUtil.getModelData(rs.getString(1));
                System.out.println(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
            }
        }
    }
}
