package com.laozhao.scala
import java.util.concurrent.TimeUnit

import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.collection.mutable.Document
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration.Duration

object tst extends App {

  implicit lazy val timeout=Duration(10, TimeUnit.SECONDS)
  // 实体类的解码工具类 与collection绑定
  val codecRegistry = fromRegistries(fromProviders(classOf[Person]), DEFAULT_CODEC_REGISTRY )
  // To directly connect to the default server localhost on port 27017
  val mongoClient = MongoClient()
  val database:MongoDatabase= mongoClient.getDatabase("sbt-scala").withCodecRegistry(codecRegistry)
  val ss:Seq[String]=  Await.result(database.listCollectionNames().toFuture(), Duration(10, TimeUnit.SECONDS))
  ss.foreach(println)

  val collection:MongoCollection[Person]= database.getCollection("user")
  val record:Seq[Person]=Await.result(collection.find().toFuture(),Duration(10, TimeUnit.SECONDS))
  record.foreach(println)
  val person: Person = Person("Ada", "Lovelace")
 // collection.insertOne(person).results()

}
