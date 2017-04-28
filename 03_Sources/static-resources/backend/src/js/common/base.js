/*
 * ======================================================================
 * base.js 
 * ! version : 1.0 
 * Copyright (c) 2017 - license.txt, Than Htike Aung
 * ======================================================================
 */

/**
 * @file          base.js
 * @description   This js file initiate all global settings for website. This file
 *                may include in most of pages.
 * @version       1.0
 * @author        Than Htike Aung
 * @contact       rage.cataclysm@gmail.com
 * @copyright     Copyright (c) 2017-2018, Than Htike Aung. This source file is free
 *                software, available under the following license: MIT license. See
 *                the license file for details.
 */
var ROW_PER_PAGE = 25;
var SECONDARY_ROW_PER_PAGE = 10;
var EAGER = "EAGER";
var LAZY = "LAZY";
var FILE_SIZE_UNITS = new Array('Bytes', 'KB', 'MB', 'GB');

$(window).on('load', function() {
  $('#status').fadeOut("slow");
  $('#preloader').fadeOut("slow");
  $('body').css({
    'overflow': 'visible'
  });
  $('.disabled').attr('tabindex', '-1');
  $('.disabled input').attr('tabindex', '-1');
});

$(document).ready(function() {
  "use strict";
  baseInit();
  init();
  baseBind();
  bind();
});

$(document).ajaxStop(function() {
  if ($.fn.selectpicker) {
    $(".selectpicker").selectpicker('refresh');
  }
});

function handleServerResponse(response) {
  if (response.status == "METHOD_NOT_ALLOWED") {
    if (response.type == "validationError") {
      $("#validationErrors").empty();
      $.each(response.fieldErrors, function(key, value) {
        $("#validationErrors").append('<span class="error-item" data-id="' + key + '" data-error-message="' + value + '" />');
      });
      loadValidationErrors();
    }
  } else if (response.status == "OK") {
    $(".modal").modal("hide");
  }
  if (response.pageMessage) {
    var pageMessage = response.pageMessage;
    notify(pageMessage.title, pageMessage.message, pageMessage.style);
  }
}

function clearOldValidationErrorMessages() {
  $(".help-block").remove();
  $(".form-group").removeClass("has-error");
  $("div.input-fieldless").hide();
}

function loadValidationErrors() {
  if ($("#validationErrors .error-item").length > 0) {
    clearOldValidationErrorMessages();
    $.each($("#validationErrors .error-item"), function(index, item) {
      var elementId = $(item).attr("data-id");
      if ($("#" + elementId).length) {
        $("#" + elementId).closest(".form-group").addClass("has-error");
        var container;
        if ($("#" + elementId).closest("div").hasClass("input-fieldless")) {
          container = $("#" + elementId).closest("div.input-fieldless").children("blockquote");
          $("#" + elementId).closest("div.input-fieldless").show();
        } else if ($("#" + elementId).closest("form").hasClass("form-horizontal")) {
          container = $("#" + elementId).closest(".form-group > div");
        } else {
          container = $("#" + elementId).closest(".form-group");
        }
        container.append('<span class="help-block">' + $(item).attr("data-error-message") + '</span>');
      }
    });
  }
}

$(document).ajaxComplete(function(event, xhr, settings) {
  if (xhr.status == 403) {
    alert("This page contains unauthorized contents for you.Please contact administrators !");
    window.location.href = getContextPath() + "/accessDenied";
  } else if (xhr.status == 208) {
    document.write(xhr.responseText);
  } else if (xhr.status == 226) {
    document.write(xhr.responseText);
  }
});

$(document).ajaxError(function(event, jqxhr, settings, thrownError) {
  alert("Error occured while loading page informations! Check your internet connection.");
});

