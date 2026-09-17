package vn.huytan.service;

import java.util.List;
import java.util.Optional;
import vn.huytan.entity.Category;

public interface ICategoryService {
    List<Category> findAll();
    Optional<Category> findById(Long id);
    Optional<Category> findByCategoryName(String name);
    Category save(Category entity);
    void delete(Category entity);
    void deleteById(Long id);
}