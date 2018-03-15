
## Pre-Compiling the JSP

```
mvn clean install -Pjetty936Jsp
```

## Sonar

```
mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=XXX
```

## Expected

 - Few XSS should be report.
 