> Отсутствие Kaspresso одно из требований тестового задания  

Запустить тесты из пакета  
Удалить локальные allure-results ```gradle deleteAllureResults```  
Забрать с устройства allure-results ```adb pull /sdcard/allure-results build/allure-results```  
Собрать отчет ```gradle allureReport```  
Посмотреть отчет ```build/reports/allure-report/index.html```  