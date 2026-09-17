package vn.huytan.controller.api;

import java.util.Date;
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
import vn.huytan.entity.Product;
import vn.huytan.model.Response;
import vn.huytan.service.ICategoryService;
import vn.huytan.service.IProductService;
import vn.huytan.service.IStorageService;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "Quản lý sản phẩm")
public class ProductApiController {

    private final IProductService productService;
    private final ICategoryService categoryService;
    private final IStorageService storageService;

    @GetMapping
    @Operation(summary = "Lấy tất cả sản phẩm")
    public ResponseEntity<?> getAllProduct() {
        return ResponseEntity.ok(new Response(true, "Thành công", productService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy sản phẩm theo ID")
    public ResponseEntity<?> getProduct(@PathVariable("id") Long id) {
        Optional<Product> opt = productService.findById(id);
        if (opt.isPresent()) {
            return ResponseEntity.ok(new Response(true, "Thành công", opt.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(false, "Không tìm thấy sản phẩm", null));
    }

    @PostMapping(path = "/addProduct", consumes = "multipart/form-data")
    @Operation(summary = "Thêm sản phẩm mới")
    public ResponseEntity<?> addProduct(
            @RequestParam("productName") String productName,
            @RequestParam("unitPrice") Double unitPrice,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("description") String description,
            @RequestParam("discount") Double discount,
            @RequestParam("status") Short status,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        Optional<Category> optCat = categoryService.findById(categoryId);
        if (optCat.isEmpty()) {
            return ResponseEntity.badRequest().body(new Response(false, "Danh mục không tồn tại", null));
        }

        Product product = new Product();
        product.setProductName(productName);
        product.setUnitPrice(unitPrice);
        product.setQuantity(quantity);
        product.setDescription(description);
        product.setDiscount(discount);
        product.setStatus(status);
        product.setCategory(optCat.get());
        product.setCreateDate(new Date());

        if (imageFile != null && !imageFile.isEmpty()) {
            String filename = storageService.getStorageFilename(imageFile, UUID.randomUUID().toString());
            storageService.store(imageFile, filename);
            product.setImages(filename);
        }

        productService.save(product);
        return ResponseEntity.ok(new Response(true, "Thêm sản phẩm thành công", product));
    }

    @PutMapping(path = "/updateProduct", consumes = "multipart/form-data")
    @Operation(summary = "Cập nhật sản phẩm")
    public ResponseEntity<?> updateProduct(
            @RequestParam("productId") Long productId,
            @RequestParam("productName") String productName,
            @RequestParam("unitPrice") Double unitPrice,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("description") String description,
            @RequestParam("discount") Double discount,
            @RequestParam("status") Short status,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        Optional<Product> optProduct = productService.findById(productId);
        if (optProduct.isEmpty()) {
            return ResponseEntity.badRequest().body(new Response(false, "Sản phẩm không tồn tại", null));
        }
        Optional<Category> optCat = categoryService.findById(categoryId);
        if (optCat.isEmpty()) {
            return ResponseEntity.badRequest().body(new Response(false, "Danh mục không tồn tại", null));
        }

        Product product = optProduct.get();
        product.setProductName(productName);
        product.setUnitPrice(unitPrice);
        product.setQuantity(quantity);
        product.setDescription(description);
        product.setDiscount(discount);
        product.setStatus(status);
        product.setCategory(optCat.get());

        if (imageFile != null && !imageFile.isEmpty()) {
            storageService.delete(product.getImages());
            String filename = storageService.getStorageFilename(imageFile, UUID.randomUUID().toString());
            storageService.store(imageFile, filename);
            product.setImages(filename);
        }

        productService.save(product);
        return ResponseEntity.ok(new Response(true, "Cập nhật thành công", product));
    }

    @DeleteMapping("/deleteProduct")
    @Operation(summary = "Xóa sản phẩm")
    public ResponseEntity<?> deleteProduct(@RequestParam("productId") Long productId) {
        Optional<Product> opt = productService.findById(productId);
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body(new Response(false, "Không tìm thấy sản phẩm", null));
        }
        storageService.delete(opt.get().getImages());
        productService.delete(opt.get());
        return ResponseEntity.ok(new Response(true, "Xóa thành công", opt.get()));
    }
}