var dataTable;
function init() {
  $('#profile-img').error(function() {
    $("#profile-img").attr("src", getContextPath() + "/images/avatar/guest.jpg");
  });
  initDataTable();
}
function bind() {
}

function initDataTable() {

  var columns = [{
    "mData": "ipAddress",
    "bSortable": false,
    "sClass": "text-center"
  }, {
    "render": function(data, type, full, meta) {
      if (full.os) { return full.os; }
      return "-";
    },
    "bSortable": false,
  }, {
    "render": function(data, type, full, meta) {
      if (full.userAgent) { return full.userAgent; }
      return "-";
    },
    "bSortable": false,
  }, {
    "render": function(data, type, full, meta) {
      var loginDate = new Date(full.loginDate);
      return "<span>" + moment(loginDate).format('DD/MM/YYYY HH:mm:ss (dddd)') + "</span>";
    },
    "bSortable": false,
    "sClass": "text-center"
  }];

  dataTable = $('#loginHistoryTable').DataTable({
    aoColumns: columns,
    ordering: false,
    ajax: {
      url: getContextPath() + '/users/api/' + $("#userId").val() + '/loginHistories',
      type: "POST",
      data: function(d) {
        delete d.columns;
        delete d.search;
        delete d.draw;
        d.id = $("#userId").val();
      }
    },
    drawCallback: function(settings) {

    }
  });
}
