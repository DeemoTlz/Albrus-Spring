# Albrus - Spring Bean

> Understanding and learning of bean life cycle in spring framework.

## 一、注册Bean

### 1.1 手动注册

1. 配置文件

   ```xml
   <bean id="car" class="com.deemo.bean.entity.Car"/>

2. 注解

   ```java
   @Configuration
   public class BeanConfiguration {
       /**
        * 默认：方法名即为容器中的ID
        * 也可以在注解 @Bean("BYD-汉") 指定
        */
       @Bean
       public Car car() {
           Car car = new Car();
           car.setName("汉 DM-P");
           return car;
       }
   }
   ```

### 1.2 自动扫描

> 扫描：@Controller、@Service、@Component、@Repository 等注解标注的类

1. 配置文件

   ```xml
   <!-- 扫描包 -->
   <context:component-scan base-package="com.deemo.bean">
       <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
   </context:component-scan>
   ```

2. 注解

   ```java
   @Configuration
   @ComponentScan(value = "com.deemo.bean", excludeFilters = {
           // 排除 Controller
           @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Controller.class})
   })
   // @ComponentScans
   public class BeanConfiguration {
   }
   ```

3. 自定义 `TypeFilter`

   ```java
   public class DeemoTypeFilter implements TypeFilter {
       /**
        * @param metadataReader 可以用来获取 当前类 的元信息
        * @param metadataReaderFactory 可以用来获取 任意类 的元信息
        */
       @Override
       public boolean match(MetadataReader metadataReader,
                            MetadataReaderFactory metadataReaderFactory) throws IOException {
           // 当前类的注解信息
           AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
           // 当前类的类信息
           ClassMetadata classMetadata = metadataReader.getClassMetadata();
           // 当前类的类资源信息（路径、、、）
           Resource resource = metadataReader.getResource();
   
           MetadataReader metadataReader1 = metadataReaderFactory.getMetadataReader("className");
           MetadataReader metadataReader2 = metadataReaderFactory.getMetadataReader(resource);
           return false;
       }
   }
   
   // 使用方式
   @ComponentScan(value = "com.deemo.bean", excludeFilters = {
           @ComponentScan.Filter(type = FilterType.CUSTOM, classes = DeemoTypeFilter.class)
   })
   ```

注意：==配置只包含时，需要配置：`use-default-filters="false"`==

### 1.3 作用域

```java
public @interface Scope {
    /**
	 * Specifies the name of the scope to use for the annotated component/bean.
	 * <p>Defaults to an empty string ({@code ""}) which implies
	 * {@link ConfigurableBeanFactory#SCOPE_SINGLETON SCOPE_SINGLETON}.
	 * @since 4.2
	 * @see ConfigurableBeanFactory#SCOPE_PROTOTYPE
	 * @see ConfigurableBeanFactory#SCOPE_SINGLETON
	 * @see org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST
	 * @see org.springframework.web.context.WebApplicationContext#SCOPE_SESSION
	 * @see #value
	 */
	@AliasFor("value")
	String scopeName() default "";
}
```

- `ConfigurableBeanFactory#SCOPE_PROTOTYPE`：多例

  多例时，需要的时候才创建Bean对象

- `ConfigurableBeanFactory#SCOPE_SINGLETON`：单例（默认）

- `WebApplicationContext#SCOPE_REQUEST`：同一个 Request 中单例

- `WebApplicationContext#SCOPE_SESSION`：同一个 Session 中单例

### 1.4 懒加载

#### 懒加载一般场景

1. 普通懒加载

   ```java
   @Lazy
   @Bean
   public Car car() {
       Car car = new Car();
       car.setName("汉 DM-P");
       return car;
   }
   ```

   Spring 初始化阶段不会对其做处理，将会在获取Bean时被初始化。

