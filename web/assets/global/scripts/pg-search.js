(function() {
	if($('.search-opt').length != 1) {
		$('.folding-search-btn').hide();
	}
	$('.search-opt').hide();
	
	$('.folding-search-btn').bind('click', function(event) {
		var btn = $(this);
		$(btn).closest('form').find('.search-opt').slideToggle('fast', function() {
			if ($(btn).closest('form').find('.search-opt').css('display') == 'none') {
				btn.removeClass('icon-arrow-up');
				btn.addClass('icon-arrow-down');
			} else {
				btn.removeClass('icon-arrow-down');
				btn.addClass('icon-arrow-up');
			}
		});
	});
	var linkEventPos = 0; // 드래그하면 링크 안되도록...
	$(document).on('mousedown', "td.link", function(event) {
		linkEventPos = event.pageX + event.pageY;
	});
	$(document).on('mouseup', "td.link", function(event) {
		if(linkEventPos === event.pageX + event.pageY) {
			location.href = $(this).data('url');
		}
		linkEventPos = 0;
	});

	$(document).on('mousedown', 'td.link_modal, td>a.link_modal', function(event) {
		linkEventPos = event.pageX + event.pageY;
	});
	$(document).on('mouseup', 'td.link_modal, td>a.link_modal', function() {
		if(linkEventPos === event.pageX + event.pageY) {
			var $modal = $('#pgmate-modal');
			if ($modal.children().length < 1) {
				$modal.empty();
			}
			var url = $(this).data('url');
			
			$modal.load(url, '', function(responseTxt, statusTxt, xhr) {
				if (statusTxt == "success") {
					$modal.modal();
					textMask();
				}
			});
		}
	});

	// page size 변경
	$(document).on('change', '#pageSize', function() {
		search();
	});

	// 검색조건 초기화 버튼
	$(document).on('click', '#SearchClear', function() {
		$('#searchForm input').val("");
		$('#searchForm select.selectpicker').val(1).selectpicker('refresh');
		$('#gradeSelector .change-btn').trigger('click');
		$('#gradeSelector .selecter-wrapper:gt(1)').addClass('hide');
		$("#formOptionInput").remove();
		
		$('.input-daterange, .datepicker').datepicker({
			format: 'yyyy-mm-dd',
			autoclose:true,
			language:'ko'
		});
		var settingDate = new Date();
		settingDate.setMonth(settingDate.getMonth()-1); //한달 전
		
		var monthFirstDate = new Date();
		monthFirstDate.setDate(1); //이번달 1일
		
		var monthLastDate = new Date();
		monthLastDate.setMonth(monthLastDate.getMonth()+1);
		monthLastDate.setDate(0);
		
		//추가 - 어제 날짜로 초기화
		var yesterday = new Date();
		yesterday.setDate(yesterday.getDate()-1);

		//PYS : 정기결제 만료일 추가
		var now = new Date();
		var rebillExpireDate = new Date(now.setFullYear(now.getFullYear() + 1));
		rebillExpireDate.setDate(rebillExpireDate.getDate() - 1);
		
		$('.input-daterange').find('input.from').datepicker('setDate', new Date());
		$('.input-daterange').find('input.to').datepicker('setDate', new Date());
		$('.input-daterange').find('input.now-date').datepicker('setDate', new Date());
		$('.input-daterange').find('input.yesterday-date').datepicker('setDate', yesterday);
		$('.input-daterange').find('input.month-first-date').datepicker('setDate', monthFirstDate);
		$('.input-daterange').find('input.month-last-date').datepicker('setDate', monthLastDate);
		$('.input-daterange').find('input.last-month-date').datepicker('setDate', settingDate);
		$('.input-daterange').find('input.rebill-expire-date').datepicker('setDate', rebillExpireDate);
		$('.input-daterange').find('input:not(.from)input:not(.to)input:not(.now-date)input:not(.yesterday-date)input:not(.last-month-date)input:not(.month-first-date)input:not(.month-last-date)input:not(.rebill-expire-date)').val('');
		
		
		$('.datepicker.from').datepicker('setDate', new Date());
		$('.datepicker.to').datepicker('setDate', new Date());
		$('.datepicker.now-date').datepicker('setDate', new Date());
		$('.datepicker.yesterday-date').datepicker('setDate', yesterday);
		$('.datepicker.last-month-date').datepicker('setDate', settingDate);
		$('.datepicker.month-first-date').datepicker('setDate', monthFirstDate);
		$('.datepicker.month-last-date').datepicker('setDate', monthLastDate);
		$('.datepicker.rebill-expire-date').datepicker('setDate', rebillExpireDate);
	});

	// 체크박스 전체 동작
	$(document).on('change', '.all-check', function() {
		$('.pg-table .row-check').prop('checked', this.checked);
	});

	// 정산 지급 데이터 (은행포멧)
	$(document).on('click', '.settle-pay-out-make', function() {
		var bankCd = $(this).attr("data-bank");
		var grade = $(this).attr("data-grade");
		var stlId = '';
		$('table.pg-table>tbody>tr').each(function(i, e) {
			if ($(e).find('input[type="checkbox"]').is(':checked')) {
				stlId += "'" + $(e).attr('data-stlId') + "',";
			}
		});

		if (stlId.length < 1) {
			bootbox.alert("지급 데이터를 생성할 대상을 체크하세요.");
		} else {
			stlId = stlId.substring(0, stlId.length - 1);
			//console.log("정산 지급 데이터 생성 : "+ bankCd + " / " + stlId);
			settlePayOutExport(bankCd, stlId, $(this).hasClass('is-sub'), grade);
		}
	});
	
	// 가상계좌정산 지급 데이터 (은행포멧)
	$(document).on('click', '.vactSettle-pay-out-make', function() {
		var bankCd = $(this).attr("data-bank");
		var grade = $(this).attr("data-grade");
		var stlId = '';
		$('table.pg-table>tbody>tr').each(function(i, e) {
			if ($(e).find('input[type="checkbox"]').is(':checked')) {
				stlId += "'" + $(e).attr('data-stlId') + "',";
			}
		});
		
		if (stlId.length < 1) {
			bootbox.alert("지급 데이터를 생성할 대상을 체크하세요.");
		} else {
			stlId = stlId.substring(0, stlId.length - 1);
			//console.log("정산 지급 데이터 생성 : "+ bankCd + " / " + stlId);
			vactSettlePayOutExport(bankCd, stlId, $(this).hasClass('is-mcht'), grade);
		}
	});

	$(document).keyup(function(e) {
		if(e.keyCode === 13) {
			searchForList();
		}
	});
	
	
	$(".typeahead").each(function() {
		var $this = $(this);
		$this.typeahead({
			source: function (query, process) {
				if(query) {
					var searchKey = $this.attr('data-search') ? $this.attr('data-search') : $this.attr('name');
					console.log($this.attr('data-search'));
					$.ajax({
						url: '/common/typeahead/' + searchKey + '/' + query,
						type : "get",
						success : function(json, textStatus) {
							if(json) {
								return process(JSON.parse(json));
							}
						},
						error : function(xhr, status, error) {
							console.log("TYPEAD ERROR.");
						}
					});
				}
			},
			autoSelect: true,
			delay:500,
			minLength:2
		});
	});
	
	$('#searchForm').submit(function() {
		return false;
	});
})();

