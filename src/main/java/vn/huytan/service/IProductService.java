package vn.huytan.service;

import java.util.List;
import java.util.Optional;
import vn.huytan.entity.Product;

public interface IProductService {
    List<Product> findAll();
    Optional<Product> findById(Long id);
    Optional<Product> findByProductName(String name);
    Product save(Product entity);
    void delete(Product entity);
    void deleteById(Long id);
}