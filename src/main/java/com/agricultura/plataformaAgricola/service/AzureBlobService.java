package com.agricultura.plataformaAgricola.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@Service
public class AzureBlobService {

	@Value("${azure.blob.connection-string}")
	private String connectionString;

	@Value("${azure.blob.container-name}")
	private String containerName;

    public String uploadImage(MultipartFile file) throws IOException {
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        BlobContainerClient containerClient = serviceClient.getBlobContainerClient(containerName);

        String filename = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        BlobClient blobClient = containerClient.getBlobClient(filename);

        blobClient.upload(file.getInputStream(), file.getSize(), true);

        return blobClient.getBlobUrl();
    }
}