// 조건을 기반으로 검색을 실행
function searchForList(callbackFnc) {
	// 페이징 초기화
	$("#currentPage").val(1);
	search(undefined, false, callbackFnc);
}

// 조건을 기반으로 엑셀 파일 출력
function searchForExcel() {
	// 페이징 초기화
	$("#currentPage").val(1);
	var pageTotal = $('#page-total').text();
	
	if(!pageTotal) {
		bootbox.alert('엑셀로 출력할 수 없습니다. 관리자에게 문의해주세요.');
		return;
	} else if(pageTotal > 1000000){
		alert('데이터가 많아 100000건 데이터만 출력됩니다.');
	}
	search(0, 'excel');
}

// 조건을 기반으로 PDF 파일 출력
function searchForPDF() {
	// 페이징 초기화
	$("#currentPage").val(1);
	search(0, 'pdf');
}

// page 이동
function pageMove() {
	var currentPage = $("#currentPage").val();
	search(currentPage);
}

// page 이동 next
function pagePrev() {
	var currentPage = parseInt($("#currentPage").val()) - 1;
	if (currentPage > 0) {
		$("#currentPage").val(currentPage);
		search(currentPage);
	}
}

// page 이동 prev
function pageNext() {
	var totalPage = parseInt($("#totalPage").val());
	var currentPage = parseInt($("#currentPage").val()) + 1;

	if (totalPage >= currentPage) {
		$("#currentPage").val(currentPage);
		search(currentPage);
	}
}

