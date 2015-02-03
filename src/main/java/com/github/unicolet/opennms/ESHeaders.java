package com.github.unicolet.opennms;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.component.elasticsearch.ElasticsearchConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * Created:
 * User: unicoletti
 * Date: 2:07 PM 1/13/15
 */
public class ESHeaders {
    Logger logger = LoggerFactory.getLogger(ESHeaders.class);

    IndexNameFunction idxName = new IndexNameFunction();

    String remainder="opennms";

    public void process(Exchange exchange) {
        Message in = exchange.getIn();
        String indexName=null;
        try {
            Map body = (Map) in.getBody();
            if(body.containsKey("@timestamp")) {
                logger.trace("Computing indexName from @timestamp: "+body.get("@timestamp"));
                indexName=idxName.apply(remainder, (Date) body.get("@timestamp"));
            } else {
                indexName = idxName.apply(remainder);
            }
        } catch(Exception e) {
            logger.error("Cannot compute index name, failing back to default");
            indexName = idxName.apply(remainder);
        }
        in.setHeader(ElasticsearchConfiguration.PARAM_INDEX_NAME, indexName);
    }

    public String getRemainder() {
        return remainder;
    }

    public void setRemainder(String remainder) {
        this.remainder = remainder;
    }
}
