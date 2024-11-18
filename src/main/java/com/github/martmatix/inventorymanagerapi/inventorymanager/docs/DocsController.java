package com.github.martmatix.inventorymanagerapi.inventorymanager.docs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocsController {

    @GetMapping(path = "/docs")
    public String returnDocs() {
        return "/docs";
    }

}
