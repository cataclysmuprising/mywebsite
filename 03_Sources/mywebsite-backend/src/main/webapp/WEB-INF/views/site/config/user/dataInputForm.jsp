<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!-- Content Header (Page header) -->
<section class="content-header">
	<h1>
		User Informations
		<small>
			<c:choose>
				<c:when test="${pageMode eq 'CREATE' }">
				(Register a new user)
			</c:when>
				<c:when test="${pageMode eq 'PROFILE' }">
				(Edit your personal informations)
			</c:when>
				<c:otherwise>
				(Edit existing user informations)
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
			<a href="${userHomeUrl}">
				<i class="fa fa-users"></i> Users
			</a>
		</li>
		<li class="active">
			<c:choose>
				<c:when test="${pageMode eq 'CREATE' }">
					Register
				</c:when>
				<c:when test="${pageMode eq 'PROFILE' }">
					Profile
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
			<c:choose>
				<c:when test="${pageMode eq 'PROFILE'}">
					<c:set var="submitUrl" value="/users/profile" />
				</c:when>
				<c:when test="${pageMode eq 'EDIT'}">
					<c:set var="submitUrl" value="/users/${user.id}/edit" />
				</c:when>
				<c:otherwise>
					<c:set var="submitUrl" value="/users/add" />
				</c:otherwise>
			</c:choose>
			<spring:url value="${submitUrl}" var="userSubmitUrl" />
			<form:form modelAttribute="user" id="userForm" action="${userSubmitUrl}" method="post" commandName="user">
				<div class="box box-primary row smallPanel">
					<div class="box-header with-border">
						<div class="profileImagePanel">
							<button type="button" class="btn btn-outline btn-default btn-flat btn-sm btn-imageUpload" id="editImage">Change Picture</button>
							<form:hidden id="contentId" path="contentId" />
							<img class="img-responsive img-thumbnail img-circle" id="profile-img" src="" width="150px" height="150px">
						</div>
						<c:if test="${pageMode ne 'CREATE'}">
							<div class="row text-center" style="padding: 5px;">
								<sec:authorize access="hasAnyAuthority(${changedPassword})">
									<button type="button" class="btn btn-primary btn-flat small-margin btn-sm btn-social" data-toggle="modal" data-target="#changePasswordModal" data-id="${user.id}"
										id="changePassword">
										<i class="fa fa-retweet"></i>Change Password
									</button>
								</sec:authorize>
								<sec:authorize access="hasAnyAuthority(${resetPassword})">
									<button type="button" class="btn btn-primary btn-flat small-margin btn-sm btn-social" data-toggle="modal" data-target="#resetPasswordModal" data-id="${user.id}"
										id="resetPassword">
										<i class="fa fa-eraser"></i>Reset Password
									</button>
								</sec:authorize>
							</div>
						</c:if>
					</div>
					<div class="box-body">
						<div class="row">
							<div class="col-sm-6 form-group">
								<label for="loginId" class="required">Login ID :</label>
								<div class="input-group">
									<span class="input-group-addon"><i class="fa fa-key"></i></span>
									<c:choose>
										<c:when test="${pageMode eq 'CREATE'}">
											<form:input id="loginId" type="text" path="loginId" class="form-control input-sm" />
										</c:when>
										<c:otherwise>
											<form:input id="loginId" type="text" path="loginId" class="form-control input-sm" readonly="true" tabindex="-1" />
										</c:otherwise>
									</c:choose>
								</div>
							</div>
							<div class="col-sm-6 form-group">
								<label for="name" class="required">Name :</label>
								<div class="input-group">
									<span class="input-group-addon"><i class="fa fa-user"></i></span>
									<form:input path="name" id="name" placeholder="Full Name" class="form-control input-sm" />
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6 form-group">
								<label for="email" class="required">Email :</label>
								<div class="input-group">
									<span class="input-group-addon">@</span>
									<c:choose>
										<c:when test="${pageMode eq 'CREATE'}">
											<form:input id="email" type="email" path="email" placeholder="abc@gmail.com" class="form-control input-sm" />
										</c:when>
										<c:otherwise>
											<form:input id="email" type="email" path="email" class="form-control input-sm" readonly="true" tabindex="-1" />
										</c:otherwise>
									</c:choose>
								</div>
							</div>
							<div class="col-sm-6 form-group">
								<label for="nrc" class="required">NRC :</label>
								<div class="input-group">
									<span class="input-group-addon"><i class="fa fa-id-card-o"></i></span>
									<form:input id="nrc" type="text" path="nrc" class="form-control input-sm" />
								</div>
							</div>
						</div>
						<c:if test="${pageMode eq 'CREATE'}">
							<div class="row">
								<div class="col-sm-6 form-group">
									<label for="password" class="required">Password :</label>
									<div class="input-group">
										<span class="input-group-addon"><i class="fa fa-lock"></i></span>
										<form:password id="password" path="password" placeholder="Enter Your Password" class="form-control input-sm" />
										<span class="input-group-addon passwordFormatTooltip"><i class="fa fa-info-circle help-tooltip"></i></span>
									</div>
								</div>
								<div class="col-sm-6 form-group">
									<label for="confirmPassword" class="required">Password Confirm :</label>
									<div class="input-group">
										<span class="input-group-addon"><i class="fa fa-expeditedssl"></i></span>
										<form:password id="confirmPassword" path="confirmPassword" placeholder="Type again your password" class="form-control input-sm" />
										<span class="input-group-addon passwordFormatTooltip"><i class="fa fa-info-circle help-tooltip"></i></span>
									</div>
								</div>
							</div>
						</c:if>
						<div class="row">
							<c:if test="${pageMode eq 'CREATE'}">
								<div class="col-sm-6 form-group">
									<label for="role" class="required">Role :</label> <input type="hidden" id="selectedRoles" value="${user.roleIds}">
									<div class="input-group">
										<span class="input-group-addon"><i class="fa fa-universal-access"></i></span>
										<form:select id="roles" path="roleIds" multiple="multiple" title="Select..." class="form-control input-sm selectpicker show-tick" data-live-search="true" data-size="5"
											data-selected-text-format="count > 3">
										</form:select>
									</div>
								</div>
							</c:if>
							<div class="${pageMode eq 'CREATE' ? 'col-sm-6' : 'col-sm-12'} form-group">
								<label for="phone" class="required">Phone :</label>
								<div class="input-group">
									<span class="input-group-addon"><i class="fa fa-phone"></i></span>
									<form:input id="phone" text="text" path="phone" class="form-control input-sm" />
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-4 form-group">
								<label for="age" class="required">Age :</label>
								<form:input id="age" path="age" placeholder="Age" class="form-control half-width input-sm" />
							</div>
							<div class="col-sm-4 form-group">
								<label for="dob">DOB :</label>
								<div class="col-sm-8 input-group date">
									<form:input id="dob" type="text" path="dobAsString" class="form-control input-sm" placeholder="Date Of Birth" />
									<span class="input-group-addon"> <span class="glyphicon glyphicon-calendar"></span>
									</span>
								</div>
							</div>
							<div class="col-sm-4 form-group">
								<label>Gender :</label>
								<div class="radio-inline-group">
									<form:radiobutton id="male" data-input-type="iCheck" value="MALE" path="gender" />
									<label for="male">Male</label>
									<form:radiobutton id="female" data-input-type="iCheck" value="FEMALE" path="gender" />
									<label for="female">Female</label>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12 form-group">
								<label for="address">Address :</label>
								<form:textarea id="address" path="address" rows="5" class="form-control input-sm" />
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
<div class="modal fade" id="changePasswordModal" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">Change Password</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal" id="change_PasswordForm" role="form">
					<div class="form-group">
						<label class="control-label col-sm-4 required">Old Password : </label>
						<div class="col-sm-8">
							<div class="input-group">
								<span class="input-group-addon"><i class="fa fa-lock"></i></span> <input type="password" id="change_oldPassword" placeholder="Type your old password"
									class="form-control input-sm" /> <span class="input-group-addon passwordFormatTooltip"><i class="fa fa-info-circle help-tooltip"></i></span>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4 required">New Password : </label>
						<div class="col-sm-8">
							<div class="input-group">
								<span class="input-group-addon"><i class="fa fa-lock"></i></span> <input type="password" id="change_newPassword" placeholder="Define for new password"
									class="form-control input-sm" /> <span class="input-group-addon passwordFormatTooltip"><i class="fa fa-info-circle help-tooltip"></i></span>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4 required">Confirm Password : </label>
						<div class="col-sm-8">
							<div class="input-group">
								<span class="input-group-addon"><i class="fa fa-expeditedssl"></i></span> <input type="password" id="change_confirmPassword" placeholder="Write your new password again"
									class="form-control input-sm" /> <span class="input-group-addon passwordFormatTooltip"><i class="fa fa-info-circle help-tooltip"></i></span>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="box-footer text-center">
				<button class="btn bg-navy btn-flat small-margin btn-sm btn-social" type="button" data-id="${user.loginId}" id="confirmChangePassword">
					<i class="fa fa-save"></i>Save
				</button>
				<button class="btn btn-danger btn-flat btn-sm btn-social" type="button" data-dismiss="modal">
					<i class="fa fa-undo"></i>Cancel
				</button>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="resetPasswordModal" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">Reset Password</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal" id="reset_PasswordForm" role="form">
					<div class="form-group">
						<label class="control-label col-sm-4 required">New Password : </label>
						<div class="col-sm-8">
							<div class="input-group">
								<span class="input-group-addon"><i class="fa fa-lock"></i></span> <input type="password" id="reset_newPassword" placeholder="Define for new password"
									class="form-control input-sm" /> <span class="input-group-addon passwordFormatTooltip"><i class="fa fa-info-circle help-tooltip"></i></span>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4 required">Confirm Password : </label>
						<div class="col-sm-8">
							<div class="input-group">
								<span class="input-group-addon"><i class="fa fa-expeditedssl"></i></span> <input type="password" id="reset_confirmPassword" placeholder="Write your new password again"
									class="form-control input-sm" /> <span class="input-group-addon passwordFormatTooltip"><i class="fa fa-info-circle help-tooltip"></i></span>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="box-footer text-center">
				<button class="btn bg-navy btn-flat small-margin btn-sm btn-social" type="button" data-id="${user.loginId}" id="confirmResetPassword">
					<i class="fa fa-save"></i>Save
				</button>
				<button class="btn btn-danger btn-flat btn-sm btn-social" type="button" data-dismiss="modal">
					<i class="fa fa-undo"></i>Cancel
				</button>
			</div>
		</div>
	</div>
