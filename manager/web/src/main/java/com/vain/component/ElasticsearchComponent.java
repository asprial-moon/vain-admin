package com.vain.component;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.vain.entity.ElasticsearchPageEntity;
import com.vain.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author vain
 * @Description
 * @date 2018/12/24 21:29
 */
@Component
@Slf4j
public class ElasticsearchComponent {

    @Autowired
    private TransportClient client;

    /**
     * 创建索引
     *
     * @param index
     * @return
     */
    public boolean createIndex(String index) {
        if (StringUtils.isEmpty(index)) {
            return false;
        }
        if (!isIndexExist(index)) {
            log.info("index {} in not exist", index);
            CreateIndexResponse execute = client.admin()
                    .indices()
                    .prepareCreate(index)
                    .execute()
                    .actionGet();
            log.info("index {} execute {}", index, execute.isAcknowledged());
        }
        return true;
    }

    /**
     * 创建索引
     *
     * @param index
     * @return
     */
    public boolean deleteIndex(String index) {
        if (StringUtils.isEmpty(index)) {
            return false;
        }
        if (!isIndexExist(index)) {
            log.info("index {} in not exist", index);
            DeleteIndexResponse deleteIndexResponse = client.admin()
                    .indices()
                    .prepareDelete(index)
                    .execute()
                    .actionGet();
            log.info("index {} execute {}", index, deleteIndexResponse.isAcknowledged());
        }
        return true;
    }

    /**
     * 判断索引是否存在
     *
     * @param index
     * @return
     */
    private boolean isIndexExist(String index) {
        if (StringUtils.isNotEmpty(index)) {
            IndicesExistsResponse indicesExistsResponse = client.admin()
                    .indices()
                    .exists(new IndicesExistsRequest(index))
                    .actionGet();
            if (indicesExistsResponse.isExists()) {
                log.info("index {} is exist", index);
            } else {
                log.info("index {} is not exist", index);
            }
            return indicesExistsResponse.isExists();
        }
        return false;
    }

    /**
     * 添加数据
     *
     * @param object 数据
     * @param index  索引
     * @param type   类型
     * @param id     id
     * @return 创建最后的id
     */
    public String add(JSONObject object, String index, String type, String id) {
        if (StringUtils.isNotEmpty(index) && StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(id)) {
            IndexResponse indexResponse = client.prepareIndex(index, type, id)
                    .setSource(object)
                    .get();
            log.info("index {} type {} add data {} response status {} id{}", index, type, JSONObject.toJSONString(object), indexResponse.status().getStatus(), indexResponse.getId());
            return indexResponse.getId();
        }
        return "";
    }


    /**
     * 批量添加数据
     *
     * @param objects 数据
     * @param index   索引
     * @param type    类型
     * @return 创建最后的id
     */
    public Integer addBatch(List<JSONObject> objects, String index, String type) {
        if (StringUtils.isNotEmpty(index) && StringUtils.isNotEmpty(type)) {
            BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
            if (!CollectionUtils.isEmpty(objects)) {
                for (JSONObject object : objects) {
                    bulkRequestBuilder.add(client.prepareIndex(index, type,
                            StringUtils.isNotEmpty(object.getString("id")) ? object.getString("id") : UUID.randomUUID().toString().replaceAll("-", ""))
                            .setSource(object));
                }
            }
            BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
            log.info("index {} type {} add batch response status {}  tookInMills {}", index, type, bulkResponse.status().getStatus(), bulkResponse.getTookInMillis());
            return bulkResponse.getItems().length;
        }
        return 0;
    }

    public String add(JSONObject object, String index, String type) {
        return this.add(object, index, type, UUID.randomUUID().toString().replaceAll("-", ""));
    }


    /**
     * 删除对应id的数据
     *
     * @param index
     * @param type
     * @param id
     * @return
     */
    public boolean delete(String index, String type, String id) {
        if (StringUtils.isNotEmpty(index) && StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(id)) {
            DeleteResponse deleteResponse = client.prepareDelete(index, type, id)
                    .execute()
                    .actionGet();
            log.info("index {} type {} delete id {} response status {}", index, type, id, deleteResponse.status().getStatus());
            return deleteResponse.status().getStatus() == HttpStatus.SC_OK;
        }
        return false;
    }

    /**
     * 根据id更新对象数据
     *
     * @param data
     * @param index
     * @param type
     * @param id
     * @return
     */
    public boolean update(JSONObject data, String index, String type, String id) {
        if (StringUtils.isNotEmpty(index) && StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(id)) {
            UpdateResponse updateResponse = client.update(new UpdateRequest().index(index).type(type).id(id).doc(data)).actionGet();
            log.info("index {} type {} update id {} response status {} data {}", index, type, id, updateResponse.status().getStatus(), JSONObject.toJSONString(data));
            return updateResponse.status().getStatus() == HttpStatus.SC_OK;
        }
        return false;
    }

    /**
     * 根据id获取数据
     *
     * @param index
     * @param type
     * @param id
     * @param fieIds 逗号分隔需要的字段 默认为全部字段
     * @return
     */
    public Map<String, Object> get(String index, String type, String id, String fieIds) {
        if (StringUtils.isNotEmpty(index) && StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(id)) {
            GetRequestBuilder getRequestBuilder = client.prepareGet(index, type, id);
            if (StringUtils.isNotEmpty(fieIds)) {
                getRequestBuilder.setFetchSource(fieIds.split(","), null);
            }
            GetResponse getResponse = getRequestBuilder.execute().actionGet();
            return getResponse.getSource();
        }
        return null;
    }

