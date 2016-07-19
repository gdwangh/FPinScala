package objsets

import TweetReader._

object test {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  val google = List("android", "Android", "galaxy", "Galaxy", "nexus", "Nexus")
                                                  //> google  : List[String] = List(android, Android, galaxy, Galaxy, nexus, Nexus
                                                  //| )
  val apple = List("ios", "iOS", "iphone", "iPhone", "ipad", "iPad")
                                                  //> apple  : List[String] = List(ios, iOS, iphone, iPhone, ipad, iPad)
    
  val set1 = new Empty                            //> set1  : objsets.Empty = objsets.Empty@181a1bc
  val set2 = set1.incl(new Tweet("a", "a body", 20))
                                                  //> set2  : objsets.TweetSet = objsets.NonEmpty@616ca2
  val set3 = set2.incl(new Tweet("b", "b body", 20))
                                                  //> set3  : objsets.TweetSet = objsets.NonEmpty@1fee20c
	val set4 = set3.incl(new Tweet("b", "galaxy", 20))
                                                  //> set4  : objsets.TweetSet = objsets.NonEmpty@4a765
	val set5 = set3.incl(new Tweet("b", "iOS", 20))
                                                  //> set5  : objsets.TweetSet = objsets.NonEmpty@3e6358
  
  val googleTweets: TweetSet = set4.filter(tw => google.exists { key => tw.text.contains(key) })
                                                  //> googleTweets  : objsets.TweetSet = objsets.NonEmpty@1550481
    
  // lazy val appleTweets: TweetSet = ???
  val appleTweets: TweetSet = set5.filter(tw => apple.exists { key => tw.text.contains(key) })
                                                  //> appleTweets  : objsets.TweetSet = objsets.NonEmpty@15f7ae5
      
  val trending: TweetList = (googleTweets union appleTweets).descendingByRetweet
                                                  //> trending  : objsets.TweetList = objsets.Cons@bc464
  
  trending.foreach(tw => println(tw.toString))    //> User: b
                                                  //| Text: galaxy [20]
                                                  //| User: b
                                                  //| Text: iOS [20]
}