</div>
<!-- Modal -->
<div class="modal fade" id="profileImageCropper" aria-labelledby="modalLabel" role="dialog" tabindex="-1">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">Edit Profile Image</h4>
			</div>
			<div class="modal-body">
				<div>
					<div class="selected_image_panel">
						<div class="preview">
							<img class="img-responsive img-thumbnail img-circle" id="selectedImage" src="" width="150px" height="150px" />
						</div>
					</div>
					<div class="row cropArea">
						<hr />
						<img id="imageHolder" src="" style="display: none" alt="" width="600px" height="250px" /> <input type="file" id="profilePhotoFileInput" accept="image/*"
							style="display: none;" />
					</div>
					<div class="docs-toolbar">
						<div class="btn btn-default btn-sm btn-group">
							<button class="btn btn-primary" data-method="zoom" data-option="0.1" type="button" title="Zoom In">
								<span class="docs-tooltip" data-toggle="tooltip" title="" data-original-title="$().cropper(&quot;zoom&quot;, 0.1)"> <span class="fa fa-search-plus"></span>
								</span>
							</button>
							<button class="btn btn-primary" data-method="zoom" data-option="-0.1" type="button" title="Zoom Out">
								<span class="docs-tooltip" data-toggle="tooltip" title="" data-original-title="$().cropper(&quot;zoom&quot;, -0.1)"> <span class="fa fa-search-minus"></span>
								</span>
							</button>
							<button class="btn btn-primary" data-method="setDragMode" data-option="move" type="button" title="Move">
								<span class="docs-tooltip" data-toggle="tooltip" title="" data-original-title="$().cropper(&quot;setDragMode&quot;, &quot;move&quot;)"> <span class="fa fa-arrows"></span>
								</span>
							</button>
							<button class="btn btn-primary" data-method="setDragMode" data-option="crop" type="button" title="Crop">
								<span class="docs-tooltip" data-toggle="tooltip" title="" data-original-title="$().cropper(&quot;setDragMode&quot;, &quot;crop&quot;)"> <span class="fa fa-crop"></span>
								</span>
							</button>
							<button class="btn btn-primary" data-method="clear" type="button" title="Clear">
								<span class="docs-tooltip" data-toggle="tooltip" title="" data-original-title="$().cropper(&quot;clear&quot;)"> <span class="fa fa-recycle"></span>
								</span>
							</button>
							<button class="btn btn-primary download" type="button" title="Download cropped image">
								<span class="docs-tooltip" data-toggle="tooltip" title=""> <span class="fa fa-download"></span>
								</span>
							</button>
						</div>
					</div>
				</div>
			</div>
			<div class="box-footer text-center">
				<button type="button" class="btn bg-olive btn-flat small-margin btn-sm btn-social" id="btnUploadImage">
					<i class="fa fa-upload"></i>Upload from my Computer
				</button>
				<button type="button" class="btn bg-teal btn-flat small-margin btn-sm btn-social" id="btnConfirmImage">
					<i class="fa fa-check-square-o"></i>Confirm
				</button>
				<button type="button" class="btn bg-purple btn-flat small-margin btn-sm btn-social" id="btnSetGravatorImage">
					<i class="fa fa-snowflake-o"></i>Use Random Gravator
				</button>
			</div>
		</div>
	</div>
</div>
