Интернет-магазин на Spring Boot

Java 17
Spring Boot 3.2.5
Build Maven
Database PostgreSQL

REST API для учебного проекта "Интернет-магазин". Бэкенд реализован на Java с использованием фреймворка Spring Boot и обеспечивает основной функционал для управления товарами, заказами и пользователями.

Основные возможности

    Аутентификация и Авторизация:

        Регистрация пользователей.

        Вход по логину и паролю с выдачей JWT.

        Разделение прав доступа по ролям: ROLE_USER и ROLE_ADMIN.

    Управление товарами:

        Публичный просмотр товаров.

        Создание, изменение и удаление товаров (только для ROLE_ADMIN).

    Управление заказами:

        Создание заказа авторизованным пользователем.

        Просмотр своей истории заказов.

        Просмотр всех заказов в системе (только для ROLE_ADMIN).

    Бизнес-логика:

        Транзакционная обработка заказов с обновлением остатков на складе.

Стек технологий

    Язык: Java 17

    Фреймворк: Spring Boot 3.2.5

    База данных: PostgreSQL

    Работа с данными: Spring Data JPA / Hibernate

    Безопасность: Spring Security & JWT

    Сборка проекта: Maven

    Вспомогательные библиотеки: Lombok

Запуск и настройка проекта

Предварительные требования

    Java 17 (JDK)

    Apache Maven

    Запущенный сервер PostgreSQL

Пошаговая инструкция

    Клонируйте репозиторий:
    git clone https://github.com/slam-ui/Controller.git
    cd Controller

    Настройте базу данных:

        Запустите ваш сервер PostgreSQL.

        С помощью любого инструмента (например, pgAdmin) создайте новую базу данных:
        CREATE DATABASE shop_db;

    Настройте файл конфигурации:

        Откройте файл src/main/resources/application.properties.

        Измените spring.datasource.username и spring.datasource.password на ваши реальные данные доступа к PostgreSQL.
        spring.datasource.url=jdbc:postgresql://localhost:5432/shop_db
        spring.datasource.username=postgres
        spring.datasource.password=ваш_пароль

    Запустите приложение:

        Через IDE: Откройте проект в IntelliJ IDEA, найдите класс DemoApplication.java и запустите main метод.

        Через терминал: В корне проекта выполните команду:
        Для Windows
        .\mvnw.cmd spring-boot:run

        Для macOS/Linux
        ./mvnw spring-boot:run
        Сервер будет запущен по адресу http://localhost:8080.

Документация API

Для тестирования эндпоинтов рекомендуется использовать Postman.

Важно: Для доступа к защищенным эндпоинтам необходимо получить accessToken через /api/auth/login и передавать его в заголовке Authorization: Bearer <token>.

Аутентификация (/api/auth)

Регистрация

    POST /api/auth/register

    Body:
    {
    "username": "user",
    "password": "password123!"
    }
    Первый зарегистрированный пользователь автоматически становится администратором (ROLE_ADMIN).

Вход

    POST /api/auth/login

    Body:
    {
    "username": "user",
    "password": "password123!"
    }

Товары (/api/products)

    GET /api/products - Получить все товары (Доступ: Все)

    GET /api/products/{id} - Получить товар по ID (Доступ: Все)

    POST /api/products - Создать товар (Доступ: ADMIN)

    PUT /api/products/{id} - Обновить товар (Доступ: ADMIN)

    DELETE /api/products/{id} - Удалить товар (Доступ: ADMIN)

Заказы (/api/orders)

    POST /api/orders - Создать заказ (Доступ: USER, ADMIN)

        Body:
        {
        "items": [
        {
        "productId": 1,
        "quantity": 2
        }
        ]
        }

    GET /api/orders - Получить свои заказы (Доступ: USER, ADMIN)

    GET /api/orders/all - Получить все заказы (Доступ: ADMIN)
