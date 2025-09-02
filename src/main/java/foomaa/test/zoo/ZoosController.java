package foomaa.test.zoo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;


@Controller
@RequiredArgsConstructor
public class ZoosController {
    private final ZoosRepository zoosRepo;
    private final WebClient client = WebClient.create("http://localhost:8080/zoos-web");

    @GetMapping("/zoos-web")
    public String getZoos(Model model) {
        model.addAttribute("zoos", zoosRepo.findAll());
        return "zoos";
    }

    /*
    @GetMapping("/zoos-web")
    public Mono<String> getZoos(Model model) {
        return client.get()
                .retrieve()
                .bodyToFlux(Zoos.class)
                .collectList()
                .subscribeOn(Schedulers.boundedElastic()) // Выносим блокирующие операции в отдельный поток
                .flatMap(zoosList -> {
                    // Выполняем в отдельном потоке
                    zoosList.forEach(zoosRepo::save);
                    model.addAttribute("zoos", zoosRepo.findAll());
                    return Mono.just("zoos");
                });
    }
     */
}

