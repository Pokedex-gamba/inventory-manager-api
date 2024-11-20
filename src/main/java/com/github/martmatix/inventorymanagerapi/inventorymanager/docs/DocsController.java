package com.github.martmatix.inventorymanagerapi.inventorymanager.docs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Controller
public class DocsController {

    private WebClient.Builder builder;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping(path = "/docs")
    public String returnDocs(Model model) {
        WebClient webClient = builder.baseUrl("http://127.0.0.1:" + serverPort).build();
        Mono<String> stringMono = webClient.get()
                .uri("/v3/api-docs")
                .retrieve()
                .bodyToMono(String.class);
        model.addAttribute("apiDocs", Objects.requireNonNull(stringMono.block()));
        return "docs";
    }

    @Autowired
    public void setBuilder(WebClient.Builder builder) {
        this.builder = builder;
    }
}
