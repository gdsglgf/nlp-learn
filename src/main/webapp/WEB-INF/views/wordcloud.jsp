<!DOCTYPE html>
<!-- https://www.jasondavies.com/wordcloud/ -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Word Cloud Generator</title>
<style>
body {
	position: relative;
	font-family: "Helvetica Neue", sans-serif;
	width: 960px;
	margin: auto;
	margin-bottom: 1em;
	margin-top: 20px;
}
#presets a { border-left: solid #666 1px; padding: 0 10px; }
#presets a.first { border-left: none; }
#keyword { width: 300px; }
#fetcher { width: 500px; }
#keyword, #go { font-size: 1.5em; }
#text { width: 100%; height: 100px; }
p.copy { font-size: small; }
#form { font-size: small; position: relative; }
hr { border: none; border-bottom: solid #ccc 1px; }
a.active { text-decoration: none; color: #000; font-weight: bold; cursor: text; }
#angles line, #angles path, #angles circle { stroke: #666; }
#angles text { fill: #333; }
#angles path.drag { fill: #666; cursor: move; }
#angles { text-align: center; margin: 0 auto; width: 350px; }
#angles input, #max { width: 42px; }
</style>

</head>
<body>
	<div id="vis"></div>
	<form id="form">
		<p style="position: absolute; right: 0; top: 0" id="status"></p>
		<div style="text-align: center">
			<div id="presets"></div>
			<div id="custom-area">
				<p>
					<label for="text">Paste your text below!</label>
				</p>
				<p>
					<textarea id="text">"I never did anything worth doing by accident, nor did any of my inventions come indirectly through accident, except the phonograph. No, when I have fully decided that a result is worth getting, I go about it, and make trial after trial, until it comes." – Thomas Edison, Inventor</textarea>
					<input type="button" id="go" value='Go!'>
				</p>
			</div>
		</div>

		<hr>

		<div style="float: right; text-align: right">
			<p><label for="max">Number of words:</label> <input type="number" value="250" min="1" id="max">
			<p><label for="per-line"><input type="checkbox" id="per-line"> One word per line</label>
			<!--<p><label for="colours">Colours:</label> <a href="#" id="random-palette">get random palette</a>-->
			<p><label>Download:</label>
			<button id="download-svg">SVG</button><!-- |
			<a id="download-png" href="#">PNG</a>-->
		</div>

		<div style="float: left">
			<p><label>Spiral:</label>
			<label for="archimedean"><input type="radio" name="spiral" id="archimedean" value="archimedean" checked="checked"> Archimedean</label>
			<label for="rectangular"><input type="radio" name="spiral" id="rectangular" value="rectangular"> Rectangular</label>
			<p><label for="scale">Scale:</label>
			<label for="scale-log"><input type="radio" name="scale" id="scale-log" value="log" checked="checked"> log n</label>
			<label for="scale-sqrt"><input type="radio" name="scale" id="scale-sqrt" value="sqrt"> √n</label>
			<label for="scale-linear"><input type="radio" name="scale" id="scale-linear" value="linear"> n</label>
			<p><label for="font">Font:</label> <input type="text" id="font" value="Impact">
		</div>

		<div id="angles">
			<p><input type="number" id="angle-count" value="5" min="1"> <label for="angle-count">orientations</label>
			<label for="angle-from">from</label> <input type="number" id="angle-from" value="-60" min="-90" max="90"> °
			<label for="angle-to">to</label> <input type="number" id="angle-to" value="60" min="-90" max="90"> °
		</div>

		<hr style="clear: both">

		<p style="float: right"><a href="about/">How the Word Cloud Generator Works</a>.
		<p style="float: left">Copyright &copy; <a href="http://www.jasondavies.com/">Jason Davies</a> | <a href="../privacy/">Privacy Policy</a>. The generated word clouds may be used for any purpose.

	</form>

	<script type="text/javascript" src="${cdnUrl}/exts/jquery/jquery.min.js"></script>
	<script src="${cdnUrl}/exts/d3/d3.min.js"></script>
	<script src="${cdnUrl}/exts/d3/cloud.min.js"></script>

	<script type="text/javascript">
		function isASCIIText(text) {
			var isASCIIText = true;
			var ASCIIRegex = /[\x00-\xff]/g;
			for (var i = 0; i < text.length; i++) {
				var singleChar = text.charAt(i).toString();
				if (singleChar.match(ASCIIRegex) == null) {
					isASCIIText = false;
					break;
				}
			}
			return isASCIIText;
		}
	</script>

	<script type="text/javascript">
		$('#go').click(function () {
			var text = $('#text').val();
			if (text == '') {
				alert('text is empty.');
				$('#text').focus();
				return;
			}

			if (isASCIIText(text)) {
				parseText(text);
				d3.event.preventDefault();
			} else {
				$.ajax({
					type: 'POST',
					url: '<c:url value="/removeStopWord.action" />',
					data: {'text': text},
					success: function(result) {
						console.log('get data from server');
						var data = result.reduce(function(a, b){return a + ' ' + b});
						parseText(data);
						d3.event.preventDefault();
					}
				});
			}
		});
	</script>
</body>
</html>