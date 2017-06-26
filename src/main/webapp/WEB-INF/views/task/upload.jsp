<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title></title>
	<link rel="stylesheet" href="${cdnUrl}/exts/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="${cdnUrl}/css/footer.css">
	<style type="text/css">
		.alert {
			outline:red Solid 1px;
		}
	</style>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/header.jsp" %>
	<div id="main-content" class="container">
		<div class="page-header">
			<h3>上传数据目录</h3>
		</div>
		<div class="row">
			<%@ include file="/WEB-INF/views/include/sidebar.jsp" %>
			<div class="col-md-9" role="main">
				<form id="fileUploadForm" class="form-horizontal" method="POST" onsubmit="onSubmit(); return false;">
					<div class="form-group">
						<div id="alert-error" class="col-sm-offset-2 col-sm-6 bg-info text-danger" style="display:none;padding: 10px;">文件夹路径不存在</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">硬盘编号</label>
						<div class="col-sm-6">
							<label class="radio-inline">
								<input type="radio" name="diskId" value="1" checked>1
							</label>
							<label class="radio-inline">
								<input type="radio" name="diskId" value="2">2
							</label>
							<label class="radio-inline">
								<input type="radio" name="diskId" value="3">3
							</label>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">文件类型</label>
						<div class="col-sm-6">
							<label class="radio-inline">
								<input type="radio" name="fileType" id="fileType" value="bz2" checked>bz2
							</label>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">文件夹路径</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" name="filePath" required>
						</div>
						<div class="col-sm-2">
							<input type="button" class="btn btn-primary" value="添加文件路径" onclick="addPath()">
						</div>
					</div>
					<div id="folders">
						<!-- <div class="form-group" id="other">
							<div class="col-sm-offset-2 col-sm-6">
								<input type="text" class="form-control" name="filePath">
							</div>
							<div class="col-sm-2">
								<input type="button" class="btn btn-primary" value="-" onclick="removePath(document.getElementById('other'))">
							</div>
						</div> -->
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="submit" class="btn btn-success">上传</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/views/include/footer.jsp" %>

	<script type="text/javascript" src="${cdnUrl}/exts/jquery/jquery.min.js"></script>
	<script type="text/javascript" src="${cdnUrl}/exts/bootstrap/js/bootstrap.min.js"></script>

	<script type="text/javascript">
		$('#taskmgr').addClass('active');
		$('#upload').addClass('active');
	</script>
	<script type="text/javascript">
		var id = 0;

		function addPath() {
			id++;
			var path = 'path' + id;
			var html = '<div class="form-group" id="' + path + '"><div class="col-sm-offset-2 col-sm-6"><input type="text" class="form-control" name="filePath" required></div><div class="col-sm-2"><input type="button" class="btn btn-primary" value="-" onclick="removePath(' + path + ')"></div></div>'
			$('#folders').append(html);
		}

		function removePath(tag) {
			console.log(tag);
			var other = document.getElementById('folders');
			other.removeChild(tag);
		}
	</script>
	<script type="text/javascript">
		function getAllFilePath() {
			var filePath = [];
			var tags = $('input[name="filePath"]');
			for (var i = 0; i < tags.length; i++) {
				filePath[i] = tags[i].value;
			}
			// tags.each(function () {
			// 	filePath.push($(this).val());
			// });
			return filePath;
		}

		function onSubmit() {
			$("#alert-error").css('display', 'none');
			$('button[type=submit]').attr('disabled', 'disabled');

			var diskId = $('input:radio[name="diskId"]:checked').val(),
				fileType = $('input:radio[name="fileType"]:checked').val(),
				filePath = getAllFilePath();

			return doUploadAction(diskId, fileType, filePath);
		}
	</script>
	<script type="text/javascript">
		function doUploadAction(diskId, fileType, filePath) {
			var postData = {
				'diskId': diskId,
				'fileType': fileType,
				'filePath': filePath
			};

			console.log(JSON.stringify(postData));

			$.ajax({
				type: 'POST',
				url: '<c:url value="/task/upload.action" />',
				data: postData,
				dataType: 'JSON',
				success: function(result){
					return processUploadResult(result);
				}
			});
		}
	</script>
	<script type="text/javascript">
		function processUploadResult (result) {
			console.log(JSON.stringify(result));
			if ( result['isSuccessful'] ) {
				var forwardUrl = '<c:url value="/task/upload-details" />';
				window.location.href = forwardUrl;
			} else {
				$("#alert-error").css('display', 'block');
				var status = result['status'];
				var tags = $('input[name="filePath"]');
				for (var i = 0; i < status.length; i++) {
					if (status[i]) {
						$(tags[i]).removeClass('alert');
					} else {
						$(tags[i]).addClass('alert');
					}
				}
			}
			$('button[type=submit]').removeAttr('disabled');
		}
	</script>
</body>
</html>