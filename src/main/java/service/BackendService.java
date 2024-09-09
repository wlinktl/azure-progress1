package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import model.SomeData;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Service
public class BackendService {

    private final WebClient webClient;

    @Autowired
    public BackendService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://backend-service-url").build();
    }

    @Async
    public CompletableFuture<SomeData> fetchDataFromBackend(String query) {
        Mono<SomeData> responseMono = webClient.get()
                .uri("/api/data?query=" + query)
                .retrieve()
                .bodyToMono(SomeData.class);

        return responseMono.toFuture();
    }
}