2. 注入懒加载属性

   1. 构造方法注入

      不能懒加载，将被直接实例化，也就是懒加载失效。

   2. 属性注入

      Spring内部是使用 `cglib ` 动态代理技术实现的懒加载机制。

      ```java
      public class Car {}
      
      @Service
      public class DeemoServiceImpl {
      
          @Lazy
          @Autowired
          private Car car;
      }
      ```

      在初始化 `DeemoServiceImpl` 时，属性 `car` 将在 `BeanPostProcessor` 阶段（实际是 `InstantiationAwareBeanPostProcessor`）被注入一个 `cglib` 构造的代理对象，在通过 `DeemoServiceImpl` 获取属性 `car` 时，通过代理机制才被创建出来，其创建机制与实例化普通Bean无异。

      ```java
      // org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#doCreateBean
      populateBean(beanName, mbd, instanceWrapper);
      
      // org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#populateBean
      postProcessProperties
          
      /**
       * Post-process the given property values before the factory applies them
       * to the given bean, without any need for property descriptors.
       * <p>Implementations should return {@code null} (the default) if they provide a custom
       * {@link #postProcessPropertyValues} implementation, and {@code pvs} otherwise.
       * In a future version of this interface (with {@link #postProcessPropertyValues} removed),
       * the default implementation will return the given {@code pvs} as-is directly.
       * @param pvs the property values that the factory is about to apply (never {@code null})
       * @param bean the bean instance created, but whose properties have not yet been set
       * @param beanName the name of the bean
       * @return the actual property values to apply to the given bean (can be the passed-in
       * PropertyValues instance), or {@code null} which proceeds with the existing properties
       * but specifically continues with a call to {@link #postProcessPropertyValues}
       * (requiring initialized {@code PropertyDescriptor}s for the current bean class)
       * @throws org.springframework.beans.BeansException in case of errors
       * @since 5.1
       * @see #postProcessPropertyValues
       */
      @Nullable
      default PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName)
          throws BeansException {
          return null;
      }
      ```

   3. 属性注入的宿主类也是懒加载

      ```java
      public class Car {}
      
      @Lazy
      @Service
      public class DeemoServiceImpl {
      
          @Lazy
          @Autowired
          private Car car;
      }
      ```

      1. 在 Spring 初始化阶段**两个Bean都不会被实例化**
      2. 在获取 `DeemoServiceImpl` 对象时，将会正常创建该对象，==此时 `car` 属性任然被注入的是 `cglib` 代理对象==
      3. 在通过 `DeemoServiceImpl` 获取 `Car` 时，通过 `cglib` 代理对象创建真正的 `Car` 对象

注意：

1. 懒加载时产生的 `cglib` 代理对象即使在真正对象被创建后也不会被替换
2. ==只要是懒加载注入，就总是注入 `cglib` 代理对象，不管之前有没有其他对象已经创建过==
3. 多个Bean懒加载注入同一个类型的Bean时，==会产生多个 `cglib` 代理对象==

