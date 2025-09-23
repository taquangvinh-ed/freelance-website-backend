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
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        return null;
    }

    @Override
    public void deleteProduct(Long productId) {

    }

    @Override
    public Page<ProductDTO> getAllProduct() {
        return null;
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        return null;
    }
}
