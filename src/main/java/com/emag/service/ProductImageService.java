package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.model.dto.productimagedto.DeleteProductImagesDTO;
import com.emag.model.dto.produtcdto.ProductWithImagesDTO;
import com.emag.model.pojo.ProductImage;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductImageService extends AbstractService{

    public ProductWithImagesDTO uploadProductImages(int id, MultipartFile[] receivedFiles) throws IOException {
        Tika tika = new Tika();
        List<String> acceptedImageMimeTypes = Arrays.asList(ACCEPTED_IMAGE_MIME_TYPES);
        boolean requestHasAnImage = false;
        for (MultipartFile file : receivedFiles){
            if (file.isEmpty()){
                continue;
            }
            requestHasAnImage = true;
            String detectedType = tika.detect(file.getBytes());
            if (!acceptedImageMimeTypes.contains(detectedType)){
                throw new BadRequestException("Image type not supported!");
            }
            File physicalFile = new File(filePath + File.separator + System.nanoTime() + "." +
                    detectedType.substring(detectedType.indexOf("/") + 1));
            try (OutputStream os = new FileOutputStream(physicalFile)){
                os.write(file.getBytes());
                ProductImage productImage = new ProductImage();
                productImage.setUrl(physicalFile.getAbsolutePath());
                productImage.setProduct(getProductIfExists(id));
                productImageRepository.save(productImage);
            }
        }
        if (!requestHasAnImage){
            throw new BadRequestException("No images uploaded");
        }
        return new ProductWithImagesDTO(getProductIfExists(id));
    }

    public List<Integer> removeProductImages(DeleteProductImagesDTO deleteImageDTO) throws IOException{
        List<Integer> imagesIds = deleteImageDTO.getImagesIds();
        imagesIds.forEach(id -> {
            if (id == null){
                throw new BadRequestException("Invalid product images ids");
            }
        });
        for (Integer id : imagesIds){
            ProductImage productImage = getProductImageIfExists(id);
            Files.delete(Path.of(productImage.getUrl()));
            productImageRepository.delete(productImage);
        }
        return imagesIds;
    }

    public byte[] getProductImage(int imageId) throws IOException {
        return Files.readAllBytes(Path.of(getProductImageIfExists(imageId).getUrl()));
    }
}
