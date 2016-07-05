package controllers

import play.api.Play
import play.api.mvc.{Action, Controller}
import javax.inject._

class SensitiveDataExposureController @Inject() (configuration: play.api.Configuration) extends Controller {

  def vulnerable1(value:String) = Action {
    // Play 2.5
    val configElementLatest = configuration.underlying.getString(value)

    // Play 2.0
    val configElement2 = Play.current.configuration.getString(value)

    NotFound("Result:\n"+configElementLatest)
    BadRequest("Result:\n"+configElementLatest)
    InternalServerError("Result:\n"+configElementLatest)
    Status(418)("Result:\n"+configElementLatest)
    Ok("Result:\n"+configElementLatest)

    NotFound("Result:\n"+configElement2)
    BadRequest("Result:\n"+configElement2)
    InternalServerError("Result:\n"+configElement2)
    Status(418)("Result:\n"+configElement2)
    Ok("Result:\n"+configElement2)
  }

  def vulnerable2(value:String) = Action {
    val systemProperty = System.getProperty(value)

    NotFound("Result:\n"+systemProperty)
    BadRequest("Result:\n"+systemProperty)
    InternalServerError("Result:\n"+systemProperty)
    Status(418)("Result:\n"+systemProperty)
    Ok("Result:\n"+systemProperty)
  }

  def vulnerable3(value:String) = Action {
    val systemEnvValue = System.getenv(value)

    NotFound("Result:\n"+systemEnvValue)
    BadRequest("Result:\n"+systemEnvValue)
    InternalServerError("Result:\n"+systemEnvValue)
    Status(418)("Result:\n"+systemEnvValue)
    Ok("Result:\n"+systemEnvValue)
  }

  def safe1() = Action {
    // Play 2.5
    val configElementLatest = configuration.underlying.getString("application.test")

    // Play 2.0
    val configElement2 = Play.current.configuration.getString("application.test")

    NotFound("Result:\n"+configElementLatest)
    BadRequest("Result:\n"+configElementLatest)
    InternalServerError("Result:\n"+configElementLatest)
    Status(418)("Result:\n"+configElementLatest)
    Ok("Result:\n"+configElementLatest)

    NotFound("Result:\n"+configElement2)
    BadRequest("Result:\n"+configElement2)
    InternalServerError("Result:\n"+configElement2)
    Status(418)("Result:\n"+configElement2)
    Ok("Result:\n"+configElement2)
  }

  def safe2() = Action {
    val systemEnvValue = System.getenv("MaxPermSize")

    NotFound("Result:\n"+systemEnvValue)
    BadRequest("Result:\n"+systemEnvValue)
    InternalServerError("Result:\n"+systemEnvValue)
    Status(418)("Result:\n"+systemEnvValue)
    Ok("Result:\n"+systemEnvValue)
  }

  def safe3() = Action {
    val systemEnvValue = System.getenv("path")

    NotFound("Result:\n"+systemEnvValue)
    BadRequest("Result:\n"+systemEnvValue)
    InternalServerError("Result:\n"+systemEnvValue)
    Status(418)("Result:\n"+systemEnvValue)
    Ok("Result:\n"+systemEnvValue)
  }
}
