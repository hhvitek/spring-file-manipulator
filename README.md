Spring-file-manipulator
=======================

General idea is to learn ***spring boot*** framework. Scenario here is the following:
* The business "logic" should be hidden behind **REST API**. 
  Therefore, UI and model should be decoupled. 
  Allowing for any kind of UI framework to depend only on REST endpoints not on java code.
  
* Learn ***spring data*** (jpa, hibernate)... Try to be "very friendly" data persistent. 
  Store data even if it doesn't make much sense.

Application concept
-------------------

Spring file manipulator. Spring as it is developed using spring framework. 
File manipulator as the app manipulates file on the user's file system.

* As an **source** there is any kind of folder (directory) hierarchy structure
  containing collections of other folders and files.
Let's imagine hierarchy of audio songs (.mp3 files) categorized into folders by an album.
  
* The app processes this hierarchy (of songs) and moves (copies) each individual file into another **destination** folder
  regardless of the file's position in the hierarchy.
In other words creating one destination folder containing all files from source folder hierarchy.
  
* The app should **store history** of any performed file operation. Allowing for a UI to retrieve it at a later date.
  
User should be allowed:

* To **select** only **some subset** of files from the source hierarchy. E.g. using some kind of **regular expression** or path wildcards such as *\*.mp3*. Therefore, ignoring any other file than mp3 files. 

* To **modify** destination **file name** using *standard operation*. 
Let's imagine removing all kinds of whitespace characters from a filename OR replacing them with an underscore (_) character...
  
Developer guide
---------------

### Terminology

####Job
desc

####Task
desc

####Operation




#### controller
Defining standard spring REST controllers and possible error handling.


service

### Spring Boot controller routes

**1) Disable whitelabel page**

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

**2) Create custom Error Controller**

This controller should handle all requests that does NOT have any known request mapping. 
In other words the Spring does not know any controller to route request to.