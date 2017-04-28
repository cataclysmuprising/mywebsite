var dataTable;
function init() {
  initDataTable();
}

function bind() {
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
    "mData": "group",
  }, {
    "mData": "subGroup",
  }, {
    "mData": "name",
  }, {
    "mData": "value",
    "bSortable": false,
  }, {
    "mData": "type",
    "bSortable": false,
  }, ];
  if (hasAuthority("settingEdit") || hasAuthority("settingRemove")) {
    columns.push({
      "render": function(data, type, full, meta) {
        var editButton = {
          label: "Edit",
          authorityName: "settingEdit",
          url: getContextPath() + "/settings/" + full.id + '/edit',
          styleClass: "",
          data_id: full.id
        }
        var removeButton = {
          label: "Remove",
          authorityName: "settingRemove",
          url: getContextPath() + "/settings/" + full.id + '/delete',
          styleClass: "remove",
          data_id: full.id
        }
        return generateAuthorizedButtonGroup([editButton, removeButton]);
      },
      "bSortable": false,
      "sClass": "text-center"
    });
  }
  dataTable = $('#settingTable').DataTable({
    aoColumns: columns,
    ajax: {
      url: getContextPath() + '/settings/api/search',
      type: "POST",
      data: function(d) {
        delete d.columns;
        delete d.search;
        delete d.draw;
        var index = $(d.order[0])[0].column;
        var dir = $(d.order[0])[0].dir;
        d.orderAs = dir;
        var head = $("#settingTable").find("thead");
        var sortColumn = head.find("th:eq(" + index + ")");
        d.orderBy = $(sortColumn).attr("data-id");
        d.word = $("#keyword").val();
      }
    },
    initComplete: function() {
      var api = this.api();
      $('#keyword').off('.DT').on('keyup.DT', function(e) {
        if (e.keyCode == 13) {
          api.search(this.value).draw();
        }
      });
    },
    drawCallback: function(settings) {
      fixedFunctionColumn("#settingTable");
      bindRemoveButtonEvent();
    }
  });
}
