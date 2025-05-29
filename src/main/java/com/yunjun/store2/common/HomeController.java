package com.yunjun.store2.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("api/")
@Tag(name = "home", description = "Home page API")
public class HomeController {

    /**
     * Using a dynamic view template example: Thymeleaf view template
     * root of the api by default convention
     * using "/"
     * @return
     */
    @GetMapping("home")
    @Operation(summary = "Get the home page")
    public String home(Model model) {
        model.addAttribute("name", "Yunjun");
        return "home";
    }

    /**
     * Using a static view template example
     * @return
     */
    @GetMapping
    @Operation(summary = "Get the index page")
    public String index() {
        return "index.html";
    }
}
