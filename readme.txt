官网文本档地址
     http://mongodb.github.io/mongo-scala-driver/2.5/getting-started/quick-tour/
在API中，返回a的所有方法Observables都是“冷”流，这意味着在订阅之前不会发生任何事情。

以下示例不执行任何操作：

val observable: Observable[Completed] = collection.insertOne(doc)
只有当Observable订阅并且请求的数据才会发生操作：

// Explictly subscribe:
observable.subscribe(new Observer[Completed] {
  override def onNext(result: Completed): Unit = println("Inserted")
  override def onError(e: Throwable): Unit = println("Failed")
  override def onComplete(): Unit = println("Completed")
})
插入文档后，onNext将调用该方法并打印“已插入！”，
然后onCompleted打印“已完成”的方法。如果由于任何原因出现错误，该onError 方法将打印“失败”。