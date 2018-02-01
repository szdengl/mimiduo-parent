package net.mimiduo.boot.common.util;

import java.io.IOException;



import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.data.domain.Persistable;

public class IdSerializer extends JsonSerializer<Persistable<Long>> {

    @Override
    public void serialize(Persistable<Long> persistable, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeObject(persistable.getId());
    }
}
