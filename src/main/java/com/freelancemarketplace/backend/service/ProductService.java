package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ProductDTO;
import org.springframework.data.domain.Page;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    void deleteProduct(Long productId);

    Page<ProductDTO> getAllProduct();

    ProductDTO getProductById(Long productId);


}
