package org.mykola.zakharov.spring.boot.first.ecommerceshop.junit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.CategoryHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.ProductHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Category;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Product;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ProductModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.CategoryService;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.ProductService;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations="classpath:application.properties")
public class ProductServiceTest {

    @Value("tests.unit.strings.image-base64")
    private String imageBase64;

    @Mock
    private ProductHibernateDAO productDAO;

    @Mock
    private CategoryHibernateDAO categoryDAO;

    @Mock
    private IProductService productServiceMock;

    @InjectMocks
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    ArgumentCaptor<Product> productArgument =
            ArgumentCaptor.forClass(Product.class);
    ArgumentCaptor<Category> categoryArgument =
            ArgumentCaptor.forClass(Category.class);

    @Test
    void shouldCreatedProductSuccessfully() {
        Optional<Category> optionalCategory =
                Optional.of(
                    Category.builder()
                        .id(1L)
                        .name("c1")
                        .build()
                );
        // что вернуть? - объект типа сущность Category
        doReturn(
                optionalCategory
        ).when(categoryDAO) // откуда? - из объекта categoryDAO
        .findById(1L); // когда? - когда в метод findById передан аргумент 1

        final ProductModel productModel =
                ProductModel.builder()
                        .title("test product 1")
                        .description("about test product 1")
                        .price(new BigDecimal(10.5))
                        .quantity(5)
                        .image(imageBase64)
                        .categoryId(1L)
                        .build();

        /* Optional<Category> categoryOptional =
                categoryDAO.findById(productModel.getCategoryId()); */

        ResponseModel responseModel =
                productService.create(productModel);
        // Проверка, что результат не равен null
        assertNotNull(responseModel);
        // Проверка, что результат содержит положительный статус-код
        assertEquals(ResponseModel.SUCCESS_STATUS, responseModel.getStatus());
        // Проверка, что в результате вызванного выше метода (create)
        // минимум 1 раз был вызван метод save
        verify(productDAO, atLeast(1))
                .save(productArgument.capture());
    }

    @Test
    void shouldUpdatedProductSuccessfully() {
        final ProductModel productModel =
                ProductModel.builder()
                        .id(1L)
                        .title("test product 1")
                        .description("about test product 1")
                        .price(new BigDecimal(10.5))
                        .quantity(5)
                        .image(imageBase64)
                        .categoryId(Long.valueOf(1))
                        .build();

        Optional<Category> categoryOptional =
                categoryDAO.findById(productModel.getCategoryId());

        ResponseModel responseModel =
                productService.create(productModel);
        // Проверка, что результат не равен null
        assertNotNull(responseModel);
        // Проверка, что результат содержит положительный статус-код
        assertEquals(ResponseModel.SUCCESS_STATUS, responseModel.getStatus());
        // Проверка, что в результате вызванного выше метода (create)
        // минимум 1 раз был вызван метод save
        verify(productDAO, atLeast(1))
                .save(productArgument.capture());
        verify(categoryDAO, atLeast(1))
                .save(categoryArgument.capture());
    }

    @Test
    void shouldReturnGetAll() {
        // Обучаем макет:
        // вернуть что? - результат, равный ...
        doReturn(
                ResponseModel.builder()
                        .status(ResponseModel.SUCCESS_STATUS)
                        .data(Arrays.asList(new ProductModel[] {
                                ProductModel.builder().
                                        id(1L).title("c1").description("about c1").price(new BigDecimal(10.5)).
                                        quantity(5).image(imageBase64).categoryId(1L).
                                        build(),
                                ProductModel.builder().
                                        id(2L).title("c2").description("about c2").price(new BigDecimal(8.5)).
                                        quantity(7).image(imageBase64).categoryId(2L).
                                        build(),
                                ProductModel.builder().
                                        id(3L).title("c3").description("about c3").price(new BigDecimal(12.5)).
                                        quantity(15).image(imageBase64).categoryId(3L).
                                        build()
                        }))
                        .build()
        ).when(productServiceMock) // откуда? - из объекта categoryServiceMock
                .getAll(); // как результат вызова какого метода? - getAll
        // вызов настроенного выше метода макета, полученного из интерфейса
        ResponseModel responseModel =
                productServiceMock.getAll();
        assertNotNull(responseModel);
        assertNotNull(responseModel.getData());
        assertEquals(((List)responseModel.getData()).size(), 3);
    }

    @Test
    void shouldThrowConstraintException() {
        // строка недопустимой длины
        final String tooLongProductName =
                "test category 1234567890 1234567890 1234567890";
        // модель, содержащая эту строку
        final ProductModel tooLongNameProductModel =
                ProductModel.builder().title(tooLongProductName).build();
        // обучение макета службы, созданного из интерйейса:
        // дано: когда будет вызван метод create службы,
        // и ему будет передан аргумент со строкой недопустимой длины -
        given(productServiceMock.create(tooLongNameProductModel))
                .willThrow(new IllegalArgumentException()); // нужно выбросить исключение IllegalArgumentException
        // проверка
        try {
            // модель, содержащая ту же слишком длинную строку
            final ProductModel productModel =
                    ProductModel.builder()
                            .title(tooLongProductName)
                            .build();
            // попытка выполнить на модели метод с аргументом,
            // который должен вызвать исключение
            productServiceMock.create(productModel);
            // если исключение не выброшено -
            // объявляем данный тест-кейс не пройденным
            // с выводом сообщения о причине
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException ex) { }
        // после проверяем, был ли хотя бы один раз вызван метод save
        // с каким-либо аргументом (универсальная заглушка)
        // на объекте categoryDAO
        then(productDAO)
                .should(never())
                .save(productArgument.capture());
    }

    @Test
    // @ExtendWith({SystemOutResource.class, SystemOutResourceParameterResolver.class})
    @ExtendWith(SystemOutResource.class)
    void shouldProductCreationSystemOut(/* SystemOutResource sysOut */) {
        Optional<Category> optionalCategory =
                Optional.of(
                        Category.builder()
                                .id(1L)
                                .name("c1")
                                .build()
                );
        // что вернуть? - объект типа сущность Category
        doReturn(
                optionalCategory
        ).when(categoryDAO) // откуда? - из объекта categoryDAO
                .findById(1L); // когда? - когда в метод findById передан аргумент 1
        final ProductModel productModel =
                ProductModel.builder()
                        .title("test product 1")
                        .description("about test product 1")
                        .price(new BigDecimal(10.5))
                        .quantity(5)
                        .image(imageBase64)
                        .categoryId(1L)
                        .build();
        productService.create(productModel);
        assertEquals(
                String.format("Product %s Created", productModel.getTitle().trim()),
                SystemOutResource.outContent.toString().trim()
        );
    }

    // посмотри что еще тестами не сделал
}
