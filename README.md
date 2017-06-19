# nlp-learn
Open Information Extraction

## Rquirements
### Frontend
- jquery [ZH](http://www.jquery123.com/) [EN](http://jquery.com/)
- bootstrap [ZH](http://v3.bootcss.com/) [EN](http://getbootstrap.com/)
- highcharts [ZH](https://www.hcharts.cn/) [EN](https://www.highcharts.com/)
- datatables [ZH](http://www.datatables.club/) [EN](https://www.datatables.net/)
- codemirror [EN](http://codemirror.net/)

### Backend
- springmvc [ZH](http://spring.cndocs.tk/index.html) [EN](http://docs.spring.io/spring-framework/docs/current/spring-framework-reference/htmlsingle/#mvc)
- mybatis [ZH](http://www.mybatis.org/mybatis-3/zh/index.html) [EN](http://www.mybatis.org/mybatis-3/)

### Other
- Database: MySQL
- Server: Tomcat


## Software Environment
### Maven
1. [Download](http://maven.apache.org/download.cgi) the file apache-maven-3.*.*-bin.tar.gz or apache-maven-3.*.*-bin.zip from .
2. Uncompress the downloaded file.
3. Add MAVEN_HOME({your_path}/apache-maven-3.*.*/bin) to Path.
4. Test whether the installation was successful. Enter "mvn -v" in console, you can see maven version info.


### XAMPP
1. [Download](https://www.apachefriends.org/download.html).
2. Install.
3. Add MySQL_HOME({your_path}/xampp/mysql/bin) to Path.

## Init Database
1. Run xampp-control.
2. Start apache and MySQL.
3. Click MySQL Admin button to open phpmyadmin in web browser.
4. New Database with **utf8_general_ci**.
5. Run batch file sql/db-init.bat to create all tables.

