## update-log
### 1.1
* 增加hbase table tool

### 1.4.0
* 增加 JSONUtils类
* 增加 MD5工具类
* 增加 验证 InStr  判断字符串在那几个字符串之中
* 增加 IP获取类
* 增加几个获取 时间相关的工具
* 增加SpringMVC 中通过注解获取request attr的工具

### 1.4.1
* 增加 HBase 相关工具类的更新 HInterface-替换为Table
* 删掉HBase Manager 类

### 1.4.2
* HBase dependency bug fixed
* HBase connection bug fixed

### 1.4.3
* RedisCacheManager里 增加几个操作方法
* XXPool bug 修复

### 1.4.4
* CouchBase 操作增加 可以抛出异常的操作
  TimeoutException ｜ RuntimeException
  CouchBaseUtils.getDataEx(String couchBaseName , String key)
  CouchBaseUtils.getObjectDataEx(String couchBaseName , String key)
  增加 参数判断空校验
  RedisCacheManager getInstance拼写错误修正(错误的拼写暂时还保留)

### 1.4.5

* 增加 mvn api 测试 ,先让API返回结果 给前端去测试  再去编写实现