# Albrus - Spring
(:3[▓▓▓▓▓▓▓▓]
> Systematically study Spring related frameworks and practice and record to deepen understanding.

## 总结

1. Spring在容器启动的时候，会事先保存所有需要注册的Bean定义信息

   1. xml注册
   2. @Service、@Bean、@Component。。。

2. Spring会在何时的时机来创建这些Bean

   1. 提前用到Bean时，利用 `getBean` 创建Bean并存放至缓存
   2. 后续在 `finishBeanFactoryInitialization` 阶段统一创建剩下的Bean

3. 后置处理器：

   每一个Bean创建完成后，都会使用各种后置处理器对Bean进行处理

   `ApplicationContextAwareProcessor`：Aware组件（`ApplicationContextAware`）

   **注意：`BeanFactoryAware` 是在 `populateBean` 之后的 `initializeBean` 阶段，执行 `invokeAwareMethods` 时触发**

   `AutowiredAnnotationBeanPostProcessor`：自动注入

   `AnnotationAwareAspectJAutoProxyCreator`：AOP

4. 事件驱动模型：

   `ApplicationListener`：事件监听

   `ApplicationEventMulticaster`：事件多播器

   `EventListenerMethodProcessor`：`@EventListener` 标注方法事件监听器



