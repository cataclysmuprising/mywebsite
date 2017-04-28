<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!-- Content Header (Page header) -->
<section class="content-header">
	<h1>
		Setting Informations
		<small>
			<c:choose>
				<c:when test="${pageMode eq 'CREATE' }">
				(Create a new Setting)
			</c:when>
				<c:otherwise>
				(Edit existing Setting)
			</c:otherwise>
			</c:choose>
		</small>
	</h1>
	<ol class="breadcrumb">
		<li>
			<a href="${homeUrl}">
				<i class="fa fa-home"></i> Home
			</a>
		</li>
		<li>
			<a href="${settingHomeUrl}">
				<i class="fa fa-gear"></i> Setting
			</a>
		</li>
		<li class="active">
			<c:choose>
				<c:when test="${pageMode eq 'CREATE' }">
					Add New
				</c:when>
				<c:otherwise>
					Edit
				</c:otherwise>
			</c:choose>
		</li>
	</ol>
</section>
<!-- Main content -->
<section class="content">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<c:set var="submitUrl" value="/settings/add" />
			<c:if test="${pageMode eq 'EDIT'}">
				<c:set var="submitUrl" value="/settings/${setting.id}/edit" />
			</c:if>
			<spring:url value="${submitUrl}" var="settingSubmitUrl" />
			<form:form modelAttribute="setting" id="settingForm" action="${settingSubmitUrl}" method="post" commandName="setting">
				<div class="box box-primary row smallPanel">
					<div class="box-header with-border text-center">
						<h3>Please fill-up required informations</h3>
					</div>
					<div class="box-body">
						<div class="row">
							<div class="col-sm-12 form-group">
								<label for="group" class="required">Group :</label>
								<form:input type="text" id="group" path="group" class="form-control input-sm" />
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12 form-group">
								<label for="subGroup" class="required">Sub Group :</label>
								<form:input type="text" id="subGroup" path="subGroup" class="form-control input-sm" />
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12 form-group">
								<label for="type" class="required">Type :</label>
								<form:input type="text" id="type" path="type" class="form-control input-sm" />
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12 form-group">
								<label for="name" class="required">Name :</label>
								<c:choose>
									<c:when test="${pageMode eq 'CREATE'}">
										<form:input type="text" id="name" path="name" class="form-control input-sm" />
									</c:when>
									<c:otherwise>
										<form:input type="text" id="name" path="name" class="form-control input-sm" readonly="true" tabindex="-1" />
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12 form-group">
								<label for="value" class="required">Value :</label>
								<form:input type="text" id="value" path="value" class="form-control input-sm" />
							</div>
						</div>
					</div>
					<div class="box-footer text-center">
						<button class="btn bg-olive btn-flat small-margin btn-sm btn-social" type="reset" id="btnReset">
							<i class="fa fa-recycle"></i>Reset
						</button>
						<button class="btn bg-navy btn-flat small-margin btn-sm btn-social" type="submit" id="btnSave">
							<i class="fa fa-save"></i>Save
						</button>
						<button class="btn btn-danger btn-flat small-margin btn-sm btn-social cancelButton" type="button" id="btnCancel">
							<i class="fa fa-undo"></i>Cancel
						</button>
					</div>
				</div>
			</form:form>
		</div>
	</div>
</section>
