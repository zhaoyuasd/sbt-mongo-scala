package com.laozhao.scala

import java.util.concurrent.TimeUnit

import com.laozhao.scala.tst.{collection, database}
import com.mongodb.async.client.Subscription
import org.mongodb.scala.bson.collection.mutable.Document
import org.mongodb.scala.{Completed, MongoClient, MongoCollection, MongoDatabase, Observable, Observer}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import org.mongodb.scala

import _root_.scala.io.StdIn


object test2 {
  val mongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("sbt-scala")
  val collection:MongoCollection[Document]= database.getCollection("user")
  def main(args: Array[String]): Unit = {
    // Await.result( database.listCollectionNames().toFuture(),Duration(10, TimeUnit.SECONDS)).foreach(println)
    listCollections
    //find
    collection.find().subscribe((a:Document)=>println(a.toJson()))  //这个方里必须写明参数类型
    StdIn.readLine()
  }

  def listCollections:Unit={
    database.listCollectionNames().subscribe((a:String)=>println(a))
  }



  def find:Unit={
    collection.find().subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = println(result.toJson())
      override def onError(e:Throwable): Unit = println(e.fillInStackTrace())
      override def onComplete(): Unit = println(" finish")
      override def onSubscribe(subscription: Subscription): Unit ={
        super.onSubscribe(subscription)
        println(" message will  get")
        println(subscription)
        subscription.request(1)
      }
    })
  }

  def insert:Unit={
    val person: Document = Document("firstName"->"Ada2222", "lastName"->"Lovelace2222")
    val observable: Observable[Completed] =collection.insertOne(person)
    observable.subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = println("Inserted")
      override def onError(e: Throwable): Unit = println("Failed")
      override def onComplete(): Unit = println("Completed")
    })
  }
}
