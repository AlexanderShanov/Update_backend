package simple.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.nio.file.Path;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import simple.code.GetFileFromBD;

@Controller
public class ControllerMultipart {
    //@GetMapping("/ControllerMultipart/{filename:.+}")
    @GetMapping("/ControllerMultipart/{filename:.+}/{fileversion:.+}")
    @ResponseBody
    //public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, @PathVariable String fileversion) {
        System.out.println("tut");
        try {
            Resource Rfile = null;
            System.out.println(filename);
            System.out.println(fileversion);
            //Rfile = new UrlResource("file:///C:/curl/bin/" + filename);
            //Rfile = new UrlResource("file:///C:/curl/bin/curl.exe");
            //C:/Work/9_java/UpData/Data/AutoCad/Руководство TERMOCLIP TOOLS.pdf
            Rfile = new UrlResource("file:///C:/Work/9_java/UpData/Data/AutoCad/Руководство TERMOCLIP TOOLS.pdf");
            System.out.println(Rfile.contentLength());
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "README" + "\"");
            responseHeaders.set(HttpHeaders.CONTENT_TYPE, "multipart/form-data");

            return ResponseEntity.ok().headers(responseHeaders).body(Rfile);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("tut2");
        }

        return null;
    }

    @GetMapping("/ControllerMultipart2/{fileid:.+}/{fileversion:.+}")
    @ResponseBody
    //public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
    public ResponseEntity<byte[]> getFile(@PathVariable String fileid, @PathVariable String fileversion) {


        Integer id_ = null;
        try{
            id_ = Integer.parseInt(fileid);
        }
        catch (Exception e){

        }

        if(id_ != null){
            System.out.println("Get file: request received. Id = " + fileid);
            //LoggerSyslog.logger.info("Get file: request received. Id = " + fileid);


            try {
                System.out.println(fileid);
                long time1 = System.currentTimeMillis();
                GetFileFromBD getFileFromBD = new GetFileFromBD();
                byte[] bytes = getFileFromBD.selectRecord(id_);
                long time2 = System.currentTimeMillis();

                //LoggerSyslog.logger.info("That get from database id = {}, lenght = {}, time = {}", id_, bytes.length, time2 - time1);
                System.out.println(bytes.length);


                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "README" + "\"");
                responseHeaders.set(HttpHeaders.CONTENT_TYPE, "multipart/form-data");

                return ResponseEntity.ok().headers(responseHeaders).body(bytes);
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("tut2");
            }
        }
        else{
            //LoggerSyslog.logger.error("File id is not correct: " + fileid);
        }


        return null;
    }

    private Path load(String filename) {
        Path rootLocation = null;
        return rootLocation.resolve(filename);
    }
}