```java
/**
 * Default {@link AopProxyFactory} implementation, creating either a CGLIB proxy
 * or a JDK dynamic proxy.
 *
 * <p>Creates a CGLIB proxy if one the following is true for a given
 * {@link AdvisedSupport} instance:
 * <ul>
 * <li>the {@code optimize} flag is set
 * <li>the {@code proxyTargetClass} flag is set
 * <li>no proxy interfaces have been specified
 * </ul>
 *
 * <p>In general, specify {@code proxyTargetClass} to enforce a CGLIB proxy,
 * or specify one or more interfaces to use a JDK dynamic proxy.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 12.03.2004
 * @see AdvisedSupport#setOptimize
 * @see AdvisedSupport#setProxyTargetClass
 * @see AdvisedSupport#setInterfaces
 */
@SuppressWarnings("serial")
public class DefaultAopProxyFactory implements AopProxyFactory, Serializable {

	@Override
	public AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException {
		if (config.isOptimize() || config.isProxyTargetClass() || hasNoUserSuppliedProxyInterfaces(config)) {
			Class<?> targetClass = config.getTargetClass();
			if (targetClass == null) {
				throw new AopConfigException("TargetSource cannot determine target class: " +
						"Either an interface or a target is required for proxy creation.");
			}
			if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
                // JDK 代理
				return new JdkDynamicAopProxy(config);
			}
            // cglib 代理
			return new ObjenesisCglibAopProxy(config);
		}
		else {
            // JDK 代理
			return new JdkDynamicAopProxy(config);
		}
	}

	/**
	 * Determine whether the supplied {@link AdvisedSupport} has only the
	 * {@link org.springframework.aop.SpringProxy} interface specified
	 * (or no proxy interfaces specified at all).
	 */
	private boolean hasNoUserSuppliedProxyInterfaces(AdvisedSupport config) {
		Class<?>[] ifcs = config.getProxiedInterfaces();
		return (ifcs.length == 0 || (ifcs.length == 1 && SpringProxy.class.isAssignableFrom(ifcs[0])));
	}

}
```

#### 懒加载单例如何处理？

```java
// ContextAnnotationAutowireCandidateResolver#buildLazyResolutionProxy
protected Object buildLazyResolutionProxy(final DependencyDescriptor descriptor, final @Nullable String beanName) {
    Assert.state(getBeanFactory() instanceof DefaultListableBeanFactory,
                 "BeanFactory needs to be a DefaultListableBeanFactory");
    final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) getBeanFactory();
    TargetSource ts = new TargetSource() {
        @Override
        public Class<?> getTargetClass() {
            return descriptor.getDependencyType();
        }
        @Override
        public boolean isStatic() {
            return false;
        }
        @Override
        public Object getTarget() {
            // 每次获取懒加载的属性都会进入这个方法
            Object target = beanFactory.doResolveDependency(descriptor, beanName, null, null);
            if (target == null) {
                Class<?> type = getTargetClass();
                if (Map.class == type) {
                    return Collections.emptyMap();
                }
                else if (List.class == type) {
                    return Collections.emptyList();
                }
                else if (Set.class == type || Collection.class == type) {
                    return Collections.emptySet();
                }
                throw new NoSuchBeanDefinitionException(descriptor.getResolvableType(),
                                                        "Optional dependency not present for lazy injection point");
            }
            return target;
        }
        @Override
        public void releaseTarget(Object target) {
        }
    };
    ProxyFactory pf = new ProxyFactory();
    pf.setTargetSource(ts);
    Class<?> dependencyType = descriptor.getDependencyType();
    if (dependencyType.isInterface()) {
        pf.addInterface(dependencyType);
    }
    return pf.getProxy(beanFactory.getBeanClassLoader());
}

// org.springframework.beans.factory.support.DefaultListableBeanFactory#doResolveDependency
@Nullable
public Object doResolveDependency(DependencyDescriptor descriptor, @Nullable String beanName,
                                  @Nullable Set<String> autowiredBeanNames, @Nullable TypeConverter typeConverter) throws BeansException {
    // ...
    
    // 里面会尝试从缓存中获取（containsSingleton(candidateName)），获取到则放进去，获取不到就放原生的Class进去
    Map<String, Object> matchingBeans = findAutowireCandidates(beanName, type, descriptor);
    
    // ...
    
    // 如果之前放的是Class，就会进这里进去初始化
    if (instanceCandidate instanceof Class) {
        // 里面就是老面孔
        instanceCandidate = descriptor.resolveCandidate(autowiredBeanName, type, this);
    }
    
    // ...
}
```

1. **每次获取懒加载的属性都会进入 `TargetSource.getTarget` 方法**
2. **懒加载的属于一旦被实例化后，会跟普通对象一样被放在缓存中，解决单例实例化问题**
3. **每次进入 `TargetSource.getTarget` 方法也就很明确了，就是进去找是否有缓存的原对象**
4. `Object beanInstance = descriptor.resolveCandidate(candidateName, requiredType, this);` 通过此接口进入Bean实例化过程代码逻辑，一切如初

