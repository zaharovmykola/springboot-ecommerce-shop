package org.mykola.zakharov.spring.boot.first.ecommerceshop.junit.service;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.CategoryHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Category;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.CategoryModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.CategoryService;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.interfaces.ICategoryService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

/*
 * Набор модульных тестов для класса CategoryService
 * */
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    // Внедрение экземпляра CategoryHibernateDAO
    // для дальнейшего использования службой CategoryService
    @Mock
    private CategoryHibernateDAO categoryDAO;

    @Mock
    private ICategoryService categoryServiceMock;

    // Внедрение экземпляра CategoryService для его дальнейшего тестирования -
    // так, что при создании в него внедрятся все необходимые зависимости,
    // помеченные в классе тестов аннтацией @Mock
    @InjectMocks
    private CategoryService categoryService;

    ArgumentCaptor<Category> categoryArgument =
            ArgumentCaptor.forClass(Category.class);

    /* @BeforeAll
    static void setup() {
        System.out.println("CategoryService Unit Test Started");
    }
    @BeforeEach
    void init() {
        System.out.println("Test Case Started");
    }
    @AfterEach
    void tearDown() {
        System.out.println("Test Case Finished");
    } */


    /* @AfterAll
    static void done() {
        System.out.println("CategoryService Unit Test Finished");
    } */

    /* @Test
    void demoCase() {
        assertTrue(2 * 2 == 4);
    }
    @Test
    void demoCase2() {
        assertTrue("Hello JUnit5".equals("Hello" + " JUnit5"));
    } */

    /* @Test
    void demoWrongCase() {
        // assertTrue(2 * 2 == 5);
        assertEquals(5, 2 * 2);
    } */

    @Test
    void shouldCreatedCategorySuccessfully() {
        final CategoryModel categoryModel =
                CategoryModel.builder()
                        .name("test category 1")
                        .build();
        ResponseModel responseModel =
                categoryService.create(categoryModel);
        // Проверка, что результат не равен null
        assertNotNull(responseModel);
        // Проверка, что результат содержит положительный статус-код
        assertEquals(ResponseModel.SUCCESS_STATUS, responseModel.getStatus());
        // Проверка, что в результате вызванного выше метода (create)
        // минимум 1 раз был вызван метод save
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
                        .data(Arrays.asList(new CategoryModel[] {
                                new CategoryModel(1L, "c1"),
                                new CategoryModel(2L, "c2"),
                                new CategoryModel(3L, "c3")
                        }))
                        .build()
        ).when(categoryServiceMock) // откуда? - из объекта categoryServiceMock
                .getAll(); // как результат вызова какого метода? - getAll
        // вызов настроенного выше метода макета, полученного из интерфейса
        ResponseModel responseModel =
                categoryServiceMock.getAll();
        assertNotNull(responseModel);
        assertNotNull(responseModel.getData());
        assertEquals(((List)responseModel.getData()).size(), 3);
    }

    @Test
    void shouldThrowConstraintException() {
        // строка недопустимой длины
        final String tooLongCategoryName =
                "test category 1234567890 1234567890 1234567890";
        // модель, содержащая эту строку
        final CategoryModel tooLongNameCategoryModel =
                CategoryModel.builder().name(tooLongCategoryName).build();
        // обучение макета службы, созданного из интерйейса:
        // дано: когда будет вызван метод create службы,
        // и ему будет передан аргумент со строкой недопустимой длины -
        given(categoryServiceMock.create(tooLongNameCategoryModel))
                .willThrow(new IllegalArgumentException()); // нужно выбросить исключение IllegalArgumentException
        // проверка
        try {
            // модель, содержащая ту же слишком длинную строку
            final CategoryModel categoryModel =
                    CategoryModel.builder()
                            .name(tooLongCategoryName)
                            .build();
            // попытка выполнить на модели метод с аргументом,
            // который должен вызвать исключение
            categoryServiceMock.create(categoryModel);
            // если исключение не выброшено -
            // объявляем данный тест-кейс не пройденным
            // с выводом сообщения о причине
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException ex) { }
        // после проверяем, был ли хотя бы один раз вызван метод save
        // с каким-либо аргументом (универсальная заглушка)
        // на объекте categoryDAO
        then(categoryDAO)
                .should(never())
                .save(categoryArgument.capture());
    }

    @Test
    // @ExtendWith({SystemOutResource.class, SystemOutResourceParameterResolver.class})
    @ExtendWith(SystemOutResource.class)
    void checkSuccessLogging(/* SystemOutResource sysOut */) {
        final CategoryModel categoryModel =
                CategoryModel.builder()
                        .name("test category 1")
                        .build();
        categoryService.create(categoryModel);
        assertEquals(
                String.format("Category %s Created", categoryModel.getName().trim()),
                SystemOutResource.outContent.toString().trim()
        );
    }
}
