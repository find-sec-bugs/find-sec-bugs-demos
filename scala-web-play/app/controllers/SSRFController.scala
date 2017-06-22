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
    WS.url("http://www.google.com").post(postValue).map { response =>
      Ok(response.body)
    }
  }

  def safeGetNotTainted() = Action.async {
    WS.url("http://www.google.com").get()

    WS.url("http://www.google.com").get().map { response =>
      Ok(response.body)
    }
  }

  def safePostNotTainted() = Action.async {
    WS.url("http://www.google.com").post("key=value").map { response =>
      Ok(response.body)
    }
  }

  def safeGetWithWhitelist(value:String, amount:Int) = Action.async {
    var url = "http://mysite.com/error"

    if (value == "transfer") {
      url = "http://mysite.com/transfer?amount=" + amount
    }

    WS.url(url).get()

    WS.url(url).get().map { response =>
      Ok(response.body)
    }
  }

  def safePostWithWhitelist(value:String, amount:Int) = Action.async {
    var url = "http://mysite.com/error"

    if (value == "transfer") {
      url = "http://mysite.com/transfer"
    }

    WS.url(url).post("amount=" + amount)

    WS.url(url).post("amount=" + amount).map { response =>
      Ok(response.body)
    }
  }
}
