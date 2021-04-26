package com.work.qa.common.utils.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.work.qa.common.utils.session.Key;
import com.work.qa.common.utils.session.Session;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonOperation<T> {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JsonOperation.class);

    private ObjectMapper objectMapper =  new ObjectMapper();

    public static boolean isJson (String text) {
        if(text.charAt(0) == '{' && text.charAt(text.length()-1) == '}') {
            return true;
        }
        else {
            return false;
        }
    }

    public static <T> T fromJSON(final TypeReference<T> type,
                                 final String jsonPacket) {
        T data = null;

        try {
            data = new ObjectMapper().readValue(jsonPacket, type);
        } catch (Exception e) {
            // Handle the problem
        }
        return data;
    }

    public String convertObjectToJson(T element) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        objectMapper.setDateFormat(format);
        return objectMapper.writeValueAsString(element);
    }

    public String convertObjectToJson(String object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    public T convertJsonToObject(String json) throws IOException {
        return objectMapper.readValue(json, new TypeReference<T>(){});
    }

    public  MapDifference<String, Object> printJsonDiff(String source, String target)
    {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, Object>> type =
                new TypeReference<HashMap<String, Object>>() {};

        MapDifference<String, Object> difference;
        try {
            Map<String, Object> leftMap = mapper.readValue(source, type);

            Map<String, Object> rightMap = mapper.readValue(target, type);

            Map<String, Object> leftFlatMap = FlatMapUtil.flatten(leftMap);
            Map<String, Object> rightFlatMap = FlatMapUtil.flatten(rightMap);

            difference = Maps.difference(leftFlatMap, rightFlatMap);

            log.info("\nEntries only on left\n--------------------------");
            difference.entriesOnlyOnLeft().forEach((key, value) ->  log.info(key + ": " + value));

            log.info("\n\nEntries only on right\n--------------------------");
            difference.entriesOnlyOnRight().forEach((key, value) -> log.info(key + ": " + value));

            log.info("\n\nEntries differing\n--------------------------");
            difference.entriesDiffering().forEach((key, value) -> log.info(key + ": " + value));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Session.getCurrentSession().put(Key.Keys.RESPONSE_DISCREPANCIES,difference);
        return difference;
    }
}
