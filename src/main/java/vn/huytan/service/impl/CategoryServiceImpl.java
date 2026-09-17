package vn.huytan.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import vn.huytan.entity.Category;
import vn.huytan.repository.CategoryRepository;
import vn.huytan.service.ICategoryService;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override public List<Category> findAll() { return categoryRepository.findAll(); }
    @Override public Optional<Category> findById(Long id) { return categoryRepository.findById(id); }
    @Override public Optional<Category> findByCategoryName(String name) { return categoryRepository.findByCategoryName(name); }
    @Override public Category save(Category entity) { return categoryRepository.save(entity); }
    @Override public void delete(Category entity) { categoryRepository.delete(entity); }
    @Override public void deleteById(Long id) { categoryRepository.deleteById(id); }
}