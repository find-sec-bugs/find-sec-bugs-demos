package controllers

import play.api.libs.ws._
import play.api.mvc.{Action, Controller}
import javax.inject._

import play.api.Play.current

import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws.ahc.AhcCurlRequestLogger

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

    wSClient.url(value).withAuth("user", "password", WSAuthScheme.BASIC).get()
    wSClient.url(value).withBody(EmptyBody).get()
    wSClient.url(value).withFollowRedirects(true).get()
    wSClient.url(value).withHeaders("Accept" -> "application/json").get()
    wSClient.url(value).withMethod("GET").get()
    wSClient.url(value).withQueryString("search" -> "play").get()
    wSClient.url(value).withRequestFilter(AhcCurlRequestLogger()).get()
    wSClient.url(value).withRequestTimeout(Duration(10000,MILLISECONDS)).get()
    wSClient.url(value).withVirtualHost("192.168.2.1").get()
    //wSClient.url(value).withProxyServer().get()

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

    wSClient.url(value).withAuth("user", "password", WSAuthScheme.BASIC).post(postValue)
    wSClient.url(value).withBody(EmptyBody).post(postValue)
    wSClient.url(value).withFollowRedirects(true).post(postValue)
    wSClient.url(value).withHeaders("Accept" -> "application/json").post(postValue)
    wSClient.url(value).withMethod("POST").post(postValue)
    wSClient.url(value).withQueryString("search" -> "play").post(postValue)
    wSClient.url(value).withRequestFilter(AhcCurlRequestLogger()).post(postValue)
    wSClient.url(value).withRequestTimeout(Duration(10000,MILLISECONDS)).post(postValue)
    wSClient.url(value).withVirtualHost("192.168.2.1").post(postValue)
    //wSClient.url(value).withProxyServer().post(postValue)

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

  def safeGetNotTainted() = Action.async {
    // ####################
    // Play v.2.5.x
    // ####################
    wSClient.url("http://www.google.com").get()

    wSClient.url("http://www.google.com").get().map { response =>
      Ok(response.body)
    }

    wSClient.url("http://www.google.ca").withAuth("user", "password", WSAuthScheme.BASIC).get()
    wSClient.url("http://www.google.ca").withBody(EmptyBody).get()
    wSClient.url("http://www.google.ca").withFollowRedirects(true).get()
    wSClient.url("http://www.google.ca").withHeaders("Accept" -> "application/json").get()
    wSClient.url("http://www.google.ca").withMethod("GET").get()
    wSClient.url("http://www.google.ca").withQueryString("search" -> "play").get()
    wSClient.url("http://www.google.ca").withRequestFilter(AhcCurlRequestLogger()).get()
    wSClient.url("http://www.google.ca").withRequestTimeout(Duration(10000,MILLISECONDS)).get()
    wSClient.url("http://www.google.ca").withVirtualHost("192.168.2.1").get()
    //wSClient.url("http://www.google.ca").withProxyServer().get()

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

    wSClient.url("http://www.google.ca").withAuth("user", "password", WSAuthScheme.BASIC).post("key=value")
    wSClient.url("http://www.google.ca").withBody(EmptyBody).post("key=value")
    wSClient.url("http://www.google.ca").withFollowRedirects(true).post("key=value")
    wSClient.url("http://www.google.ca").withHeaders("Accept" -> "application/json").post("key=value")
    wSClient.url("http://www.google.ca").withMethod("POST").post("key=value")
    wSClient.url("http://www.google.ca").withQueryString("search" -> "play").post("key=value")
    wSClient.url("http://www.google.ca").withRequestFilter(AhcCurlRequestLogger()).post("key=value")
    wSClient.url("http://www.google.ca").withRequestTimeout(Duration(10000,MILLISECONDS)).post("key=value")
    wSClient.url("http://www.google.ca").withVirtualHost("192.168.2.1").post("key=value")
    //wSClient.url("http://www.google.ca").withProxyServer().post("key=value")

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
    wSClient.url(url).get()

    wSClient.url(url).get().map { response =>
      Ok(response.body)
    }

    wSClient.url(url).withAuth("user", "password", WSAuthScheme.BASIC).get()
    wSClient.url(url).withBody(EmptyBody).get()
    wSClient.url(url).withFollowRedirects(true).get()
    wSClient.url(url).withHeaders("Accept" -> "application/json").get()
    wSClient.url(url).withMethod("GET").get()
    wSClient.url(url).withQueryString("search" -> "play").get()
    wSClient.url(url).withRequestFilter(AhcCurlRequestLogger()).get()
    wSClient.url(url).withRequestTimeout(Duration(10000,MILLISECONDS)).get()
    wSClient.url(url).withVirtualHost("192.168.2.1").get()
    //wSClient.url(url).withProxyServer().get()

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

    wSClient.url(url).withAuth("user", "password", WSAuthScheme.BASIC).post("amount=" + amount)
    wSClient.url(url).withBody(EmptyBody).post("amount=" + amount)
    wSClient.url(url).withFollowRedirects(true).post("amount=" + amount)
    wSClient.url(url).withHeaders("Accept" -> "application/json").post("amount=" + amount)
    wSClient.url(url).withMethod("POST").post("amount=" + amount)
    wSClient.url(url).withQueryString("search" -> "play").post("amount=" + amount)
    wSClient.url(url).withRequestFilter(AhcCurlRequestLogger()).post("amount=" + amount)
    wSClient.url(url).withRequestTimeout(Duration(10000,MILLISECONDS)).post("amount=" + amount)
    wSClient.url(url).withVirtualHost("192.168.2.1").post("amount=" + amount)
    //wSClient.url(url).withProxyServer().get()

    // ####################
    // Play v.2.4.x
    // ####################
    WS.url(url).post("amount=" + amount)

    WS.url(url).post("amount=" + amount).map { response =>
      Ok(response.body)
    }
  }
}
