package controllers

import java.io.File
import java.net.URI
import org.apache.commons.io.FilenameUtils

import play.api.mvc.{Action, Controller}

import scala.io.Source

class PathTraversalController extends Controller {

  def vulnerableIn(value:String) = Action {
    // The 3 following cases should be handled by the Java PathTraversal detector
    //Source.fromFile(new File(value))
    //Source.fromFile(new File(value), 100)
    //Source.fromFile(new File(value), "UTF-8")

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
    Source.fromFile(new URI("Safe"), "UTF-8")

    val filename = "directory/" + FilenameUtils.getName(value)
    Source.fromFile(new File(filename))
    Source.fromFile(new File(filename), 100)
    Source.fromFile(new File(filename), "UTF-8")
    Source.fromFile(filename)
    Source.fromFile(filename, "UTF-8")
    Source.fromFile(new URI(filename))
    val result = Source.fromFile(new URI(filename), "UTF-8")

    Ok("Result:\n"+result.getLines().mkString)
  }

  def safeOut(value:String) = Action {
    reflect.io.File("Safe")

    val filename = "directory/" + FilenameUtils.getName(value)
    reflect.io.File(filename)

    Ok("Result:\n"+"Done.")
  }
}
