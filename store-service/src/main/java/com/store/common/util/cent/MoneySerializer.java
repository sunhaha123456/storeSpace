package com.store.common.util.cent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MoneySerializer extends JsonSerializer {

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String result;
        if (o == null) {
            result = "0";
        } else {
            final String str = o.toString();
            result = CentTranslateUtil.cent2Yuan(str, true);
        }
        jsonGenerator.writeString(result);
    }
}