<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<c:set var="root" scope="application" value="${pageContext.request.contextPath}" />
<tiles:importAttribute name="layoutScripts" ignore="false" />
<tiles:importAttribute name="pageScripts" ignore="false" />
<tiles:importAttribute name="layoutStyles" ignore="false" />
<tiles:importAttribute name="pageStyles" ignore="false" />
<!DOCTYPE html>
<html lang="en">
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
		<link href="${root}/css/common/style-min.css?v=${projectVersion}" rel="stylesheet">
		<link href="${root}/css/common/theme-min.css?v=${projectVersion}" rel="stylesheet">
		<link href="${root}/css/common/base-min.css?v=${projectVersion}" rel="stylesheet">
		<c:forEach var="attr" items="${pageStyles}">
			<link href="${root}/<c:out value='${attr}' />-min.css?v=${projectVersion}" rel="stylesheet">
		</c:forEach>
	</c:when>
	<c:otherwise>
		<c:forEach var="attr" items="${layoutStyles}">
			<link href="${root}/<c:out value='${attr}' />.css?v=${projectVersion}" rel="stylesheet">
		</c:forEach>
		<c:forEach var="attr" items="${pageStyles}">
			<link href="${root}/<c:out value='${attr}' />.css?v=${projectVersion}" rel="stylesheet">
		</c:forEach>
	</c:otherwise>
</c:choose>
<script>
        window.paceOptions = {
            ajax: {
                trackMethods: ['GET', 'POST']
            }
        };
    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini layout-boxed">
	<!-- Preloader -->
	<div id="preloader">
		<div id="status">
			<img src="${root}/images/gears.gif"></img>
		</div>
	</div>
	<div class="wrapper">
		<tiles:insertAttribute name="header" />
		<tiles:insertAttribute name="leftMenu" />
		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper">
			<tiles:insertAttribute name="content" />
		</div>
		<tiles:insertAttribute name="footer" />
		<tiles:insertAttribute name="rightMenu" />
		<div class="control-sidebar-bg"></div>
	</div>
	<c:choose>
		<c:when test="${isProduction}">
			<script type="text/javascript" src="${root}/js/common/main-min.js?v=${projectVersion}"></script>
			<c:forEach var="attr" items="${pageScripts}">
				<script type="text/javascript" src="${root}/<c:out value='${attr}' />-min.js?v=${projectVersion}"></script>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<c:forEach var="attr" items="${layoutScripts}">
				<script type="text/javascript" src="${root}/<c:out value='${attr}' />.js?v=${projectVersion}"></script>
			</c:forEach>
			<c:forEach var="attr" items="${pageScripts}">
				<script type="text/javascript" src="${root}/<c:out value='${attr}' />.js?v=${projectVersion}"></script>
			</c:forEach>
		</c:otherwise>
	</c:choose>
</body>