package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.util.List;

@Data
@AllArgsConstructor
public class CombinedResponse {
    private List<File> images;
    private SomeData backendData;
}

