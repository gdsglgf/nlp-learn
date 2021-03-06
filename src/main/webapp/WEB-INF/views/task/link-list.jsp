<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title></title>
	<link rel="stylesheet" href="${cdnUrl}/exts/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="${cdnUrl}/exts/datatables/jquery.dataTables.min.css">
	<link rel="stylesheet" href="${cdnUrl}/css/footer.css">
	<style type="text/css">
		tr td:nth-child(1) {
			width: 12%;
		}
		tr td:nth-child(2) {
			width: 20%;
		}
		tr td:nth-child(3) {
			width: 12%;
		}
		tr td:nth-child(4) {
			width: 12%;
		}
		tr td:nth-child(5) {
			width: 44%;
		}
	</style>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/header.jsp" %>
	<div id="main-content" class="container">
		<div class="page-header">
			<h3>实体链接页面</h3>
		</div>
		<div class="row">
			<%@ include file="/WEB-INF/views/include/sidebar.jsp" %>
			<div class="col-md-9" role="main">
				<div class="bs-docs-section">
					<form id="dataForm" class="form-horizontal" method="POST" onsubmit="onSubmit(); return false;">
						<div class="form-group">
							<label class="col-sm-2 control-label">命名实体</label>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="title" required>
							</div>
							<div class="col-sm-4">
								<button type="submit" class="btn btn-success">查询</button>
							</div>
						</div>
					</form>
				</div>
				<div class="bs-docs-section">
					<table id="example" class="table table-striped" style="display:none;">
						<thead>
							<th>编号</th>
							<th>地址</th>
							<th>链入</th>
							<th>链出</th>
							<th>标题</th>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	<%@ include file="/WEB-INF/views/include/footer.jsp" %>

	<script type="text/javascript" src="${cdnUrl}/exts/jquery/jquery.min.js"></script>
	<script type="text/javascript" src="${cdnUrl}/exts/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${cdnUrl}/exts/datatables/jquery.dataTables.min.js"></script>
	
	<script type="text/javascript">
		$('#infomgr').addClass('active');
		$('#link-list').addClass('active');
	</script>
	<script type="text/javascript">
		$.extend( true, $.fn.dataTable.defaults, {
			pagingType : "simple_numbers", // 设置分页控件的模式 simple_numbers, full_numbers
			lengthMenu : [ 10, 50, 100 ], //设置一页展示10条记录
			//lengthChange : false, //屏蔽tables的一页展示多少条记录的下拉列表
			searching: false, // 屏蔽datatales的查询框
			ordering: false,  // 屏蔽排序功能
			processing : true, // 打开数据加载时的等待效果
			serverSide : true, // 打开后台分页
			language: {
				url: "${cdnUrl}/exts/datatables/Chinese.json"
			},
		} );
	
		var table = null;
		function loadTable() {
			console.log('title:' + $('#title').val())
			table = $('#example').DataTable({
				ajax : {
					type: "POST",
					url : '<c:url value="/task/linkData.action" />',
					dataSrc : "aaData",
					data : function(data) {
						console.log("-----origin data------");
						console.log(JSON.stringify(data));
						
						// var formData = new Object();
						// formData = new Object();
						// formData.draw = data.draw;		// 请求次数
						// formData.start = data.start;	// 分页参数
						// formData.length = data.length;	// 每页显示的条数
						// formData.title = $('#title').val();

						var formData = {
							'draw': data.draw,
							'start': data.start,
							'length': data.length,
							'params': {
								'title': $('#title').val()
							}
						}
						
						console.log("-----form data------");
						console.log(JSON.stringify(formData));
						
						return formData;
					}
				},
				columns: [
					{ data: "id" },
					{ data: "url.url" },
					{ data: "in" },
					{ data: "out" },
					{ data: "title" },
				]
			});
		}

		function initTable() {
			loadTable();
			$("#example").css('display', 'block');
		}

		initTable();

		function onSubmit() {
			var title = $('#title').val();
			if ($.trim(title) == '') {
				alert('请输入命名实体!');
				$('#title')[0].focus();
				$('#title')[0].select();
			} else {
				table.ajax.reload();
			}
			
			return false;
		}
	</script>
</body>
</html>