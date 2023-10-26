package simple.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RequestValueFolder {
    @JsonProperty("currantUnit")
    private final String currantUnit;
    @JsonProperty("idTargetFirstPath")
    private final String idTargetFirstPath;
    public RequestValueFolder(
            @JsonProperty("currantUnit") String currantUnit,
            @JsonProperty("targetFolder") String idTargetFirstPath){
        this.currantUnit = currantUnit;
        this.idTargetFirstPath = idTargetFirstPath;
    }

    public String getCurrantUnit() {
        return currantUnit;
    }

    public String getIdTargetFirstPath() {
        return idTargetFirstPath;
    }
}
