/*
 * ======================================================================
 * dataTables-select.js 
 * ! version : 1.0 
 * Copyright (c) 2017 - license.txt, Than Htike Aung
 * ======================================================================
 */

/**
 * @file          dataTables-select.js
 * @description   Make JQuery DataTable selectable with the help of iCheck plugin (http://icheck.fronteed.com/).
 * @version       1.0
 * @author        Than Htike Aung
 * @contact       rage.cataclysm@gmail.com
 * @copyright     Copyright (c) 2017-2018, Than Htike Aung. This source file is free
 *                software, available under the following license: MIT license. See
 *                the license file for details.
 */

var eventFromSelectAll = false;
/*
 * @param tableSelector {string} pass table selector
 * @param acceptanceElemSelector {string} you must define an element this can accept for value insert.
 *        <input type="hidden"> is preferable.
*/
function setSelectable(tableSelector, acceptanceElemSelector, itemCounts) {
  // make all checkbox for table select row as iCheck style
  $(tableSelector + ' [data-input-type="iCheck"]').iCheck({
    checkboxClass: 'icheckbox_minimal-blue',
    radioClass: 'iradio_minimal-blue',
  });

  // no records ? no selectable
  var selectAllCheckBox = tableSelector + ' thead [data-input-type="iCheck"]';
  if (itemCounts == 0) {
    eventFromSelectAll = true;
    $(selectAllCheckBox).iCheck('uncheck');
    eventFromSelectAll = false;
    $(selectAllCheckBox).iCheck("disable");
    return false;
  }
  $(selectAllCheckBox).iCheck("enable");

  if (!$(tableSelector).hasClass("selectable")) {
    setup(tableSelector);
  }
  setDefaultSelect(tableSelector, acceptanceElemSelector);
  bindSelectRowEvent(tableSelector, acceptanceElemSelector);
}

// bind events for table select rows
function bindSelectRowEvent(tableSelector, acceptanceElemSelector) {
  var selectAllCheckBox = tableSelector + ' thead [data-input-type="iCheck"]';
  var selectRowCheckBoxes = tableSelector + ' tbody tr [data-input-type="iCheck"]';
  // write the code for single select row event
  // when checkbox was clicked, make associated table row as selected style or not
  $(selectRowCheckBoxes).on('ifToggled', function(e) {
    $(this).closest("tr").toggleClass("selected");
    if (eventFromSelectAll == true) { return false; }
    // determine select all checkbox should checked or not
    var totalItemCounts = getTotalItemCounts(tableSelector);
    var selectedItemCounts = getSelectedCounts(tableSelector);
    eventFromSelectAll = true;
    if (selectedItemCounts == totalItemCounts) {
      $(selectAllCheckBox).iCheck('check');
    } else {
      $(selectAllCheckBox).iCheck('uncheck');
    }
    eventFromSelectAll = false;
    // in my case, dataId is Integer type
    var dataId = parseInt($(this).attr("data-id"));
    // record selected Item
    recordSelectedItemBySingleSelect(tableSelector, acceptanceElemSelector, dataId, $(this).prop('checked'));
  });

  //bind select all,de-select all events
  $(selectAllCheckBox).on('ifToggled', function(e) {
    if (!eventFromSelectAll) {
      eventFromSelectAll = true;
      if ($(this).prop('checked')) {
        $(selectRowCheckBoxes).iCheck('check');
      } else {
        $(selectRowCheckBoxes).iCheck('uncheck');
      }
      eventFromSelectAll = false;
      // record selected Item
      recordSelectedItemBySelectAll(tableSelector, acceptanceElemSelector, $(this).prop('checked'));
    }
  });

  // when table's row was clicked, make toggle on the checkbox also
  // but don't write the code for select row event here
  $(tableSelector + " tbody tr").on("click", function(e) {
    $(this).find('[data-input-type="iCheck"]').iCheck('toggle');
  });
}

