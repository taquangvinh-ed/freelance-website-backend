package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    void deleteProduct(Long productId);

    Page<ProductDTO> getAllProduct(Pageable pageable);

    ProductDTO getProductById(Long productId);


}