function baseInit() {
  $.widget.bridge('uibutton', $.ui.button);

  $('.user-profile-image').error(function() {
    $(this).attr("src", getContextPath() + "/images/avatar/guest.jpg");
  });

  if ($.fn.datetimepicker) {
    $(".input-group.date").datetimepicker({
      format: 'DD/MM/YYYY',
      showClear: true
    });
  }
  initAdminLTETheme();
  initLobiboxSettings();
  $(".selectpicker").val('').selectpicker('refresh');
  if ($.fn.DataTable) {
    $.extend(true, $.fn.dataTable.defaults, {
      "dom": '<"top">rt<"bottom"ifp><"clear">',
      "bFilter": false,
      pagingType: "first_last_numbers",
      "pageLength": ROW_PER_PAGE,
      processing: false,
      serverSide: true,
      aaSorting: [[0, "desc"]],
      oLanguage: {
        "sEmptyTable": "No records found. "
      },
      autoWidth: false,
      infoCallback: function(roles, start, end, max, total, pre) {
        return "Showing " + start + " to " + end + " of " + total + " entries";
      }
    });
  }

  $('.img-circle.profile-image').error(function() {
    $(this).attr("src", getContextPath() + "/images/avatar/guest.jpg");
  });

  $('form').attr('autocomplete', 'off');

  $('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
    if ($(".table").length) {
      $.fn.dataTable.tables({
        visible: true,
        api: true
      }).columns.adjust();
    }
  });

  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");
  $(document).ajaxSend(function(e, xhr, options) {
    xhr.setRequestHeader(header, token);
  });

  loadValidationErrors();
  initPageMessage();

}

function initICheck() {
  $('[data-input-type="iCheck"]').iCheck({
    checkboxClass: 'icheckbox_minimal-blue',
    radioClass: 'iradio_minimal-blue',
  });
}

function initLobiboxSettings() {
  Lobibox.notify.DEFAULTS = $.extend({}, Lobibox.notify.DEFAULTS, {
    size: 'mini',
    iconSource: "fontAwesome",
    showClass: 'zoomIn',
    hideClass: 'lightSpeedOut',
    continueDelayOnInactiveTab: true,
    pauseDelayOnHover: true,
    sound: false,
    delay: 10000,
    img: getContextPath() + "/images/logo.png",
    warning: {
      title: 'Warning',
      iconClass: 'fa fa-exclamation-circle'
    },
    info: {
      title: 'Information',
      iconClass: 'fa fa-info-circle'
    },
    success: {
      title: 'Success',
      iconClass: 'fa fa-check-circle'
    },
    error: {
      title: 'Error',
      iconClass: 'fa fa-times-circle'
    }
  });
}

function initPageMessage() {
  var pageMessage = $("#pageMessage");
  if (pageMessage) {
    var title = $(pageMessage).attr("data-title");
    var message = $(pageMessage).attr("data-info");
    var style = $(pageMessage).attr("data-style");
    notify(title, message, style);
  }
}

function notify(title, message, style) {
  $(".lobibox-notify-wrapper").remove();
  Lobibox.notify(style, {
    msg: message,
    title: title
  });
}
function initAdminLTETheme() {
  // Fix for IE page transitions
  $("body").removeClass("hold-transition");

  // Extend options if external options exist
  if (typeof AdminLTEOptions !== "undefined") {
    $.extend(true, $.AdminLTE.options, AdminLTEOptions);
  }

  // Easy access to options
  var o = $.AdminLTE.options;

  // Set up the object
  _init();

  // Activate the layout maker
  $.AdminLTE.layout.activate();

  // Enable sidebar tree view controls
  $.AdminLTE.tree('.sidebar');

  // Enable control sidebar
  if (o.enableControlSidebar) {
    $.AdminLTE.controlSidebar.activate();
  }

  // Add slimscroll to navbar dropdown
  if (o.navbarMenuSlimscroll && typeof $.fn.slimscroll != 'undefined') {
    $(".navbar .menu").slimscroll({
      height: o.navbarMenuHeight,
      alwaysVisible: false,
      size: o.navbarMenuSlimscrollWidth
    }).css("width", "100%");
  }

  // Activate sidebar push menu
  if (o.sidebarPushMenu) {
    $.AdminLTE.pushMenu.activate(o.sidebarToggleSelector);
  }

  // Activate Bootstrap tooltip
  // if (o.enableBSToppltip) {
  // $('body').tooltip({
  // selector: o.BSTooltipSelector
  // });
  // }

  // Activate box widget
  if (o.enableBoxWidget) {
    $.AdminLTE.boxWidget.activate();
  }

  // Activate fast click
  if (o.enableFastclick && typeof FastClick != 'undefined') {
    FastClick.attach(document.body);
  }

  // Activate direct chat widget
  if (o.directChat.enable) {
    $(document).on('click', o.directChat.contactToggleSelector, function() {
      var box = $(this).parents('.direct-chat').first();
      box.toggleClass('direct-chat-contacts-open');
    });
  }

  /*
   * INITIALIZE BUTTON TOGGLE ------------------------
   */
  $('.btn-group[data-toggle="btn-toggle"]').each(function() {
    var group = $(this);
    $(this).find(".btn").on('click', function(e) {
      group.find(".btn.active").removeClass("active");
      $(this).addClass("active");
      e.preventDefault();
    });

  });
}