### 1.5 Condition

1. 实现自定义 `Condition` 接口

   ```java
   public class LinuxCondition implements Condition {
   
       @Override
       public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
           System.out.println(metadata.getClass());
   
           String osName = context.getEnvironment().getProperty("os.name", "");
           return osName.contains("linux");
       }
   
   }
   ```

2. 注册Bean时使用自定义 `Condition`

   ```java
   @Bean
   @Conditional({LinuxCondition.class})
   public OperationSystem linux() {
       System.out.println("create linux os...");
       OperationSystem linux = new OperationSystem();
       linux.setName("Linux");
       return linux;
   }
   
   @Bean
   @Conditional({WindowsCondition.class})
   public OperationSystem windows() {
       System.out.println("create windows os...");
       OperationSystem windows = new OperationSystem();
       windows.setName("Windows");
       return windows;
   }
   ```

### 1.6 Import

1. `@Import`

   ```java
   public @interface Import {
   	/**
   	 * {@link Configuration}, {@link ImportSelector}, {@link ImportBeanDefinitionRegistrar}
   	 * or regular component classes to import.
   	 */
   	Class<?>[] value();
   }
   
   @Configuration
   @Import({Pig4Import.class})
   public class BeanConfiguration {...}
   ```

2. `@ImportSelector`

   ```java
   public class BeanImportSelector implements ImportSelector {
       /**
        * @param importingClassMetadata annotation metadata of the importing class
        */
       @Override
       public String[] selectImports(AnnotationMetadata importingClassMetadata) {
           System.out.println(importingClassMetadata.getAnnotationTypes());
           return new String[]{Dog4Import.class.getTypeName()};
       }
   }
   
   @Configuration
   @Import({BeanImportSelector.class})
   public class BeanConfiguration {...}
   ```

3. `@ImportBeanDefinitionRegistrar`

   ```java
   public class BeanImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
       /**
        * @param importingClassMetadata annotation metadata of the importing class
        * @param registry current bean definition registry
        */
       @Override
       public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
           System.out.println(Arrays.toString(registry.getBeanDefinitionNames()));
           if (registry.containsBeanDefinition("com.deemo.bean.entity.Pig4Import") && registry.containsBeanDefinition("com.deemo.bean.entity.Dog4Import")) {
               System.out.println("register cat...");
               registry.registerBeanDefinition("cat", new RootBeanDefinition(Cat4Import.class));
           }
       }
   }
   
   @Configuration
   @Import({BeanImportBeanDefinitionRegistrar.class})
   public class BeanConfiguration {...}
   ```

### 1.7 BeanFactory

```java
public class BeanFactoryBean implements FactoryBean<Bird4Import> {
    @Override
    public Bird4Import getObject() throws Exception {
        System.out.println("factory create bird...");
        Bird4Import bird4Import = new Bird4Import();
        bird4Import.setColor("五颜六色");
        return bird4Import;
    }

    @Override
    public Class<?> getObjectType() {
        return Bird4Import.class;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}

@Configuration
public class BeanConfiguration {
    @Bean
    public BeanFactoryBean beanFactoryBean() {
        return new BeanFactoryBean();
    }
}
```

- 此时可以发现注册的Bean名称是：`beanFactoryBean`，获取这个名称的Bean对象会是谁呢？

  ```java
  @Test
  public void beanFactoryBeanTest() {
      System.out.println(this.applicationContext.getBean(BeanFactoryBean.class));
      System.out.println(this.applicationContext.getBean(Bird4Import.class));
      System.out.println(this.applicationContext.getBean("beanFactoryBean"));
      System.out.println(this.applicationContext.getBean("&beanFactoryBean"));
  }
  
  // com.deemo.bean.factory.BeanFactoryBean@5443d039
  // factory create bird...
  // Bird4Import(id=null, color=五颜六色)
  // Bird4Import(id=null, color=五颜六色)
  // com.deemo.bean.factory.BeanFactoryBean@5443d039
  ```

