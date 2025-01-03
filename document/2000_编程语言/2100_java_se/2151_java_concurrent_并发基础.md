---
title: java_concurrent_并发基础
date: 2020-04-10 00:00:00
---


## 并发问题的来源

主要有：
```
（1）操作系统增加了进程、线程，以分时复用CPU，均衡CPU与I/O设备的速度差异，导致 原子性问题
（2）CPU增加缓存L1/L2，以均衡与内存的速度差异，导致 可见性问题
（3）CPU或编译器指令重排序，提高运行效率，导致 有序性问题
```


<hr/>

## java内存模型简介（JMM）

java解决并发问题的方式，使用java内存模型规范了jvm如何解决上述的3个并发问题



**java并发内存模型（JSR-133）如下：**
```
jvm的实现必须遵守以下内容

1）内存模型抽象：
>**主内存：** 共享变量构成的内存部分
>**工作内存：** 每个线程使用到共享变量时，将主内存中的共享变量内存拷贝一份到自己的工作内存中

2）原子操作
>（1）lock锁定：作用于主内存的变量，把变量标识为一条线程独占的状态
>（2）unlock解锁：作用于主内存的变量，把锁定状态的变量释放出来
>（3）read读取：作用于主内存的变量，把一个变量的值从主内存传输到线程的工作内存中，以便随后的load动作使用
>（4）load载入：作用于工作内存的变量，把read操作从主内存中得到的变量值放入工作内存的变量副本中
>（5）use使用：作用于工作内存的变量，把工作内存的变量值传递给执行引擎，每当虚拟机遇到一个需要使用到变量的值的字节码指令时将会执行这个操作
>（6）assign赋值：作用于工作内存中的变量，把一个从执行引擎接收到的值赋给工作内存中的变量，每当虚拟机遇到一个给变量赋值的字节码指令时执行这个操作
>（7）store存储：作用于工作内存的变量，把工作内存中的一个变量的值传送到主内存中，一遍随后的write操作使用
>（8）write写入：作用于主内存的变量，把store操作从工作内存中得到的变量的值放入主内存的变量中

3）原子操作执行顺序
>（1）read/load 或 store/write 都必须顺序执行，但不要求连续，且不允许单个出现
>（2）不允许丢弃assign操作，即工作内存中改变了变量的值必须同步回主内存，不允许没有发生assign把变量同步回主内存
>（3）新变量只能在主内存中诞生，use和assign前必须分别执行load和assign操作
>（4）一个变量在同一时刻只允许一个线程对其lock，一个变量可以被同一个线程执行多次lock，后续必须执行相同次数的unlock才能解锁（可重入）
>（5）一个变量的unlock操作必须在lock之后
>（6）一个变量unlock前必须使用store和write将其同步回主内存

4）long和double的非原子性协定
>允许虚拟机将没有被volatile修饰的64位数据的读写操作划分为两次32位的操作来进行
>即允许虚拟机实现自行选择是否要保证64位数据类型的load、 store、 read和write这四个操作的原子性
>主流jvm64位不会出现问题，32位可能出现，一般可不用考虑



5）先行发生原则（Happens-Before）
对上述java内存模型的各种规范中 可见性和有序性 的一种近似性的描述，不够严谨，但便于日常程序开发参考使用

>（1）**程序顺序规则：** 一个线程中的每个操作，happens-before于该线程中的任意后续操作
>（2）**监视器锁规则：** 对一个锁的解锁，happens-before于随后对这个锁的加锁（synchronized）
>（3）**volatile变量规则：** 对一个volatile域的写，happens-before于任意后续对这个volatile域的读
>（4）**传递性：** 如果A happens-before B，且B happens-before C，那么A happens-before C
>（5）**start规则：** 如果线程A执行操作ThreadB.start()（启动线程B），那么A线程的ThreadB.start()操作happens-before于线程B中的任意操作
>（6）**join规则：** 如果线程A执行操作ThreadB.join()并成功返回，那么线程B中的任意操作happens-before于线程A从ThreadB.join()操作成功返回



6）java内存模型对并发问题的处理

原子性问题：
（1）这里的原子性理解：同一时刻，一个线程要么执行，要么不执行
（2）read、load、use、assign、store、write这6个操作可以直接保证基本类型的原子性操作（long、double 64位类型特殊，无需考虑）
（3）lock、unlock是用于保证更大范围的原子性，例如字节码指令 monitorenter 和 monitorexit （synchronized）就是这两个抽象操作的具体实现

可见性问题：
（1）voliatile变量，在工作内存修改后会立即更新到主内存，普通变量在工作内存修改后回写主内存时间不确定
（2）lock、unlock，（synchronized）在释放锁之前会对变量的修改刷新到主内存，从而保证可见性


有序性问题：
（1）happens-before规则中保证有序
（2）synchronized中的代码块保证有序

```
