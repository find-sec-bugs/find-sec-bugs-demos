
## Compiling

Only the Java code

```
mvn clean install
```

with the JSP precompiled

```
mvn clean install -Pjetty936Jsp
```

## SonarQube

```
mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=XXXX
```