- 通过注册的BeanFactory名称 `beanFactoryBean` 获取到的Bean其实是 `FactoryBean.getObject()` 方法返回的对象

- 如果要获取原 `FactoryBean` 对象，需要在名称前面加 `&`：`&beanFactoryBean`

## 二、生命周期

> Bean生命周期：==Bean创建 ---- 初始化（属性值都设置之后执行） ---- 销毁== 的过程。
>
> 容器管理Bean生命周期：可以自定义初始化和销毁方法，容器在Bean的各个阶段会来调用自定义的初始化和销毁方法。

### 2.1 初始化和销毁

==初始化是在所有属性值设置之后执行。==

#### 2.1.1 方式一

`initMethod, destroyMethod`

1. 配置文件

   指定Bean的 `init-method` 和 `destory-method`

2. 注解

   ```java
   @Bean(initMethod = "init", destroyMethod = "destroy")
   ```

注意：==单例会调用 `destroyMethod`，多例时，不会进行 `destroyMethod`==

#### 2.1.2 方式二

`InitializingBean, DisposableBean`

```java
public class Cat4LifeCycle implements InitializingBean, DisposableBean {
    private Long id;
    private String name;
    private String brand;

    /**
     * Invoked by the containing BeanFactory after it has set all bean properties and satisfied BeanFactoryAware, ApplicationContextAware etc.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("cat afterPropertiesSet...");
    }

    @Override
    public void destroy() {
        System.out.println("cat destroy...");
    }

}
```

#### 2.1.3 方式三

JSR250标准：`@PostConstruct, @PreDestroy`，==底层是使用 `CommonAnnotationBeanPostProcessor` 执行==。

```java
public class Dog4LifeCycle {
    private Long id;
    private String name;
    private String brand;

    @PostConstruct
    public void init() {
        System.out.println("dog init...");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("dog destroy...");
    }

}
```

#### 2.1.4 方式四

实现 `BeanPostProcessor` 接口。

```java
public class DeemoBeanPostProcessor implements BeanPostProcessor {

    /**
     * Apply this BeanPostProcessor to the given new bean instance
     * before any bean initialization callbacks (like InitializingBean's afterPropertiesSet or a custom init-method).
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization...");
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    /**
     * Apply this BeanPostProcessor to the given new bean instance
     * after any bean initialization callbacks (like InitializingBean's afterPropertiesSet or a custom init-method).
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization...");
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

}
```

### 2.2 初始化和销毁底层实现

> 三种方式调用时机没有差别，仅调用方式的区别而已。

1. ==在 Spring 的 `finishBeanFactoryInitialization(beanFactory);` 阶段执行==

   ```java
   // Instantiate all remaining (non-lazy-init) singletons.
   finishBeanFactoryInitialization(beanFactory);
   ```

2. 均是在 `org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#doCreateBean` 中执行

