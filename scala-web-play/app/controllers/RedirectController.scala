package controllers

import play.api.mvc.{Action, Result, Controller}

class RedirectController extends Controller {

  def redirect1(url:String) = Action {
    Redirect(url)
  }

  def redirect2(url:String) = Action {
    Redirect(url,302)
  }


  def seeOther1(url:String) = Action {
    SeeOther(url)
  }

  def movedPermanently1(url:String) = Action {
    MovedPermanently(url)
  }

  def temporaryRedirect1(url:String) = Action {
    TemporaryRedirect(url)
  }
}
