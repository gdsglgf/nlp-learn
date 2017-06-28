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
### Java Platform (JDK)
1. [Download](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
2. Install.
3. Set JAVA_HOME = {your_path}\jdk1.\*.\*
4. Set CLASSPATH = .;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar
5. Add **;%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin** to Path
6. Test with "java -version" in console.

### Maven
1. [Download](http://maven.apache.org/download.cgi) the file apache-maven-3.\*.\*-bin.tar.gz or apache-maven-3.\*.\*-bin.zip from .
2. Uncompress the downloaded file.
3. Add MAVEN_HOME({your_path}/apache-maven-3.\*.\*/bin) to Path.
4. Test whether the installation was successful. Enter "mvn -v" in console, you can see maven version info.

### XAMPP
1. [Download](https://www.apachefriends.org/download.html).
2. Install.
3. Add MySQL_HOME({your_path}/xampp/mysql/bin) to Path.

### ActiveMQ
1. [Download](http://activemq.apache.org/).
2. Install.
3. Enable jmxrmi as follows:
```xml
## Edit {ActiveMQ_HOME}\conf\activemq.xml

<broker xmlns="http://activemq.apache.org/schema/core" brokerName="localhost" dataDirectory="${activemq.data}">
replace with
<broker xmlns="http://activemq.apache.org/schema/core" brokerName="localhost" dataDirectory="${activemq.data}" useJmx="true">

<managementContext createConnector="false"/>
replace with
<managementContext createConnector="true" connectorPath="/jmxrmi" connectorPort="1099" />
```

## Init Database
1. Run xampp-control.
2. Start apache and MySQL.
3. Click MySQL Admin button to open phpmyadmin in web browser.
4. New Database with **utf8_general_ci**.
5. Run batch file sql/db-init.bat to create all tables.


## Command Line
```
cd nlp-learn

# For single thread job
mvn exec:java -Dexec.mainClass="com.nlp.job.StandaloneRunner"

# For multithread job
mvn exec:java -Dexec.mainClass="com.nlp.job.ConcurrentJob"

mvn exec:java -Dexec.mainClass="com.nlp.job.ConcurrentJob" -Dexec.args="/data/a.bz2 /data/b.bz2"

# For activeMQ
mvn exec:java -Dexec.mainClass="com.nlp.job.ActiveMQRunner"

# tools
mvn exec:java -Dexec.mainClass="com.nlp.tool.Statistics" -Dexec.args="'2017-06-23 17:53:00' '2017-06-24 13:49:00' 7676"

mvn exec:java -Dexec.mainClass="com.nlp.tool.FileScanner"
```