package com.terran.ecm.action;

import com.terran.ecm.util.DbUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    /**
     * HR转移后，记录存储至数据库
     * @param oldHrId
     * @param newHrId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/terran/getHrCodeOrigin",method = RequestMethod.GET)
    public String insertHrCodeId(@RequestParam(value = "oldHrId") String oldHrId,@RequestParam(value = "newHrId") String newHrId) throws Exception{

        return "成功连接本地";
    }
}
