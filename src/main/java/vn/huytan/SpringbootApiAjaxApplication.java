package vn.huytan;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import vn.huytan.service.IStorageService;

@SpringBootApplication
public class SpringbootApiAjaxApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootApiAjaxApplication.class, args);
    }

    @SuppressWarnings("unused")
	@Bean
    CommandLineRunner init(IStorageService storageService) {
        return (args -> {
            storageService.init();
        });
    }
}