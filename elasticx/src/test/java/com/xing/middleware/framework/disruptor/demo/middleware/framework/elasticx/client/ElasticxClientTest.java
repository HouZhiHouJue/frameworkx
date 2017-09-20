package com.xing.middleware.framework.disruptor.demo.middleware.framework.elasticx.client;

import com.alibaba.fastjson.TypeReference;

import com.xing.middleware.framework.disruptor.demo.middleware.framework.elasticx.client.model.Person;
import com.xing.middleware.framework.disruptor.demo.middleware.framework.elasticx.client.model.QueryListResult;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Jecceca on 2017/8/28.
 */
public class ElasticxClientTest extends TestCase {

    public void testElasticx() throws Exception {
        ElasticxClient elasticxClient = new ElasticxClient("139.224.137.75,139.224.137.83");
        elasticxClient.afterPropertiesSet();
        ArrayList<Person> datas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Person data = new Person();
            data.setName("test2");
            data.setAge(i);
            data.setBirthDay(Calendar.getInstance().getTime());
            datas.add(data);
        }
        boolean result = elasticxClient.batchSave("test", datas);
        QueryListResult<Person> queryListResult = elasticxClient.query("SELECT * FROM cu-test-2017.09.20 limit 10",
                new TypeReference<QueryListResult<Person>>(Person.class) {
                });
        Assert.assertTrue(result);
        Assert.assertNotNull(queryListResult);
        elasticxClient.destroy();
    }

    public void testJDBC() throws Exception {
//        Properties properties = new Properties();
//        properties.put("url", "jdbc:elasticsearch://139.224.137.75:9800/");
//        DruidDataSource dds = (DruidDataSource) ElasticSearchDruidDataSourceFactory.createDataSource(properties);
//        Connection connection = dds.getConnection();
//        String sql = "SELECT\n" +
//                "\tprovinceId,\n" +
//                "\tprovinceName,\n" +
//                "\tcityId,\n" +
//                "\tcityName,\n" +
//                "\tdistrictId,\n" +
//                "\tdistrictName,\n" +
//                "\tcompanyId,\n" +
//                "\tcompanyName,\n" +
//                "\tvehicleOperateTypeId,\n" +
//                "\tvehicleOperateTypeCode,\n" +
//                "\tvehicleOperateTypeName,\n" +
//                "\tsum(ONLINE) AS onlineQuantity,\n" +
//                "\tcount(*) AS totalQuantity,\n" +
//                "\tsum(todayDuration) AS totalOnlineTime\n" +
//                "FROM\n" +
//                "\tcu-vehicle_state_history_data-*\n" +
//                "WHERE companyId=4444\n" +
//                "GROUP BY\n" +
//                "\tprovinceId,\n" +
//                "    provinceName,\n" +
//                "\tcityId,\n" +
//                "\tcityName,\n" +
//                "\tdistrictId,\n" +
//                "\tdistrictName,\n" +
//                "\tcompanyId,\n" +
//                "\tcompanyName,\n" +
//                "\tvehicleOperateTypeId,\n" +
//                "\tvehicleOperateTypeCode,\n" +
//                "\tvehicleOperateTypeName\n" +
//                "\n";
//        PreparedStatement ps = connection.prepareStatement(sql);
//        ResultSet resultSet = ps.executeQuery();
//
//        ps.close();
//        connection.close();
//        dds.close();
    }
}


