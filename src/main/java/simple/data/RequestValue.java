package simple.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RequestValue {
    @JsonProperty("name")
    private final String name;
    @JsonProperty("myArray")
    private final RequestValueFolder[] myArray;

    public RequestValue(@JsonProperty("name") String name,
                        @JsonProperty("myArray") RequestValueFolder[] myArray){
        this.name = name;
        this.myArray = myArray;
    }

    public RequestValueFolder[] getMyArray() {
        return myArray;
    }

    public String getName() {
        return name;
    }
}