function baseBind() {
  setBootstrapDropDownAbsolute();
  $(window).scroll(function() {
    if ($(this).scrollTop() > 150) {
      $('.scrollToTop').fadeIn();
    } else {
      $('.scrollToTop').fadeOut();
    }
  });
  $(window).resize(function() {
    if (this.resizeTO) clearTimeout(this.resizeTO);
    this.resizeTO = setTimeout(function() {
      $(this).trigger('resizeEnd');
    }, 500);
  });
  $(window).bind('resizeEnd', function() {
    $(".datatable_scrollArea").scroll();
  });

  $('.scrollToTop').click(function() {
    $('html, body').animate({
      scrollTop: 0
    }, 800);
    return false;
  });

  $('table.dataTable').on('processing.dt', function(e, settings, processing) {
    if (processing) {
      Pace.start();
    } else {
      Pace.stop();
    }
  });
  disableFormSubmitEvent();
}

// stackoverflow.com/questions/32526201/bootstrap-dropdown-menu-hidden-behind-other-elements#32527231
function setBootstrapDropDownAbsolute() {
  // hold onto the drop down menu
  var dropdownMenu;

  // and when you show it, move it to the body
  $(window).on('show.bs.dropdown', function(e) {

    // grab the menu
    dropdownMenu = $(e.target).find('.datatable-cell-item.dropdown-menu');

    // detach it and append it to the body
    $('body').append(dropdownMenu.detach());

    // grab the new offset position
    var eOffset = $(e.target).offset();

    // make sure to place it where it would normally go (this could be
    // improved)
    dropdownMenu.css({
      'display': 'block',
      'top': eOffset.top + $(e.target).outerHeight(),
      'left': eOffset.left
    });
  });

  // and when you hide it, reattach the drop down, and hide it normally
  $(window).on('hide.bs.dropdown', function(e) {
    $(e.target).append(dropdownMenu.detach());
    dropdownMenu.hide();
  });
}

function setScrollX(tableId) {
  var scrollArea = $('<div></div>');
  $(scrollArea).addClass("datatable_scrollArea");
  $(tableId).wrap(scrollArea);
}

