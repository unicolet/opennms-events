package com.github.unicolet.opennms;

import org.apache.camel.component.properties.PropertiesFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created:
 * User: unicoletti
 * Date: 9:24 AM 1/13/15
 */
public class IndexNameFunction implements PropertiesFunction {

    Logger logger = LoggerFactory.getLogger(IndexNameFunction.class);

    private SimpleDateFormat df=null;

    public IndexNameFunction() {
        df=new SimpleDateFormat("YYYY.MM");
    }

    public IndexNameFunction(String dateFormat) {
        df=new SimpleDateFormat(dateFormat == null ? "YYYY.MM" : dateFormat);
    }

    @Override
    public String getName() {
        return "index";
    }

    @Override
    public String apply(String remainder) {
        String result=null;
        result=remainder.toLowerCase()+"-"+df.format(new Date());

        if(logger.isTraceEnabled()) {
            logger.trace("IndexNameFunction.apply=" + result);
        }
        return result;
    }

    public String apply(String remainder, Date date) {
        String result=null;
        result=remainder.toLowerCase()+"-"+df.format(date);

        if(logger.isTraceEnabled()) {
            logger.trace("IndexNameFunction.apply=" + result);
        }

        return result;
    }
}