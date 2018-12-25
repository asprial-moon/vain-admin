import com.alibaba.fastjson.JSONObject;
import com.vain.Application;
import com.vain.component.ElasticsearchComponent;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void testHighlight() {
        HighlightBuilder hiBuilder = new HighlightBuilder();
        hiBuilder.preTags("<h2>");
        hiBuilder.postTags("</h2>");
        hiBuilder.field("about");
        Map<String, Object> matchString = new HashMap<>();
        matchString.put("about", "理念");
        // 搜索数据
        List<Map<String, Object>> response = elasticsearchComponent.search("memories", null, 0, 0, 20, null, "age",
                true, "about", matchString, hiBuilder);
        if (null != response) {
            for (Map<String, Object> stringObjectMap : response) {
                for (Map.Entry<String, Object> stringObjectEntry : stringObjectMap.entrySet()) {
                    log.info("key {} value {} ", stringObjectEntry.getKey(), JSONObject.toJSONString(stringObjectEntry.getValue()));
                }
            }
        }

    }
}
