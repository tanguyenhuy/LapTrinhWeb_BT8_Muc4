package vn.huytan.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import vn.huytan.entity.Product;
import vn.huytan.repository.ProductRepository;
import vn.huytan.service.IProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    @Override public List<Product> findAll() { return productRepository.findAll(); }
    @Override public Optional<Product> findById(Long id) { return productRepository.findById(id); }
    @Override public Optional<Product> findByProductName(String name) { return productRepository.findByProductName(name); }
    @Override public Product save(Product entity) { return productRepository.save(entity); }
    @Override public void delete(Product entity) { productRepository.delete(entity); }
    @Override public void deleteById(Long id) { productRepository.deleteById(id); }
}