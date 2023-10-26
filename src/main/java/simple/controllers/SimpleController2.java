package simple.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
//import org.apache.logging.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simple.code.*;
import simple.data.ChangeVersion;
import simple.data.RequestValue;
import simple.data.RequestValueFolder;

import java.io.*;
import java.net.URL;
//import java.util.logging.Logger;
/*import org.slf4j.Logger;
import org.slf4j.LoggerFactory;*/

@RestController
@RequestMapping(value = "/simple2", method = RequestMethod.POST, consumes ="application/json", produces ="application/json")
//@RequestMapping(value = "/simple2", method = RequestMethod.POST)
public class SimpleController2 {
    private final Logger LOGGER = LoggerFactory.getLogger(SimpleController2.class);


    @PostMapping("/simple2")
    public ResponseEntity<String> simple2(@RequestBody String inputData) {
        HttpHeaders responseHeaders = new HttpHeaders();
       /* запрос на создание билда из веб страницы*/

        // запись всей строки
        String text = inputData;
        String str = text.replace("\\", "");
        String json = str.substring(1, str.length()-1);

        DataBaseWork  dataBaseWork = new DataBaseWork();
        dataBaseWork.creadeNewRelease(json);

        System.out.println("pizdec");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("{\"pizdec\":\"pizdec12__18_23\"}");
    }

    @PostMapping("/CreateNewBuildFromDecktopRequest")
    public ResponseEntity<String> CreateNewBuildFromDecktopRequest(@RequestBody String inputData) {
        /* запрос на создание билда из Decktop*/

        HttpHeaders responseHeaders = new HttpHeaders();

        try(FileWriter writer = new FileWriter("CreateNewBuildFromDecktopRequestJson.txt", true))
        {
            writer.write(inputData);
            writer.flush();
        }
        catch(IOException ex){
            LOGGER.error("CreateNewBuildFromDecktopRequest: " + ex.getMessage());
        }

        String json = inputData;
        LOGGER.error("CreateNewBuildFromDecktopRequest: " + inputData);
        DataBaseWork  dataBaseWork = new DataBaseWork();
        dataBaseWork.creadeNewRelease(json);
        LOGGER.error("CreateNewBuildFromDecktopRequest123321");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("{\"CreateNewBuildFromDecktopRequest\":\"OK\"}");
    }


    @PostMapping("/getAllNameVersion")
    public ResponseEntity<String> getAllNameVersion(@RequestBody String inputData) {
        HttpHeaders responseHeaders = new HttpHeaders();
        //LoggerSyslog.logger.warn("Get all name version: Request received");
        System.out.println("Get all name version: Request received");
        GetAllBuildVersions getAllBuildVersions = new GetAllBuildVersions();
        System.out.println("2");
        String json = getAllBuildVersions.getBuildVersions();
        System.out.println("getAllNameVersion132");
        LOGGER.error("getAllNameVersionLogger");

        System.out.println("getAllNameVersion");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(json);
    }

    @PostMapping("/getListNameUpdataFiles")
    public ResponseEntity<String> getListNameUpdataFiles(@RequestBody String inputData) {

        try(FileWriter writer = new FileWriter("Json.txt", true))
        {
            writer.write(inputData);
            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        String json = "{}";
        try{
            ChangeVersion changeVersion = Serialize.deserializeChangeVersion(inputData);

            System.out.println("1");

            ListUpdataFiles listUpdataFiles = new ListUpdataFiles(changeVersion);
            json = listUpdataFiles.getJsonListUpdataFilesFromBD();

            System.out.println("2");
        }
        catch (Exception e){
            json =e.toString();

        }



        System.out.println("getAllNameVersion");
        //return "{\"pizdec\":\"pizdec123\"}";

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(json);
    }



    @PostMapping("/ControllerMultipartGet")
    public ResponseEntity<String> ControllerMultipartGet() {

        //
        System.out.println("1");
        CloseableHttpClient httpclient = HttpClients.custom()
                .setRedirectStrategy(new LaxRedirectStrategy()) // adds HTTP REDIRECT support to GET and POST methods
                .build();
        System.out.println("2");
        try {
            File dstFile= null;

            HttpGet get = new HttpGet("http://localhost:8080/simple/ControllerMultipart/tets1234213/v12313424"); // we're using GET but it could be via POST as well
            System.out.println("3");
            File downloaded = httpclient.execute(get, new FileDownloadResponseHandler(dstFile));
            System.out.println("4");
            System.out.println(downloaded.getName());
        } catch (Exception e) {
            System.out.println("5");
            throw new IllegalStateException(e);
        } finally {
            //IOUtils.closeQuietly(httpclient);
        }


        HttpHeaders responseHeaders = new HttpHeaders();
        System.out.println("6");
        GetAllBuildVersions getAllBuildVersions = new GetAllBuildVersions();
        System.out.println("7");
        String json = "{}";


        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(json);
    }

    static class FileDownloadResponseHandler implements ResponseHandler<File> {

        private final File target;

        public FileDownloadResponseHandler(File target) {
            this.target = target;
        }

        @Override
        public File handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            InputStream source = response.getEntity().getContent();
            //FileUtils.copyInputStreamToFile(source, this.target);
            return this.target;
        }

    }





}
