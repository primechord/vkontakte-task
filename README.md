> Отсутствие Kaspresso одно из требований тестового задания  

Удалить локальные allure-results ```gradle deleteAllureResults ```  
Забрать результаты ```adb pull /sdcard/allure-results build/allure-results```  
Собрать отчет ```gradle allureReport```  
И посмотреть отчет ```build/reports/allure-report/index.html```  