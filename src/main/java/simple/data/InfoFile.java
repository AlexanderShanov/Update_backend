package simple.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InfoFile {
    @JsonProperty("idFile")
    private final int IdFile;
    @JsonProperty("path")
    private final String Path;
    @JsonProperty("id_target_path")
    private final String id_target_path;

    public InfoFile(@JsonProperty("idFile") int IdFile, @JsonProperty("path") String Path, @JsonProperty("id_target_path") String id_target_path){
        this.IdFile = IdFile;
        this.Path = Path;
        this.id_target_path = id_target_path;
    }
}
