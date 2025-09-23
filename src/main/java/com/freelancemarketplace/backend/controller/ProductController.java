package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.ProductDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/products", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    ResponseEntity<ResponseDTO>createProduct(@RequestBody ProductDTO productDTO){
        ProductDTO newProduct = productService.createProduct(productDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        ResponseStatusCode.CREATED,
                        ResponseMessage.CREATED,
                        newProduct
                ));
    }

    @PutMapping("/{productId}")
    ResponseEntity<ResponseDTO>updateProduct(@PathVariable Long productId,
                                             @RequestBody ProductDTO productDTO){
        ProductDTO updatedProduct = productService.updateProduct(productId, productDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        updatedProduct));
    }

    @DeleteMapping("/{productId}")
    ResponseEntity<ResponseDTO>deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ResponseDTO(
                        ResponseStatusCode.NO_CONTENT,
                        ResponseMessage.NO_CONTENT
                ));
    }

    @GetMapping("/getAll")
    ResponseEntity<ResponseDTO>getAllProduct(Pageable pageable){
        Page<ProductDTO> productDTOPage = productService.getAllProduct(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        productDTOPage));
    }
}
