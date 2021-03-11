# Spring-file-manipulator


General idea is to learn ***spring boot*** framework. 
* The business "logic" should be hidden behind **REST API**. 
  Therefore, UI and model should be decoupled. 
  Allowing for any kind of UI framework to depend only on REST endpoints not on a java code.
  
* Learn ***spring data*** (jpa, hibernate)... Try to be "very friendly" data persistent. 
  Store data even if it doesn't make much sense.


# Application concept


Spring file manipulator. *Spring* part meaning: developed using spring (boot) framework. 
*File manipulator* part meaning: the app manipulates files on a user's file system.

* As an **source** there is any kind of folder (directory) hierarchy structure
  containing collections of other folders and files.
Let's imagine hierarchy of audio songs (.mp3 files) categorized into folders by an album.
  
* The app processes this hierarchy (of songs) and moves (or copies, or ...) each individual file into another **destination** folder
  regardless of the file's position in the hierarchy.
In other words creating one destination folder containing all files from source folder hierarchy.
  
* The app should **store history** of any performed file operation. Allowing for a UI to retrieve it at a later date.
  
User should be allowed:

* To **select** only **some subset** of files from the source hierarchy. E.g. using some kind of **regular expression** or path wildcards such as *\*.mp3*. Therefore, ignoring any other file than mp3 files. 

* To **modify** destination **file names** using *predefined operation*. 
Let's imagine removing all kinds of whitespace characters from filenames OR replacing them with an underscore (_) character...


# Terminology (Task*, Job, Operation)


## Job

Specific action, piece of code, implementing interface Job is called Job.

```java
public interface Job {
    /**
     * Execute job synchronously. Returns something
     */
    Object start();

    /**
     * Signal to a running job to stop gracefully.
     * It is just "pretty please stop as soon as you want".
     * May actually stop a several "days" later...
     */
    void stop();
}
```