3. 在 `populateBean` 之后的 `initializeBean` 中执行

   ```java
   // org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#doCreateBean
   protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final Object[] args)
       throws BeanCreationException {
       // ...
       
       // 设置属性值
       populateBean(beanName, mbd, instanceWrapper);
       // 在这里面执行 init 初始化方法
       exposedObject = initializeBean(beanName, exposedObject, mbd);
       
       // ...
   }
   
   // org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#initializeBean(java.lang.String, java.lang.Object, org.springframework.beans.factory.support.RootBeanDefinition)
   protected Object initializeBean(final String beanName, final Object bean, RootBeanDefinition mbd) {
       Object wrappedBean = bean;
       if (mbd == null || !mbd.isSynthetic()) {
           // 方式三、四 @PostConstruct, BeanPostProcessor
           wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
       }
       
       try {
           // 方式一、二 initMethod
           invokeInitMethods(beanName, wrappedBean, mbd);
       }
       
       if (mbd == null || !mbd.isSynthetic()) {
           // 方式四 BeanPostProcessor
           wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
       }
   }
   
   // 方式三、四 @PostConstruct, BeanPostProcessor
   // org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInitialization
   @Override
   public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
       throws BeansException {
   
       Object result = existingBean;
       for (BeanPostProcessor processor : getBeanPostProcessors()) {
           // CommonAnnotationBeanPostProcessor 执行 @PostConstruct, @PreDestroy
           Object current = processor.postProcessBeforeInitialization(result, beanName);
           if (current == null) {
               return result;
           }
           result = current;
       }
       return result;
   }
   
   // 方式一、二 initMethod
   // org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#invokeInitMethods
   protected void invokeInitMethods(String beanName, final Object bean, RootBeanDefinition mbd)
       throws Throwable {
   
       boolean isInitializingBean = (bean instanceof InitializingBean);
       if (isInitializingBean && (mbd == null || !mbd.isExternallyManagedInitMethod("afterPropertiesSet"))) {
           // 执行 afterPropertiesSet
           ((InitializingBean) bean).afterPropertiesSet();
       }
   
       if (mbd != null && bean.getClass() != NullBean.class) {
           String initMethodName = mbd.getInitMethodName();
           if (StringUtils.hasLength(initMethodName) &&
               !(isInitializingBean && "afterPropertiesSet".equals(initMethodName)) &&
               !mbd.isExternallyManagedInitMethod(initMethodName)) {
               // 执行 initMethod
               invokeCustomInitMethod(beanName, bean, mbd);
           }
       }
   }
   
   // 方式四 BeanPostProcessor
   // org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsAfterInitialization
   @Override
   public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
       throws BeansException {
   
       Object result = existingBean;
       for (BeanPostProcessor processor : getBeanPostProcessors()) {
           // 执行 postProcessAfterInitialization
           Object current = processor.postProcessAfterInitialization(result, beanName);
           if (current == null) {
               return result;
           }
           result = current;
       }
       return result;
   }
   ```

   1. ==方式三其实就是内置的 `BeanPostProcessor: CommonAnnotationBeanPostProcessor`==，所以也就是方式四接口的一种实现

      `CommonAnnotationBeanPostProcessor` 继承于 `InitDestroyAnnotationBeanPostProcessor`，其实是由 `InitDestroyAnnotationBeanPostProcessor` 来实现的

   2. `BeanPostProcessor` 的 `postProcessBeforeInitialization` 和 `postProcessAfterInitialization` 在每个Bean的生命周期中都会被调用

### 2.3 `BeanPostProcessor` 应用

> ==最熟悉、最常见、使用最多的：`ApplicationContextAware`：为一个Bean对象设置IOC容器。==
>
> 类似的 `BeanPostProcessor` 应用还有 `BeanValidationPostProcessor` 校验、`AutowiredAnnotationBeanPostProcessor` 处理属性自动注入的等等等等。
>
> 还有其他生命周期注解：`@Async` 等等相关的实现。

- `ApplicationContextAware`

  ```java
  public interface ApplicationContextAware extends Aware {
  
  	/**
  	 * Set the ApplicationContext that this object runs in.
  	 * Normally this call will be used to initialize the object.
  	 * <p>Invoked after population of normal bean properties but before an init callback such
  	 * as {@link org.springframework.beans.factory.InitializingBean#afterPropertiesSet()}
  	 * or a custom init-method. Invoked after {@link ResourceLoaderAware#setResourceLoader},
  	 * {@link ApplicationEventPublisherAware#setApplicationEventPublisher} and
  	 * {@link MessageSourceAware}, if applicable.
  	 * @param applicationContext the ApplicationContext object to be used by this object
  	 * @throws ApplicationContextException in case of context initialization errors
  	 * @throws BeansException if thrown by application context methods
  	 * @see org.springframework.beans.factory.BeanInitializationException
  	 */
  	void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
  
  }
  ```

