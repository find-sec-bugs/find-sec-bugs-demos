package controllers

import play.api.libs.ws._
import play.api.mvc.{Action, Controller}
import javax.inject._

import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits._

/**
  * This controller is used to test detection of potential SSRF.
  *
  * See CWE-918 :
  *     https://cwe.mitre.org/data/definitions/918.html
  */
class SSRFController @Inject() (wSClient: WSClient)  extends Controller {

  def vulnerableGet(value:String) = Action.async {

    // ####################
    // SBT 2.5.x
    // ####################
    // This method could expose internal services like mongodb to an attacker
    wSClient.url(value).get().map { response =>
      Ok(response.body)
    }

    // ####################
    // SBT 2.4.x
    // ####################

    // This method could expose internal services like mongodb to an attacker
    WS.url(value).get().map { response =>
      Ok(response.body)
    }
  }

  def vulnerablePost(value:String, postValue:String) = Action.async {

    // ####################
    // SBT 2.5.x
    // ####################
    // These method could expose internal services like mongodb to an attacker
    wSClient.url(value).post(postValue).map { response =>
      Ok(response.body)
    }

    wSClient.url(value).post("key=value").map { response =>
      Ok(response.body)
    }

    // This call could be used to attack another host from our server
    wSClient.url("http://www.google.com").post(postValue).map { response =>
      Ok(response.body)
    }

    // ####################
    // SBT 2.4.x
    // ####################
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

  def derp() = {
    wSClient.url("http://www.google.com").get()

    wSClient.url("http://www.google.com").get().map { response =>
      Ok(response.body)
    }

    WS.url("http://www.google.com").get()

    WS.url("http://www.google.com").get().map { response =>
      Ok(response.body)
    }
  }

  def safeGetNotTainted() = Action.async {
    // ####################
    // Play v.2.5.x
    // ####################
    wSClient.url("http://www.google.com").get()

    wSClient.url("http://www.google.com").get().map { response =>
      Ok(response.body)
    }

    // ####################
    // Play v.2.4.x
    // ####################
    WS.url("http://www.google.com").get()

    WS.url("http://www.google.com").get().map { response =>
      Ok(response.body)
    }
  }

  def safePostNotTainted() = Action.async {
    // ####################
    // Play v.2.5.x
    // ####################
    wSClient.url("http://www.google.com").post("key=value")

    wSClient.url("http://www.google.com").post("key=value").map { response =>
      Ok(response.body)
    }

    // ####################
    // Play v.2.4.x
    // ####################
    WS.url("http://www.google.com").post("key=value")

    WS.url("http://www.google.com").post("key=value").map { response =>
      Ok(response.body)
    }
  }

  def safeGetWithWhitelist(value:String, amount:Int) = Action.async {
    var url = "http://mysite.com/error"

    if (value == "transfer") {
      url = "http://mysite.com/transfer?amount=" + amount
    }

    // ####################
    // Play v.2.5.x
    // ####################
    wSClient.url("http://www.google.com").get()

    wSClient.url("http://www.google.com").get().map { response =>
      Ok(response.body)
    }

    // ####################
    // Play v.2.4.x
    // ####################
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

    // ####################
    // Play v.2.5.x
    // ####################
    wSClient.url(url).post("amount=" + amount)

    wSClient.url(url).post("amount=" + amount).map { response =>
      Ok(response.body)
    }

    // ####################
    // Play v.2.4.x
    // ####################
    WS.url(url).post("amount=" + amount)

    WS.url(url).post("amount=" + amount).map { response =>
      Ok(response.body)
    }
  }
}
