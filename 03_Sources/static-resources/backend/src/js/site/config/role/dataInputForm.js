var userTable;
var accessTable;
function init() {
  getPages();
  initUserTable();
  initAccessTable();
}

function bind() {
  $("#btnSave").on("click", function(e) {
    convertJSONValueToCommaSeparateString("#userIds");
    convertJSONValueToCommaSeparateString("#actionIds");
    $("#roleForm").submit();
  });
  $("#btnUserReset").on("click", function(e) {
    $("#user-keyword").val("");
    userTable.draw();
  });
  $("#btnUserSearch").on("click", function(e) {
    userTable.draw();
  });
  $("#btnAccessSearch").on("click", function(e) {
    accessTable.draw();
  });
  $("#btnAccessReset").on("click", function(e) {
    $("#access-keyword").val("");
    $("#pages").val('').selectpicker('refresh');
    accessTable.draw();
  });

  $("#pages").on("change", function(e) {
    accessTable.draw();
  });
  $("#btnReset").on("click", function(e) {
    reloadCurrentPage();
  });
  $("#btnCancel").on("click", function(e) {
    goToHomePage();
  });
}

function initUserTable() {
  var columns = [{
    "render": function(data, type, full, meta) {
      return '<input data-input-type="iCheck" data-id="' + full.id + '"type="checkbox">';
    },
    "bSortable": false,
    "sClass": "text-center"
  }, {
    "mData": "loginId"
  }, {
    "mData": "name"
  }, {
    "mData": "email",
    "bSortable": false,
  }, {
    "mData": "nrc",
    "bSortable": false,
  }];

  userTable = $('#userTable').DataTable({
    aoColumns: columns,
    aaSorting: [[1, "desc"]],
    "pageLength": SECONDARY_ROW_PER_PAGE,
    ajax: {
      url: getContextPath() + '/users/api/search',
      type: "POST",
      data: function(d) {
        delete d.columns;
        delete d.search;
        delete d.draw;
        var index = $(d.order[0])[0].column;
        var dir = $(d.order[0])[0].dir;
        d.orderAs = dir;
        var head = $("#userTable").find("thead");
        var sortColumn = head.find("th:eq(" + index + ")");
        d.orderBy = $(sortColumn).attr("data-id");
        d.word = $("#user-keyword").val();
      }
    },
    initComplete: function() {
      var api = this.api();
      $('#user-keyword').off('.DT').on('keyup.DT', function(e) {
        if (e.keyCode == 13) {
          api.search(this.value).draw();
        }
      });
    },
    drawCallback: function(settings) {
      setSelectable("#userTable", "#userIds", settings._iRecordsDisplay);
    }
  });
}
function initAccessTable() {
  var columns = [{
    "render": function(data, type, full, meta) {
      return '<input data-input-type="iCheck" data-id="' + full.id + '"type="checkbox">';
    },
    "bSortable": false,
    "sClass": "text-center"
  }, {
    "mData": "page",
  }, {
    "mData": "displayName",
    "bSortable": false,
  }, {
    "mData": "description",
    "bSortable": false,
  }];

  accessTable = $('#accessTable').DataTable({
    aoColumns: columns,
    aaSorting: [[1, "desc"]],
    "pageLength": SECONDARY_ROW_PER_PAGE,
    ajax: {
      url: getContextPath() + '/actions/api/search',
      type: "POST",
      data: function(d) {
        delete d.columns;
        delete d.search;
        delete d.draw;
        var index = $(d.order[0])[0].column;
        var dir = $(d.order[0])[0].dir;
        d.orderAs = dir;
        var head = $("#accessTable").find("thead");
        var sortColumn = head.find("th:eq(" + index + ")");
        d.orderBy = $(sortColumn).attr("data-id");
        d.word = $("#access-keyword").val();
        d.page = $("#pages").val();
      }
    },
    initComplete: function() {
      var api = this.api();
      $('#access-keyword').off('.DT').on('keyup.DT', function(e) {
        if (e.keyCode == 13) {
          api.search(this.value).draw();
        }
      });
    },
    drawCallback: function(settings) {
      setSelectable("#accessTable", "#actionIds", settings._iRecordsDisplay);
    }
  });
}
function getPages() {
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: getContextPath() + '/actions/api/pages',
    dataType: 'json',
    timeout: 100000,
    success: function(data) {
      $.each(data.pageNames, function(index) {
        $("#pages").append("<option value='" + data.pageNames[index] + "'>" + data.pageNames[index] + "</option>");
      });
    }
  });
}