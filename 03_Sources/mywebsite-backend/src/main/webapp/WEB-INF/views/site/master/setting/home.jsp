<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!-- Content Header (Page header) -->
<section class="content-header">
	<h1>
		Setting
		<small>(view all setting informations)</small>
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
		<li class="active">Setting List</li>
	</ol>
</section>
<!-- Main content -->
<section class="content">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="box box-primary">
				<div class="box-header with-border">
					<form class="form-horizontal control-block" role="form" method="post">
						<div class="col-sm-4">
							<input id="keyword" placeholder="Search..." type="text" class="form-control input-sm">
						</div>
						<div class="col-sm-8">
							<button type="button" class="btn btn-primary btn-flat btn-sm btn-social" id="btnSearch">
								<i class="fa fa-search"></i>Search
							</button>
							<button type="button" class="btn bg-olive btn-flat btn-sm btn-social" id="btnReset">
								<i class="fa fa-recycle"></i>Reset
							</button>
							<spring:url value="/settings/add" var="settingAddUrl" />
							<sec:authorize access="hasAnyAuthority(${settingAdd})">
								<a href="${settingAddUrl}" class="btn btn-default btn-flat btn-sm btn-social btn-add-new" role="button">
									<i class="fa fa-plus"></i>Add New
								</a>
							</sec:authorize>
						</div>
					</form>
				</div>
				<div class="box-body">
					<table id="settingTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
						<thead>
							<tr>
								<th data-id="group">Group</th>
								<th data-id="subGroup">Sub Group</th>
								<th data-id="name">Name</th>
								<th>Value</th>
								<th>Type</th>
								<sec:authorize access="hasAnyAuthority(${settingEdit}${not empty settingEdit ? ',':''}${settingRemove})">
									<th class="functionColumn">Function</th>
								</sec:authorize>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>
<div id="deleteConfirmModal" class="modal fade">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">Confirmation</h4>
			</div>
			<div class="modal-body">
				<p class="text-center">Are you sure you want to remove this record ?</p>
				<p class="text-warning text-center">We have to make sure about this because this process can't retain the informations.</p>
			</div>
			<div class="modal-footer">
				<button type="button" id="confirmDelete" class="btn btn-sm btn-primary">Yes, I'm sure.</button>
				<button type="button" class="btn btn-sm btn-danger" data-dismiss="modal" style="width: 50px;">No</button>
			</div>
		</div>
	</div>
</div>