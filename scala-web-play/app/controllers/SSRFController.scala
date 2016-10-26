package controllers

import play.api.libs.ws._
import play.api.mvc.{Action, Controller}

import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits._

/**
  * This controller is used to test detection of potential SSRF.
  *
  * See CWE-918 :
  *     https://cwe.mitre.org/data/definitions/918.html
  */
class SSRFController extends Controller {

  def vulnerableGet(value:String) = Action.async {

    // This method could expose internal services like mongodb to an attacker
    WS.url(value).get().map { response =>
      Ok(response.body)
    }
  }

  def vulnerablePost(value:String, postValue:String) = Action.async {

    // These method could expose internal services like mongodb to an attacker
    WS.url(value).post(postValue).map { response =>
      Ok(response.body)
    }

    WS.url(value).post("key=value").map { response =>
      Ok(response.body)
    }

    // This call could be used to attack another host from our server
    WS.url("http://google.com").post(postValue).map { response =>
      Ok(response.body)
    }
  }

  def safeGetNotTainted() = Action.async {
    WS.url("http://google.com").get().map { response =>
      Ok(response.body)
    }
  }

  def safePostNotTainted() = Action.async {
    WS.url("http://google.com").post("key=value").map { response =>
      Ok(response.body)
    }
  }
}
