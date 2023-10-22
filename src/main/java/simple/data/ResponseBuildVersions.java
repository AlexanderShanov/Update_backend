package simple.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResponseBuildVersions {
    @JsonProperty("versions")
    private final String[] Versions;
    public ResponseBuildVersions(@JsonProperty("versions") String[] Versions){
        this.Versions = Versions;
    }

}
