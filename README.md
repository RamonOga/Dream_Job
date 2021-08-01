
# Проект "Dream Job"

* [Описание](#описание)
* [Функционал](#функционал)
* [Технологии](#технологии)
* [Интерфейс](#интерфейс)
* [Автор](#автор)

## Описание
CRUD-MVC приложение на сервлетах и JSP, реализующее простую биржу
вакансий и кандидатов.
Можно добавлять/изменять данные по каждой вакансии и кандидату.
По кандидатам так же поддерживается хранение фотографии.

## Функционал
* Регистрация пользователя
* Аутентификация на сервлет-фильтрах
* Авторизация через БД PostgreSQL
* Добавление/изменение вакансий
* Добавление/изменение соискателей
* Добавление/изменение/скачивание фотографии соискателя
* Две молдели хранения данных MemStore PsqlStore

## Технологии
* Java14
* JDBC
* PostgreSQL
* Servlet&JSP&JSTL
* HTML, CSS, BOOTSTRAP, JS, AJAX, JQUERY
* Apache Tomcat Server
* Junit, Mockito, Powermock
* Log4j
* Apache Commons Fileupload


* Добавление кандидата
![ScreenShot](images/addCandidate.PNG)

* Добавление вакансии
![ScreenShot](images/addPost.PNG)

* Список кандидатов 
![ScreenShot](images/candidates.PNG)

* Список вакансий 
![ScreenShot](images/posts.PNG)

* Авторизация
![ScreenShot](images/login.PNG)

* Регистрация
![ScreenShot](images/registration.PNG)

* Проверка подлиности пароля
![ScreenShot](images/wrongPass.PNG)

## Автор

Маркелов Роман Игоревич

Java-разработчик

roman.sercent@gmail.com

+79178764086

[![Build Status](https://travis-ci.com/RamonOga/dream_job.svg?branch=master)](https://travis-ci.com/RamonOga/Dream_Job)