# 轻量级的springboot + quartz使用手册

* job
job的名称／组，job执行的方法
* trigger
trigger的名称／组，触发时间
* schedule
任务调度器
* misfire
错过的，指本来应该被执行但实际没有被执行的任务调度

## Quartz 任务调度的基本实现原理

Quartz 任务调度的核心元素是 scheduler, trigger 和 job，其中 trigger 和 job 是任务调度的元数据， scheduler 是实际执行调度的控制器。

在 Quartz 中，trigger 是用于定义调度时间的元素，即按照什么时间规则去执行任务。Quartz 中主要提供了四种类型的 trigger：SimpleTrigger，CronTirgger，DateIntervalTrigger，和 NthIncludedDayTrigger。这四种 trigger 可以满足企业应用中的绝大部分需求。我们将在企业应用一节中进一步讨论四种 trigger 的功能。

在 Quartz 中，job 用于表示被调度的任务。主要有两种类型的 job：无状态的（stateless）和有状态的（stateful）。对于同一个 trigger 来说，有状态的 job 不能被并行执行，只有上一次触发的任务被执行完之后，才能触发下一次执行。Job 主要有两种属性：volatility 和 durability，其中 volatility 表示任务是否被持久化到数据库存储，而 durability 表示在没有 trigger 关联的时候任务是否被保留。两者都是在值为 true 的时候任务被持久化或保留。一个 job 可以被多个 trigger 关联，但是一个 trigger 只能关联一个 job。

在 Quartz 中， scheduler 由 scheduler 工厂创建：DirectSchedulerFactory 或者 StdSchedulerFactory。 第二种工厂 StdSchedulerFactory 使用较多，因为 DirectSchedulerFactory 使用起来不够方便，需要作许多详细的手工编码设置。 Scheduler 主要有三种：RemoteMBeanScheduler， RemoteScheduler 和 StdScheduler。本文以最常用的 StdScheduler 为例讲解。这也是笔者在项目中所使用的 scheduler 类。

参考资料:

[http://www.quartz-scheduler.org/documentation/](http://www.quartz-scheduler.org/documentation/)

[https://www.ibm.com/developerworks/cn/opensource/os-cn-quartz/](https://www.ibm.com/developerworks/cn/opensource/os-cn-quartz/)

[http://cron.qqe2.com/](http://cron.qqe2.com/)
