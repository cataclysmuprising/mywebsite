<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<section class="card">
	<div class="cardHeader">
		<div class="title">500</div>
		<div class="errorMessage text-muted">Error! Some Problem might occurs in server.</div>
	</div>
	<div class="cardContent">
		<div class="errorType">Server crush !</div>
		<hr />
		<div class="cardFooter">
			<spring:url value="/" var="homeUrl" />
			Goback to
			<a href="${homeUrl}">Home</a>
			(or) Contact Administrators
		</div>
	</div>
</section>