This application implements only one Job called [***TaskJobImpl***](#taskjobimpl)


## TaskJobImpl

The implementation of Job interface. 
- Performs any and all file operations on the users file system.
- Takes all required user parameters (see [***Task***](#task)).
- Signals it's computing progress using spring events ***ApplicationEventPublisher***

## Task

There is no class/object called ***Task*** in this application.
This is just an idea.
There are, however, many classes starting with a name prefix ***Task***.
Such as: 
 - TaskEntity (C), TaskService (I), TaskController (C)
 - TaskStatusType (E), TaskStatusService (I)
 - TaskScheduler (I)
 - TaskEvent (I)
 - TaskStatusServiceEntity (C)

The application manipulates user's file system. 
The user configures application parameters and executes *action* called: 
 - [***Job***](#job) action implementing Job interface, which is a specific / concrete part of a more general idea called:
 - [***Task***](#task) abstract naming of all what is happening underhood of this application after the user configures and executes this application.


## Operation

In the previous mentioned scenario, the user must configure the following parameters:

- Source folder containing collection of mp3 audio files.
- Destination folder for the output to be placed to.
- ***String operation*** to be performed on any processed filename.
- ***File operation*** to be performed on any processed file.

There are two kinds of operation:

 - ***String operation***. Those operations are applied on file names during File operations.
   - removing whitespaces
   - replacing whitespaces with underscore
   - squeezing consecutive characters
   - ...
 - ***File operation***. 
   - copy, move, delete files, ...


## TaskEntity

Spring entity pojo object representing ***Task***.
Basically one pojo contains all task-persistent data.


## TaskService

Spring service layer for ***Task***
Provides access to all task relevant data.


## TaskStatus

There actually is only one Task and that is [***TaskJobImpl***](#taskjobimpl).
Therefore Task could be in some of predefined states (=statuses). 
Those statuses are defined in Enum class called ***TaskStatusType***.

```java
public enum TaskStatusType {
    CREATED,
    SCHEDULED,
    RUNNING,
    FINISHED_CANCELLED,
    FINISHED_OK,
    FINISHED_PARTIALLY_ERROR,
    FINISHED_ERROR
}
```

This status (***TaskStatusesType***) is directly contained in [***TaskEntity***](#taskentity) as a string and therefore also into DB.

***TaskStatusService*** provides access to predefined TaskStatus type information.
Allowing for easy access to all defined statuses without needing to use  taskStatusesType enum directly.

***TaskStatusServiceEntity*** This one has been implemented just for the heck of it. Allowing for internalization use in TaskStatus. Basically inside the DB the predefined unique TaskStatusType type is stored, but the user uses and sees only internalized string.


## Summary of Tasks

The Task is idea represented performed by the implementation of the ***Job*** interface ***TaskJobImpl***.
Any persistent task data are stored in database and represented by the spring entity ***TaskEntity***.
The spring controller layer can access those data using the spring service layer, specifically: ***TaskService*** and/or ***TaskStatusService***.

The basic prerequisite should be to ensure Job must not be dependent on its implementations.
Meaning Job database should not be bidirectional. JobEntity should not know anything about TaskEntity



# Developer points


## UML diagrams

There are some very simplified uml diagrams in ***resource*** folder.
Diagrams were created using PlantUML language...


## Spring Boot Error handling


**step 1. (deprecated) Disable whitelabel page**

**!!(this section is deprecated skip to step 3)**

Spring boot uses default "WhiteLabel" error page.
It's a generic error page that is displayed when no custom controller page mapping exists.
Default mapping is "/error".

This default behaviour is disabled to acquire total control over error mapping of this application. There are several steps to achieve it.

 - Modify **application.yaml** properties file by adding the following:
```yaml
server:
  error:
    whitelabel:
      enabled: false
```

 - Starting Spring boot application without ErrorMvcAutoConfiguration.class:
```java
// disable default spring whitelabel page /error basicErrorController
@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class SpringFileManipulatorApplication {

    public static void main(String[] args) {
        // ...
    }
}
```

**step 2. Change default JSON response format**

**!!(this section is deprecated skip to step 3)**

This controller should handle all requests that does NOT have any known request mapping. 
In other words the Spring does not know any controller to route request to.

Spring boot auto-generates general JSON error response in the following format:
```json
{
  "timestamp": "2021-01-25T16:56:45.760+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "No message available",
  "path": "/api/file_regex_predefined_categories/2x"
}
```

Let's change it to app's own general format:
```json
{
  "timestamp" : "2021-01-25T18:04:53.1654393",
  "status_code" : 404,
  "status_error" : "Not Found",
  "error_message" : "Most likely the requested api endpoint: /api/file_regex_predefined_categories/2x is unknown...",
  "error_message_detail" : null,
  "api_path" : "/api/file_regex_predefined_categories/2x"
}
```

Override BasicErrorAttributes:
```java
@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

  @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
      final Map<String, Object> defaultErrorAttributes = super.getErrorAttributes(webRequest, options);
      RestApiError restApiError = RestApiError.fromDefaultAttributeMap(defaultErrorAttributes);
      return restApiError.toAttributeMap();
  }
}
```

Where **RestApiError** class is our custom "POJO" class.
RestApiError class is able to transform BasicErrorAttributes into custom format
and produce it's own key-value (JSON) output.


**step 3. Exception handling**

If an exception/error occur anywhere in the code,
the Spring searches for a relevant error handler.

1. The Spring first look for the @ControllerAdvice annotated class.
   (Methods within this class annotated @ExceptionHandler) are used
   for exception resolution.
   
   This application uses one such class:

    ```java
    @RestControllerAdvice
    public class RestExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
   
        @ExceptionHandler(ItemNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public RestApiError handleEntityNotFoundException() {
            //...
        }

    }
    ```
   
2. In case no related @ExceptionHandler method is found,
   the global ErrorContro;ler implementation is called
   to handle this unexpected situation.
   
   Spring provides general BasicErrorController implementation.
   However if any custom class is implementing ErrorController interface
   it takes higher precedence...

Spring boot auto-generates default BasicErrorController. 
This controller redirects all unknown requests to /error url. 
This url shows general WhiteLabel page with response HTTP code, and some basic error message.

Simply replace Spring Boot implementation with custom one by extending AbstractErrorController.

```java
@RestController
@RequestMapping(path = "${server.error.path:/error}", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomErrorController extends AbstractErrorController {
    // should take advantage of RestApiError class...
}
```

## Automatically obtain all mappings

**1. (!!deprecated) Enable Spring Boot Actuator by adding started dependency into pom maven file.**

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

**2. Use native RequestMappingHandlerMapping**

Using this we can obtain methods and patterns etc.


## Let's describe app's workflow

1. User configure relevant parameters and executes the app.
2. Parameters are validated.
    1. Bean Validation *syntax* using @Pattern
    2. Custom Spring data REST validation *semantic* using BeforeCreateEvent
3. TaskEntity is created from Parameters (Task's data pojo representation, to be stored into DB)
4. Relevant Job (TaskJob) is created, scheduled and executed in background as soon as possible.
    1. TaskJob reports its work progress using Spring events (ApplicationEventPublisher)
    2. TaskJob does NOT store any data into DB. This is performed in event handlers. 
       TaskJob just provides relevant data in form of its representation as TaskEntity.
5. TaskEntity is returned to user JSONified.

Now user and/or application can use REST endpoints to get TaskJob status / results. 


## Internalization

Internalization support using *Spring messages*.
Used just to try internationalization mechanisms.
Implemented only during new Task creation. Try calling the following endpoint with an unexpected JSON body payload.
It should return

```json
POST /api/tasks/createNewTask?lang=cs_CZ
{
  "source_folder": "string",
  "destination_folder": "string",
  "syntax_and_pattern": "string",
  "file_operation": "string"
}
```

Response similar to the following.
```json
{
  "timestamp": "2021-01-28T17:13:13.6430498",
  "status_code": 400,
  "status_error": "Bad Request",
  "error_message": "Validation failed. Cannot proceed.",
  "error_message_detail": "Encountered errors count: 1\r\nsyntax_and_pattern musí být ve formátu syntax:pattern",
  "api_path": ""
}
```

It's possible to use url parameter *lang=cs_CZ*. 
```
http://server:port/api/endpoint?lang=cs_CZ
```


## Task/Job scheduling

The app internally uses ***ThreadPoolTaskScheduler*** with ***CompletableFuture***.
The ThreadPoolTaskScheduler is configured to number of Threads using:
```java
Runtime.getRuntime().availableProcessors()
```

The scheduled job API is represented by relevant Repository, Entity and Service.
Just scheduled job *ID* and job *status* is stored.

Mapping between ***Task*** and Scheduled ***Job*** is ensured by mapping DB table.


**!deprecated**
Any and all scheduling relevant data is stored *in an app memory*. 
No persistent data!
If any job is running (or has just been scheduled), and the app is forcefully terminated, NO job CAN be restored and resumed.
Must be properly configured and executed again.


## Weird thing with OneToOne mapping using JoinTable

***TaskEntity*** and ***ScheduledJobEntity*** are mapped using JoinTable.
They could more easily be joined using Foreign Keys but let's try...

> I could not make it done! It just throws NullPointer Exception.

<pre>
Caused by: java.lang.NullPointerException: null
	at org.hibernate.mapping.Column.getSqlTypeCode(Column.java:198) ~[hibernate-core-5.4.25.Final.jar:5.4.25.Final]
	at org.hibernate.hql.spi.id.AbstractMultiTableBulkIdStrategyImpl.buildIdTableCreateStatement(AbstractMultiTableBulkIdStrategyImpl.java:146) ~[hibernate-core-5.4.25.Final.jar:5.4.25.Final]
	<b>at org.hibernate.hql.spi.id.persistent.PersistentTableBulkIdStrategy.buildIdTableInfo(PersistentTableBulkIdStrategy.java:130)</b> ~[hibernate-core-5.4.25.Final.jar:5.4.25.Final]
	at org.hibernate.hql.spi.id.persistent.PersistentTableBulkIdStrategy.buildIdTableInfo(PersistentTableBulkIdStrategy.java:43) ~[hibernate-core-5.4.25.Final.jar:5.4.25.Final]
	at org.hibernate.hql.spi.id.AbstractMultiTableBulkIdStrategyImpl.prepare(AbstractMultiTableBulkIdStrategyImpl.java:84) ~[hibernate-core-5.4.25.Final.jar:5.4.25.Final]
	at org.hibernate.internal.SessionFactoryImpl.<init>(SessionFactoryImpl.java:309) ~[hibernate-core-5.4.25.Final.jar:5.4.25.Final]
	at org.hibernate.boot.internal.SessionFactoryBuilderImpl.build(SessionFactoryBuilderImpl.java:469) ~[hibernate-core-5.4.25.Final.jar:5.4.25.Final]
	at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilderImpl.java:1259) ~[hibernate-core-5.4.25.Final.jar:5.4.25.Final]
	at org.springframework.orm.jpa.vendor.SpringHibernateJpaPersistenceProvider.createContainerEntityManagerFactory(SpringHibernateJpaPersistenceProvider.java:58) ~[spring-orm-5.3.2.jar:5.3.2]
	at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFactory(LocalContainerEntityManagerFactoryBean.java:365) ~[spring-orm-5.3.2.jar:5.3.2]
	at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:409) ~[spring-orm-5.3.2.jar:5.3.2]
	... 26 common frames omitted
</pre>

Studying exception it has something to do with PersistentTableBulkIdStrategy.
Upgrading to spring boot from 2.4.1 to 2.4.3 solves the issue of NullPointerException, BUT, 
Some tables prefixed HT_* are automatically created by Hibernate. 
Those are *temporary* tables created during bulk operations.

See [batch-bulk-strategies](https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#batch-bulk-hql-strategies) on Jboss.org

Seems to point to changing configuration properties. 
There are 4 options. 
It seems *InlineIdsOrClauseBulkIdStrategy* is the most supported strategy by major DB systems.

```
application.properties:

spring.jpa.hibernate.hql.bulk_id_strategy=org.hibernate.hql.spi.id.inline.InlineIdsInClauseBulkIdStrategy
```

> This issue seems to be resolved by updating Hibernate SqliteDialect library from 0.1.0 to 0.1.2
> This update resolves both curious UNIQUE contraint on create-sql issue AND
> Bulk ID Strategy compatibility...
> See [History of gwenn/sqlite-dialect](https://github.com/gwenn/sqlite-dialect/commits/master/src/main/java/org/sqlite/hibernate/dialect/SQLiteDialect.java)

## Spring boot Data Repositories

See [spring-data-repositories](https://www.baeldung.com/spring-data-repositories) on Baeldung.com

We can let Spring Boot to automatically generate basic database CRUD operations for us.
This is done by simply extending one of three possible Repository interfaces (see above).

```java
import org.springframework.data.repository.CrudRepository;

interface MyCustomRepository extends CrudRepository<MyCustomEntity, Long> {
}
```

Basic CRUD operations returns works either with *null* or *Optional<>* if no item is found.
I wanted to throw exception in this case. To workaround this I chose to add custom methods
into Spring generated CRUD ones.

```java
Optional<TaskEntity> findById(Integer id);
```
additional custom repository method:
```java
TaskEntity findByIdIfNotFoundThrow(Integer id) throws TaskNotFoundException;
```

To add customized methods to automatically generated CRUD ones, the following steps are necessary:
Official documentation see: [Custom Implementations for Spring Data Repositories](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.custom-implementations)

```java
interface UserRepository extends CrudRepository<User, Long>, CustomizedUserRepository {
    // Declare query methods here
}

interface CustomizedUserRepository {
    void someCustomMethod(User user);
}

class CustomizedUserRepositoryImpl implements CustomizedUserRepository {
    public void someCustomMethod(User user) {
        // Your custom implementation
    }
}
```

There is a slight caveat to the above implementation.

What if you want to use automatically generated CRUD methods in your own custom method.
You have to autowire the Repository, that you are customizing (adding your custom method to).

This came out as a circular dependency. You simply cannot use constructor autowiring and go with it.
The Field or the Setter injection should help, but in reality I cannot get it to work.
My expectations were that the field injection should be postponed to later date and therefore it should work.

Trying a constructor and/or field injection results in the following exception:

> Error creating bean with name 'taskRepositoryImpl': Bean with name 'taskRepositoryImpl' has been injected into other beans [taskRepository]
> in its raw version as part of a circular reference, but has eventually been wrapped.
> This means that said other beans do not use the final version of the bean.
> This is often the result of over-eager type matching - consider using 'getBeanNamesForType' with the 'allowEagerInit' flag turned off, for example.

### @Lazy annotation
This eager stuff led me to stumple upon ***@Lazy*** Spring annotation.
See [Lazy annotation](https://www.baeldung.com/spring-lazy-annotation)

Spring uses a proxy instead of the real object at the injection point. 
This proxy delays the initialization of the underlying object until it is first used.

The final working simplified version. See ***@Lazy*** annotation in the constructor:

```java
@Lazy
@Repository
interface TaskRepository extends TaskRepositoryCustomMethods {
}

interface TaskRepositoryCustomMethods {
    // custom method declaration
}

@Repository
class TaskRepositoryCustomMethodsImpl implements TaskRepositoryCustomMethods {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskRepositoryCustomMethodsImpl(@Lazy TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // custom methods definition
}
```

> There is a one little thing to consider when using @Lazy inicialization.
> This can hide any future potential app problems.
> You won't get any potential related error message immediately when application starts...


## Json serializer / deserializer

```java
com.fasterxml.jackson.databind.exc.InvalidDefinitionException: 
No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor 
and no properties discovered to create BeanSerializer 
(to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) 
(through reference chain: java.util.ArrayList[3]->spring.filemanipulator.entity.TaskEntity["job_entity"]
->spring.filemanipulator.entity.JobEntity$HibernateProxy$NYn8bxTW["hibernate_lazy_initializer"])
```

Basically this means the following:
1. There is LAZYly loaded property.
2. We are trying to serialize this Entity, but the Entity
is not yet fully populated with data from DB -> LAZY
   
Instruct spring to ignore those errors by adding to application.properties:
>fail-on-empty-beans: false

```json
"job_entity": {
    "id": 1,
    "job_status_unique_name_id": "SCHEDULED_RUNNING",
    "hibernate_lazy_initializer": {}
}
```

To get rid of hibernate_lazy_initializer property, use special annotaiton:

>@JsonIgnoreProperties({"hibernate_lazy_initializer"}) 


## Spring events - Observer pattern

***ApplicationEventPublisher*** allow an Object to publish events.

1. Let's define a custom event object containing all relevant data for all custom events.
```java
public class CustomEvent {
    public enum EventType {
        EVENT_TYPE_1,
        EVENT_TYPE_2
    }
    
    private EventType eventType;
    private CustomObject1 customObject1;
    // ... others
    
    public CustomEvent(EventType eventType) {
        // ...
    }
    // ... other custructors, setters and getters
}
```

2. We use ***eventPublisher*** in Spring like this:
```java
// ...
ApplicationEventPublisher eventPublisher;
// ...
CustomEvent event = new CustomEvent(EventType.EVENT_TYPE_1);
event.setXXX();
eventPublisher.publishEvent(event);
// ..
```

3. To listen to custom events we need to implement event listener.
This is achieved just by adding
   - ***@EventListener*** annotation to method
   - Proper method parameter ***CustomEvent***
```java
public class CustomEventListener {

    @EventListener
    public handleCustomEvents(CustomEvent event) {
        EventType type = event.getEventType();
        // ...
    }
}
```

4. If we want to handle events asynchronously...
   (No incoming event handling ORDER). Event1 may come
   first, but it's handling may finished the last.
```java
public class CustomEventListener {

    @Async
    @EventListener
    public handleCustomEvents(CustomEvent event) {
        EventType type = event.getEventType();
        // ...
    }
}
```

5. We have async event handling, but only in ONE background thread.
Effectively achieving one-by-one event handling.
```java
public class CustomEventListener {
    @Bean(name = "customObjectListenerAsync")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1); // queuing all event handling
        executor.setThreadNamePrefix("customEventListener-");
        executor.initialize();
        log.debug("taskListener: corePoolSize-{}, maxPoolSize-{}",
                executor.getCorePoolSize(),
                executor.getMaxPoolSize());
        return executor;
    }

    @Async("customEventListenerAsync")
    @EventListener
    public handleCustomEvents(CustomEvent event) {
        EventType type = event.getEventType();
        // ...
    }
}
```

# Database

Sqlite has been chosen.
Physical location ./resources/db/db.sqlite3.
DDL scripts schema-sqlite.sql, data-sqlite.sql used

> Note: There is a slight situation around UNIQUE constraint. 
> It generates DB schema correctly, BUT, an exception is thrown and 
> data-sqlite.sql script is not executed!
> This happens the first time the schema is being created.
> Modify the hibernate.ddl-auto setting property to "update", and it runs okeish. 

> !!!new The above seems to be resolved by updatig SqliteDialect library from 0.1.0 to 0.1.2
> See [History of gwenn/sqlite-dialect](https://github.com/gwenn/sqlite-dialect/commits/master/src/main/java/org/sqlite/hibernate/dialect/SQLiteDialect.java)

## Sqlite locking

```
The following was determined to be necessary for multiple threaded writers in Sqlite database:
1] PRAGMA journal_mode=WAL - allows for writers and readers to coexist,
                            - sqlite database is spread into three separate files.
2] PRAGMA busy_timeout=millis - any writer will lock database on the file system level. Even for readers.
                               - This should stop/wait any additional request/threads for millis before throwing error-exception
3] Also there is Jpa-level query timeout that should be configured appropriately
4] Also it is required to use separate EntityManagers / connections for every thread

This class ensures the 1] and 2]. The third one is placed into persistence.xml. The fourth one is ensured by creation
of only two entityManagers for the whole application. One for app-user operations and the other one for scheduled periodic
operation that is automatically executed every X seconds and may result into write into DB.
```

>However it is much easier just to limit database connections overall to ONE
>spring.datasource.hikari.maximum-pool-size=1