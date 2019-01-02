import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.vain.Application;
import com.vain.component.ElasticsearchComponent;
import com.vain.constant.ElasticsearchConstant;
import com.vain.entity.ElasticsearchPageEntity;
import com.vain.entity.OperationLog;
import com.vain.pool.ThreadPool;
import com.vain.service.IOperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author vain
 * @Description
 * @date 2018/12/25 21:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@Slf4j
public class ElasticSearchTest {

    @Autowired
    private ElasticsearchComponent elasticsearchComponent;

    @Resource
    private IOperationLogService operationLogService;

    @Resource
    private ThreadPool threadPool;

    @Test
    public void testHighlight() {
        HighlightBuilder hiBuilder = new HighlightBuilder();
        hiBuilder.preTags("<h2>");
        hiBuilder.postTags("</h2>");
        hiBuilder.field("operationData");
        Map<String, Object> matchString = new HashMap<>();
        matchString.put("operationData", "");
        // 搜索数据
        ElasticsearchPageEntity page = elasticsearchComponent.page(ElasticsearchConstant.LOGS_INDEX, ElasticsearchConstant.LOGS_TYPE, 0, 0, null, "createTime",
                false, "operationData", matchString, null, 1, 20);
        if (null != page) {
            for (Map<String, Object> stringObjectMap : page.getData()) {
                for (Map.Entry<String, Object> stringObjectEntry : stringObjectMap.entrySet()) {
                    log.info("key {} value {} ", stringObjectEntry.getKey(), JSONObject.toJSONString(stringObjectEntry.getValue()));
                }
            }
        }
    }

    @Test
    public void deleteHighlight() {
        System.out.println(elasticsearchComponent.deleteIndex("memories"));
    }

    @Test
    public void createIndex() {
        System.out.println(elasticsearchComponent.createIndex(ElasticsearchConstant.LOGS_INDEX));
    }

    @Test
    public void deleteByID() {
        System.out.println(elasticsearchComponent.delete(ElasticsearchConstant.LOGS_INDEX, ElasticsearchConstant.LOGS_TYPE, 330 + ""));
    }


    @Test
    public void addLog() {

        long time = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            threadPool.execute(() -> {
                List<OperationLog> operationLogList = Lists.newArrayList();
                for (int j = 0; j < 1000; j++) {
                    OperationLog operationLog = new OperationLog();
                    operationLog.setInfo(this.getRandomChar(10));
                    operationLog.setOperationData(getRandomChar(20));
                    operationLog.setMethodName(this.getRandomChar(10));
                    operationLog.setClassName(this.getRandomChar(5));
                    operationLog.setCreateTime(new Date());
                    operationLog.setOperationIP(new Random().nextInt(100000) + "");
                    operationLog.setUserId(new Random().nextInt(10000));
                    operationLog.setUserName(this.getRandomChar(3));
                    operationLog.setOperationType(new Random().nextInt(7));
                    operationLogList.add(operationLog);
                }
                log.info("{} ", operationLogService.addBatch(operationLogList));
            });
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("execution time {}", System.currentTimeMillis() - time);
    }

    public String getRandomChar(int size) {
        StringBuilder str = new StringBuilder();
        int highCode;
        int lowCode;

        for (int i = 0; i < size; i++) {
            Random random = new Random();

            highCode = (176 + Math.abs(random.nextInt(39))); //B0 + 0~39(16~55) 一级汉字所占区
            lowCode = (161 + Math.abs(random.nextInt(93))); //A1 + 0~93 每区有94个汉字

            byte[] b = new byte[2];
            b[0] = (Integer.valueOf(highCode)).byteValue();
            b[1] = (Integer.valueOf(lowCode)).byteValue();
            try {
                str.append(new String(b, "GBK"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return str.toString();
    }
}
