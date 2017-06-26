<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title></title>

	<link rel="stylesheet" href="${cdnUrl}/exts/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="${cdnUrl}/css/footer.css">
	<link rel="stylesheet" href="${cdnUrl}/css/word.css">
</head>
<body>
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">NLP工具</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
					<li><a href="#segment" onclick="segmentPage()">分词标注</a></li>
					<li><a href="#entity">实体抽取</a></li>
					<li><a href="#keywords">关键词提取</a></li>
					<li><a href="#summary">摘要提取</a></li>
					<li><a href="#wordcloud">词云图</a></li>
					<li><a href="<c:url value="/wordcloud" />">词云图2</a></li>
					<li><a href="#">帮助</a></li>
					<li><a>其他</a></li>
				</ul>
			</div>
		</div>
	</div>
	<div class="container-fluid">
		<div class="jumbotron">
			<h3>页面文本数据</h3>
			<div>
				<textarea id="text" class="form-control" rows="15">程序员(英文Programmer)是从事程序开发、维护的专业人员。一般将程序员分为程序设计人员和程序编码人员，但两者的界限并不非常清楚，特别是在中国。软件从业人员分为初级程序员、高级程序员、系统分析员和项目经理四大类。</textarea>
			</div>
			<p style="float: right; padding-top: 10px;">
				<input style="width: 500px;" type="text" placeholder="URL" id="url">
				<button id="getBtn">抓取</button>
			</p>
		</div>
		<div class="col-md-10 col-md-offset-1" style="margin-bottom:30px;">
			<div class="row" id="detail"></div>
		</div>

	</div>

	<footer class="footer">
		<div class="container-fluid">
			<p class="text-muted">Copyright &copy; 2017. All rights reserved.</p>
		</div>
	</footer>

	<script type="text/javascript" src="${cdnUrl}/exts/jquery/jquery.min.js"></script>
	<script type="text/javascript" src="${cdnUrl}/exts/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$('#getBtn').click(function () {
			var url = $('#url').val().trim();
			if (url.length == 0) {
				alert('URL is invalid. Please enter new one.');
				$('#url').focus();
				return;
			}
			console.log(url);
			$.ajax({
				type: 'POST',
				url: '<c:url value="/loadPage.action" />',
				data: {'url': url},
				success: function(result) {
					console.log('get data from server')
					if (result == '') {
						alert('The URL does not contain text.')
					} else {
						$('#text').val(result);
					}
				}
			});
		});
	</script>
	<script type="text/javascript">
		function segmentPage() {
			var text = $('#text').val().trim();
			if (text == '') {
				alert('text is empty.');
				$('#text').focus();
				return;
			}
			$.ajax({
				type: 'POST',
				url: '<c:url value="/segment" />',
				data: {'text': text},
				success: function(result) {
					console.log('get data from server')
					if (result == '') {
						alert('The URL does not contain text.')
					} else {
						$('#detail').html(result);
					}
				}
			});
		}
	</script>
</body>
</html>