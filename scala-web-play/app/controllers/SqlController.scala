package controllers

import javax.inject.Inject

import models.Person
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.{Action, Controller}
import slick.driver.JdbcProfile
import slick.jdbc.GetResult

import scala.concurrent.{ExecutionContext, Future}

class SqlController @Inject() (dbConfigProvider: DatabaseConfigProvider, configuration: play.api.Configuration)
                              (implicit ec: ExecutionContext) extends Controller {

  // We want the JdbcProfile for this provider
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import driver.api._

  implicit val getPersonResult = GetResult(r => Person(r.nextLong(), r.nextString(), r.nextInt()))

  def vulnerableSlick1(value:String) = Action.async { implicit request =>
    val result: Future[Seq[Person]] = db.run {
      sql"select * from people where name = '#$value'".as[Person]
    }

    result.map { implicit people =>
      Ok("Result:\n"+people)
    }
  }

  def variousSafeSlick1(value:String) = Action.async { implicit request =>
    val result: Future[Seq[Person]] = db.run {
      sql"select * from people where name = 'John Doe'".as[Person]
      sql"select * from people where name = $value".as[Person]
    }

    result.map { implicit people =>
      Ok("Result:\n"+people)
    }
  }

  def vulnerableSlick2(oldValue:String, newValue:String) = Action.async { implicit request =>
    val result: Future[Int] = db.run {
      sqlu"update people set name = '#$newValue' where name = '#$oldValue'"
    }

    result.map { implicit rowsAffected =>
      Ok("Result:\n"+rowsAffected+" rows affected.")
    }
  }

  def variousSafeSlick2(oldValue:String, newValue:String) = Action.async { implicit request =>
    val result: Future[Int] = db.run {
      sqlu"update people set name = 'Jane Doe' where name = 'John Doe'"
      sqlu"update people set name = $newValue where name = $oldValue"
    }

    result.map { implicit rowsAffected =>
      Ok("Result:\n"+rowsAffected+" rows affected.")
    }
  }
}