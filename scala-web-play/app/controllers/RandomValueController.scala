package controllers

import java.security.SecureRandom

import scala.util.Random

import scala.util.Random._

import play.api.mvc.{Action, Controller}

class RandomValueController extends Controller {

  def random1() = Action {
    val rand = new Random()
    val result = rand.nextInt()
    Ok("Result:\n"+result)
  }

  def random2() = Action {
    val result = Seq.fill(16)(Random.nextInt)
    Ok("Result:\n"+result.map("%02x" format _).mkString)
  }

  def random3() = Action {
    val result = Stream.continually(Random.nextInt(255)).take(20)
    Ok("Result:\n"+result.map("%02x" format _).mkString)
  }

  def random4() = Action {
    var r = new java.util.Random
    var result = 1 to 100 map { _ => r.nextInt(100) }
    Ok("Result:\n"+result.map("%02x" format _).mkString)
  }

  def random5_various() = Action {
    //scala.util.Random constructor
    var rand = new Random()
    rand = new Random(1L)
    rand = new Random(1)
    //
    rand.nextInt(256)
    rand.nextDouble()
    //next methods
    Random.nextBoolean()
    Random.nextBytes(Array[Byte](1, 2, 3, 4))
    Random.nextDouble()
    Random.nextFloat()
    Random.nextGaussian()
    Random.nextInt()
    Random.nextInt(111)
    Random.nextLong()
    Random.nextString(16)
    Random.nextPrintableChar()

    Ok("Result:\ndone")
  }

  def generateSecretToken1() {
    val result = Seq.fill(16)(Random.nextInt)
    return result.map("%02x" format _).mkString
  }

  def generateSecretToken2() {
    val secRandom = javaRandomToRandom(new java.security.SecureRandom())
    val result = Seq.fill(16)(secRandom.nextInt)
    return result.map("%02x" format _).mkString
  }

  def generateSecretToken3() {
    val rand = new SecureRandom()
    val value = Array.ofDim[Byte](16)
    rand.nextBytes(value)
    return value.map("%02x" format _).mkString
  }
}
