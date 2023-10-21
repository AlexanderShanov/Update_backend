package simple.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeVersionInfoFiles {
    @JsonProperty("filesAdd")
    private final InfoFile[] filesAdd;
    @JsonProperty("filesDelete")
    private final InfoFile[] filesDelete;

    public ChangeVersionInfoFiles(@JsonProperty("filesAdd") InfoFile[] filesAdd,
                                  @JsonProperty("filesDelete") InfoFile[] filesDelete){
        this.filesAdd = filesAdd;
        this.filesDelete = filesDelete;
    }


}
