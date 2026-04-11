package com.freelancemarketplace.backend.product.api.controller;

import com.freelancemarketplace.backend.product.dto.ProductDTO;
import com.freelancemarketplace.backend.common.api.response.ApiResponse;
import com.freelancemarketplace.backend.product.application.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/products", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    ApiResponse<?> createProduct(@RequestBody ProductDTO productDTO){
        ProductDTO newProduct = productService.createProduct(productDTO);
        return ApiResponse.created(newProduct);
    }

    @PutMapping("/{productId}")
    ApiResponse<?> updateProduct(@PathVariable Long productId,
                                             @RequestBody ProductDTO productDTO){
        ProductDTO updatedProduct = productService.updateProduct(productId, productDTO);
        return ApiResponse.success(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    ApiResponse<?> deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return ApiResponse.noContent();
    }

    @GetMapping("/getAll")
    ApiResponse<?> getAllProduct(Pageable pageable){
        Page<ProductDTO> productDTOPage = productService.getAllProduct(pageable);
        return ApiResponse.success(productDTOPage);
    }
}
