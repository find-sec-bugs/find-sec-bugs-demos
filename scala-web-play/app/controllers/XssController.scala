package controllers

import play.api.mvc.{Action, Controller}
import play.twirl.api.Html;

class XssController extends Controller {

  def vulnerable1(value:String) = Action {
    Ok("Hello "+value+" !").as("text/html")
  }

  def vulnerable2(value:String, contentType:String) = Action {
    Ok("Hello "+value+" !").as(contentType)
  }

  def vulnerable3(value:String) = Action {
    Ok(views.html.xssHtml.render(Html.apply("Hello "+value+" !")))
  }

  def potentiallyVulnerable(value:String) = Action {
    Ok(views.html.xssString.render(value))
  }

  def variousSafe(value:String) = Action {
    Ok("Hello "+value+" !")
    Ok(s"Hello $value !").as("text/json")
    Ok("<b>Hello !</b>").as("text/html")
    Ok(views.html.xssHtml.render(Html.apply("<b>Hello !</b>")))
  }
}
