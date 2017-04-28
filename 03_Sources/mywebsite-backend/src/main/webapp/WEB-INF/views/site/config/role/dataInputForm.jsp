<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!-- Content Header (Page header) -->
<section class="content-header">
	<h1>
		Role Informations
		<small>
			<c:choose>
				<c:when test="${pageMode eq 'CREATE' }">
				(Create a new Role)
			</c:when>
				<c:otherwise>
				(Edit existing Role)
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
			<a href="${roleHomeUrl}">
				<i class="fa fa-universal-access"></i> Role
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
			<c:set var="submitUrl" value="/roles/add" />
			<c:if test="${pageMode eq 'EDIT'}">
				<c:set var="submitUrl" value="/roles/${role.id}/edit" />
			</c:if>
			<spring:url value="${submitUrl}" var="roleSubmitUrl" />
			<form:form class="form-horizontal" modelAttribute="role" id="roleForm" action="${roleSubmitUrl}" method="post" commandName="role">
				<div class="box box-primary">
					<div class="box-header with-border">
						<div class="form-group">
				      		<label class="col-md-2 control-label required" for="name">Name : </label>
				      		<div class="col-md-4">
					      		<div class="input-group"> 
					      			<span class="input-group-addon"><i class="fa fa-id-badge"></i></span>
					      			<form:input path="name" type="text" class="form-control input-sm" id="name"/>
					      		</div>				      		
				      		</div>
				     	</div>
						<div class="form-group">
				      		<label class="col-md-2 control-label" for="description">Description : </label>
				      		<div class="col-md-6"> 
				      			<form:textarea path="description" rows="5" class="form-control input-sm" id="description"></form:textarea>
				      		</div>
				     	</div>
						<div class="form-group col-md-12">
							<div class="input-fieldless">
	 							<form:hidden id="userIds" path="userIds"/>
								<form:hidden id="actionIds" path="actionIds"/>	
								<blockquote>
								</blockquote>						
							</div>
						</div>						
					</div>
					<div class="box-body">
						<div class="nav-tabs-custom" id="tabView">
							<ul class="nav nav-tabs">
								<li class="active">
									<a data-toggle="tab" data-target="#userTab" href="">
										<span class="detail-tab-header">Member</span>
									</a>
								</li>
								<li class="loginHistory">
									<a data-toggle="tab" data-target="#actionTab" href="">
										<span class="detail-tab-header">Access</span>
									</a>
								</li>
							</ul>
							<div class="tab-content">
								<div id="userTab" class="tab-pane fade in active">
									<div class="col-md-12 filterPanel">
										<div class="col-sm-4">
											<input id="user-keyword" placeholder="Search..." type="text" class="form-control input-sm">
										</div>
										<div class="col-sm-8">
											<button type="button" class="btn btn-primary btn-flat btn-sm btn-social" id="btnUserSearch">
												<i class="fa fa-search"></i>Search
											</button>
											<button type="button" class="btn bg-olive btn-flat btn-sm btn-social" id="btnUserReset">
												<i class="fa fa-recycle"></i>Reset
											</button>
										</div>
									</div>
									<table id="userTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
										<thead>
											<tr>
												<th class="select_all_header"><input class="select_all" data-input-type="iCheck" type="checkbox"></th>
												<th data-id="loginId">Login Id</th>
												<th data-id="name">User Name</th>
												<th>Email</th>
												<th>NRC</th>
											</tr>
										</thead>
										<tbody></tbody>
									</table>
								</div>
								<div id="actionTab" class="tab-pane fade">
									<div class="col-md-12 filterPanel">
										<div class="col-sm-4">
											<input id="access-keyword" placeholder="Search..." type="text" class="form-control input-sm">
										</div>
										<div class="col-sm-2">
											<select id="pages" class="form-control input-sm selectpicker show-tick" title="Pages"></select>
										</div>										
										<div class="col-sm-6">
											<button type="button" class="btn btn-primary btn-flat btn-sm btn-social" id="btnAccessSearch">
												<i class="fa fa-search"></i>Search
											</button>
											<button type="button" class="btn bg-olive btn-flat btn-sm btn-social" id="btnAccessReset">
												<i class="fa fa-recycle"></i>Reset
											</button>
										</div>
									</div>
									<table id="accessTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
										<thead>
											<tr>
												<th><input data-input-type="iCheck" type="checkbox"></th>
												<th data-id="page">Page</th>
												<th>Action Name</th>
												<th>Description</th>
											</tr>
										</thead>
										<tbody></tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
					<div class="box-footer text-center">
						<button class="btn bg-olive btn-flat small-margin btn-sm btn-social" type="button" id="btnReset">
							<i class="fa fa-recycle"></i>Reset
						</button>
						<button class="btn bg-navy btn-flat small-margin btn-sm btn-social" type="button" id="btnSave">
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
