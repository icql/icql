---
title: mybatis
sort: 2401
date: 2018-10-23 00:00:00
category:
    - 'java'
tags: 
    - 'mybatis'
comments: true
---
## MyBatis介绍
![MyBatis介绍](../../../resource/img/265-1.png)

## MyBaits整体架构
![MyBaits整体架构](../../../resource/img/265-2.png)

## 开发方式
* 传统dao开发
* Mapper代理开发（推荐使用）
	* 1）mapper接口的全限定名要和mapper映射文件的namespace值一致
	* 2）mapper接口的方法名称要和mapper映射文件的statement的id一致
	* 3）mapper接口的方法参数类型要和mapper映射文件的statement的parameterType的值一致，而且它的参数是一个
	* 4）mapper接口的方法返回值类型要和mapper映射文件的statement的resultType的值一致

## 全局配置
* 默认配置
``` java
//SqlMapConfig.xml的配置内容和顺序如下（顺序不能乱）：
Properties（属性）
Settings（全局参数设置）
typeAliases（类型别名）
typeHandlers（类型处理器）
objectFactory（对象工厂）
plugins（插件）
environments（环境信息集合）
	environment（单个环境信息）
		transactionManager（事物）
		dataSource（数据源）
mappers（映射器）
```

* spring集成
``` java
//spring配置
<!-- 加载配置文件 -->
<context:property-placeholder location="classpath:properties/*.properties"/>

<!-- 数据库连接池 -->
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource"
      destroy-method="close">
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
    <property name="driverClassName" value="${jdbc.driver}"/>
    <property name="maxActive" value="10"/>
    <property name="minIdle" value="5"/>
</bean>

<!-- 让spring管理sqlsessionfactory 使用mybatis和spring整合包中的 -->
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <!-- 数据库连接池 -->
    <property name="dataSource" ref="dataSource"/>
    <!-- 加载mybatis的全局配置文件 -->
    <property name="configLocation" value="classpath:mybatis/SqlMapConfig.xml"/>
</bean>

<!--mapper配置文件扫描-->
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="basePackage" value="work.icql.sample.ssm.mapper"/>
</bean>

//SqlMapConfig配置
<configuration>
    <settings>
        <!-- 打印查询语句 -->
        <setting name="logImpl" value="STDOUT_LOGGING" />
    </settings>

    <plugins>
        <!-- 配置分页插件 -->
        <plugin interceptor="com.github.pagehelper.PageHelper">
            <!-- 指定使用的数据库是什么 -->
            <property name="dialect" value="mysql"/>
        </plugin>
    </plugins>
</configuration>
```

## Mapper映射文件
* CRUD标签
``` java
//select – 书写查询sql语句
select中的几个属性说明：
id属性：当前名称空间下的statement的唯一标识(必须属性)；
resultType：将结果集映射为java的对象类型。必须（和 resultMap 二选一）
parameterType：传入参数类型。可以省略

//insert
insert 的几个属性说明：
id：当前名称空间下的statement的唯一标识(必须属性)；
parameterType：参数的类型，使用动态代理之后和方法的参数类型一致
useGeneratedKeys：开启主键回写
keyColumn：指定数据库的主键
keyProperty：主键对应的pojo属性名
标签内部：具体的sql语句

//update
id属性：当前名称空间下的statement的唯一标识(必须属性)；
parameterType：传入的参数类型，可以省略。
标签内部：具体的sql语句

//delete
delete 的几个属性说明：
id属性：当前名称空间下的statement的唯一标识(必须属性)；
parameterType：传入的参数类型，可以省略。
标签内部：具体的sql语句
```

* 输入映射
	* parameterType：完全可以省略不写，类型的全限定名，不要使用基本类型，用对应的包装类型代替
	* #{}和${}：单个参数不需要，任意名字均可，多个参数需在接口方法参数使用@Param("xxx")用以区分，并且不需要写parameterType
		* 1）#{}，占位符?
		* 2）${}，字符串拼接
