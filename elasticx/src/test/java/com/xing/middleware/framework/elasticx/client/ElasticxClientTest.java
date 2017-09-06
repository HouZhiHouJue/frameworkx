package com.xing.middleware.framework.elasticx.client;

import com.alibaba.fastjson.TypeReference;
import com.xing.middleware.framework.elasticx.client.model.QueryListResult;
import com.xing.middleware.framework.elasticx.client.model.Person;
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
        QueryListResult<Person> queryListResult = elasticxClient.query("SELECT * FROM cu-test-2017.09.02 limit 10", new TypeReference<QueryListResult<Person>>() {
        });
        Assert.assertTrue(result);
        elasticxClient.destroy();
    }
}


