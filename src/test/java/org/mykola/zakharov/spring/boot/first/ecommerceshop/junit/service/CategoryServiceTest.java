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
import java.util.Optional;

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

    @Test
    void shouldCreatedCategorySuccessfully() {
        ResponseModel responseModel =
                categoryService.create(returnCategoryModel("test category 1"));
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
                                CategoryModel.builder().id(1L).name("c1").build(),
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
            // попытка выполнить на модели метод с аргументом,
            // который должен вызвать исключение, моделью, содержащей ту же слишком длинную строку
            categoryServiceMock.create(returnCategoryModel(tooLongCategoryName));
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
    // подключение объекта, выполняющего пользовательские пред- и пост-действия
    // только для промаркированных им классов / методов
    @ExtendWith(SystemOutResource.class)
        // кейс проверки текста, выводмого при работе тестируемого метода
        // в терминал (например, отладочная информация, журнал операций)
    void shouldCategoryCreationSystemOut(/* SystemOutResource sysOut */) {
        categoryService.create(returnCategoryModel("test category 1"));
        // SystemOutResource должен перехватить текст из метода create,
        // предназначавшийся для терминала,
        // затем сравниваем этот текст с эталоном
        assertEquals(
                String.format("Category %s Created", returnCategoryModel("test category 1").getName().trim()),
                SystemOutResource.outContent.toString().trim()
        );
    }

    // посмотри что еще тестами не сделал
    // пробую создать дополнительные тесты

    @Test
    void shouldUpdatedCategorySuccessfully() {
        ResponseModel responseModel =
                categoryService.update(returnCategoryModel("test category 1 - v2"));
        // Проверка, что результат не равен null
        assertNotNull(responseModel);
        // Проверка, что результат содержит положительный статус-код
        assertEquals(ResponseModel.SUCCESS_STATUS, responseModel.getStatus());
        // Проверка, что в результате вызванного выше метода (create)
        // минимум 1 раз был вызван метод save
        verify(categoryDAO, atLeast(1))
                .save(categoryArgument.capture());
    }

//     try to descride delete method

    @Test
    void shouldDeletedCategorySuccessfully() {
        Optional<Category> optionalCategory =
                Optional.of(Category.builder().id(1L).name("c1").build());
        doReturn(optionalCategory
        ).when(categoryDAO) // откуда? - из объекта categoryDAO
                .findById(1L); // когда? - когда в метод findById передан аргумент 1
        final Category category = optionalCategory.get();

        ResponseModel responseModel =
                categoryService.delete(category.getId());
        // Проверка, что результат не равен null
        assertNotNull(category);
        // Проверка, что результат содержит положительный статус-код
        assertEquals(ResponseModel.SUCCESS_STATUS, responseModel.getStatus());
        // Проверка, что в результате вызванного выше метода (create)
        // минимум 1 раз был вызван метод save
        verify(categoryDAO, atLeast(1))
                .delete(categoryArgument.capture());
    }

    CategoryModel returnCategoryModel (String categoryName) {
        return CategoryModel.builder().name(categoryName).build();
    }

}
