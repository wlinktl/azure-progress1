package service;

import com.azure.core.util.IterableStream;
import com.azure.search.documents.SearchClient;
import com.azure.search.documents.models.SearchOptions;
import com.azure.search.documents.util.SearchPagedIterable;
import com.azure.search.documents.models.SearchResult;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class AzureImageService {

    private final BlobServiceClient blobServiceClient;
    private final SearchClient searchClient;

    @Autowired
    public AzureImageService(BlobServiceClient blobServiceClient, SearchClient searchClient) {
        this.blobServiceClient = blobServiceClient;
        this.searchClient = searchClient;
    }

    @Async
    public CompletableFuture<List<File>> searchAndDownloadImages(String searchTerm) throws UnsupportedEncodingException {
        List<File> downloadedImages = new ArrayList<>();

        // 1. Search images using Azure Cognitive Search
        SearchOptions options = new SearchOptions();
        SearchPagedIterable searchResults = searchClient.search(searchTerm);

        // 2. Download images from Azure Blob Storage
        for (SearchResult result : searchResults) {
            String blobUrl = result.getDocument(String.class).getBytes("imageUrl").toString();
            BlobClient blobClient = blobServiceClient.getBlobContainerClient("my-container").getBlobClient(blobUrl);
            File file = downloadImage(blobClient);
            downloadedImages.add(file);
        }

        return CompletableFuture.completedFuture(downloadedImages);
    }

    private File downloadImage(BlobClient blobClient) {
        File tempFile = new File("temp-" + UUID.randomUUID() + ".jpg");
        blobClient.downloadToFile(tempFile.getAbsolutePath());
        return tempFile;
    }
}