// to show the pre-selected elements
function setDefaultSelect(tableSelector, acceptanceElemSelector) {
  // uncheck selectall checkbox first for page navigation
  var selectAllCheckBox = tableSelector + ' thead [data-input-type="iCheck"]';
  $(selectAllCheckBox).iCheck('uncheck');

  var selectedItems = [];
  var originalValue = $(acceptanceElemSelector).val();
  if (originalValue && originalValue.length > 0) {
    if (!originalValue.startsWith('[')) {
      originalValue = "[" + originalValue + "]";
    }
    selectedItems = JSON.parse(originalValue);
    $.each(selectedItems, function(e, dataId) {
      var checkBox = $(tableSelector + ' tbody [data-input-type="iCheck"][data-id="' + dataId + '"]');
      $(checkBox).iCheck("check");
      $(checkBox).closest("tr").addClass("selected");
    });

    // determine select all checkbox should checked or not
    var totalItemCounts = getTotalItemCounts(tableSelector);
    var selectedItemCounts = getSelectedCounts(tableSelector);
    if (selectedItemCounts == totalItemCounts) {
      $(selectAllCheckBox).iCheck('check');
    }

    $(acceptanceElemSelector).val(JSON.stringify(selectedItems));
    // show how many items are selected
    showSelectedCounts(tableSelector, selectedItems.length);
  } else {
    // hide
    $(tableSelector).parent().find(".itemCountWrapper").hide();
  }
}

function getTotalItemCounts(tableSelector) {
  return $(tableSelector + ' tbody tr [data-input-type="iCheck"]').length;
}

function getSelectedCounts(tableSelector) {
  return $(tableSelector + ' tbody tr [data-input-type="iCheck"]:checked').length;
}

function showSelectedCounts(tableSelector, count) {
  var itemCountElem = $(tableSelector).parent().find(".itemCountWrapper");
  $(itemCountElem).show();
  $(itemCountElem).children(".count").text(count);
  if (count > 0) {
    $(itemCountElem).animate({
      "opacity": "1",
      top: "3px"
    }, 300);
  } else {
    $(itemCountElem).animate({
      "opacity": "0",
      top: "20px"
    }, 300);
  }
}

// for single selected row
function recordSelectedItemBySingleSelect(tableSelector, acceptanceElemSelector, dataId, isChecked) {
  var selectedItems = [];
  if ($(acceptanceElemSelector).val() && $(acceptanceElemSelector).val().length > 0) {
    selectedItems = JSON.parse($(acceptanceElemSelector).val());
  }
  var index = selectedItems.indexOf(dataId);
  if (index > -1) {
    selectedItems.splice(index, 1);
  }
  if (isChecked) {
    selectedItems.push(dataId);
  }
  $(acceptanceElemSelector).val(JSON.stringify(selectedItems));
  // show how many items are selected
  showSelectedCounts(tableSelector, selectedItems.length);
}

//for select all or de-select all
function recordSelectedItemBySelectAll(tableSelector, acceptanceElemSelector, isChecked) {
  var selectedItems = [];
  // put back items from previous pages
  var originalValue = $(acceptanceElemSelector).val();
  if (originalValue && originalValue.length > 0) {
    if (!originalValue.startsWith('[')) {
      originalValue = "[" + originalValue + "]";
    }
    selectedItems = JSON.parse(originalValue);
  }

  var selectRowCheckBoxes = $(tableSelector + ' tbody tr [data-input-type="iCheck"]');
  $.each(selectRowCheckBoxes, function(e, elem) {
    // in my case, dataId is Integer type
    var dataId = parseInt($(elem).attr("data-id"));
    var index = selectedItems.indexOf(dataId);
    if (index > -1) {
      selectedItems.splice(index, 1);
    }
    if (isChecked) {
      selectedItems.push(dataId);
    }
  });
  $(acceptanceElemSelector).val(JSON.stringify(selectedItems));
  // show how many items are selected
  showSelectedCounts(tableSelector, selectedItems.length);
}

// setup for dataTable select
function setup(tableSelector) {
  // add selectable class
  $(tableSelector).addClass("selectable");

  // this element will show selected item counts
  var itemCountElem = $('<div class="itemCountWrapper">Total ( <span class="count">0</span> ) selected</div>');
  $(tableSelector).before(itemCountElem);
}

// fetch selected items from selectable dataTable
function getSelectedItems(acceptanceElemSelector) {
  if ($(acceptanceElemSelector).val() && $(acceptanceElemSelector).val().length > 0) {
    return JSON.parse($(acceptanceElemSelector).val());
  } else {
    return "";
  }
}

function convertJSONValueToCommaSeparateString(acceptanceElemSelector) {
  var JSONValue = $(acceptanceElemSelector).val();
  if (JSONValue && JSONValue.length > 0) {
    $(acceptanceElemSelector).val(JSON.parse(JSONValue));
  }
}