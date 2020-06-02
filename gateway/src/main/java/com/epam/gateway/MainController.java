package com.epam.gateway;

import com.epam.gateway.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("message", "Hello");

        return "index";
    }

    @RequestMapping(value = { "/users" }, method = RequestMethod.GET)
    public String songs(Model model) {

        List<User> list = new ArrayList<>();
        list.add(User.builder().name("User1").account("ffff@ff.ff").build());
        list.add(User.builder().name("User2").account("gggg@gg.gg").build());
        model.addAttribute("users", list);

        return "users";
    }

}
