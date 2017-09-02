package com.xing.middleware.framework.elasticx.client;

import com.xing.middleware.framework.elasticx.client.pojo.Person;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

/**
 * Created by Jecceca on 2017/8/28.
 */
public class ElasticxClientTest extends TestCase {

    public void testElasticx() throws Exception {
        HashSet<String> servers = new HashSet<>();
        servers.add("139.224.137.75");
        servers.add("139.224.137.83");
        ElasticxClient elasticxClient = new ElasticxClient(servers);
        elasticxClient.afterPropertiesSet();
        ArrayList<Person> datas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Person data = new Person();
            data.setName("test2");
            data.setAge(i);
            data.setBirthDay(Calendar.getInstance().getTime());
            datas.add(data);
        }
        for (int i = 0; i < 10; i++) {
            boolean result1 = elasticxClient.save("cu-test", "test", datas.get(0));
            boolean result2 = elasticxClient.batchSave("cu-test", "test", datas);
            Assert.assertTrue(result1 && result2);
        }
        elasticxClient.destroy();
    }
}


