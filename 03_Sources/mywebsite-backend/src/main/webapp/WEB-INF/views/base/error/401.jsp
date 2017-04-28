<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<section class="card">
	<div class="cardHeader">
		<div class="title">401</div>
		<div class="errorMessage text-muted">
			Sorry <span class="userName"><c:out value="${loginUserName}"></c:out></span> ! You were <strong>AFK</strong><em> (away from keyboard)</em> for a long-time.Your session was
			expired.
		</div>
	</div>
	<div class="cardContent">
		<div class="errorType">Session Time Out !</div>
		<hr />
		<div class="cardFooter">
			<spring:url value="/" var="homeUrl" />
			<spring:url value="/login" var="loginUrl" />
			<a href="${loginUrl}">Re-login</a>
			with a new session (or)
			<a href="${header.referer}">Redirect</a>
			to your last page.
		</div>
	</div>
</section>