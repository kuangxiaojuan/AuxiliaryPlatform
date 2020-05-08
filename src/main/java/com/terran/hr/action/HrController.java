package com.terran.hr.action;

import com.google.common.cache.Cache;
import com.terran.hr.util.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@RestController
public class HrController {
    /*
     *hr测试库
     */
    public static String driverCLass = "oracle.jdbc.driver.OracleDriver";
    public static String url = "jdbc:oracle:thin:@172.30.10.113:1521:orcl";
    public static String username = "yksoft";
    public static String password = "yksoft1919";
    public static String pool = "druid";

    @Autowired
    private Cache<String, Object> guavaCache;

    /**
     * HR转移后，记录存储至数据库,同步处理
     *
     * @param oldHrId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/terran/getHrCodeOrigin", method = RequestMethod.GET)
    public String getOriginHrCodeId(@RequestParam(value = "oldHrId") String oldHrId) throws Exception {
        String params = null;
        if (!StringUtils.isEmpty(oldHrId)) {
            if (oldHrId.indexOf(",") > -1) {
                String[] oldHrIds = oldHrId.split(",");
                String oldHrIdTemp = "";
                for (String hrId : oldHrIds) {
                    oldHrIdTemp += "'" + hrId + "',";
                }
                oldHrIdTemp = oldHrIdTemp.substring(0, oldHrIdTemp.length() - 1);
                params = " in (" + oldHrIdTemp + ")";
            } else params = "='" + oldHrId + "'";
        }
        //根据需要转移的部门id获取该部门下的所有人
        String sql = "select u.a0100 a0100," +
                "u.b0110 b0110," +
                "org.codeitemdesc codeitemdesc," +
                "u.e0122 e0122," +
                "o.CODEITEMDESC codeitem," +
                "u.e01a1 e01a1," +
                "u.e0127 e0127," +
                "u.a0101 a0101," +
                "u.modtime time " +
                "from usra01 u inner join (" +
                "select * from organization start with codeitemid "+params+" " +
                "connect by prior codeitemid=parentid and codesetid='UM' " +
                "and end_date = to_date('9999-12-31','yyyy-MM-dd')) o " +
                "on u.e0122 = o.codeitemid " +
                "inner join organization org on " +
                "org.codeitemid = u.b0110" +
                "order by u.modtime desc";
        List<Map<String, Object>> peoples = DbUtils.getDataFormJdbc(driverCLass, url, username, password, sql, null);
        guavaCache.put(oldHrId, peoples);
        return "成功连接本地";
    }

    /**
     * 数据库插入记录，异步处理
     * @param oldHrId
     * @param newHrId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/terran/insertHrCodeOrigin",method=RequestMethod.GET)
    public String insertHrCodeId(@RequestParam(value = "oldHrId") String oldHrId, @RequestParam(value = "newHrId") String newHrId) throws Exception {
        Thread.sleep(15000);//沉睡15s,待hr处理
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        List<Map<String, Object>> peoples = (List<Map<String, Object>>)guavaCache.getIfPresent(oldHrId);
        String sql = " insert into usra17(A0100,I9999,A1706,A1707,A1708,A17AA,A17AB,A17AC,A17AL,A17AV)" +
                "values (?,?,?,?,?,?,?,?,?,sysdate)";
        String sql1 = "select max(i9999) i9999 " +
                "from usra17 where a0100=? " +
                "order by i9999 desc";
        String sql2 = "select b0110,e0122,e01a1 from usra01 where A0100 = ?";
        try{
            conn = DbUtils.getConnection(driverCLass, url, username, password);
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            for (Map<String, Object> people : peoples) {
                //查询出最大排序
                ps1 = conn.prepareStatement(sql1);
                ps1.setString(1,people.get("a0100").toString());
                rs1 = ps1.executeQuery();
                String maxString = "";
                while(rs1.next()){
                    maxString = rs1.getString("i9999");
                }
                int i9999 = 0;
                if(maxString!=null)i9999 = Integer.parseInt(maxString);
                else i9999 = 0;

                //查询出该people当前公司，部门，岗位信息等
                ps2 = conn.prepareStatement(sql2);
                ps2.setString(1,people.get("a0100").toString());
                rs2 = ps2.executeQuery();
                String b0110 = "";
                String e0122 = "";
                String e01a1 = "";
                while(rs2.next()){
                    b0110 = rs2.getString("b0110");
                    e0122 = rs2.getString("e0122");
                    e01a1 = rs2.getString("e01a1");
                }
                //插入数据库
                ps.setString(1,people.get("a0100").toString());
                ps.setInt(2,i9999+1);
                ps.setString(3,b0110);
                ps.setString(4,e0122);
                ps.setString(5,e01a1);
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
            ps.clearBatch();
        }catch (Exception e){
            conn.rollback();
            try {
                if(conn != null) conn.close();
                if(ps != null)ps.close();
                if(ps2 != null)ps2.close();
                if(ps1 != null)ps1.close();
                if(rs2 != null)rs2.close();
                if(rs1 != null)rs1.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                if(conn != null) conn.close();
                if(ps != null)ps.close();
                if(ps2 != null)ps2.close();
                if(ps1 != null)ps1.close();
                if(rs2 != null)rs2.close();
                if(rs1 != null)rs1.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}