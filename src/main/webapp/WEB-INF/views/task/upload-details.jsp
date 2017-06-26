<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title></title>
	<link rel="stylesheet" href="${cdnUrl}/exts/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="${cdnUrl}/css/footer.css">
</head>
<body>
	<%@ include file="/WEB-INF/views/include/header.jsp" %>
	<div id="main-content" class="container">
		<div class="page-header">
			<h3>文件统计信息</h3>
		</div>
		<div class="row">
			<%@ include file="/WEB-INF/views/include/sidebar.jsp" %>
			<div class="col-md-9" role="main">
				<div class="bs-docs-section">
					<div>
						<div id="container" style="min-width:400px;height:400px"></div>
					</div>
				</div>
				<div class="bs-docs-section">
					<h2 class="page-header">文件详细信息</h2>
					<table class="table table-striped">
						<thead>
							<tr>
								<th>文件夹编号</th>
								<th>硬盘编号</th>
								<th>目录路径</th>
								<th>文件类型</th>
								<th>文件个数</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${details}" var="item" varStatus="status">
								<tr>
									<th scope="row">${status.count}</th>
									<td>${diskId}</td>
									<td>${item.path}</td>
									<td>${fileType}</td>
									<td>${item.filecount}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					
					<a href="<c:url value="/task/run-all" />" class="btn btn-success">处理所有数据</a>
					<a href="<c:url value="/task/publish" />" class="btn btn-success">处理部分数据</a>
				</div>
			</div>
		</div>
	</div>
	
	<%@ include file="/WEB-INF/views/include/footer.jsp" %>

	<script type="text/javascript" src="${cdnUrl}/exts/jquery/jquery.min.js"></script>
	<script type="text/javascript" src="${cdnUrl}/exts/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${cdnUrl}/exts/highcharts/highcharts.js"></script>
	<script type="text/javascript" src="${cdnUrl}/exts/highcharts/exporting.js"></script>
	<script type="text/javascript" src="${cdnUrl}/exts/highcharts/highcharts-zh_CN.js"></script>
	<script type="text/javascript">
		$('#taskmgr').addClass('active');
		$('#upload').addClass('active');
	</script>
	<script type="text/javascript">
		function cols() {
			var len = parseInt('${length}');
			var data = [];
			for (var i = 0; i < len; i++) {
				data[i] = i.toString();
			}
			return data;
		}
	</script>
	<script type="text/javascript">
		$('#container').highcharts({
		chart: {
			type: 'bar'
		},
		title: {
			text: '文件统计信息'
		},
		xAxis: {
			categories: cols(),
			title: {
				text: '文件夹'
			}
		},
		yAxis: {
			min: 0,
			title: {
				text: '文件个数 (个)',
				align: 'high'
			},
			labels: {
				overflow: 'justify'
			}
		},
		tooltip: {
			valueSuffix: ' 个'
		},
		plotOptions: {
			bar: {
				dataLabels: {
					enabled: true,
					allowOverlap: true
				}
			}
		},
		legend: {
			layout: 'vertical',
			align: 'right',
			verticalAlign: 'top',
			x: -40,
			y: 100,
			floating: true,
			borderWidth: 1,
			backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
			shadow: true
		},
		credits: {
			enabled: false
		},
		series: [{
			name: '文件个数',
			data: JSON.parse('${cnts}')
		}]
	});
	</script>
</body>
</html>