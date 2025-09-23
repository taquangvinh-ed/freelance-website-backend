package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.ProductDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.ProductMapper;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.ProductModel;
import com.freelancemarketplace.backend.model.SkillModel;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.ProductsRepository;
import com.freelancemarketplace.backend.repository.SkillsRepository;
import com.freelancemarketplace.backend.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class ProductServiceImp implements ProductService {

    private final ProductsRepository productsRepository;
    private final ProductMapper productMapper;
    private final SkillsRepository skillsRepository;
    private final FreelancersRepository freelancersRepository;

    public ProductServiceImp(ProductsRepository productsRepository, ProductMapper productMapper, FreelancersRepository freelancersRepository, SkillsRepository skillsRepository) {
        this.productsRepository = productsRepository;
        this.productMapper = productMapper;
        this.freelancersRepository = freelancersRepository;
        this.skillsRepository = skillsRepository;
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        ProductModel newProduct = productMapper.toEntity(productDTO);

        FreelancerModel freelancer = freelancersRepository.findById(productDTO.getFreelancerId()).orElseThrow(
                ()-> new ResourceNotFoundException("Freelancer with id: " + productDTO.getFreelancerId()+ " not found")
        );

        newProduct.setFreelancer(freelancer);

        if (productDTO.getSkillIds() != null) {
            Set<SkillModel> foundSkills = skillsRepository.findAllById(productDTO.getSkillIds()).stream().collect(toSet());

            if (foundSkills.size() != productDTO.getSkillIds().size()) {
                Set<Long> missingSkillId = new HashSet<>(productDTO.getSkillIds());
                missingSkillId.removeAll(foundSkills.stream().map(SkillModel::getSkillId).collect(toSet()));
                throw new ResourceNotFoundException("Skill with id: " + missingSkillId + " not found");
            }
            newProduct.getSkills().addAll(foundSkills);
        }
            ProductModel savedProduct = productsRepository.save(newProduct);
            return productMapper.toDto(savedProduct);
        }


    @Override
    @Transactional
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        ProductModel product = productsRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product with id: " + productId + " not found")
        );

        ProductModel updatedProduct = productMapper.partialUpdate(productDTO, product);
        ProductModel savedProduct = productsRepository.save(updatedProduct);
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        if(!productsRepository.existsById(productId))
            throw new ResourceNotFoundException("Product with id: " +productId+" not found");
        productsRepository.deleteById(productId);
    }

    @Override
    public Page<ProductDTO> getAllProduct(Pageable pageable) {
        Page<ProductModel> productModelPage = productsRepository.findAll(pageable);
        return productMapper.toDTOpage(productModelPage, pageable);
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        return null;
    }
}
