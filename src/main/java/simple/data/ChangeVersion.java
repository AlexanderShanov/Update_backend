package simple.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

@ToString
public class ChangeVersion {
    @JsonProperty("idCurrentVersion")
    private final String idCurrentVersion;
    @JsonProperty("idNewVersion")
    private final String idNewVersion;

    public ChangeVersion(@JsonProperty("idCurrentVersion") String IdCurrentVersion,
                         @JsonProperty("idNewVersion") String idNewVersion){
        this.idCurrentVersion = IdCurrentVersion;
        this.idNewVersion = idNewVersion;
    }

    public String getIdCurrentVersion() {
        return idCurrentVersion;
    }

    public String getIdNewVersion() {
        return idNewVersion;
    }

}
