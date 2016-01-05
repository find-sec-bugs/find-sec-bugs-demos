# Scala (SBT + Play Framework) configuration samples

The code of this application is a copy of [the template project play-scala-intro](https://github.com/playframework/playframework/tree/master/templates/play-scala-intro
) a demonstration project created by [the Play Framework team](https://github.com/playframework/playframework).

Many vulnerable samples were add to test rules from Find Security Bugs. 
**DO NOT deploy this application on production server.**

## FindBugs configuration

*TODO*

## SonarQube configuration

The following configuration demonstrate the [SonarQube SBT plugin]( https://github.com/aol/sbt-sonarrunner-plugin) maintained by [AOL and its community](https://github.com/aol).

**plugins.sbt** : Activate the plugin

```scala
addSbtPlugin("com.aol.sbt" % "sbt-sonarrunner-plugin" % "1.0.4")
```

**build.sbt** : Activate the plugin

```scala
lazy val root = (project in file(".")).enablePlugins(PlayScala,SonarRunnerPlugin)
```

**build.sbt** : Configure the plugin
```scala
sonarProperties ++= Map(
  "sonar.jdbc.username" -> "sonar",
  "sonar.jdbc.password" -> "changeme",
  "sonar.jdbc.url" -> "jdbc:postgresql://127.0.0.1:5432/sonarqube",
  "sonar.host.url" -> "http://localhost:9000"
)
```

More information : [https://github.com/aol/sbt-sonarrunner-plugin#installation]()
