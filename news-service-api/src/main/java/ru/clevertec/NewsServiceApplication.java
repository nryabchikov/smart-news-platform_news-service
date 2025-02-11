package ru.clevertec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Главный класс приложения.
 */
@SpringBootApplication
@EnableFeignClients
public class NewsServiceApplication {

	/**
	 * Точка входа в приложение.
	 * @param args Аргументы командной строки.
	 */
	public static void main(String[] args) {
		SpringApplication.run(NewsServiceApplication.class, args);
	}

}
