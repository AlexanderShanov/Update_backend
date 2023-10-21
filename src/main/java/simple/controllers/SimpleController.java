package simple.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SimpleController {

    @GetMapping("/simple")
    /*public @ResponseBody SimpleResponce logList(*/
    public String logList(
            Model model) {

        System.out.println("test");

        return "simple";
    }


}
