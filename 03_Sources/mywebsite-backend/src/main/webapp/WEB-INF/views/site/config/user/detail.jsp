<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!-- Content Header (Page header) -->
<section class="content-header">
	<h1>User Informations</h1>
	<ol class="breadcrumb">
		<li>
			<a href="${homeUrl}">
				<i class="fa fa-home"></i> Home
			</a>
		</li>
		<li>
			<a href="${userHomeUrl}">
				<i class="fa fa-users"></i> Users
			</a>
		</li>
		<li class="active">${user.name}</li>
	</ol>
</section>
<section class="content">
	<div class="row">
		<div class="nav-tabs-custom" id="tabView">
			<ul class="nav nav-tabs">
				<li class="active">
					<a data-toggle="tab" data-target="#userDetails" href="">
						<span class="detail-tab-header">Personal Data</span>
					</a>
				</li>
				<li class="loginHistory">
					<a data-toggle="tab" data-target="#loginHistories" href="">
						<span class="detail-tab-header">Login History</span>
					</a>
				</li>
			</ul>
			<div class="tab-content">
				<div id="userDetails" class="tab-pane fade in active">
					<input type="hidden" id="userId" value="${user.id}">
					<div class="box box-primary smallPanel">
						<div class="box-body box-profile">
							<img class="profile-user-img img-responsive img-circle" src="${root}/files/${user.contentId}" width="150px" alt="">
							<h3 class="profile-username text-center">${user.name}</h3>
							<p class="text-muted text-center">(${user.email})</p>
							<ul class="list-group list-group-unbordered">
								<li class="list-group-item">
									<b>NRC</b>
									<a class="pull-right">${user.nrc}</a>
								</li>
								<li class="list-group-item">
									<b>Phone</b>
									<a class="pull-right">${user.phone}</a>
								</li>
								<li class="list-group-item">
									<b>Gender</b>
									<a class="pull-right">${user.gender}</a>
								</li>
								<li class="list-group-item">
									<b>DOB</b>
									<a class="pull-right">${user.dobAsString}</a>
								</li>
								<li class="list-group-item">
									<b>Age</b>
									<a class="pull-right">${user.age}</a>
								</li>
								<li class="list-group-item">
									<b>Roles</b>
									<a class="pull-right">
										<c:forEach var="role" items="${user.roles}" varStatus="status">
											<c:out value="${role.name}"></c:out>
											<c:if test="${not status.last}">
				       							,
				       						</c:if>
										</c:forEach>
									</a>
								</li>
								<li class="list-group-item">
									<b>Address</b>
									<a class="pull-right">${user.address}</a>
								</li>
							</ul>
							<a href="${root}/download/user/${user.id}" class="btn btn-primary btn-block">
								<b>Download</b>
							</a>
						</div>
					</div>
				</div>
				<div id="loginHistories" class="tab-pane fade">
					<table id="loginHistoryTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
						<thead>
							<tr>
								<th>Client IP</th>
								<th>Operating System</th>
								<th>User Agent</th>
								<th>Login Date/Time</th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>