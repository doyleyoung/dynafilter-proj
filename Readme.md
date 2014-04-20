### Dyna Filter

Dynamic Object filtering for Json serialization.

This library provides a non-intrusive way to dynamically filter fields for serialization.

Although designed to be used with JSON, any other serialization mechanism (e.g. binary) could also make use of the filter.

#### Current Release

```xml
<dependency>
	<groupId>com.github.bmsantos</groupId>
	<artifactId>dynafilter</artifactId>
	<version>1.0.0</version>
</dependency>
```

#### How does it work?

The **@DynaFilter** annotation is used to filter out the fields of interest from any data type and works in conjunction with the usual request handler method:

```java
@RequestMapping(value = "collection", method = GET, produces = "application/json")
@DynaFilter(value = Data.class, fields = { "name", "description" }, includeNulls = true)
public @ResponseBody List<Data> returnCollection() {
    // ...
}
```

When necessary, the annotation is processed by a Spring handler. This is similar to the mechanism use by an interceptor.
 
To filter multiple data types use **@DynaFilters** to wrap multiple **@DynaFilter** annotations.

```java
@RequestMapping(value = "hybrid", method = GET, produces = "application/json")
@DynaFilters({
    @DynaFilter(value = User.class, fields = "name"),
    @DynaFilter(value = Address.class, fields = "id")
})
public @ResponseBody List<Object> returnHybrid() {
    // ...
}
```

To use the same filter multiple times, use the **@NamedDynaFilters** annotation:

```java
@RequestMapping(value = "named", method = GET, produces = "application/json")
@NamedDynaFilters(value = { "userAge", "addressOnly" })
public @ResponseBody List<Object> shouldUseNamedFitlers() {
    // ...
}
```

and configure the named filters in the respective factory:

```xml
    <bean class="com.github.bmsantos.dynafilter.DynaFilterFactory">
        <property name="namedFilters">
            <list>
                <bean class="com.github.bmsantos.dynafilter.NamedDynaFilter">
                    <constructor-arg value="userAge" /> // Filter name
                    <constructor-arg value="com.github.bmsantos.dynafilter.controller.User" /> // Type
                    <constructor-arg value="true" /> // Include nulls
                    <constructor-arg value="id,age" /> // Fields
                </bean>
                <bean class="com.github.bmsantos.dynafilter.NamedDynaFilter">
                    <constructor-arg value="addressOnly" /> // Filter name
                    <constructor-arg value="com.github.bmsantos.dynafilter.controller.Address" /> // Type
                    <constructor-arg value="id,address" /> // Field
                </bean>
            </list>
        </property>
    </bean>
```

#### Lookup strategy

When looking for fields listed in the annotation, the following lookup strategy is used:

1. Fields/attributes/properties
2. Access methods (Camel cased methods that start with *get*) [1]
3. Exact method (methods with the exact same name) [1]  

[1] Methods must have no arguments 


#### Usage

Add the DynaFilterFactory to your Spring MVC Context configuration XML or bean:

```xml
    <bean class="com.github.bmsantos.dynafilter.DynaFilterFactory" />
```

or

```java
@Configuration
public class AppConfig {

    @Bean
    public DynaFilterFactory DynaFilterFactory() {
        return new DynaFilterFactory();
    }

}
```

Annotate the controller or any other bean with one or more filter annotations as shown in the "How does it work?" section above.
The provided test controller and Spring context files demonstrate how easy it is with a working example.

#### Build project and run tests

    mvn clean install

#### Manual test execution

	cd web-test
	mvn jetty:run
    
Then use the following links to see examples of:

1. [Simple instance with primitive types](http://localhost:8080/jsonfilter/simple)
2. [Collection of simple instances](http://localhost:8080/jsonfilter/collection)
3. [Collection of mixed simples instances](http://localhost:8080/jsonfilter/hybrid)
4. [Graph of complex instance](http://localhost:8080/jsonfilter/composite)
5. [Fully filtered object](http://localhost:8080/jsonfilter/empty)
6. [Fully filtered collection](http://localhost:8080/jsonfilter/emptyarray)
7. [Include nulls](http://localhost:8080/jsonfilter/includenulls)
8. [Named filters](http://localhost:8080/jsonfilter/named)
9. [Method access](http://localhost:8080/jsonfilter/accessors)
