<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!-- Left side column. contains the logo and sidebar -->
<aside class="main-sidebar">
	<!-- sidebar: style can be found in sidebar.less -->
	<section class="sidebar">
		<!-- Sidebar user panel -->
		<div class="user-panel">
			<div class="pull-left image">
				<img class="img-circle user-profile-image" src="${root}/files/${contentId}" height="30px" alt="User Image">
			</div>
			<div class="pull-left info">
				<p>
					<c:out value="${loginUserName}" default="Guest" />
				</p>
				<a href="#">
					<i class="fa fa-circle text-success"></i> Online
				</a>
			</div>
		</div>
		<!-- sidebar menu: : style can be found in sidebar.less -->
		<ul class="sidebar-menu">
			<li class="header">MAIN NAVIGATION</li>
			<li class="treeview">
				<a href="#">
					<i class="fa fa-home"></i><span>Home</span> <span class="pull-right-container"> <i class="fa fa-angle-left pull-right"></i></span>
				</a>
				<ul class="treeview-menu">
					<spring:url value="/dashboard" var="dashboardURL" scope="application" />
					<li class="active">
						<a href="${dashboardURL}">
							<i class="fa fa-dashboard"></i>Dashboard
						</a>
					</li>
				</ul>
			</li>
			<sec:authorize access="hasAnyAuthority(${userList})">
				<spring:url value="/users" var="userHomeUrl" scope="application" />
				<li class="${page eq 'user' ? 'active' : '' }">
					<a href="${userHomeUrl}">
						<i class="fa fa-users"></i> <span>User</span>
					</a>
				</li>
			</sec:authorize>
			<sec:authorize access="hasAnyAuthority(${roleList})">
				<spring:url value="/roles" var="roleHomeUrl" scope="application" />
				<li class="${page eq 'role' ? 'active' : '' }">
					<a href="${roleHomeUrl}">
						<i class="fa fa-universal-access"></i> <span>Role</span>
					</a>
				</li>
			</sec:authorize>
			<sec:authorize access="hasAnyAuthority(${settingList})">
				<spring:url value="/settings" var="settingHomeUrl" scope="application" />
				<li class="${page eq 'setting' ? 'active' : '' }">
					<a href="${settingHomeUrl}">
						<i class="fa fa-gear"></i> <span>Setting</span>
					</a>
				</li>
			</sec:authorize>
		</ul>
	</section>
	<!-- /.sidebar -->
</aside>