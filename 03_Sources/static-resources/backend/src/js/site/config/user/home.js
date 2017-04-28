var dataTable;
var select2;

function init() {
  getRoles();
  initDataTable();
}

function bind() {
  $("#roles").on("change", function(e) {
    dataTable.draw();
  });

  $("#btnSearch").click(function(e) {
    dataTable.search($(this).val()).draw();
  });

  $("#btnReset").click(function(e) {
    $('form.control-block').trigger('reset');
    dataTable.search($(this).val()).draw();
  });
}

function initDataTable() {
  var columns = [{
    "mData": "loginId",
    "sClass": "text-left",
  }, {
    "mData": "name",
  }, {
    "mData": "email",
    "bSortable": false
  }, {
    "mData": "nrc",
    "bSortable": false,
  }, {
    "render": function(data, type, full, meta) {
      if (full.lastLoginDate) {
        var lastLoginDate = new Date(full.lastLoginDate);
        return "<span>" + moment(lastLoginDate).format('DD/MM/YYYY HH:mm:ss') + "</span>";
      } else {
        return '-';
      }

    },
  }, {
    "render": function(data, type, full, meta) {
      if (full.roles.length > 1) {
        var rolesString = "";
        if ($("#roles").val() != "" && $.inArray(parseInt($("#roles").val()), full.roleIds) > -1) {
          rolesString += $("#roles option:selected").text();
        } else {
          rolesString += full.roles[0].name;
        }
        rolesString += ' <a href="#" data-toggle="popover" data-trigger="focus" title="Roles" data-content="';
        for (var i = 0; i < full.roles.length; i++) {
          rolesString += "<div class='popover-item'>" + full.roles[i].name + "</div>";
        }
        rolesString += '"><span class="glyphicon glyphicon-list-alt glyphicon-color"></span></a>';
        return rolesString;
      } else if (full.roles.length == 1) {
        return full.roles[0].name;
      } else {
        return "-";
      }

    },
    "sClass": "text-left",
    "bSortable": false
  }, ];
  if (hasAuthority("userDetail") || hasAuthority("userEdit") || hasAuthority("userRemove")) {
    columns.push({
      "render": function(data, type, full, meta) {
        var detailButton = {
          label: "View",
          authorityName: "userDetail",
          url: getContextPath() + "/users/" + full.id,
          styleClass: "",
          data_id: full.id
        }
        var editButton = {
          label: "Edit",
          authorityName: "userEdit",
          url: getContextPath() + "/users/" + full.id + '/edit',
          styleClass: "",
          data_id: full.id
        }
        var removeButton = {
          label: "Remove",
          authorityName: "userRemove",
          url: getContextPath() + "/users/" + full.id + '/delete',
          styleClass: "remove",
          data_id: full.id
        }
        return generateAuthorizedButtonGroup([detailButton, editButton, removeButton]);
      },
      "bSortable": false,
      "sClass": "text-center"
    });
  }
  dataTable = $('#userTable').DataTable({
    aoColumns: columns,
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
        d.loginId = $("#loginId").val();
        d.word = $("#keyword").val();
        d.roleId = $("#roles").val();
        d.fetchMode = EAGER;
      }
    },
    initComplete: function() {
      var api = this.api();
      $('#keyword,#loginId').off('.DT').on('keyup.DT', function(e) {
        if (e.keyCode == 13) {
          api.search(this.value).draw();
        }
      });
    },
    drawCallback: function(settings) {
      fixedFunctionColumn("#userTable");
      $('[data-toggle="popover"]').popover({
        html: true,
        placement: 'top',
        animation: true,
        container: 'body',
        content: function() {
          return $(this).attr("data-content").html();
        }
      });
      bindRemoveButtonEvent();
    }
  });
}

function getRoles() {
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: getContextPath() + '/roles/api/searchAll',
    dataType: 'json',
    timeout: 100000,
    success: function(data) {
      $.each(data.aaData, function(key, value) {
        $("#roles").append("<option value='" + value.id + "'>" + value.name + "</option>");
      });
    }
  });
}