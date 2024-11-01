package com.gustavo.shoppingcart.service.image;

import com.gustavo.shoppingcart.dto.ImageDto;
import com.gustavo.shoppingcart.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(long id);

    void deleteImageById(long id);

    List<ImageDto> saveImages(List<MultipartFile> file, long productId);

    void updateImage(MultipartFile file, long imageId);
}
