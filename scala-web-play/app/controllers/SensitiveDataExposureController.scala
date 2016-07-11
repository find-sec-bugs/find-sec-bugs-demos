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
    val systemProperty2 = System.getProperty(value, "val")

    NotFound("Result:\n"+systemProperty)
    BadRequest("Result:\n"+systemProperty)
    InternalServerError("Result:\n"+systemProperty)
    Status(418)("Result:\n"+systemProperty)
    Ok("Result:\n"+systemProperty)

    NotFound("Result:\n"+systemProperty2)
    BadRequest("Result:\n"+systemProperty2)
    InternalServerError("Result:\n"+systemProperty2)
    Status(418)("Result:\n"+systemProperty2)
    Ok("Result:\n"+systemProperty2)
  }

  def vulnerable3(value:String) = Action {
    val systemEnvValue = System.getenv(value)

    NotFound("Result:\n"+systemEnvValue)
    BadRequest("Result:\n"+systemEnvValue)
    InternalServerError("Result:\n"+systemEnvValue)
    Status(418)("Result:\n"+systemEnvValue)
    Ok("Result:\n"+systemEnvValue)
  }

  def safePlayConfig() = Action {
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

  def safeEnvProperty() = Action {
    val systemProperty = System.getProperty("Xpermsize")

    val systemEnvValue = System.getenv("path")

    NotFound("Result:\n"+systemProperty)
    BadRequest("Result:\n"+systemProperty)
    InternalServerError("Result:\n"+systemProperty)
    Status(418)("Result:\n"+systemProperty)
    Ok("Result:\n"+systemProperty)

    NotFound("Result:\n"+systemEnvValue)
    BadRequest("Result:\n"+systemEnvValue)
    InternalServerError("Result:\n"+systemEnvValue)
    Status(418)("Result:\n"+systemEnvValue)
    Ok("Result:\n"+systemEnvValue)
  }

  def safeUntainted(value:String) = Action {
    // Play 2.5
    val configElementLatest = configuration.underlying.getString(value)

    // Play 2.0
    val configElement2 = Play.current.configuration.getString(value)

    val systemProperty = System.getProperty(value)

    val systemEnvValue = System.getenv(value)

    NotFound("Result:\n"+"Done.")
    BadRequest("Result:\n"+"Done.")
    InternalServerError("Result:\n"+"Done.")
    Status(418)("Result:\n"+"Done.")
    Ok("Result:\n"+"Done.")

    NotFound("Result:\n"+value)
    BadRequest("Result:\n"+value)
    InternalServerError("Result:\n"+value)
    Status(418)("Result:\n"+value)
    Ok("Result:\n"+value)
  }

  def safeNoSensitiveData(value:String) = Action {
    NotFound("Result:\n"+"Done.")
    BadRequest("Result:\n"+"Done.")
    InternalServerError("Result:\n"+"Done.")
    Status(418)("Result:\n"+"Done.")
    Ok("Result:\n"+"Done.")

    NotFound("Result:\n"+value)
    BadRequest("Result:\n"+value)
    InternalServerError("Result:\n"+value)
    Status(418)("Result:\n"+value)
    Ok("Result:\n"+value)
  }
}
