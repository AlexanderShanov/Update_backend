package simple.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InfoFile {
    @JsonProperty("idFile")
    private final int IdFile;
    @JsonProperty("path")
    private final String Path;

    public InfoFile(@JsonProperty("idFile") int IdFile, @JsonProperty("path") String Path){
        this.IdFile = IdFile;
        this.Path = Path;
    }
}
