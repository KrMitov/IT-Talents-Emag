package com.emag.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class FileCreator {

    @Value("${statistics.file}")
    private String filePath;

    public void addTextToFile(String text) throws IOException {
        File file = new File(filePath, "statistics.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(text);
        }
    }

}
