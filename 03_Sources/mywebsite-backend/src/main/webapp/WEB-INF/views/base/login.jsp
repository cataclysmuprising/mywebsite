<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<tiles:importAttribute name="pageScripts" ignore="false" />
<tiles:importAttribute name="pageStyles" ignore="false" />
<c:set var="root" scope="application" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta content="IE=edge" http-equiv="X-UA-Compatible">
<meta content="width=device-width, initial-scale=1" name="viewport">
<meta content="" name="description">
<meta content="" name="author">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<link rel="shortcut icon" href="${root}/images/favicon.ico?v=2" type="image/x-icon" />
<title>My Website</title>
<c:choose>
	<c:when test="${isProduction}">
		<c:forEach var="attr" items="${pageStyles}">
			<link href="${root}/<c:out value='${attr}' />-min.css?v=${projectVersion}" rel="stylesheet">
		</c:forEach>
	</c:when>
	<c:otherwise>
		<c:forEach var="attr" items="${pageStyles}">
			<link href="${root}/<c:out value='${attr}' />.css?v=${projectVersion}" rel="stylesheet">
		</c:forEach>
	</c:otherwise>
</c:choose>
</head>
<body class="hold-transition login-page">
	<section class="login-box">
		<div class="login-logo">
			<img src="/mywebsite-backend/images/logo.png"> <span class="site-name">MyWebsite</span>
		</div>
		<div class="login-box-body">
			<p class="login-box-msg">Sign in to start your session</p>
			<form action="login" method="post">
				<c:if test="${not empty pageMessage}">
					<div class="alert alert-sm ${messageStyle} fade in">
						<a class="close" data-dismiss="alert" aria-label="close">&times;</a>
						<div class="message">${pageMessage}</div>
					</div>
				</c:if>
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				<div class="form-group has-feedback">
					<input type="text" name="loginId" class="form-control" placeholder="Login ID"> <span class="fa fa-key form-control-feedback"></span>
				</div>
				<div class="form-group has-feedback">
					<input type="password" name="password" class="form-control" placeholder="Password"> <span class="fa fa-lock form-control-feedback"></span>
				</div>
				<div class="row">
					<div class="col-xs-8">
						<div class="checkbox icheck">
							<label> <input type="checkbox" data-input-type="iCheck" name="remember-me"> <span class="lblRememberMe">Remember Me</span>
							</label>
						</div>
					</div>
					<div class="col-xs-4">
						<button type="submit" class="btn btn-primary btn-block btn-flat">Sign In</button>
					</div>
				</div>
			</form>
		</div>
	</section>
	<footer>
		<c:choose>
			<c:when test="${isProduction}">
				<c:forEach var="attr" items="${pageScripts}">
					<script type="text/javascript" src="${root}/<c:out value='${attr}' />-min.js?v=${projectVersion}"></script>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<c:forEach var="attr" items="${pageScripts}">
					<script type="text/javascript" src="${root}/<c:out value='${attr}' />.js?v=${projectVersion}"></script>
				</c:forEach>
			</c:otherwise>
		</c:choose>
		<script>
	      $('[data-input-type="iCheck"]').iCheck({
	        checkboxClass: 'icheckbox_minimal-blue',
	        radioClass: 'iradio_minimal-blue',
	      });
    	</script>
	</footer>
</body>
</html>