// 리스트 ajax 통신
function search(page, type, callbackFnc) {
	var f = $("#searchForm"); // 검색에 사용할 폼
	type = !type ? 'list' : type;
	var currentPage = (page !== undefined && page > 0) ? page : 1; // 현재 페이지
	var pageSize = $("#pageSize").val(); // 리스트 갯수
	var keyDataArr = new Array();
	// 정렬을 위한 검색일 경우 기존 검색 조건은 그대로 유지하며, 기존 정렬관련 데이터는 삭제한다.
	if (type == 'sort') {jQuery.removeData($("#searchForm .form-body input, #searchForm .form-body select"),"order");
		// 검색조건이 축소되어 있을 때 search-opt 조건은 검색조건에서 제외시키기 위해 내용을 삭제한다. (정렬인 경우 제외)
	} else if (f.find('.search-opt').css('display') == 'none') {
		f.find('.search-opt').find('input').val("");
		f.find('.search-opt').find('select.selectpicker').val(1);
		f.find('.search-opt').find('select.selectpicker').selectpicker('refresh');
	}

	$("#searchForm .form-body input").each(function(i, e) {
		if ($(e).data("reg") != false && $(e).attr('name') && ($(e).val() || $(e).data("empty") == true)) {
			var oper = $(e).data("oper");
			var keyData = new Object();
			keyData.name = $(e).attr("name");
			keyData.val = $(e).parent().hasClass('input-daterange') ? $(e).val().replace(/-/gi, '') : $(e).val();
			keyData.oper = (oper == undefined) ? "lk" : oper;
			keyData.order = "";
			keyData.key = true;
			keyDataArr.push(keyData);
		} else if ($(e).attr('data-order')) {
			var keyData = new Object();
			keyData.name = $(e).attr("name");
			keyData.order = $(e).data('order');
			keyDataArr.push(keyData);
		}
	});

	$("#searchForm .form-body select").each(function(i, e) {
		if ($(e).data("reg") != false && $(e).attr('name') && $(e).val()) {
			var oper = $(e).data("oper");
			var keyData = new Object();
			keyData.name = $(e).attr("name");
			keyData.val = $(e).val();
			keyData.oper = (oper == undefined) ? "eq" : oper;
			keyData.order = "";
			keyData.key = true;
			keyDataArr.push(keyData);
		}
	});
	

	var pageData = new Object();
	pageData.current = currentPage;
	pageData.total = 0;
	
	if (type == 'excel' || type == 'pdf') {
		pageData.size = 100000;
	} else {
		pageData.size = pageSize;
	}
	var searchData = new Object();
	searchData.type = type;
	searchData.reason = type;
	searchData.redirect = "";
	searchData.page = pageData;
	searchData.data = keyDataArr;

	if (type == 'excel' || type == 'pdf') {
		if ($('#searchForm input[name="thead"]').val() == undefined) {
			bootbox.alert("다운로드가 지정되지 않았습니다.");
			return;
		}
		searchData.reason = $('#searchForm input[name="reason"]').val();
		searchData.thead = $('#searchForm input[name="thead"]').val();
	}

	searchDataJson = JSON.stringify(searchData);

	// =========================================================================================
	//console.log('json : ' + searchDataJson);

	searchAjax(searchDataJson, f.attr("action"), type, callbackFnc);
};

function searchAjax(searchData, action, type, callbackFnc) {
	$.ajax({
		type : "post",
		url : action,
		dataType : "text",
		beforeSend : function(xhr) {
			xhr.setRequestHeader("Content-type",
					"application/json;charset=utf-8");
		},
		data : searchData,
		success : function(json, textStatus) {
			if (type == 'excel' || type == 'pdf') {
				var jsData = $.parseJSON(json);
				if(jsData.result.code == '601') {
					bootbox.alert(jsData.result.message);
				}else {
					location.href = jsData.file.link;
				}
			} else {
				if(json.substring(0,1) != '{') {
					$("#searchResult").html(json);
					textMask();
					if(callbackFnc) {
						setTimeout(function() { callbackFnc(); }, 300);
					}	
				} else {
					bootbox.alert("검색에 실패하였습니다.");
				}
			}
		},
		error : function(xhr, status, error) {
			bootbox.alert("검색에 실패하였습니다.");
		}
	});
};

