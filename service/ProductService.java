package com.sadeem.smap.service;

import com.sadeem.smap.dto.ProductDto;
import com.sadeem.smap.model.Product;
import com.sadeem.smap.repository.DepartmentRepository;
import com.sadeem.smap.repository.PartRepository;
import com.sadeem.smap.repository.ProductRepository;
import com.sadeem.smap.repository.RawMaterialRepository;
import com.sadeem.smap.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    @Autowired
    private FileUtil fileUtil;

    public Iterable<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<ProductDto> getProductsPaginated(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::convertToDto);
    }

    public Optional<ProductDto> getProductById(Long id) {
        return productRepository.findById(id).map(this::convertToDto);
    }

    public void createProduct(ProductDto productDto, MultipartFile file) throws IOException {
        Product product = convertToEntity(productDto);
        productRepository.save(product);
        fileUtil.writeToFile(file, "/opt/images/products/" + product.getProductId() + ".jpg");
    }

    public void updateProduct(ProductDto productDto, MultipartFile file) throws IOException {
        Optional<Product> optionalProduct = productRepository.findById(productDto.getId());
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setProductName(productDto.getName());
            product.setProductSerial(productDto.getSerial());
            product.setProductDescription(productDto.getDescription());

            // Update product parts
            product.setProductParts(productDto.getProductParts().stream()
                    .map(partId -> partRepository.findById(partId).orElseThrow())
                    .collect(Collectors.toSet()));

            // Update raw materials
            product.setRawMaterials(productDto.getRawMaterialIds().stream()
                    .map(rawMaterialId -> rawMaterialRepository.findById(rawMaterialId).orElseThrow())
                    .collect(Collectors.toSet()));

            // Update departments
            product.setDepartments(productDto.getDepartmentIds().stream()
                    .map(departmentId -> departmentRepository.findById(departmentId).orElseThrow())
                    .collect(Collectors.toSet()));

            productRepository.save(product);

            if (file != null && !file.isEmpty()) {
                fileUtil.writeToFile(file, "/opt/images/products/" + product.getProductId() + ".jpg");
            }
        }
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setIsDeleted(true);
        productRepository.save(product);
    }

    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getProductId());
        dto.setName(product.getProductName());
        dto.setSerial(product.getProductSerial());
        dto.setDescription(product.getProductDescription());
        dto.setPartIds(product.getProductParts().stream()
                .map(part -> part.getPartId())
                .collect(Collectors.toSet()));
        dto.setRawMaterialIds(product.getRawMaterials().stream()
                .map(rawMaterial -> rawMaterial.getRawMaterialId())
                .collect(Collectors.toSet()));
        dto.setDepartmentIds(product.getDepartments().stream()
                .map(department -> department.getDepartmentId())
                .collect(Collectors.toSet()));
        return dto;
    }

    private Product convertToEntity(ProductDto productDto) {
        Product product = new Product();
        product.setProductId(productDto.getId());
        product.setProductName(productDto.getName());
        product.setProductSerial(productDto.getSerial());
        product.setProductDescription(productDto.getDescription());

        // Set product parts
        product.setProductParts(productDto.getPartIds().stream()
                .map(partId -> partRepository.findById(partId).orElseThrow())
                .collect(Collectors.toSet()));

        // Set raw materials
        product.setRawMaterials(productDto.getRawMaterialIds().stream()
                .map(rawMaterialId -> rawMaterialRepository.findById(rawMaterialId).orElseThrow())
                .collect(Collectors.toSet()));

        // Set departments
        product.setDepartments(productDto.getDepartmentIds().stream()
                .map(departmentId -> departmentRepository.findById(departmentId).orElseThrow())
                .collect(Collectors.toSet()));

        return product;
    }
}