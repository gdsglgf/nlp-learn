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
			<h3>任务监控信息</h3>
		</div>
		<div class="row">
			<%@ include file="/WEB-INF/views/include/sidebar.jsp" %>
			<div class="col-md-9" role="main">
				<div class="bs-docs-section">
					<div>
						<div id="container" style="min-width:400px;height:400px"></div>
					</div>
					<div>
						<form id="dataForm" class="form-horizontal" method="POST" onsubmit="onSubmit(); return false;">
							<div class="form-group">
								<div class="col-sm-offset-6 col-sm-4">
									<button type="submit" class="btn btn-success">刷新</button>
								</div>
							</div>
						</form>
					</div>
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
		$('#openie').addClass('active');
		$('#queue-report').addClass('active');
	</script>
	<script type="text/javascript">
		var chart = Highcharts.chart('container', {
            chart: {
                type: 'column'
            },
            title: {
                text: '消息队列统计信息'
            },
            xAxis: {
                categories: []
            },
            yAxis: {
                min: 0,
                title: {
                    text: '队列数量'
                },
                stackLabels: {
                    enabled: true,
                    style: {
                        fontWeight: 'bold',
                        color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                    }
                }
            },
            legend: {
                align: 'right',
                x: -30,
                verticalAlign: 'top',
                y: 25,
                floating: true,
                backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
                borderColor: '#CCC',
                borderWidth: 1,
                shadow: false
            },
    		credits: {
    			enabled: false
    		},
            tooltip: {
                formatter: function () {
                    return '<b>' + this.x + '</b><br/>' +
                        this.series.name + ': ' + this.y + '<br/>' +
                        '入队数量: ' + this.point.stackTotal;
                }
            },
            plotOptions: {
                column: {
                    stacking: 'normal',
                    dataLabels: {
                        enabled: true,
                        color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
                        style: {
                            textShadow: '0 0 3px black'
                        }
                    }
                }
            },
            series: [{
                name: '出队数量',
                data: []
            }, {
                name: '剩余数量',
                data: []
            }]
        });
	</script>
    <script type="text/javascript">
        function loadChart() {
            $.ajax({
                type: 'POST',
                url: '<c:url value="/task/queueData.action" />',
                dataType: 'JSON',
                success: function(result){
                    console.log(JSON.stringify(result));
                    chart.xAxis[0].setCategories(result['qname']);
                    chart.series[0].setData(result['deqSize']);
                    chart.series[1].setData(result['qsize']);
                }
            });
        }

        loadChart();

        function onSubmit() {
        	console.log("refresh...");
            loadChart();
        }
        // 30秒刷新一次
        setInterval("loadChart()", 30 * 1000);
    </script>
</body>
</html>