    /**
     * 查询
     *
     * @param index          索引名称
     * @param type           类型名称 多个用逗号隔开
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @param size           大小限制 默认10
     * @param fields         需要显示的字段 默认为全部
     * @param sortFields     排序字段
     * @param matchPhrase    是否精准匹配
     * @param highlightField 高亮字段
     * @param matchString    匹配条件
     * @return
     */
    public List<Map<String, Object>> search(String index, String type, long startTime, long endTime, Integer size, String fields, String sortFields,
                                            boolean matchPhrase, String highlightField, Map<String, Object> matchString, HighlightBuilder highlightBuilder) {
        if (StringUtils.isNotEmpty(index)) {
            SearchRequestBuilder searchRequestBuilder = getSearchRequestBuilder(index, type, startTime, endTime, fields, sortFields, matchPhrase, highlightField, matchString, highlightBuilder);
            //数据大小
            if (null != size && size > 0) {
                searchRequestBuilder.setSize(size);
            }
            SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
            long totalHits = searchResponse.getHits().totalHits;
            long length = searchResponse.getHits().getHits().length;

            log.info("search total size {} result size {}", totalHits, length);

            if (HttpStatus.SC_OK == searchResponse.status().getStatus()) {
                return convertResult(searchResponse, highlightField);
            }
        }
        return null;
    }


    /**
     * 查询
     *
     * @param index          索引名称
     * @param type           类型名称 多个用逗号隔开
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @param pageSize       大小限制 默认10
     * @param fields         需要显示的字段 默认为全部
     * @param sortFields     排序字段
     * @param matchPhrase    是否精准匹配
     * @param highlightField 高亮字段
     * @param matchString    匹配条件
     * @return
     */
    public ElasticsearchPageEntity page(String index, String type, long startTime, long endTime, String fields, String sortFields,
                                        boolean matchPhrase, String highlightField, Map<String, Object> matchString, HighlightBuilder highlightBuilder,
                                        int currentPage, int pageSize) {
        if (StringUtils.isNotEmpty(index)) {
            SearchRequestBuilder searchRequestBuilder = getSearchRequestBuilder(index, type, startTime, endTime, fields, sortFields, matchPhrase, highlightField, matchString, highlightBuilder);
            searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH);
            searchRequestBuilder.setFrom(currentPage).setSize(pageSize);
            //按照匹配精准排序
            searchRequestBuilder.setExplain(true);
            SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
            long totalHits = searchResponse.getHits().totalHits;
            long length = searchResponse.getHits().getHits().length;

            log.info("search total size {} page result size {}", totalHits, length);

            if (HttpStatus.SC_OK == searchResponse.status().getStatus()) {
                return new ElasticsearchPageEntity(currentPage, pageSize, (int) totalHits, convertResult(searchResponse, highlightField));
            }
        }
        return null;
    }

    private SearchRequestBuilder getSearchRequestBuilder(String index, String type, long startTime, long endTime, String fields, String sortFields, boolean matchPhrase, String highlightField, Map<String, Object> matchString, HighlightBuilder highlightBuilder) {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        if (StringUtils.isNotEmpty(type)) {
            searchRequestBuilder.setTypes(type.split(","));
        }
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //时间范围
        if (startTime > 0 && endTime < 0) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("processTime")
                    .format("epoch mills")
                    .from(startTime)
                    .to(endTime)
                    .includeLower(true)
                    .includeUpper(true));
        }
        //匹配条件
        if (null != matchString) {
            for (Map.Entry<String, Object> match : matchString.entrySet()) {
                if (null != match.getValue() && !"".equals(match.getValue())){
                    boolQueryBuilder.should(matchPhrase ?
                            QueryBuilders.matchPhraseQuery(match.getKey(), match.getValue()) :
                            QueryBuilders.matchQuery(match.getKey(), match.getValue()));
                }
            }
        }
        //高亮字段
        if (StringUtils.isNotEmpty(highlightField)) {
            if (null == highlightBuilder) {
                highlightBuilder = new HighlightBuilder();
                //设置前缀
                highlightBuilder.preTags("<span style='color:red'>");
                //设置后缀
                highlightBuilder.postTags("</span>");
                highlightBuilder.field(highlightField);
            }
            searchRequestBuilder.highlighter(highlightBuilder);
        }
        searchRequestBuilder.setQuery(boolQueryBuilder);
        //需要字段
        if (StringUtils.isNotEmpty(fields)) {
            searchRequestBuilder.setFetchSource(fields.split(","), null);
        }
        //排序字段
        if (StringUtils.isNotEmpty(sortFields)) {
            searchRequestBuilder.addSort(sortFields, SortOrder.DESC);
        }
        return searchRequestBuilder;
    }

    /**
     * 将返回的高亮字段覆盖原来的结果
     *
     * @param searchResponse
     * @param highlightField
     * @return
     */
    private List<Map<String, Object>> convertResult(SearchResponse searchResponse, String highlightField) {
        List<Map<String, Object>> result = Lists.newArrayList();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            if (StringUtils.isNotEmpty(highlightField)) {
                if (null != hit.getHighlightFields().get(highlightField)) {
                    //elasticsearch 高亮的字段
                    Text[] fragments = hit.getHighlightFields().get(highlightField).getFragments();
                    if (null != fragments && fragments.length > 0) {
                        //替换原字段
                        hit.getSource().put(highlightField, Arrays.stream(fragments).map(Text::string).collect(Collectors.joining()));
                    }
                }
            }
            hit.getSource().put("id", hit.getId());
            result.add(hit.getSource());
        }
        return result;
    }
}
