# Particle Accelerator Monitoring

## Video of the application

https://youtu.be/U5acSE3dVK4

## Local Setup

**1. Install Java 17 version on your machine. It's fine if you install also via editor.**

**2. Installation of PostgreSQL database. My local version used in this application is 14.**

**3. The database you can create with help of pgAdmin where you can create with any name you want.**

**4. Create a database with any of the name you want and set it up in the application.properties file like the following.**

```
## PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/HERE_NAME_OF_YOUR_DATABASE
spring.datasource.username=NAME_OF_YOUR_USERNAME_IN_THE_DATABASE
spring.datasource.password=NAME_OF_YOUR_PASSWORD_IN_THE_DATABASE
```

After this you can run the application by clicking the ``Running`` or ``Debugging`` button on the IDE. In my case I am using the Intellij IDEA ULTIMATE so you can run it in this way.

You can open the application on the following url: ``localhost:8080/temperatures`` and you can interact with the application.