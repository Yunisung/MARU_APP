// KBR : 즉시 작동 함수 표현
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
	});

	// 체크박스 전체 동작
	$(document).on('change', '.all-check', function() {
		$('.pg-table .row-check').prop('checked', this.checked);
	});

	// 정산 지급 데이터 (은행포멧)
	$(document).on('click', '.settle-pay-out-make', function() {
		//KJM : 은행코드, 가맹점, 정산번호
		var bankCd = $(this).attr("data-bank");
		var grade = $(this).attr("data-grade");
		var stlId = '';
		//KJM : 사용자가 선택한 거래의 정산번호 가져옴 (정산번호1, 정산번호2,...)
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
// KBR: 제일 상단 함수 끝

// 조건을 기반으로 검색을 실행
function searchForList(callbackFnc) {
	// KBR : callbackFnc:undefined (가맹점 조회일 경우)
	// 다른 기능 조회시에도 callbackFnc : undefined 나옴....ㅜ
	// 페이징 초기화
	$("#currentPage").val(1);
	//KJM : 첫번째 매개변수는 page 정보, search버튼이나 메뉴를 눌러서 리스트 조회할때는 무조건 1페이지 부터 시작
	//search함수에 page정보가 undefinde로 들어가면 page는 1로 세팅됨
	//이 외의 경우 직접 페이지 수를 지정, 페이지 화살표 클릭해서 페이지 이동하는 경우에는 pageMove라는 함수를 통해 page값을 가지고 search함수로 가게되어 1이 아닌 다른 수의 페이지 값 가질 수 있음
	// KBR : 첫번째 매개변수를 undefined로 지정해주는 이유가,,,몰까,,?
	search(undefined, false, callbackFnc);
}

// 조건을 기반으로 엑셀 파일 출력
function searchForExcel() {
	// 페이징 초기화
	$("#currentPage").val(1);
	// 리스트 내 결과 건수
	var pageTotal = $('#page-total').text();
	
	// 리스트에 내역이 없을 경우
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

// page 이동 prev
function pagePrev() {
	// 현재 페이지 = 현재 페이지 - 1
	var currentPage = parseInt($("#currentPage").val()) - 1;
	// 현재 페이지가 0보다 클 때 jsp의 현재 페이지 값 수정 후 search 수행
	if (currentPage > 0) {
		$("#currentPage").val(currentPage);
		search(currentPage);
	}
}

// page 이동 next
function pageNext() {
	// 전체 페이지
	var totalPage = parseInt($("#totalPage").val());
	// 현재 페이지 = 현재 페이지 + 1
	var currentPage = parseInt($("#currentPage").val()) + 1;
	
	// 전체 페이지가 현재 페이지보다 같거나 클 때 jsp의 현재 페이지 수정 후 search 수행
	if (totalPage >= currentPage) {
		$("#currentPage").val(currentPage);
		search(currentPage);
	}
}

// KRB : 페이지 내 리스트건일 경우 해당 함수 실행됨
// 리스트 ajax 통신
function search(page, type, callbackFnc) {
	// KBR 
	// console.log('page , type , collbackFnc' , page, type, callbackFnc) 가맹점 조회 검색없이 카테고리 클릭 시 result : undefined,false,undefined
	
	// KBR : f = searchForm 안에 들어있는 action값 사용하기 위한 변수 
	var f = $("#searchForm"); // 검색에 사용할 폼
	// KBR : type : excel / pdf / true / false << 종류 4가지 
	type = !type ? 'list' : type;
	// KBR : 페이지가 undefined이 아니면서 0보다 크면 page 
	var currentPage = (page !== undefined && page > 0) ? page : 1; // 현재 페이지
	// KBR : 리스트를 한꺼번에 몇개씩 볼것인지의 갯수 / (default : 20개씩보기 )
	var pageSize = $("#pageSize").val(); // 리스트 갯수
	
	var keyDataArr = new Array();
	
	// 정렬을 위한 검색일 경우 기존 검색 조건은 그대로 유지하며, 기존 정렬관련 데이터는 삭제한다.
	if (type == 'sort') {jQuery.removeData($("#searchForm .form-body input, #searchForm .form-body select"),"order");
	
	// KBR : 픽스 되어 있는 폼말고 아래방향 아이콘 클릭 시 나오는 폼이 안보일 경우 
	// 검색조건이 축소되어 있을 때 search-opt 조건은 검색조건에서 제외시키기 위해 내용을 삭제한다. (정렬인 경우 제외)
	} else if (f.find('.search-opt').css('display') == 'none') {
		// KBR : 접었을 경우 폼값 초기화 
		f.find('.search-opt').find('input').val("");
		f.find('.search-opt').find('select.selectpicker').val(1);
		f.find('.search-opt').find('select.selectpicker').selectpicker('refresh');
	}
	
	// KBR : form 안 input 모든 값 ( hidden 포함 ) 
	$("#searchForm .form-body input").each(function(i, e) {
		// 단순조회 시 거래일자의 값들로 인해 if문 실행됨 (날짜값 2개 존재하기 때문에 2번 실행되는거 같음) (매입현황조회 기준)
		// reg의 경우 undefined(=false)인데 왜 if문 실행되는지...???? / name : regDay / val : 오늘 날짜 
		// ★★★ KBR : 밑 if문은 input에 값이 있는 상태로 search 클릭 시 발생 ★★★
		if ($(e).data("reg") != false && $(e).attr('name') && ($(e).val() || $(e).data("empty") == true)) {
			
			var oper = $(e).data("oper");
			
			var keyData = new Object();
			keyData.name = $(e).attr("name");
			// KBR : input-daterange 클래스가 있으면 replace로 공백으로 대체 , 아니면 기존 벨류 값 사용
			keyData.val = $(e).parent().hasClass('input-daterange') ? $(e).val().replace(/-/gi, '') : $(e).val();
			// 거래일자 조회시 oper의 값은 ge, le
			keyData.oper = (oper == undefined) ? "lk" : oper; 
			keyData.order = "";
			// KBR : controller 에서 key값 true인것 체크하여 input값이 들어왔는지 여부 확인
			keyData.key = true;
			keyDataArr.push(keyData);
			
			} 
		else if ($(e).attr('data-order')) {
			var keyData = new Object();
			keyData.name = $(e).attr("name");
			keyData.order = $(e).data('order');
			keyDataArr.push(keyData);
			
		}
	});
	
	// KBR : select 폼만 따로 체크
	// 조회 조건 중 상태 입력 조건 있을 시 실행 됨()
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
	
	// KBR : 페이지 값 셋팅
	var pageData = new Object();
	pageData.current = currentPage;
	pageData.total = 0;
	
	if (type == 'excel' || type == 'pdf') {
		pageData.size = 100000;
	} else {
		pageData.size = pageSize;
	}
	var searchData = new Object();
	// 단순 조회 시 type은 list의 값을 가지고 있다 => searchData.type, reason = "list"
	searchData.type = type;
	searchData.reason = type;
	searchData.redirect = "";
	// 조회 시 기본 값 ↓
	//searchData.page = [current = 1, total = 0]
	//searchData.data = [name = reg, val = 오늘날짜, oper = ge, order = "", key = true], [name = reg, val = 오늘날짜, oper = le, order = "", key = true] 
	searchData.page = pageData;
	searchData.data = keyDataArr;
	
	// KBR : excel , pdf 일경우  
	if (type == 'excel' || type == 'pdf') {
		// thead가 없을 경우 
		if ($('#searchForm input[name="thead"]').val() == undefined) {
			bootbox.alert("다운로드가 지정되지 않았습니다.");
			return;
		}
		
		// KBR : 파일명 셋팅 
		searchData.reason = $('#searchForm input[name="reason"]').val();
		// KBR : 테이블 head값 셋팅
		searchData.thead = $('#searchForm input[name="thead"]').val();
		
	}
	
	// KBR : 넘길 데이터 변환 
	searchDataJson = JSON.stringify(searchData);
	
	// KBR : 가맹점리스트일경우 action 값이 : /mcht/list 값임
	// KBR : searchDataJson > 해당 데이터 안에는 input 값 , select 값 , 페이징 값 , type값 등등이 들어가있음 
	searchAjax(searchDataJson, f.attr("action"), type, callbackFnc);
	
};

// form > list 페이지의 업체 리스트 가져옴
function searchAjax(searchData, action, type, callbackFnc) {
	console.log('searchData : ' + searchData + '\n-----------------------------------------------------------------------------\naction : \n' + action);
	$.ajax({
		type : "post",
		url : action,
		dataType : "text",
		// ajax 요청하기 직전 실행 함수 
		beforeSend : function(xhr) {
			// XHR Header를 포함해서 HTTP Request를 하기전에 호출된다.
			xhr.setRequestHeader("Content-type",
					"application/json;charset=utf-8");
		},
		// data가 컨트롤러에서 CPRequest 객체의 데이터로 들어간다
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
					// 가맹점 조회 시 가맹점 리스트 mcht/list.jsp페이지를 뿌려줌
					$("#searchResult").html(json);
					// KBR 예기치 않게 종료 되지 않을 수 있으니 타임아웃으로 종료 시킴 (ex:http://charlie0301.blogspot.com/2014/12/jquery-ajax-jqxhr-error-code-0.html)
					if(callbackFnc) {
						setTimeout(function() { callbackFnc(); }, 300);
					}	
				} else {
					bootbox.alert("검색에 실패하였습니다.");
				}
			}
		},
		error : function(request,xhr, status, error) {
//			consoel.log('request :' , request.status);
//			consoel.log('xhr :' , xhr);
//			consoel.log('status :' , status);
//			consoel.log('error :' , error);
			bootbox.alert("검색에 실패하였습니다.");
		}
	});
};

