# Android configuration sample (Insecure Bank v.2)

The code of this application is a copy of [Insecure Bank v.2](https://github.com/dineshshetty/Android-InsecureBankv2) a vulnerable mobile application maded by [Dinesh Shetty](https://github.com/dineshshetty).

The configuration for FindBugs is made in the Gradle configuration (`build.gradle`).

For licensing refer to the [Insecure Bank v.2](https://github.com/dineshshetty/Android-InsecureBankv2).

## Configuration summary

Add the findbugs dependencies

```groovy
dependencies {
    [...]
    
    
    //Dependencies for FindBugs and FindBugs plugins
    findbugs 'com.google.code.findbugs:findbugs:3.0.1'
    findbugs configurations.findbugsPlugins.dependencies
    findbugsPlugins 'com.h3xstream.findsecbugs:findsecbugs-plugin:1.4.4'
}
```

Add the following task to configure bytecode location and the security rules filter.

```groovy
apply plugin: 'findbugs'

...

//FindBugs task that load security rules only
task findSecurityBugs(type: FindBugs) {

    classes = fileTree(project.rootDir.absolutePath).include("**/*.class");
    source = fileTree(project.rootDir.absolutePath).include("**/*.java");
    classpath = files()
    pluginClasspath = project.configurations.findbugsPlugins

    findbugs {
        toolVersion = "3.0.1"
        sourceSets = [android.sourceSets]
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

```
> gradlew findSecurityBugs
Parallel execution with configuration on demand is an incubating feature.
:app:findSecurityBugs
FindBugs rule violations were found. See the report at: file:///C:/Code/workspace-java/find-sec-bugs-demos/android-insecure-bank-v2/app/build/findbugsReports/findSecurityBugs.xml

BUILD SUCCESSFUL

Total time: 30.557 secs
```

Special thanks to [Krishna Pandey](https://github.com/krishna-pandey) who provided the [initial configuration example](https://github.com/find-sec-bugs/find-sec-bugs/issues/134#issuecomment-162152102)
