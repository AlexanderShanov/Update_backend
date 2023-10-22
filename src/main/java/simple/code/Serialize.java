package simple.code;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import simple.data.ChangeVersion;
import simple.data.ChangeVersionInfoFiles;
import simple.data.RequestValue;

public class Serialize {

    public static RequestValue deserialize(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RequestValue person = objectMapper.readValue(json, RequestValue.class);
        System.out.println("deserialize: " + person);
        return person;
    }
    public static String serialize(RequestValue requestValue) throws JsonProcessingException{
        ObjectWriter objectWriter = new ObjectMapper().writer();//.writerWithDefaultPrettyPrinter()
        String json = objectWriter.writeValueAsString(requestValue);
        System.out.println("serialize: \n" + json);
        return json;
    }

    public static ChangeVersion deserializeChangeVersion(String json) throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        ChangeVersion changeVersion = objectMapper.readValue(json, ChangeVersion.class);
        System.out.println("deserializ: " + changeVersion);

        return changeVersion;
    }

    public static String serialize(ChangeVersionInfoFiles changeVersionInfoFiles) throws JsonProcessingException{
        ObjectWriter objectWriter = new ObjectMapper().writer();
        String json = objectWriter.writeValueAsString(changeVersionInfoFiles);
        System.out.println("serialize: \n" + json);
        return json;
    }

}
