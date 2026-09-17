package vn.huytan.controller.api;

import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import vn.huytan.entity.Category;
import vn.huytan.model.Response;
import vn.huytan.service.ICategoryService;
import vn.huytan.service.IStorageService;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Tag(name = "Category API", description = "Quản lý danh mục sản phẩm")
public class CategoryApiController {

    private final ICategoryService categoryService;
    private final IStorageService storageService;

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả danh mục")
    public ResponseEntity<?> getAllCategory() {
        return ResponseEntity.ok(new Response(true, "Thành công", categoryService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết danh mục theo ID")
    public ResponseEntity<?> getCategory(@PathVariable("id") Long id) {
        Optional<Category> opt = categoryService.findById(id);
        if (opt.isPresent()) {
            return ResponseEntity.ok(new Response(true, "Thành công", opt.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(false, "Không tìm thấy", null));
    }

    @PostMapping(path = "/addCategory", consumes = "multipart/form-data")
    @Operation(summary = "Thêm danh mục mới có ảnh icon")
    public ResponseEntity<?> addCategory(
            @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "icon", required = false) MultipartFile icon) {

        if (categoryService.findByCategoryName(categoryName).isPresent()) {
            return ResponseEntity.badRequest().body(new Response(false, "Category đã tồn tại", null));
        }

        Category category = new Category();
        category.setCategoryName(categoryName);

        if (icon != null && !icon.isEmpty()) {
            String filename = storageService.getStorageFilename(icon, UUID.randomUUID().toString());
            storageService.store(icon, filename);
            category.setIcon(filename);
        }

        categoryService.save(category);
        return ResponseEntity.ok(new Response(true, "Thêm thành công", category));
    }

    @PutMapping(path = "/updateCategory", consumes = "multipart/form-data")
    @Operation(summary = "Cập nhật danh mục")
    public ResponseEntity<?> updateCategory(
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "icon", required = false) MultipartFile icon) {

        Optional<Category> opt = categoryService.findById(categoryId);
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body(new Response(false, "Không tìm thấy Category", null));
        }

        Category category = opt.get();
        category.setCategoryName(categoryName);

        if (icon != null && !icon.isEmpty()) {
            storageService.delete(category.getIcon());
            String filename = storageService.getStorageFilename(icon, UUID.randomUUID().toString());
            storageService.store(icon, filename);
            category.setIcon(filename);
        }

        categoryService.save(category);
        return ResponseEntity.ok(new Response(true, "Cập nhật thành công", category));
    }

    @DeleteMapping("/deleteCategory")
    @Operation(summary = "Xóa danh mục")
    public ResponseEntity<?> deleteCategory(@RequestParam("categoryId") Long categoryId) {
        Optional<Category> opt = categoryService.findById(categoryId);
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body(new Response(false, "Không tìm thấy Category", null));
        }

        try {
            Category category = opt.get();
            String iconFile = category.getIcon();


            categoryService.delete(category);

            if (iconFile != null && !iconFile.isBlank()) {
                storageService.delete(iconFile);
            }

            return ResponseEntity.ok(new Response(true, "Xóa thành công", category));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response(false, "Lỗi xóa CSDL (có thể danh mục này đang chứa sản phẩm): " + e.getMessage(), null));
        }
    }
}