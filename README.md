# Shops MicroServices Asset Challenge

This is the result of the Asset Challenge proposed by DB.
 
### Requeriments
This application is developed under Java 8, gradle and Google Maps API and Windows Environment.
It is mandatory to have installed:
Java SDK 8
Gradle 3.4

### HOWTO

#### Testing part
There are several developed Integration and JUnit tests in order to check the stability and behaviour inside the application. 
For that reason, they could be executed whether any IDE or through Gradle. 
If they are tested with gradle, use the command:
```sh
gradlew.bat test
```
#### Build the APP
In the case it is required to compile and generated the specific artefact, it could be done with the command, it generates the artefact "shops-microservices-example-0.0.1-SNAPSHOT.jar" inside the build\libs folder:
```sh
gradlew.bat build
```
#### Run the APP
In order to start the application and init the Tomcat, it will be deployed under http://localhost:8080/shops (or the IP of your computer), use:
```sh
gradlew.bat bootRun 
```

##### Postman Testing
As it was indicated in the application description, it was developed the application with Postman chrome application, in order to check the outgoing data from the server. Because of the output of the application is JSON media type.
There are a collection of few actions checked with the application inside the file `shops-microservices.postman_collection.json` and for each action some tests checking the response from the server, for instance:
```sh
var jsonData = JSON.parse(responseBody);
tests["Status code is 200"] = responseCode.code === 200;
tests["Distance"] = jsonData.distancesToAnotherPoint=163.56529618725796;
tests["Shop name"] = jsonData.name=== "Test 3";
tests["Shop Address"] = jsonData.shopAddress.street==="1 Chambers St, Edinburgh EH1 1JF, United Kingdom";
```

### Reasons of each selection

#### Architecture
It was developed following the MVC pattern. In this way, it is very easy including new objects and functionality. The controller part is the Shops API part, the model is formed by the Service, Dao and Data packaged. In the Dao package is included the "database part", which was selected to be implemented by a `ConcurrentHashmap` object, because I considered the easier and faster solution to implement a mock of database and, furthermore, solve the issue of concurrency. The view is the produced JSON's objects.

#### Properties, Labels, key
As far as I am concerned, all of the labels, passwords and so on... must be stored in a properties file. However, in this case and due to time restrictions to develope a whole application, are stored in an interface class. However, the Google API_KEY was stored separately in one configuration file (for instance the application.properties, it was the same as application.yml) and was read through the `@Value("#{...}") String googleKey...` in the server side.

#### Google Maps API

Firstly, when the user wants to create a new Shop, the shop is saved in the database and then an asynchronous task with `Future` is triggered to retrieve its coordinates and store them in the database the same object again but this time with its Lat & Lng included.
Secondly, when the user wants to know the nearest shop from his coordinates, it is calculated via internal function, avoiding including a new Service call.
 
#### Rest

The service is based on Spring HATEOAS and implements some common best practices such as returning the location of a resource on POST
or adding links to resources.
The service validates the request's body and tries to return meaningful errors.

### My Comments And Questions
It was a great and exciting challenge from my point of view, in order to increaase my own knowledge of these frameworks (Microservices, Spring Boot, Gradle), because I have never worked before on those technologies and I had to learn them before coding it. However, I was focused more on backend technologies and internal methodologies and requirements (config. and release managament, ...) . I wish I had had more time for developing a more complete solution instead of develope it.
I hope you consider it as a good result and we can see in the future.

#### How you would expand this solution, given a longer development period? 
In my opinion, as a `Product Owner` and without restriction of 1 Sprint of time, the Product Backlog could be:
+ create a base MVC Controller, for the basics CRUD methods and with a basic shops_text.properties where the labels could be stored,
+ request a commercial Google Maps Key (I am supposing a big use of this app) and it should be stored inside the server, encrypted as a any other password of any other projects,
+ separate the process of reading the coordinates in a Thread or in an async. process (because it is an enrichment action for the new/updated Shop),
+ create an Oracle schema database in order to persist correctly the objects and updating the Primary Key to NUMBER (Autogenerated value) and creating the Auditory table in order to know the changes made of each shop (and the person who made that change).
+ include Angular framwork to generate a complete View to manage the application,
+ create a custom Android client application (to be similar programing language than Java, but it could be also iOS) to consume these API methods,
+ diversification of the Shops' types (Banks, comercial, supermarket) and specify the products sold (including a type Product and the relationships in the application)

#### How would you go about testing this solution? 
If only I had had more time, I would have developed the solution with BDD testing (such us jBehave or Cucumber framework)

#### How would you integrate this solution into an existing collection of solutions used by the Retail Manager? 
As far as I know, I would include it as microservice in an Eureka container in a different context of the current deployed applications and using uDeploy and all the features available for the CI (Sonar, Veracode, Docker, BitBucket,...) in order to ensure the stability of the application. 

#### How would you go about deploying this solution to production systems?
This application it is supposed to be a microservice, for that reason it is fully deployable in any environment due to its internal Tomcat. It could be necessary to have any cluster administrator, such as, Openshift, Kebernetes or someone like them. 