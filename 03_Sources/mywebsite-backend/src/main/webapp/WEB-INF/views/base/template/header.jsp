<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<header class="main-header">
	<input type="hidden" id="pageMode" value="${pageMode}">
	<c:if test="${not empty pageMessage}">
		<span id="pageMessage" data-title="${pageMessage.title}" data-info="${pageMessage.message}" data-style="${pageMessage.style}" style="display: none;"></span>
	</c:if>
	<c:forEach var="access" items="${accessments}">
		<input type="hidden" id="${access.key}" value="${access.value}">
	</c:forEach>
	<div id="validationErrors" style="display: none;">
		<c:forEach items="${validationErrors}" var="item">
			<span class="error-item" data-id="${item.key}" data-error-message="${item.value}"></span>
		</c:forEach>
	</div>
	<a class="scrollToTop">
		<i class="glyphicon glyphicon-chevron-up"></i>
	</a>
	<!-- Logo -->
	<spring:url value="/" var="homeUrl" scope="application" />
	<a href="${homeUrl}" class="logo">
		<!-- mini logo for sidebar mini 50x50 pixels -->
		<div class="logo-mini">
			<img src="${root}/images/logo.png">
		</div>
		<!-- logo for regular state and mobile devices -->
		<div class="logo-lg">
			<img src="${root}/images/logo.png"> <span class="site-name">MyWebsite</span>
		</div>
	</a>
	<!-- Header Navbar: style can be found in header.less -->
	<nav class="navbar navbar-static-top">
		<!-- Sidebar toggle button-->
		<a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
			<span class="sr-only">Toggle navigation</span>
		</a>
		<div class="navbar-custom-menu">
			<ul class="nav navbar-nav">
				<!-- User Account: style can be found in dropdown.less -->
				<li class="dropdown user user-menu">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown">
						<img class="user-image user-profile-image" src="${root}/files/${contentId}" alt=""> <span class="hidden-xs"><c:out value="${loginUserName}" default="Guest" /></span>
					</a>
					<ul class="dropdown-menu">
						<!-- User image -->
						<li class="user-header">
							<img class="img-circle" height="30px" src="${root}/files/${contentId}" alt="">
							<p>
								<c:out value="${loginUserName}" default="Guest" />
								<small>Member since ${since}</small>
							</p>
						</li>
						<!-- Menu Footer-->
						<li class="user-footer">
							<sec:authorize access="hasAnyAuthority(${userProfile})">
								<div class="pull-left">
									<spring:url value="/users/profile" var="profileUrl" scope="application" />
									<a href="${profileUrl}" class="btn btn-default btn-flat">Profile</a>
								</div>
								<div class="pull-right">
									<c:if test="${not empty loginUserId}">
										<input id="loginUserId" type="hidden" value="${loginUserId}" />
									</c:if>
									<spring:url value="${not empty loginUserName ? '/logout': '/login'}" var="authenticationUrl" />
									<a class="btn btn-default btn-flat" href="${authenticationUrl}"> ${not empty loginUserName ? 'Sign out': 'Sign in'} </a>
								</div>										
							</sec:authorize>
							<sec:authorize access="!hasAnyAuthority(${userProfile})">
								<div class="text-center">
									<c:if test="${not empty loginUserId}">
										<input id="loginUserId" type="hidden" value="${loginUserId}" />
									</c:if>
									<spring:url value="${not empty loginUserName ? '/logout': '/login'}" var="authenticationUrl" />
									<a class="btn btn-default btn-flat" href="${authenticationUrl}"> ${not empty loginUserName ? 'Sign out': 'Sign in'} </a>
								</div>							
							</sec:authorize>							
						</li>
					</ul>
				</li>
				<!-- Control Sidebar Toggle Button -->
				<li>
					<a href="#" data-toggle="control-sidebar">
						<i class="fa fa-gears"></i>
					</a>
				</li>
			</ul>
		</div>
	</nav>
</header>