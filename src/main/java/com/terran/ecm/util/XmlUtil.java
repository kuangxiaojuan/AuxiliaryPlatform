package com.terran.ecm.util;

import org.apache.poi.util.StringUtil;

import java.beans.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlUtil {
    /**
     * java持久化,xmlEncoder形式
     * https://blog.csdn.net/pacosonswjtu/article/details/50723360
     * XMLEncoder,XMLDecoder 方式
     */
    public static String objectXmlEncoder(Object obj) throws Exception{
        //非固定内存缓冲区，可扩容。非BufferedOutputStream固定内存
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(byteArrayOutputStream);
        //BigDecimal特殊委托处理
        encoder.setPersistenceDelegate(BigDecimal.class,bigDecimalPersistenceDelegate);
        encoder.writeObject(obj);
        encoder.flush();
        encoder.close();
        String retnVal = new String(byteArrayOutputStream.toByteArray(),"UTF-8");
        byteArrayOutputStream.close();
        return retnVal;
    }
    /**
     * 获取java持久化对象
     */
    public static Map<String,Object> getModelData(String xml) throws Exception{
            List datas = XmlUtil.objectXMLDecoderByString(xml);
        return new HashMap((Map)datas.get(0));
    }




    private static List objectXMLDecoderByString(String xml) throws Exception{
        String safeIns = xml.replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]", "");
        return objectXmlDecoder(new ByteArrayInputStream(safeIns.getBytes("UTF-8")));
    }
    private static List objectXmlDecoder(InputStream ins) throws IOException,
            Exception {
        List objList = new ArrayList();
        XMLDecoder decoder = new XMLDecoder(ins);
        Object obj = null;
        try {
            while ((obj = decoder.readObject()) != null) {
                objList.add(obj);
            }
        } catch (ArrayIndexOutOfBoundsException e)
        {} finally {
            ins.close();
            decoder.close();
        }
        return objList;
    }
    private static final PersistenceDelegate bigDecimalPersistenceDelegate = new DefaultPersistenceDelegate() {
        protected Expression instantiate(Object oldInstance,
                                         Encoder out) {
            BigDecimal bd = (BigDecimal) oldInstance;
            return new Expression(oldInstance, oldInstance
                    .getClass(), "new", new Object[] { bd
                    .toString() });
        }

        protected boolean mutatesTo(Object oldInstance,
                                    Object newInstance) {
            return oldInstance.equals(newInstance);
        }
    };
}
