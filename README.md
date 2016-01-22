
## wecome
* 欢迎 fork 或 直接 提交代码 增加更多的工具类 和改进


## `注意事项：`
* `1. 每次提交请填写 详细的修改注释`
* `2. 在README.md里加入 正在使用这个jar包的 项目名(方便有重大改动和更新的时候通知你)`
* `3. 重要的方法或易出错的方法或在项目中大量使用或调用频繁的方法请写上 测试用例`

## usage :

```

<dependency>
  <groupId>cn.jpush</groupId>
  <artifactId>developer-commons</artifactId>
  <version>1.3.1</version>
</dependency>


```

## 建议:
* 写完Utils; 再写个测试方法 或 使用方法在里面
* 增加utils后 提醒楼主发包 ;
* 每次提交都要写详细的备注





## dependency
* apache-common-lang
* guava


## 功能

* 缓存工具类 redis
* 任务工具类 quartz job


## 注意
* 添加的 dependency : 可以设scope 为 <scope>provided</scope>；为使用者 按需引用
* 为你自己使用的工具类 写测试用例: 以后谁改了,一定要通过测试用例 防止对你的项目产生影响



## 已经在使用的项目
`使用过的 可以将项目git或项目名 记录在下面,方便以后有合理变动时 适当通知下当事人`

* data-api


## 发布命令
* mvn deploy

## update-log
### 1.1
* 增加hbase table tool