``` java
//1、简单类型
User login(@Param("username") String userName, @Param("password") String password);

<select id="login" resultType="work.icql.sample.pojo.User">
    select * from tb_user where user_name = #{username,jdbcType=VARCHAR} and password = #{password,jdbcType=VARCHAR}
</select>

//2、pojo类型
User login(User user);

<select id="login" resultType="work.icql.sample.pojo.User">
    select * from tb_user where user_name = #{user.username,jdbcType=VARCHAR} and password = #{user.password,jdbcType=VARCHAR}
</select>

//3、List
int insertForeach(List<User> list);

<insert id="insertForeach" parameterType="java.util.List" useGeneratedKeys="false">
	insert into user
	(username,password)
	values
	<foreach collection="list" item="item" index="index" separator=",">
	(#{item.username},#{item.password})
	</foreach>		
</insert>

//4、Map
//遍历
<insert id="XXX" parameterType="java.util.Map">
INSERT INTO table(a, b)
VALUES
<foreach collection="param.entrySet()" open="(" separator="," close=")" index="key" item="val">
	#{key}, #{val}
</foreach>
</insert>

//直接取key，value
User login(Map<String,String> user);
//username和password为map的key
<select id="login" resultType="work.icql.sample.pojo.User">
    select * from tb_user where user_name = #{user.username,jdbcType=VARCHAR} and password = #{user.password,jdbcType=VARCHAR}
</select>
```

* 输出映射
	* 1）resultType：类型的全限定名，不要使用基本类型，用对应的包装类型代替
		* 需要查询出的列名和映射的对象的属性名一致，才能映射成功，可用sql列别名解决字段与列名不一致问题
	* 2）resultMap：
``` java
<resultMap id="BaseResultMap" type="work.icql.sample.pojo.User">
    <id column="username" jdbcType="VARCHAR" property="username" />
    <result column="password" jdbcType="VARCHAR" property="password" />
</resultMap>

//使用 resultMap 代替 resultType
<select id="login" resultMap="BaseResultMap">
    select * from tb_user where user_name = #{user.username,jdbcType=VARCHAR} and password = #{user.password,jdbcType=VARCHAR}
</select>
```

* 动态sql
``` java
//sql片段：让代码有更高的可重用性，需要先定义后使用
<sql id="Base_Column_List">
	id, name, price, status, created, updated, version
</sql>

//if标签
<if test="user != null">
	sql语句
</if>

//where标签：主要是用来简化sql语句中where条件判断的书写的，会自动将其后第一个条件的and或者是or给忽略掉
<select id="selectByParams" parameterType="map" resultType="user">
	select * from user
	<where>
		<if test="id != null ">
			id=#{id}
		</if>
		<if test="name != null and name.length()>0" >
			and name=#{name}
		</if>
		<if test="gender != null and gender.length()>0">
			and gender = #{gender}
		</if>
	</where>
</select>

//foreach标签
<foreach collection="list" item="item" index="index" separator=",">
	(#{item.username},#{item.password})
</foreach>
```

* 高级结果映射：https://www.cnblogs.com/selene/p/4627446.html
``` java
//一对一，一对多
public class Order {
    private List<OrderDetail> detailList;
}

//一对一association，一对多collection
<resultMap id="OrderUserDetailResultMap" type="Order" autoMapping="true">
    <id column="id" property="id"/>
    <association property="user" javaType="com.zpc.mybatis.pojo.User">
        <id column="user_id" property="id"/>
		<result column="password" jdbcType="VARCHAR" property="password" />
    </association>
    <collection property="detailList" javaType="List" ofType="OrderDetail">
        <id column="id" property="id"/>
		<result column="password" jdbcType="VARCHAR" property="password" />
    </collection>
</resultMap>

<select id="queryOrderWithUserAndDetailByOrderNumber" resultMap="OrderUserDetailResultMap">
   select * from tb_order o
   left join tb_user u on o.user_id=u.id
   left join tb_orderdetail od on o.id=od.order_id
   where o.order_number = #{number}
</select>

//多对多，省略

```

## 延迟加载（懒加载）
* Mybatis支持延迟加载，默认是关闭的，只支持这两个标签：一对一association，一对多collection
``` java
//需要在SqlMapConfig.xml文件中通过setting标签配置来开启延迟加载功能
//开启延迟加载的属性：
//lazyLoadingEnabled：全局性设置懒加载。如果设为‘false’，则所有相关联的都会被初始化加载。默认为false
//aggressiveLazyLoading：当设置为‘true’的时候，懒加载的对象可能被任何懒属性全部加载。否则，每个属性都按需加载。默认为true
<settings>
    <!--开启延迟加载-->
    <setting name="lazyLoadingEnabled" value="true"/>
    <!--关闭积极加载-->
    <setting name="aggressiveLazyLoading" value="false"/>
</settings>
```

## MyBatis查询缓存 https://www.cnblogs.com/xrq730/p/6991655.html
* 一级缓存，默认开启，sqlsession级别

* 二级缓存，默认不开启（不建议使用），mapper级别