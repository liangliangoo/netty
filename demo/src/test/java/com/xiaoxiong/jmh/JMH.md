# JMH

### JMH是什么

JMH 是 Java Microbenchmark Harness 的缩写。中文意思大致是 “JAVA 微基准测试套件”。首先先明白什么是“基准测试”。百度百科给的定义如下：

> [基准测试](https://baike.baidu.com/item/基准测试/5876292)是指通过设计科学的测试方法、测试工具和测试系统，实现对一类测试对象的某项性能指标进行定量的和可对比的测试。

可以简单的类比成我们电脑常用的鲁大师，或者手机常用的跑分软件安兔兔之类的性能检测软件。都是按一定的基准或者在特定条件下去测试某一对象的的性能，比如显卡、IO、CPU之类的。



### 为什么要使用 JMH

基准测试的特质有如下几种：

> **①、可重复性**：可进行重复性的测试，这样做有利于比较每次的测试结果，得到性能结果的长期变化趋势，为系统调优和上线前的容量规划做参考。

> **②、可观测性**：通过全方位的监控（包括测试开始到结束，执行机、服务器、数据库），及时了解和分析测试过程发生了什么。

> **③、可展示性**：相关人员可以直观明了的了解测试结果（web界面、仪表盘、折线图树状图等形式）。

> **④、真实性**：测试的结果反映了客户体验到的真实的情况（真实准确的业务场景+与生产一致的配置+合理正确的测试方法）。

> **⑤、可执行性**：相关人员可以快速的进行测试验证修改调优（可定位可分析）。

可见要做一次符合特质的基准测试，是很繁琐也很困难的。外界因素很容易影响到最终的测试结果。特别对于 JAVA的基准测试。


有些文章会告诉我们 JAVA是 C++编写的，一般来说 JAVA编写的程序不太可能比  C++编写的代码运行效率更好。但是JAVA在某些场景的确要比 C++运行的更高效。不要觉得天方夜谭。其实  JVM随着这些年的发展已经变得很智能，它会在运行期间不断的去优化。

 
这对于我们程序来说是好事，但是对于性能测试就头疼的。你运行的次数与时间不同可能获得的结果也不同，很难获得一个比较稳定的结果。对于这种情况，有一个解决办法就是大量的重复调用，并且在真正测试前还要进行一定的预热，使结果尽可能的准确。

除了这些，对于结果我们还需要一个很好的展示，可以让我们通过这些展示结果判断性能的好坏。

**而这些JMH都有！**



### 如何使用 JMH

下面我们以字符串拼接的几种方法为例子使用JMH做基准测试。

> JMH是 JDK9自带的，如果你是 JDK9 之前的版本也可以通过导入 openjdk

```xml

<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-core</artifactId>
    <version>1.19</version>
</dependency>
<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-generator-annprocess</artifactId>
    <version>1.19</version>
</dependency>
```



### JMH注解

#### @BenchmarkMode

用来配置 Mode 选项，可用于类或者方法上，这个注解的 value 是一个数组，可以把几种 Mode 集合在一起执行，如：`@BenchmarkMode({Mode.SampleTime, Mode.AverageTime})`，还可以设置为 `Mode.All`，即全部执行一遍。

1. Throughput：整体吞吐量，每秒执行了多少次调用，单位为 `ops/time`
2. AverageTime：用的平均时间，每次操作的平均时间，单位为 `time/op`
3. SampleTime：随机取样，最后输出取样结果的分布
4. SingleShotTime：只运行一次，往往同时把 Warmup 次数设为 0，用于测试冷启动时的性能
5. All：上面的所有模式都执行一次



#### @State

通过 State 可以指定一个对象的作用范围，JMH 根据 scope 来进行实例化和共享操作。@State 可以被继承使用，如果父类定义了该注解，子类则无需定义。由于 JMH 允许多线程同时执行测试，不同的选项含义如下：

1. Scope.Benchmark：所有测试线程共享一个实例，测试有状态实例在多线程共享下的性能
2. Scope.Group：同一个线程在同一个 group 里共享实例
3. Scope.Thread：默认的 State，每个测试线程分配一个实例



#### @OutputTimeUnit

为统计结果的时间单位，可用于类或者方法注解



#### @Warmup

预热所需要配置的一些基本测试参数，可用于类或者方法上。一般前几次进行程序测试的时候都会比较慢，所以要让程序进行几轮预热，保证测试的准确性。参数如下所示：

1. iterations：预热的次数
2. time：每次预热的时间
3. timeUnit：时间的单位，默认秒
4. batchSize：批处理大小，每次操作调用几次方法

> **为什么需要预热？**
>  因为 JVM 的 JIT 机制的存在，如果某个函数被调用多次之后，JVM 会尝试将其编译为[机器码](https://www.zhihu.com/search?q=机器码&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A1259967560})



#### @Measurement

实际调用方法所需要配置的一些基本测试参数，可用于类或者方法上，参数和 `@Warmup` 相同。



#### @Threads

每个进程中的测试线程，可用于类或者方法上。



#### @Fork

进行 fork 的次数，可用于类或者方法上。如果 fork 数是 2 的话，则 JMH 会 fork 出两个进程来进行测试。



#### @Param

指定某项参数的多种情况，特别适合用来测试一个函数在不同的参数输入的情况下的性能，只能作用在字段上，使用该注解必须定义 @State 注解。

在介绍完常用的注解后，让我们来看下 JMH 有哪些陷阱。



### 生成 jar 包执行

对于一些小测试，直接用上面的方式写一个 main 函数手动执行就好了。

对于大型的测试，需要测试的时间比较久、线程数比较多，加上测试的服务器需要，一般要放在 Linux 服务器里去执行。

JMH 官方提供了生成 jar 包的方式来执行，我们需要在 maven 里增加一个 plugin，具体配置如下：

```xml
<plugins>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>2.4.1</version>
        <executions>
            <execution>
                <phase>package</phase>
                <goals>
                    <goal>shade</goal>
                </goals>
                <configuration>
                    <finalName>jmh-demo</finalName>
                    <transformers>
                        <transformer
                                implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                            <mainClass>org.openjdk.jmh.Main</mainClass>
                        </transformer>
                    </transformers>
                </configuration>
            </execution>
        </executions>
    </plugin>
</plugins>
```

接着执行 maven 的命令生成可执行 jar 包并执行：

```shell
mvn clean install 
java -jar target/jmh-demo.jar StringConnectTest
```

## JMH 陷阱

在使用 JMH 的过程中，一定要避免一些陷阱。

比如 JIT 优化中的死码消除，比如以下代码：

```java
@Benchmark
public void testStringAdd(Blackhole blackhole) {
    String a = "";
    for (int i = 0; i < length; i++) {
        a += i;
    }
}
```

JVM 可能会认为变量 `a` 从来没有使用过，从而进行优化把整个方法内部代码移除掉，这就会影响测试结果。

JMH 提供了两种方式避免这种问题，一种是将这个变量作为方法返回值 return a，一种是通过 Blackhole 的 consume 来避免 JIT 的优化消除。

其他陷阱还有常量折叠与常量传播、永远不要在测试中写循环、使用 Fork 隔离多个测试方法、方法内联、伪共享与缓存行、分支预测、[多线程测试](https://www.zhihu.com/search?q=多线程测试&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A1259967560})