- 该接口生效依赖于：`ApplicationContextAwareProcessor`

  ```java
  // 是一个 BeanPostProcessor
  class ApplicationContextAwareProcessor implements BeanPostProcessor {
      @Override
  	@Nullable
  	public Object postProcessBeforeInitialization(final Object bean, String beanName) throws BeansException {
          invokeAwareInterfaces(bean);
      }
      
      private void invokeAwareInterfaces(Object bean) {
  		if (bean instanceof Aware) {
  			if (bean instanceof EnvironmentAware) {
  				((EnvironmentAware) bean).setEnvironment(this.applicationContext.getEnvironment());
  			}
  			if (bean instanceof EmbeddedValueResolverAware) {
  				((EmbeddedValueResolverAware) bean).setEmbeddedValueResolver(this.embeddedValueResolver);
  			}
  			if (bean instanceof ResourceLoaderAware) {
  				((ResourceLoaderAware) bean).setResourceLoader(this.applicationContext);
  			}
  			if (bean instanceof ApplicationEventPublisherAware) {
  				((ApplicationEventPublisherAware) bean).setApplicationEventPublisher(this.applicationContext);
  			}
  			if (bean instanceof MessageSourceAware) {
  				((MessageSourceAware) bean).setMessageSource(this.applicationContext);
  			}
              // 这里便是注入 IOC容器给其他类
  			if (bean instanceof ApplicationContextAware) {
  				((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
  			}
  		}
  	}
  }
  ```

  - ==可以看到，其他 `XxxxAware` 都可以这样操作。==

  - `ApplicationContextAwareProcessor` 是一个==缺省描述符==的类，只能被同包引用！

  - ==被同包下容器主类 `AbstractApplicationContext` 添加使用==

    ```java
    // org.springframework.context.support.AbstractApplicationContext#prepareBeanFactory
    /**
     * Configure the factory's standard context characteristics,
     * such as the context's ClassLoader and post-processors.
     * @param beanFactory the BeanFactory to configure
     */
    protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
    }
    ```

## 三、自动装配

### 3.1 @Autowired

可以配置 `required = false`，可使用`@Qualifier`、`@Primary` 等明确需要装配的Bean。

当容器中有多个相同类型的Bean时，默认会**优先注入匹配相同属性名称的Bean**。

### 3.2 @Resource & @Inject

1. `@Resource`：JSR250

   Java规范提供的注解，相比 `@Autowired` 不能响应 `@Primary` 等Spring支持的功能。

2. `@Inject`：JSR330

   需要引入依赖：

   ```xml
   <dependency>
       <groupId>javax.inject</groupId>
       <artifactId>javax.inject</artifactId>
       <version>1</version>
   </dependency>
   ```

   和 `@Autowired` 功能，一样，但 `@Inject` 不能配置 `required`。

### 3.3 构造器注入

1. 当只有一个构造器方法时，可以省略 `@Autowired` 注解，将默认使用该构造器方法
2. 当有多个构造器方法时
   1. 默认使用无参构造器方法
   2. 可以使用 `@Autowired` 标注指定的构造器方法
   3. 否则在初始化Bean时，抛出异常：`java.lang.NoSuchMethodException: XxxxBean.<init>()`

### 3.4 @Profile

`@Profile` 标注在 `@Bean` 或上 `@Configuration` 上或其他位置，指定在指定的环境下才加入到容器中，默认是：`default`，没有标注环境的代表任意环境都加入到容器中。

可使用命令参数：`-Dspring.prifiles.active="profile1,profile2"` 指定环境。

```java
@Profile("dev")
@Bean
public Car car() {...}

@Profile("test")
@Bean
public Car car() {...}
```

