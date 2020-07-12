```
 _____ ________  _________ _       ___ _____ _____ 
|_   _|  ___|  \/  || ___ \ |     / _ \_   _|  ___|
  | | | |__ | .  . || |_/ / |    / /_\ \| | | |__  
  | | |  __|| |\/| ||  __/| |    |  _  || | |  __| 
  | | | |___| |  | || |   | |____| | | || | | |___ 
  \_/ \____/\_|  |_/\_|   \_____/\_| |_/\_/ \____/ 
                                                   
                                                   
```

# template-server
服务单机模板

# 集成组件
- jdk8
- springboot
- mybatis
- mysql
- resdis
- springboot-serssion
- lombok
- unirest

# 特点
- 读写分离

# 使用方法
可以将代码clone到本地，在根目录执行下面目录将archetype安装到本地maven仓库
mvn clean archetype:create-from-project && cd target/generated-sources/archetype && mvn install

然后使用命令行使用项目模板创建工程
mvn archetype:generate