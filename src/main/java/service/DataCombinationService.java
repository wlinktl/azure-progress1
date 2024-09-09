package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import model.CombinedResponse;
import model.SomeData;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DataCombinationService {

    private final AzureImageService azureImageService;
    private final BackendService backendService;

    @Autowired
    public DataCombinationService(AzureImageService azureImageService, BackendService backendService) {
        this.azureImageService = azureImageService;
        this.backendService = backendService;
    }

    @Async
    public CompletableFuture<CombinedResponse> processRequest(String searchTerm, String backendQuery) {
        CompletableFuture<List<File>> imagesFuture = null;
        try {
            imagesFuture = azureImageService.searchAndDownloadImages(searchTerm);
        } catch (UnsupportedEncodingException e) {
            // Handle the exception here
            e.printStackTrace();
        }
        CompletableFuture<SomeData> backendDataFuture = backendService.fetchDataFromBackend(backendQuery);

        // Combine the results once both futures are completed
        return imagesFuture.thenCombine(backendDataFuture, 
            (images, backendData) -> new CombinedResponse(images, backendData));
    }
}
