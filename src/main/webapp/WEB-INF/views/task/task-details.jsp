<!DOCTYPE html>
<html lang="en">
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
			<h3>查看信息抽取任务</h3>
		</div>
		<div class="row">
			<%@ include file="/WEB-INF/views/include/sidebar.jsp" %>
			<div class="col-md-9" role="main">
				<div class="bs-docs-section">
					<form method="POST" onsubmit="onSubmit(); return false;">
						<label>任务编号: ${task.taskId}</label>
						<input type="text" hidden name="taskId" value="${task.taskId}">
						<table class="table table-striped">
							<thead>
								<th>#</th>
								<th>文件编号</th>
								<th>硬盘编号</th>
								<th>文件目录</th>
								<th>文件名</th>
								<th>文件字节数</th>
							</thead>
							<tbody>
								<c:forEach items="${task.items}" var="item" varStatus="status">
									<tr>
										<th scope="row">${status.count}</th>
										<td>${item.file.fileId}</td>
										<td>${item.file.folder.diskId}</td>
										<td>${item.file.folder.folderPath}</td>
										<td>${item.file.filename}</td>
										<td>${item.file.bytesize}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<!-- <button type="submit" class="btn btn-success">启动任务</button> -->
						<a href="<c:url value="/task/running/${task.taskId}" />" class="btn btn-success">启动任务</a>
					</form>
				</div>
			</div>
		</div>
	</div>
	
	<%@ include file="/WEB-INF/views/include/footer.jsp" %>

	<script type="text/javascript" src="${cdnUrl}/exts/jquery/jquery.min.js"></script>
	<script type="text/javascript" src="${cdnUrl}/exts/bootstrap/js/bootstrap.min.js"></script>

	<script type="text/javascript">
		$('#taskmgr').addClass('active');
		$('#publish').addClass('active');
	</script>
</body>
</html>