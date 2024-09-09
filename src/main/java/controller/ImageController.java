package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.CombinedResponse;
import service.DataCombinationService;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class ImageController {

    private final DataCombinationService dataCombinationService;

    @Autowired
    public ImageController(DataCombinationService dataCombinationService) {
        this.dataCombinationService = dataCombinationService;
    }

    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<CombinedResponse>> searchImagesAndFetchData(
            @RequestParam String searchTerm, @RequestParam String backendQuery) {

        return dataCombinationService.processRequest(searchTerm, backendQuery)
                .thenApply(response -> ResponseEntity.ok(response));
    }
}
