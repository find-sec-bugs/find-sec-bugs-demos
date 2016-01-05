package controllers

import play.api.mvc.{Action, Controller}

class XssController extends Controller {

  def xssTest1(value:String) = Action {
    Ok("Hello "+value+" !")
  }

  def xssTest2(value:String) = Action {
    Ok("Hello "+value+" !").as("text/html")
  }
}
