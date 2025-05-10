package com.yunjun.store2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/** Dynamic View Templates
 *
 * Template Engines
 * JSP(JavaServer Pages) - outdated
 * Freemaker
 * Mustache
 * Thymeleaf
 */
@Controller
public class HomeController {

    /**
     * Using a dynamic view template example: Thymeleaf view template
     * root of the api by default convention
     * using "/"
     * @return
     */
    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("name", "Yunjun");
        return "home";
    }

    /**
     * Using a static view template example
     * @return
     */
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
}
