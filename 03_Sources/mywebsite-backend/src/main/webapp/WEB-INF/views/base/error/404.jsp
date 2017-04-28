<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<section class="card">
	<div class="cardHeader">
		<div class="title">404</div>
		<div class="errorMessage text-muted">Error ! Your search page that cannot be available.</div>
	</div>
	<div class="cardContent">
		<div class="errorType">Page Not Found !</div>
		<hr />
		<div class="cardFooter">
			<spring:url value="/" var="homeUrl" />
			Goback to
			<a href="${homeUrl}">Home</a>
			(or) check again your requested URL.
		</div>
	</div>
</section>
