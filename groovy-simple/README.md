# Using Findbugs with Groovy

This project is using very similar configration to the Android project. It has one element added the static compilation configuration for the Groovy compiler.

## Configuration summary

Create the Groovy compiler configuration script

**gradle/config.groovy**
```groovy
withConfig(configuration) {
     ast(groovy.transform.CompileStatic)
}
```

Configure the Groovy compiler

**build.gradle**
```groovy
compileGroovy {
    //Configuration that will force the static compilation
    groovyOptions.configurationScript = file("gradle/config.groovy")
}
```

Add the findbugs dependencies

**build.gradle**
```groovy
dependencies {
    [...]
    
    //Dependencies for FindBugs and FindBugs plugins
    findbugs 'com.google.code.findbugs:findbugs:3.0.1'
    findbugs configurations.findbugsPlugins.dependencies
    findbugsPlugins 'com.h3xstream.findsecbugs:findsecbugs-plugin:1.4.6'
}
```

Add the following task to configure bytecode location and the security rules filter.

**build.gradle**
```groovy
apply plugin: 'findbugs'

...

//FindBugs task that load security rules only
task findSecurityBugs(type: FindBugs) {

    classes = fileTree(project.rootDir.absolutePath).include("**/*.class");
    source = fileTree(project.rootDir.absolutePath).include("**/*.groovy");
    classpath = files()
    pluginClasspath = project.configurations.findbugsPlugins

    findbugs {
        toolVersion = "3.0.1"
        ignoreFailures = true
        reportsDir = file("$project.buildDir/findbugsReports")
        effort = "max"
        reportLevel = "low"
        includeFilter = file("$rootProject.projectDir/fsb-include.xml")
        excludeFilter = file("$rootProject.projectDir/fsb-exclude.xml")
    }
}
```


## Usage

First compile from the source. The clean task is important to avoid leaving previously compiled classes (`.class`).

```
> gradle clean build
:clean
:compileJava UP-TO-DATE
:compileGroovy
:processResources UP-TO-DATE
:classes
:jar
:assemble
:findbugsMain UP-TO-DATE
:compileTestJava UP-TO-DATE
:compileTestGroovy UP-TO-DATE
:processTestResources UP-TO-DATE
:testClasses UP-TO-DATE
:findbugsTest UP-TO-DATE
:test UP-TO-DATE
:check UP-TO-DATE
:build

BUILD SUCCESSFUL

Total time: 7.363 sec
```

Second, run findbugs task:

```
> gradle findSecurityBugs
:findSecurityBugs
FindBugs rule violations were found. See the report at: file:///[...]/groovy-simple/build/findbugsReports/findSecurityBugs.xml

BUILD SUCCESSFUL

Total time: 9.221 secs
```
