<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<section class="card">
	<div class="cardHeader">
		<div class="title">208</div>
		<div class="errorMessage text-muted">Your last summitted informations contain duplicated value(s) that the server didn't allowed for processed.</div>
	</div>
	<div class="cardContent">
		<div class="errorType">Already Reported</div>
		<hr />
		<div class="cardFooter">
			<spring:url value="/" var="homeUrl" />
			<spring:url value="/login" var="loginUrl" />
			Contact your administrators (or) Go
			<a href="${header.referer}">Back</a>
			to your last submitted form.
		</div>
	</div>
</section>