## demo 一个可以动态添加 quartz任务的 工程骨架


---


## build

* 本地环境  mvn clean package
* 开发环境  mvn clean package -Pdev
* 生产环境  mvn clean package -Pproduct

---

## run

* 开发环境 ：``` java -jar quartzProject-0.0.1-SNAPSHOT.jar ``` 
* 生产环境 : ``` nohup java -jar quartzProject-0.0.1-SNAPSHOT.jar >> quartzProjectRun.log 2>&1 & ```

* 开启debug ```java -Xdebug -Xrunjdwp:transport=dt_socket,address=8585,server=y,suspend=n -jar quartzProject-0.0.1-SNAPSHOT.jar```


default port: 8099

properties global.properties

---

## logs

log in ```./logs/demo-all.log```
