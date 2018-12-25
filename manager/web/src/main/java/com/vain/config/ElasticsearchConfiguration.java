package com.vain.config;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

/**
 * @author vain
 * @Description
 * @date 2018/12/24 21:15
 */
@Configuration
@Slf4j
public class ElasticsearchConfiguration {

    @Value("${spring.data.elasticsearch.ip}")
    private String hostName;

    @Value("${spring.data.elasticsearch.port}")
    private String port;

    @Value("${spring.data.elasticsearch.cluster-name}")
    private String clusterName;

    @Value("${spring.data.elasticsearch.pool}")
    private String poolSize;

    @Bean
    public TransportClient init() {
        TransportClient client = null;
        try {
            Settings setting = Settings.builder()
                    .put("cluster.name", clusterName)
                    //增加嗅探机制，找到ES集群
                    .put("client.transport.sniff", true)
                    .put("thread_pool.search.size", Integer.valueOf(poolSize))
                    .build();
            client = new PreBuiltTransportClient(setting);
            //连接elasticsearch传输协议端口
            InetSocketTransportAddress inetSocketTransportAddress = new InetSocketTransportAddress(InetAddress.getByName(hostName), Integer.valueOf(port));
            client.addTransportAddress(inetSocketTransportAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }


}
