<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title></title>
	<link rel="stylesheet" href="${cdnUrl}/exts/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="${cdnUrl}/exts/datatables/jquery.dataTables.min.css">
	<link rel="stylesheet" href="${cdnUrl}/css/footer.css">
	<style type="text/css">
		.more-data {display: none;}
		.doc-space {margin-left:10px;margin-right:10pt;}
	</style>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/header.jsp" %>
	<div id="main-content" class="container">
		<div class="page-header">
			<h2>发布信息抽取任务</h2>
		</div>
		<div class="row">
			<%@ include file="/WEB-INF/views/include/sidebar.jsp" %>
			<div class="col-md-9" role="main">
				<div class="bs-docs-section">
					<form method="POST" onsubmit="onSubmit(); return false;">
						<div style="padding-bottom:10px;">
							<button type="button" id="selectAllBtn" class="btn btn-primary">全选</button>
							<button type="button" id="inverSelectionBtn" class="doc-space btn btn-primary">反选</button>
							<button type="button" id="cancleSelectionBtn" class="btn btn-primary">取消</button>
						</div>
						<table id="example" class="table table-striped display" cellspacing="0" width="100%">
							<thead>
								<th>#</th>
								<th>文件编号</th>
								<th>文件目录</th>
								<th>文件名</th>
								<th>文件字节数</th>
							</thead>
						</table>
						<a href="<c:url value="/task/run-all" />" class="btn btn-success">处理所有数据</a>
						<button type="submit" class="btn btn-success">发布任务</button>
					</form>
				</div>
			</div>
		</div>
	</div>
	
	<%@ include file="/WEB-INF/views/include/footer.jsp" %>

	<script type="text/javascript" src="${cdnUrl}/exts/jquery/jquery.min.js"></script>
	<script type="text/javascript" src="${cdnUrl}/exts/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${cdnUrl}/exts/datatables/jquery.dataTables.min.js"></script>
	<script type="text/javascript">
		$('#taskmgr').addClass('active');
		$('#publish').addClass('active');
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
	
		var table;
		$(document).ready(function() {
			table = $('#example').DataTable({
				ajax : {
					type: "POST",
					url : '<c:url value="/task/fileData.action" />',
					dataSrc : "aaData",
					data : function(data) {
						console.log("-----origin data------");
						console.log(JSON.stringify(data));
						
						var formData = new Object();
						formData.draw = data.draw;		// 请求次数
						formData.start = data.start;	// 分页参数
						formData.length = data.length;	// 每页显示的条数
						
						console.log("-----form data------");
						console.log(JSON.stringify(formData));
						
						return formData;
					}
				},
				columns: [
					{
						data: "fileId",
						render: function ( data, type, row, meta ) {
							return '<input type="checkbox" name="fileId" value="' + data + '">';
						}
					},
					{ data: "fileId" },
					{ data: "folder.folderPath" },
					{ data: "filename" },
					{ data: "bytesize" }
				]
			});
		});
	</script>
	<script type="text/javascript">
		$('#selectAllBtn').click(function() {
			$('input[name="fileId"]').prop("checked", true);
		});
		$('#inverSelectionBtn').click(function() {
			$('input[name="fileId"]').each(function() {
				this.checked = !this.checked;
			});
		});
		$('#cancleSelectionBtn').click(function() {
			// $('input[name="fileId"]').prop("checked", false);
			$('input[name="fileId"]').removeProp("checked");
		});
	</script>
	<script type="text/javascript">
		function onSubmit() {
			console.log('onSubmit');
			var checked = $('input[name="fileId"]:checked');
			if (checked.length == 0) {
				alert('未选择要处理的文件');
			} else {
				var fileIds = [];
				checked.each( function() {
					fileIds.push($(this).val());
				});
				console.log(fileIds);

				return doPublishAction(fileIds);
			}
		}
	</script>
	<script type="text/javascript">
		function doPublishAction(fileIds) {
			var postData = {
				'fileIds': fileIds
			};
			console.log(JSON.stringify(postData));
			$.ajax({
				type: 'POST',
				url: '<c:url value="/task/publish.action" />',
				data: postData,
				dataType: 'JSON',
				success: function(result){
					return processPublishResult(result);
				}
			});
		}
	</script>
	<script type="text/javascript">
		function processPublishResult(result) {
			if ( result['isSuccessful'] ) {
				var forwardUrl = result['forwardUrl'];
				window.location.href = forwardUrl;
			} else {
				
			}
		}
	</script>
</body>
</html>