<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!-- Content Header (Page header) -->
<section class="content-header">
	<h1>
		Chat
		<small>(let's talk about somethings)</small>
	</h1>
	<ol class="breadcrumb">
		<li>
			<a href="${homeUrl}">
				<i class="fa fa-home"></i> Home
			</a>
		</li>
		<li>
			<a href="${roleHomeUrl}">
				<i class="fa fa-universal-access"></i> Chat
			</a>
		</li>
		<li class="active">Role List</li>
	</ol>
</section>
<!-- Main content -->
<section class="content">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div>
			    <div>
			        <button id="connect" onclick="connect();">Connect</button>
			        <button id="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button><br/><br/>
			    </div>
			    <div id="calculationDiv">
			        <label>Number One:</label><input type="text" id="num1" /><br/>
			        <label>Number Two:</label><input type="text" id="num2" /><br/><br/>
			        <button id="sendNum" onclick="sendNum();">Send to Add</button>
			        <p id="calResponse"></p>
			    </div>
			</div>
		</div>
	</div>
</section>
