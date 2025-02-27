---
title: java_framework_spring_cloud
date: 2019-07-27 00:00:00
---

## Eureka

### 1）功能

#### （1）服务注册
>Eureka Client会通过发送REST请求的方式向Eureka Server注册自己的服务
>提供自身的元数据：比如 IP 地址、端口、运行状况指标的URL、主页地址等
>Eureka Server接收到注册请求后
>将元数据信息存储在一个ConcurrentHashMap中

#### （2）服务续约
>服务注册后，Eureka Client会维护一个心跳来持续通知Eureka Server
>说明服务一直处于可用状态，防止被剔除
>Eureka Client在默认的情况下会每隔30秒发送一次心跳来进行服务续约

#### （3）服务同步
>Eureka Server之间会互相进行注册
>构建Eureka Server集群
>不同Eureka Server之间会进行服务同步
>用来保证服务信息的一致性

#### （4）获取服务
>服务消费者（Eureka Client）在启动的时候
>会发送一个REST请求给Eureka Server
>获取上面注册的服务清单
>并且缓存在Eureka Client本地，默认缓存30秒
>同时，为了性能考虑，Eureka Server也会维护一份只读的服务清单缓存，该缓存每隔30秒更新一次

#### （5）服务调用
>服务消费者在获取到服务清单后，就可以根据清单中的服务列表信息
>查找到其他服务的地址，从而进行远程调用
>Eureka有Region和Zone的概念
>一个Region可以包含多个Zone
>在进行服务调用时，优先访问处于同一个Zone中的服务提供者

#### （6）服务下线
>当Eureka Client需要关闭或重启时，就不希望在这个时间段内再有请求进来
>所以，就需要提前先发送REST请求给Eureka Server，告诉Eureka Server自己要下线了
>Eureka Server在收到请求后，就会把该服务状态置为下线（DOWN），并把该下线事件传播出去

#### （7）服务剔除
>有时候，服务实例可能会因为网络故障等原因导致不能提供服务
>而此时该实例也没有发送请求给Eureka Server来进行服务下线
>所以，还需要有服务剔除的机制
>Eureka Server在启动的时候会创建一个定时任务
>每隔一段时间（默认60秒）
>从当前服务清单中把超时没有续约（默认90秒）的服务剔除

#### （8）自我保护
>既然Eureka Server会定时剔除超时没有续约的服务
>那就有可能出现一种场景，网络一段时间内发生了异常
>所有的服务都没能够进行续约
>Eureka Server就把所有的服务都剔除了
>这样显然不太合理
>所以，就有了自我保护机制，当短时间内，统计续约失败的比例
>如果达到一定阈值，则会触发自我保护的机制
>在该机制下，Eureka Server不会剔除任何的微服务，等到正常后，再退出自我保护机制


## Feign
声明式http客户端
https://www.cnblogs.com/rickiyang/p/11802487.html

## Ribbon
http客户端负载均衡

https://www.cnblogs.com/rickiyang/p/11802465.html
https://www.cnblogs.com/rickiyang/p/11802474.html
https://www.cnblogs.com/rickiyang/p/11802484.html

## Hystrix
熔断器，服务隔离
https://www.cnblogs.com/rickiyang/p/11853315.html
https://www.cnblogs.com/rickiyang/p/11853315.html

## zuul
网关
https://www.cnblogs.com/rickiyang/p/12057661.html
https://www.cnblogs.com/rickiyang/p/12080755.html

## config
配置中心



