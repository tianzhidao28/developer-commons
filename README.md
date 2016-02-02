
## wecome
* 欢迎 fork 或 直接 提交代码 增加更多的工具类 和改进
* 以后 奇数作为测试版 偶数为测试版上线后发布的; eg:1.2.1增加了 es tools ,而后测试稳定后 发布1.2.2

## `注意事项：`
* `1. 每次提交请填写 详细的修改注释`
* `2. 在README.md里加入 正在使用这个jar包的 项目名(方便有重大改动和更新的时候通知你)`
* `3. 重要的方法或易出错的方法或在项目中大量使用或调用频繁的方法请写上 测试用例`

## usage :

* 如果用在restful项目里,建议用4.1以上的spring版本

```

<dependency>
  <groupId>cn.jpush</groupId>
  <artifactId>developer-commons</artifactId>
  <version>1.4.1</version>
</dependency>


```

## 建议:
* 写完Utils; 再写个测试方法 或 使用方法在里面
* 增加utils后 提醒楼主发包 ;
* 每次提交都要写详细的备注





## dependency
* common-lang3
* guava
* fastjson


## 功能

* 缓存工具类 redis
* 任务工具类 quartz job
* 增加 JSONUtils类
* 增加 MD5工具类
* 增加 验证 InStr  判断字符串在那几个字符串之中(Hibernate Valid 校验工具类)
* 增加 IP获取类
* 增加几个获取 时间相关的工具
* 增加SpringMVC 中通过注解获取request attr的工具
* 增加 CounchBase 超时次数达到20次时 自动断开重练(CouchBaseUtils.getData("app-stats","key");这个用法)
* 增加 HBase 相关工具类的更新 HInterface-替换为Table



## 注意
* 添加的 dependency : 可以设scope 为 <scope>provided</scope>；为使用者 按需引用
* 为你自己使用的工具类 写测试用例: 以后谁改了,一定要通过测试用例 防止对你的项目产生影响



## 使用方法:

> 在 classpath:下面的 system-config.properties (优先) 或者system.properties文件里 加入如下配置
```
app-stats.couchbase.host=192.168.249.124:8091,192.168.249.193:8091
app-stats.couchbase.bucket=appxxxxxx
app-stats.couchbase.pass=Apxxxxxxxxx


CouchBaseUtils.getData("app-stats","key");
CouchBaseUtils.getObjectData("app-stats","你的key");



Hbase 相关的操作 用pool 下的新版
```


```




```



## 已经在使用的项目
`使用过的 可以将项目git或项目名 记录在下面,方便以后有合理变动时 适当通知下当事人`

* data-api
* 数据组给portal提供thrift服务的项目
* 今后rocyuan参与的所有java项目


## 发布命令
* mvn deploy


## 异常code
```
84	公共组建异常	Blocker	JAVA公共组件异常	SOA	developer-commons公共组件	微信,抑制
```
