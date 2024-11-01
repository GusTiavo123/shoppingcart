package com.gustavo.shoppingcart.service.image;

import com.gustavo.shoppingcart.dto.ImageDto;
import com.gustavo.shoppingcart.exceptions.image.ImageNotFoundException;
import com.gustavo.shoppingcart.model.Image;
import com.gustavo.shoppingcart.model.Product;
import com.gustavo.shoppingcart.repository.ImageRepository;
import com.gustavo.shoppingcart.service.product.IProductService;
import com.gustavo.shoppingcart.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;
    private final IProductService productService;

    @Override
    public Image getImageById(long id) {
        return imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException("Image not found"));
    }

    @Override
    public void deleteImageById(long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,
                () -> {
                    throw new ImageNotFoundException("Image not found");
                });
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> saveImageDtos = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                Image savedImage = imageRepository.save(image);
                savedImage.setDownloadUrl("/api/v1/image/download/" + savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setImageId(savedImage.getId());
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                saveImageDtos.add(imageDto);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return saveImageDtos;
    }

    @Override
    public void updateImage(MultipartFile file, long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
