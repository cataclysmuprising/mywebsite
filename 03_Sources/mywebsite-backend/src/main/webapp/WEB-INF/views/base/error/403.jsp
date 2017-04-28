<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<section class="card">
	<div class="cardHeader">
		<div class="title">403</div>
		<div class="errorMessage text-muted">
			Sorry <span class="userName"><c:out value="${loginUserName}"></c:out></span> ! You don't have permission to access this content.
		</div>
	</div>
	<div class="cardContent">
		<div class="errorType">Access Denied !</div>
		<hr />
		<div class="cardFooter">
			<spring:url value="/" var="homeUrl" />
			<spring:url value="/login" var="loginUrl" />
			Goback to
			<a href="${homeUrl}">Home</a>
			(or)
			<a href="${loginUrl}">Sign In</a>
			with different Account.
		</div>
	</div>
</section>