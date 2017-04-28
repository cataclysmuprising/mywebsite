<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<section class="card">
	<div class="cardHeader">
		<div class="title">226</div>
		<div class="errorMessage text-muted">Rejected : Unable to remove your requested informations because this was connected with other resources.If you try to forcely remove
			it, the entire application will loose consistency.
		</div>
	</div>
	<div class="cardContent">
		<div class="errorType">I'm still in Used</div>
		<hr />
		<div class="cardFooter">
			<spring:url value="/" var="homeUrl" />
			<spring:url value="/login" var="loginUrl" />
			Contact your administrators (or) Go
			<a href="${header.referer}">Back</a>
			to your previous page.
		</div>
	</div>
</section>