package simple.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RequestValueFolder {
    @JsonProperty("currantUnit")
    private final String currantUnit;
    @JsonProperty("targetFolder")
    private final String targetFolder;
    public RequestValueFolder(
            @JsonProperty("currantUnit") String currantUnit,
            @JsonProperty("targetFolder") String targetFolder){
        this.currantUnit = currantUnit;
        this.targetFolder = targetFolder;
    }

    public String getCurrantUnit() {
        return currantUnit;
    }
}
