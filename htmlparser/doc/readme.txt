
## reference
Java实现从Html文本中提取纯文本
http://blog.csdn.net/fjssharpsword/article/details/53467079

使用 HttpClient 和 HtmlParser 实现简易爬虫
https://www.ibm.com/developerworks/cn/opensource/os-cn-crawler/

基于htmlparser实现网页内容解析
http://www.cnblogs.com/coding-hundredOfYears/archive/2012/12/15/2819217.html

htmlparser的编码问题
http://gbfd2012.iteye.com/blog/732042


使用HtmlParser提取HTML文本块
http://blog.csdn.net/zzxian/article/details/6748177






网页就分成两种类型：主题型（topic）和hub型，这种分类有一个明显的差别，即主题型相对hub型网页正文要占可视文本的绝大多数。比如，baidu空间，这种博客类型的网页，多数情况下都是大段大段的文本块，而又如新华网首页，经过分析提取出来1,000多的出链，打开网页映入我们眼帘实际上是这些链接的锚文本。锚文本与正文文本块一个明显的差别就是短小，而门户网站的锚文本又多了一个性质：种类庞杂，条目众多。


细节问题还有一箩筐，比如要剔除广告信息怎么处理，如何利用版权声明中的"About us"或者"关于我们"的链接网页，提取信息补充关键词和摘要信息，同时将声明的其余部分毫不保留的过滤掉....
补充说一下，有人提出通过遍历html所有标签，统计其中的文本文字的比特数与标签的比率，根据到达正文尾部可以达到最大化的统计假设来实现

网页信噪比是指一个网页上的文字内容与html代码的比率。
http://baike.baidu.com/link?url=GhQ3_yxfB5a3w-n8R90EDs7l4HPAgmF-Z0UzTJnHuLspWBr5psWcAjlbABGVrXCaQTDw7ZW22E7kM7iCKtYPuAXIUehbOEYNH378r3qkzP7bfvRWp5fNqchLW7bPDu46




mvn exec:java -Dexec.mainClass="com.oie.EntityCounter"
mvn exec:java -Dexec.mainClass="com.oie.EntityCounterWithQueue"

