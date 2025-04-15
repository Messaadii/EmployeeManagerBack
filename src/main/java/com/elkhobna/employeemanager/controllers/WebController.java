package com.elkhobna.employeemanager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
    @RequestMapping(value = { "/", "/{path:^(?!api|static|.*\\..*).*$}" })
    public String index() {
        return "forward:/index.html";
    }
}
