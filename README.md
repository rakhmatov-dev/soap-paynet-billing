# SOAP-интерфейс для биллинга мерчанта платежной системы Paynet

Скомпилированное java-приложение запускает SOAP-интерфейс, соответствуюший требованиям из документации платежной системы Paynet. Документацию можно найти по ссылке - [https://github.com/rakhmatov-dev/soap-paynet-billing/blob/master/docs/ProviderWebServiceDeveloperManual-v1.3.pdf](https://github.com/rakhmatov-dev/soap-paynet-billing/blob/master/docs/ProviderWebServiceDeveloperManual-v1.3.pdf). Не во всех учетных системах (биллинговая система мерчанта) есть технологическая возможность реализовать описанный в документации SOAP-интерфейс (к примеру системы написанные на платформе 1С). Поэтому данный проект можно рассмотреть в качестве посредника между платежной системой и биллингом мерчанта. В проекте реализована переадресация всех запросов из документации в непосредственно API биллинга мерчанта по протоколу HTTP/HTTPS. 

Версия Java: 1.8.0.241.

## Запуск

Для запуска java-приложения необходимо запустить следующую команду:

java -jar <Полный путь до файла soap-paynet-billing-0.0.1-SNAPSHOT.jar> <номер порта на котором будет доступен SOAP API> <Пользователь SOAP API> <Пароль пользователя SOAP API> <Путь до http-сервиса биллинга мерчанта> <Пользователь http-сервиса биллинга мерчанта> <Пароль пользователя http-сервиса биллинга мерчанта>

Пример команды с конкретными значениями:

java -jar С:\Projects\Java\Soap\soap-paynet-billing\target\soap-course-management-0.0.1-SNAPSHOT.jar 8383 user Qwerty123 [http://localhost/paynet/hs/vendoo_paynet/v1.1](http://localhost/paynet/hs/vendoo_paynet/v1.1) user1C Qwerty123 

После запуска команды выше SOAP API доступен по адресу:

[http://localhost:8383/ws/ProviderWebService.wsdl](http://localhost:8383/ws/ProviderWebService.wsdl)

## Важно

В файле https://github.com/rakhmatov-dev/soap-paynet-billing/blob/master/pom.xml есть закомментированные плагины. Это сделано для того, чтобы при каждом построении проекта классы на основе файла https://github.com/rakhmatov-dev/soap-paynet-billing/blob/master/src/main/resources/ProviderWebService.xsd не пересоздавались, так как после первичного создания эти классы были отредактированы вручную.