function fixedFunctionColumn(tableId) {
  // remove existing fixedColumnsTable
  $(tableId).siblings(".fixedColumnsTable").remove();
  // make sure it is not an empty table
  if ($(tableId + " tbody tr").length == 1) {
    if ($(tableId + " tbody tr td:last-child").hasClass("dataTables_empty")) { return false; }
  }
  if (!$(tableId).parent().hasClass("datatable_scrollArea")) {
    setScrollX(tableId);
  }
  // hide original table's header
  $(".functionColumn").css("opacity", "0");
  var tableWidth = $(tableId + " thead tr th:last-child").outerWidth() + 1;
  var scrollArea = $(tableId).closest(".datatable_scrollArea");
  // remove existing events
  $(scrollArea).unbind();
  // create table
  var table = $('<table class="fixedColumnsTable"></table>');
  $(table).addClass($(tableId).attr("class"));
  // create table header
  var thead = $("<thead></thead>");
  var tr_head = $('<tr><th class="text-center sorting_disabled">Function</th></tr>');
  thead.append(tr_head);
  table.append(thead);
  // create table body
  var tbody = $("<tbody></tbody>");
  $.each($(tableId + " tbody tr td:last-child"), function(index, lastChild) {
    var tr = $('<tr></tr>');
    var td = $(lastChild).clone();
    // hide original row
    $(lastChild).css("opacity", "0");
    // centering the content
    $(td).addClass("text-center");
    tr.append(td);
    tbody.append(tr);
  });
  table.append(tbody);
  $(table).attr("style", "width:" + tableWidth + "px !important;");
  $(table).css({
    "right": 1,
    "position": "absolute",
    "top": 0,
    "background-color": "#fff",
  });
  // add the table
  scrollArea.append(table);
  // bind the scroll event on scroll panel
  $(scrollArea).on("scroll", function(e) {
    var _this = this;
    // don't move. Stay at your position
    var fixedColumnsTable = $(this).children('.fixedColumnsTable');
    var leftOffset = $(this).width() - tableWidth + _this.scrollLeft;
    $(fixedColumnsTable).css({
      'left': leftOffset,
      "right": 1
    });
  });
}

function goToHomePage() {
  $(".breadcrumb > li > a")[1].click();
}

function reloadCurrentPage() {
  location.reload(true);
}

function getContextPath() {
  return window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
}

function getPageMode() {
  return $("#pageMode").val();
}

function generateAuthorizedButtonGroup(buttons) {
  var html = [];
  var availableButtons = [];
  for (i = 0; i < buttons.length; i++) {
    var button = buttons[i];
    if (button != undefined && hasAuthority(button.authorityName)) {
      availableButtons.push(button);
    }
  }
  if (availableButtons.length == 1) {
    var button = availableButtons[0];
    html.push('<a href="' + availableButtons[0].url + '" data-id="' + button.data_id + '" class="btn btn-sm btn-default btn-flat' + button.styleClass + '">'
            + availableButtons[0].label + '</a>');
  } else if (availableButtons.length > 1) {
    var button = availableButtons[0];
    html.push('<div class="btn-group">');
    html.push('<a href="' + button.url + '" data-id="' + button.data_id + '" class="btn btn-sm btn-default btn-flat ' + button.styleClass + '">' + button.label + '</a>');
    html.push('<button type="button" class="btn btn-sm btn-default btn-flat dropdown-toggle" data-toggle="dropdown">');
    html.push('<span class="caret"></span>');
    html.push('</button>');
    html.push('<ul class="datatable-cell-item dropdown-menu" role="menu">');
    for (i = 1; i < availableButtons.length; i++) {
      var button = availableButtons[i];
      html.push('<li><a href="' + button.url + '" data-id="' + button.data_id + '" class="' + button.styleClass + '">' + button.label + '</a></li>');
    }
    html.push("</ul>");
    html.push('<div>');
  }
  return html.join('');
}

function disableFormSubmitEvent() {
  $('form').on('keyup keypress', function(e) {
    var keyCode = e.keyCode || e.which;
    if (keyCode === 13) {
      e.preventDefault();
      return false;
    }
  });
}

function bindRemoveButtonEvent() {
  $(".remove").on("click", function(e) {
    e.preventDefault();
    var url = $(this).attr("href");
    $("#deleteConfirmModal").modal({
      backdrop: 'static',
      keyboard: false
    });
    $("#confirmDelete").off('click').on('click', function(e) {
      $("#deleteConfirmModal").modal("hide");
      window.location.href = url;
    });
  });
}

function hasAuthority(actionName) {
  return $("#" + actionName).val() == "true";
}