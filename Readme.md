### Dyna Filter

Dynamic Object filtering for Json serialization.

This library provides a non-intrusive way to dynamically filter the desired fields to be passed for a serialization mechanism.

Although designed to be used with Json, other serialization mechanism (e.g. binary) could also make use of the filter.

#### How it works?

Use the **@DynaFilter** annotation to filter out the fields of interest of any given data type and applied to the usual request handler method:

```java
@RequestMapping(value = "collection", method = GET, produces = "application/json")
@DynaFilter(value = Data.class, fields = { "name", "description" }, includeNulls = true)
public @ResponseBody List<Data> returnCollection() {
    // ...
}
```

The annotation get's processed by a spring handler when necessary (similar in concept to a interceptor).
 
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

For reusability use **@NamedDynaFilters** annotation:

```java
@RequestMapping(value = "named", method = GET, produces = "application/json")
@NamedDynaFilters(value = { "userAge", "addressOnly" })
public @ResponseBody List<Object> shouldUseNamedFitlers() {
    // ...
}
```

and configure the named filters in the respective factory:

```xml
    <bean class="org.dynafilter.DynaFilterFactory">
        <property name="namedFilters">
            <list>
                <bean class="org.dynafilter.NamedDynaFilter">
                    <constructor-arg value="userAge" /> // Filter name
                    <constructor-arg value="org.dynafilter.controller.User" /> // Type
                    <constructor-arg value="true" /> // Include nulls
                    <constructor-arg value="id,age" /> // Fields
                </bean>
                <bean class="org.dynafilter.NamedDynaFilter">
                    <constructor-arg value="addressOnly" /> // Filter name
                    <constructor-arg value="org.dynafilter.controller.Address" /> // Type
                    <constructor-arg value="id,address" /> // Field
                </bean>
            </list>
        </property>
    </bean>
```

#### Lookup strategy

When looking for the configured fields, the lookup strategy is the following:

1. Fields/attributes/properties
2. Access methods (Camel cased methods that start with *get*) [1]
3. Exact method (methods with the exact same name) [1]  

[1] Methods must have no arguments 


#### Usage

Add the DynaFilterFactory to your Spring MVC Context configuration xml or bean:

```xml
    <bean class="org.dynafilter.DynaFilterFactory" />
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

And annotate the controller or any other bean with the one or more filter annotation as shown in the "How it works?" section above.
Make sure you look into the provided test controller and spring context for a working example.

#### Build project and run tests

    mvn clean install

.

#### Manual test execution

	cd web-test
	mvn jetty:run
    
Then point your browser to:

1. [Simple instance with primitive types](http://localhost:8080/jsonfilter/simple)
2. [Collection of simple instances](http://localhost:8080/jsonfilter/collection)
3. [Collection of mixed simples instances](http://localhost:8080/jsonfilter/hybrid)
4. [Graph of complex instance](http://localhost:8080/jsonfilter/composite)
5. [Fully filtered object](http://localhost:8080/jsonfilter/empty)
6. [Fully filtered collection](http://localhost:8080/jsonfilter/emptyarray)
7. [Include nulls](http://localhost:8080/jsonfilter/includenulls)
8. [Named filters](http://localhost:8080/jsonfilter/named)
9. [Method access](http://localhost:8080/jsonfilter/accessors)
