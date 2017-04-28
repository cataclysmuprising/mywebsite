<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!-- Content Header (Page header) -->
<section class="content-header">
	<h1>
		Dashboard
		<small>(Control panel)</small>
	</h1>
	<ol class="breadcrumb">
		<li>
			<a href="${homeUrl}">
				<i class="fa fa-home"></i> Home
			</a>
		</li>
		<li class="active">
			<i class="fa fa-dashboard"></i> Dashboard
		</li>
	</ol>
</section>
<!-- Main content -->
<section class="content">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="callout callout-info" style="margin-bottom: 0 !important;">
				<h4>
					<i class="fa fa-info"></i> Note:
				</h4>
				This is template project for Spring MVC with AdminLTE theme.
			</div>
			<div class="box">
				<div class="box-header with-border">
					<h3 class="box-title">Welcome Message</h3>
					<div class="box-tools pull-right">
						<button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
							<i class="fa fa-minus"></i>
						</button>
						<button type="button" class="btn btn-box-tool" data-widget="remove" data-toggle="tooltip" title="Remove">
							<i class="fa fa-times"></i>
						</button>
					</div>
				</div>
				<div class="box-body">
					<bloquote>
						<p>
							Welcome to the MyWebsite! We hope you find the information helpful. If you have questions, please call
							<code>(+95-09795674175)</code>
							or e-mail at
							<code>rage.cataclysm@gmail.com</code>
						</p>
						<small>
							Sincerely, <cite title="Than Htike Aung">Than Htike Aung</cite>
						</small> 
					</bloquote>
				</div>
			</div>
		</div>
	</div>
</section>
