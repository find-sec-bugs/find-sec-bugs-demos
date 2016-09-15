package controllers

import java.io.File
import java.net.URI

import play.api.mvc.{Action, Controller}

import scala.io.Source

class PathTraversalController extends Controller {

  def vulnerableIn(value:String) = Action {
    Source.fromFile(new File(value))
    Source.fromFile(new File(value), 100)
    Source.fromFile(new File(value), "UTF-8")
    Source.fromFile(value)
    Source.fromFile(value, "UTF-8")
    Source.fromFile(new URI(value))
    val result = Source.fromFile(new URI(value), "UTF-8")

    Ok("Result:\n"+result.getLines().mkString)
  }

  def vulnerableOut(value:String) = Action {
    reflect.io.File(value)

    Ok("Result:\n"+"Done.")
  }

  def safeIn(value:String) = Action {
    Source.fromFile(new File("Safe"))
    Source.fromFile(new File("Safe"), 100)
    Source.fromFile(new File("Safe"), "UTF-8")
    Source.fromFile("Safe")
    Source.fromFile("Safe", "UTF-8")
    Source.fromFile(new URI("Safe"))
    val result = Source.fromFile(new URI("Safe"), "UTF-8")

    Ok("Result:\n"+result.getLines().mkString
  }

  def safeOut(value:String) = Action {
    reflect.io.File("Safe")

    Ok("Result:\n"+"Done.")
  }
}
