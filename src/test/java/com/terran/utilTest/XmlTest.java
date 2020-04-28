package com.terran.utilTest;

import com.terran.ecm.util.DbUtils;
import com.terran.ecm.util.XmlUtil;
import com.terran.scheduled.api.model.SysJobConfig;
import oracle.sql.CLOB;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
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

    //xml 持久化
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
        String sql = "select extend_data_xml extend from km_review_main where fd_id= ? ";
        Object[] obj = {"160b62201f035d9cef43af94a2da7329"};
        List<Map<String,Object>> retlist = DbUtils.getDataFormJdbc(driverCLass1, url1, username1, password1,sql,obj);
        for (Map<String,Object> stringObjectMap : retlist) {
            String objMap = stringObjectMap.get("extend").toString();
            System.out.println(objMap);
        }
    }
}
