<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登录</title>
<link rel="stylesheet" type="text/css"
	href="/static/js/lib/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css"
	href="/static/js/lib/bootstrap/css/bootstrap-theme.min.css" />
<script type="text/javascript" src="/static/js/lib/jquery/jquery-min.js"></script>
<script type="text/javascript"
	src="/static/js/lib/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
	<div class="container">
		<div class="panel panel-success">
			<div class="panel-heading">
				<div class="panel-title">用户登录</div>
			</div>
			<div class="panel-body">
				<p>j-spring-security-check</p>
				<form class="form-horizontal" action="/login"
					method="post">
					<div class="form-group">
						<label for="inputEmail3" class="col-sm-2 control-label">用户名</label>
						<div class="col-sm-10">
							<input type="text" name="username" class="form-control"
								id="inputEmail3" placeholder="Email">
						</div>
					</div>
					<div class="form-group">
						<label for="inputPassword3" class="col-sm-2 control-label">密码</label>
						<div class="col-sm-10">
							<input type="password" name="password" class="form-control"
								id="inputPassword3" placeholder="Password">
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<div class="checkbox">
								<label> <input type="checkbox" name="remember-me">
									记住我
								</label>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="submit" class="btn btn-default">登录</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

</body>
</html>