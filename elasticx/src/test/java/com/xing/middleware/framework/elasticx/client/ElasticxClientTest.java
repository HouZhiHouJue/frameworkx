package com.xing.middleware.framework.elasticx.client;

import com.alibaba.druid.pool.ElasticSearchDruidDataSource;
import com.alibaba.druid.pool.ElasticSearchDruidDataSourceFactory;
import com.alibaba.fastjson.TypeReference;

import com.xing.middleware.framework.elasticx.client.model.Person;
//import com.xing.middleware.framework.elasticx.client.model.PersonVO;
import com.xing.middleware.framework.elasticx.client.model.QueryListResult;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

/**
 * Created by Jecceca on 2017/8/28.
 */
public class ElasticxClientTest extends TestCase {

    public void testElasticx() throws Exception {
        Properties properties = new Properties();
        properties.put("url", "jdbc:elasticsearch://139.224.137.75:9800/");
        properties.put("testWhileIdle", "false");
        ElasticSearchDruidDataSource dds = (ElasticSearchDruidDataSource) ElasticSearchDruidDataSourceFactory.createDataSource(properties);
        ElasticxClient elasticxClient = new ElasticxClient("139.224.137.75,139.224.137.83", dds);
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
        QueryListResult<Person> queryListResult = elasticxClient.query("SELECT * FROM cu-test-2017.10.11 limit 10",
                new TypeReference<QueryListResult<Person>>(Person.class) {
                });
        Assert.assertTrue(result);
        Assert.assertNotNull(queryListResult);
//        String groupSql = "select sum(online) as onlineQuantity,count(*) as totalQuantity,sum(todayDuration) " +
//                "as totalOnlineTime from cu-vehicle_state_history_data-*" +
//                " group by provinceId,provinceName,cityId,cityName,districtId,districtName,companyId," +
//                "companyName,vehicleOperateTypeId,vehicleOperateTypeCode,vehicleOperateTypeName";
//
//        List<VehicleStatisticsPojo> demoBeanList = elasticxClient.query(groupSql, VehicleStatisticsPojo.class);
        String groupSql2 = "SELECT count(age) FROM cu-test-2017.10.11 group by birthDay";
//        List<PersonVO> personVOList = elasticxClient.query(groupSql2, PersonVO.class);
//        Assert.assertNotNull(demoBeanList);
        elasticxClient.destroy();
    }


}


