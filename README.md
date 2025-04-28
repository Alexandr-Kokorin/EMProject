![Project](https://github.com/Alexandr-Kokorin/EMProject/actions/workflows/project.yml/badge.svg)

# Тестовое Effective Mobile

## Описание решения
**Стек технологий:**
* Java 21
* Spring Framework
* Liquabase
* PostgreSQL
* Maven
* Lombok
* Testcontainers
* JUnit
* Docker/Docker Compose
* Grafana
* Prometheus

---

## Инструкция по запуску

* Запустить docker (версия 4.34.2 и новее)
* Выбрать способ запуска:
    * Отладочный - БД в контейнере, приложение отдельно - запустить `environment-compose.yml`
      и после запустить класс `Application`
    * Чистый - всё собирается в одном контейнере - запустить `compose.yml`
    * Полный - запускается все, что и в чистом способе запуска, только вместе с системами метрик и мониторинга - запустить `metrics-compose.yml`

Так же можно локально выполнить проверку код-stile, введя команду `mvn checkstyle:check`. Или же полную проверку, как 
в github, введя команду `mvn package`.




