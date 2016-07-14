package controllers

import javax.inject.Inject

import anorm._
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

  val peopleParser = Macro.parser[Person]("id", "name", "age")

  def vulnerableAnorm1(value:String) =  Action {
    implicit val con: java.sql.Connection = dbConfig.db.createSession().conn

    val people: List[Person] = SQL("select * from people where name = '" + value + "'").as(peopleParser.*)
    Ok("Result:\n"+people)
  }

  def vulnerableAnorm2(value:String) = Action {
    implicit val con: java.sql.Connection = dbConfig.db.createSession().conn

    SQL"select * from people where name = '#$value'".as(peopleParser.*)
    val people: List[Person] = SQL(s"select * from people where name = '$value'").as(peopleParser.*)

    Ok("Result:\n"+people)
  }

  def vulnerableAnorm3(value:String) = Action {
    implicit val con: java.sql.Connection = dbConfig.db.createSession().conn

    val people: List[Person] = SQL("select * from people where name = '" + value + "'").executeQuery().as(peopleParser.*)
    Ok("Result:\n"+people)
  }

  def vulnerableAnorm4(value:String) = Action {
    implicit val con: java.sql.Connection = dbConfig.db.createSession().conn

    SQL"select * from people where name = '#$value'".executeQuery().as(peopleParser.*)
    val people: List[Person] = SQL(s"select * from people where name = '$value'").executeQuery().as(peopleParser.*)

    Ok("Result:\n"+people)
  }

  def vulnerableAnorm5(oldValue:String, newValue:String) = Action {
    implicit val con: java.sql.Connection = dbConfig.db.createSession().conn

    val result: Boolean = SQL("update people set name = '" + newValue + "' where name = '" + oldValue + "'").execute()
    Ok("Result:\n"+result)
  }

  def vulnerableAnorm6(oldValue:String, newValue:String) = Action {
    implicit val con: java.sql.Connection = dbConfig.db.createSession().conn

    SQL"update people set name = '#$newValue' where name = '#$oldValue'".execute()
    val result: Boolean = SQL(s"update people set name = '$newValue' where name = '$oldValue'").execute()

    Ok("Result:\n"+result)
  }

  def vulnerableAnorm7(oldValue:String, newValue:String) = Action {
    implicit val con: java.sql.Connection = dbConfig.db.createSession().conn

    val result: Int = SQL("update people set name = '" + newValue + "' where name = '" + oldValue + "'").executeUpdate()
    Ok("Result:\n"+result)
  }

  def vulnerableAnorm8(oldValue:String, newValue:String) = Action {
    implicit val con: java.sql.Connection = dbConfig.db.createSession().conn

    SQL"update people set name = '#$newValue' where name = '#$oldValue'".executeUpdate()
    val result: Int = SQL(s"update people set name = '$newValue' where name = '$oldValue'").executeUpdate()

    Ok("Result:\n"+result)
  }

  def vulnerableAnorm9(id:Int, name:String, age:Int) = Action {
    implicit val con: java.sql.Connection = dbConfig.db.createSession().conn

    SQL("insert into people values ('" + id + "','" + name + "','" + age + "')").executeInsert()
    Ok("Result:\n"+"Done.")
  }

  def vulnerableAnorm10(id:Int, name:String, age:Int) = Action {
    implicit val con: java.sql.Connection = dbConfig.db.createSession().conn

    SQL"insert into people values ('#$id','#$name','#$age')".executeInsert()
    SQL(s"insert into people values ('$id','$name','$age')").executeInsert()

    Ok("Result:\n"+"Done.")
  }

  def vulnerableAnorm11(oldValue:String, newValue:String) = Action {
    implicit val con: java.sql.Connection = dbConfig.db.createSession().conn

    BatchSql("update people set name = '" + newValue + "' where name = '" + oldValue + "'").execute()
    BatchSql("update people set name = '" + newValue + "' where name = {name}", Seq[NamedParameter]("name" -> oldValue)).execute()
    BatchSql("update people set name = '" + newValue + "' where name = {name}", Seq[NamedParameter]("name" -> oldValue), Seq[NamedParameter]("name" -> "John Doe")).execute()

    BatchSql(s"update people set name = '$newValue' where name = '$oldValue'").execute()
    BatchSql(s"update people set name = '$newValue' where name = {name}", Seq[NamedParameter]("name" -> oldValue)).execute()
    BatchSql(s"update people set name = '$newValue' where name = {name}", Seq[NamedParameter]("name" -> oldValue), Seq[NamedParameter]("name" -> "John Doe")).execute()

    Ok("Result:\n"+"Done.")
  }

  def variousSafeAnormSelect(value:String) = Action {
    implicit val con: java.sql.Connection = dbConfig.db.createSession().conn

    SQL("select * from people where name = 'John Doe'").as(peopleParser.*)
    SQL(s"select * from people where name = 'John Doe'").as(peopleParser.*)
    SQL"select * from people where name = 'John Doe'".as(peopleParser.*)
    SQL"select * from people where name = $value".as(peopleParser.*)

    SQL("select * from people where name = 'John Doe'").executeQuery().as(peopleParser.*)
    SQL(s"select * from people where name = 'John Doe'").executeQuery().as(peopleParser.*)
    SQL"select * from people where name = 'John Doe'".executeQuery().as(peopleParser.*)
    val people: List[Person] = SQL"select * from people where name = $value".executeQuery().as(peopleParser.*)

    Ok("Result:\n"+people)
  }

  def variousSafeAnormUpdate(oldValue:String, newValue:String) = Action {
    implicit val con: java.sql.Connection = dbConfig.db.createSession().conn

    SQL("update people set name = 'Jane Doe' where name = 'John Doe'").execute()
    SQL(s"update people set name = 'Jane Doe' where name = 'John Doe'").execute()
    SQL"update people set name = 'Jane Doe' where name = 'John Doe'".execute()
    SQL"update people set name = $newValue where name = $oldValue".execute()

    SQL("update people set name = 'Jane Doe' where name = 'John Doe'").executeUpdate()
    SQL(s"update people set name = 'Jane Doe' where name = 'John Doe'").executeUpdate()
    SQL"update people set name = 'Jane Doe' where name = 'John Doe'".executeUpdate()
    val result: Int = SQL"update people set name = $newValue where name = $oldValue".executeUpdate()

    Ok("Result:\n"+result)
  }

  def variousSafeAnormInsert(id:Int, name:String, age:Int) = Action {
    implicit val con: java.sql.Connection = dbConfig.db.createSession().conn

    SQL("insert into people values (10, 'John Doe', 25)").executeInsert()
    SQL(s"insert into people values (11, 'John Doe', 25)").executeInsert()
    SQL"insert into people values (12, 'John Doe', 25)".executeInsert()
    SQL"insert into people values ($id,$name,$age)".executeInsert()

    Ok("Result:\n"+"Done.")
  }

  def variousSafeAnormBatch(oldValue:String, newValue:String) = Action {
    implicit val con: java.sql.Connection = dbConfig.db.createSession().conn

    BatchSql("update people set name = 'Jane Doe' where name = 'John Doe'").execute()
    BatchSql(s"update people set name = 'Jane Doe' where name = 'John Doe'").execute()
    BatchSql("update people set name = {newName} where name = {oldName}", Seq[NamedParameter]("newName" -> newValue, "oldName" -> oldValue)).execute()
    BatchSql("update people set name = {newName} where name = {oldName}", Seq[NamedParameter]("newName" -> "Jane Doe", "oldName" -> "John Doe")).execute()
    BatchSql("update people set name = {newName} where name = {oldName}", Seq[NamedParameter]("newName" -> newValue, "oldName" -> oldValue), Seq[NamedParameter]("newName" -> "Jane Doe", "oldName" -> "John Doe")).execute()

    Ok("Result:\n"+"Done.")
  }

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