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
    public static String pool1 = "druid";
    //hr测试库
    public static String driverCLass2 = "oracle.jdbc.driver.OracleDriver";
    public static String url2 = "jdbc:oracle:thin:@172.30.10.113:1521:orcl";
    public static String username2 = "yksoft";
    public static String password2 = "yksoft1919";
    public static String pool2 = "druid";
    //本地库
    public static String driverCLass = "oracle.jdbc.driver.OracleDriver";
    public static String url = "jdbc:postgresql://192.168.76.144:5432/postgres";
    public static String username = "postgres";
    public static String password = "123456";
    public static String pool = "druid";

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
    @Test
    public void test2() throws Exception{
        //根据需要转移的部门id获取该部门下的所有人
        String sql = "select u.a0100 a0100," +
                " u.b0110 b0110," +
                " u.e0122 e0122," +
                " u.e01a1 e01a1," +
                " u.e0127 e0127," +
                " u.a0101 a0101 " +
                "from usra01 u inner join ( " +
                "select * from organization start with codeitemid='100013070A'  " +
                "connect by prior codeitemid=parentid and codesetid='UM' " +
                "and end_date = to_date('9999-12-31','yyyy-MM-dd')) o " +
                "on u.e0122 = o.codeitemid";
        List<Map<String,Object>> peoples = DbUtils.getDataFormJdbc(driverCLass2,url2,username2,password2,sql,null);
        for (Map<String, Object> people : peoples) {
            System.out.println(people.get("a0100"));
            System.out.println(people.get("b0110"));
            System.out.println(people.get("e0122"));
            System.out.println(people.get("e01a1"));
            System.out.println(people.get("e0127"));
            System.out.println(people.get("a0101"));
        }
    }
    public static void main(String[] args) {
        String sql = "select extend_data_xml extend from km_review_main where fd_id= ? ";
        Object[] obj = {"160b62201f035d9cef43af94a2da7329"};
        List<Map<String,Object>> retlist = DbUtils.getDataFormJdbc(driverCLass1,url1, username1, password1,sql,obj);
        for (Map<String,Object> stringObjectMap : retlist) {
            String objMap = stringObjectMap.get("extend").toString();
            System.out.println(objMap);
        }
    }
}
