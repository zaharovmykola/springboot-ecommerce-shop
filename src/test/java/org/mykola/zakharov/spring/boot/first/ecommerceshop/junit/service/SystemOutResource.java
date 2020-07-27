package org.mykola.zakharov.spring.boot.first.ecommerceshop.junit.service;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SystemOutResource implements BeforeEachCallback, AfterEachCallback {

    // для хранения ссылки на поток исходящих данных, направленный в терминал
    private PrintStream sysOut;
    // для перехвата и накопления всех байтов,
    // предназначавшихся для вывода в терминал
    public static ByteArrayOutputStream outContent =
            new ByteArrayOutputStream();

    // выполнится перед каждым тест-кейсом, помеченным специальной аннотацией
    // с указанием данного класса
    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        System.out.println("Before checkSuccessLogging");
        // сохраняем ссылку на поток для исходящих данных, направленный в терминал
        sysOut = System.out;
        // и вместо терминала для всех функций вывода данных подключаем
        // в качестве приемника ByteArrayOutputStream
        System.setOut(new PrintStream(outContent));
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        // возвращаем методам вывода данных виртуальной машины Джава ссылку
        // на поток для исходящих данных, направленный в терминал
        System.setOut(sysOut);
        System.out.println("After checkSuccessLogging");
    }

    // для получения результата - данных, предназначенных для вывода
    // в терминал (сейчас не используется, а вместо него
    // результат считывается напрямую из статического поля outContent)
    public String asString() {
        return outContent.toString();
    }
}
