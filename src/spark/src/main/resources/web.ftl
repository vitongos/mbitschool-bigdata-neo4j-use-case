<html>
	<head>
		<title>App ${appId}</title>
		<style>
			.circular1 {
				width: 6.0rem;
				height: 6.0rem;
				font-size: 3em;
				border: 1px solid #748341;
				background-color: #748341;
				border-radius: 3em;
				display: inline-block;
				margin: 0 0.5em;
				line-height: 2em;
				text-align: center;
				color: white;
			}
			a {
				color: white;
				text-decoration: none;
				cursor: cursor;
			}
			.circular2 {
				width: 36.0rem;
				height: 36.0rem;
				font-size: 18em;
				border: 1px solid #411383;
				background-color: #411383;
				border-radius: 3em;
				display: inline-block;
				margin: 0 0.2em;
				line-height: 2em;
				text-align: center;
				color: white;
			}
		</style>
	</head>
	<body>
		<ul>
		<#list apps as app>
			<li class="circular1"><a href="/app/${app}">${app}</a></li>
		</#list>
		</ul>
		<div class="circular2">${appId}</div>
	</body>
</html>