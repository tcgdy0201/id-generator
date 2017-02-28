# 工程内readme.md
## 背景
给集群环境下生产一定命名规范下的id,如订单号等。

开源在github的这个版本可以理解为是一个抛砖引玉的原型工程。如果要真实使用的话，需要fork后自己实现一套UniqueNumberProducer。

## 方案
通过集中式缓存持久一份全局唯一的数字在本地,每次生成单号时会取这个号。参考的主要是oracle rac环境下sequence的高性能创建方式`cache noorder`。

本地缓存通过

## 类图
![15:30:03.jpg](http://ww2.sinaimg.cn/large/7853084cgw1f7angi4q76j20lc0fk0ul.jpg)

## 存在问题
唯一数字的长度越短,在大规模集群下出现重复的概率越大。

如:如200台机器的集群,可用长度只有5,每次缓存1000时,所有机器取一次缓存就会消耗1000 * 200 = 200000,就有可能出现重复。

但是目前我们的规模还不会出现这种问题。

# 使用方式
1. 参考CodisUniqueNumberProducer实现一套基于适合你们公司现状的方式，比如你们使用redis，就写个redis的获取实现。没有nosql，那用db来获取也是OK的。 
2. 引入唯一单号生成器的依赖包

```xml
<dependency>
  <groupId>com.guxinruo</groupId>
  <artifactId>id-generator</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

## 配置工作
找一个现成的或新增一个spring配置文件进行相应配置：

```
    <bean id="idGenConfig" class="com.hujiang.foe.id.generator.core.IdGenConfig"/>
    <bean id="codisUniqueNumberProducer" class="com.hujiang.foe.id.generator.core.CodisUniqueNumberProducer">
        <constructor-arg index="0" ref="jodisClient"/>
        <constructor-arg index="1" value="${yourRedisKey}"/>
        <constructor-arg index="2" ref="idGenConfig"/>
    </bean>

    <bean id="idGenerator" class="com.hujiang.foe.id.generator.core.IdGenerator">
        <constructor-arg index="1" ref="idGenConfig"/>
        <constructor-arg index="0" ref="codisUniqueNumberProducer"/>
    </bean>
```

### 配置说明
`${yourRedisKey}`每个不同业务下的单号，请用不同的redisKey，可写死，也可配在properties内

`com.hujiang.foe.id.generator.core.IdGenConfig`详解：

```
/**
 * ID生成器配置
 * Created by 三石她爸
 * Date : 2016-08-26
 */
public class IdGenConfig implements InitializingBean{
    private static final int DEFAULT_ID_LENGTH = 32;//最大长度
    private static final int DEFAULT_CACHE_SIZE = 1000;//最大长度
    private static final String DEFAULT_DATE_PATTERN = "yyyyMMddHHmmss";//日期格式
    private static final String DEFAULT_PREFIX = "";//默认前缀
    private static final String DEFAULT_SUFFIX = "";//默认前缀


    private String prefix = DEFAULT_PREFIX;//前缀
    private String suffix = DEFAULT_SUFFIX;//后缀
    private String datePattern = DEFAULT_DATE_PATTERN;//日期格式
    private int length = DEFAULT_ID_LENGTH;//id长度
    private boolean cache = true;//是否缓存
    private int cacheSize = 1000;//缓存数
```
最终单号的拼装逻辑为:`prefix + 按datePattern格式化后的字符串 + 全局唯一的数字(不足长度左补0) + suffix`

当`cache=false`时，会每次都从codis内incr一下取值。

建议使用默认配置即可。


## 生成单号api
```
    @Autowired
    private IdGenerator idGenerator;
    
    public void xxxx(){
        String orderNo = idGenerator.next();
    }

```


