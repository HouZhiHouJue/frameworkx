package com.xing.demo.controller;


import com.google.common.io.Files;
import com.luckytiger.framework.dfs.DfsClient;
import com.luckytiger.framework.dfs.FileInfo;
import com.xing.demo.model.DemoBean;
import com.xing.demo.model.Order;
import com.xing.demo.model.Person;
import com.xing.middleware.framework.elasticx.client.ElasticxClient;
import com.xing.middleware.framework.elasticx.client.model.QueryListResult;
import com.xing.middleware.framework.fdfsx.client.FdfsFileInfo;
import com.xing.middleware.framework.fdfsx.client.FdfsxClient;
import com.xing.middleware.framework.fdfsx.client.UploadResult;
import com.xing.middleware.framework.rocketx.client.Producer;
import org.csource.common.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jecceca on 2017/9/4.
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DfsClient dfsClient;

    @RequestMapping(value = "/dfs", method = RequestMethod.GET)
    public
    @ResponseBody
    String es() throws Exception {
        File file = new File("f:/1.zip");
        FileInfo fileInfo = new FileInfo(file.getName(), Files.toByteArray(file));
        com.luckytiger.framework.dfs.UploadResult uploadResult = dfsClient.upload(fileInfo);
        return uploadResult.getFid();
    }


//    @Autowired
//    private ElasticxClient elasticxClient;
//
//    @Autowired
//    private FdfsxClient fdfsxClient;
//    @Autowired
//    private Producer producer;
//
//    @RequestMapping(value = "/es", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    String es() throws Exception {
//        ArrayList<Person> datas = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            Person data = new Person();
//            data.setName("test2");
//            data.setAge(i);
//            data.setBirthDay(Calendar.getInstance().getTime());
//            datas.add(data);
//        }
//        boolean result = elasticxClient.batchSave("test", datas);
//        QueryListResult<Person> queryListResult = elasticxClient.query("SELECT * FROM cu-test-2017.09.20 order by birthDay desc limit 10"
//                , new TypeReference<QueryListResult<Person>>(Person.class) {
//                });
//        Person person = queryListResult.getHits().getHits().get(0).getSource();
//        Assert.isTrue(result);
//        Assert.notNull(person);
//        return "Hello Elasticsearch";
//    }
//
//    @RequestMapping(value = "/esJdbc", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    String esJdbc() throws Exception {
//        String groupSql = "SELECT\n" +
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
//                "GROUP BY\n" +
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
//                "\tvehicleOperateTypeName";
//        List<DemoBean> demoBeanList = elasticxClient.query(groupSql, DemoBean.class);
//        Assert.notEmpty(demoBeanList);
//        return "Hello Elasticsearch Jdbc";
//    }
//
//    @RequestMapping(value = "/rocketmq", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    String rocketmq() throws InterruptedException, RemotingException, MQClientException, MQBrokerException, UnsupportedEncodingException {
//        Order order = new Order();
//        order.setOrderId("order-111");
//        order.setMessage("order message");
//        SendResult sendResult = producer.syncSend("fxTestTopic", "order-" + Long.toString(System.currentTimeMillis()), order);
//        Assert.isTrue(sendResult.getSendStatus().equals(SendStatus.SEND_OK));
//        return "Hello Rocketmq";
//    }
//
//    @RequestMapping(value = "/fdfs", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    String fdfs() throws Exception {
//        final FdfsFileInfo fdfsFileInfo = new FdfsFileInfo();
//        fdfsFileInfo.setExtName("jpg");
//        fdfsFileInfo.setFileBuffer(Files.toByteArray(new File("E:/1.jpg")));
//        NameValuePair[] nameValuePairs = new NameValuePair[4];
//
//        nameValuePairs[0] = new NameValuePair("width", "1024");
//        nameValuePairs[1] = new NameValuePair("heigth", "768");
//        nameValuePairs[2] = new NameValuePair("bgcolor", "#000000");
//        nameValuePairs[3] = new NameValuePair("title", "Untitle");
//
//        fdfsFileInfo.setMetadata(nameValuePairs);
//        UploadResult uploadResult = null;
//        uploadResult = fdfsxClient.uploadFile(fdfsFileInfo);
//        System.out.print(uploadResult.getGroupName() + "/" + uploadResult.getRemoteFileName() + "\n");
//        FdfsFileInfo fdfsFileInfo2 = fdfsxClient.downloadFile(uploadResult.getGroupName(), uploadResult.getRemoteFileName());
//        org.junit.Assert.assertArrayEquals(fdfsFileInfo.getFileBuffer(), fdfsFileInfo2.getFileBuffer());
//        return "Hello Fdfs ";
//    }
}
