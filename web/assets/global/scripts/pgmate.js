/**
Custom script
**/
//if(!Array.indexOf){ Array.prototype.indexOf = function(obj){ for(var i=0; i<this.length; i++){ if(this[i]==obj){ return i; } } return -1; } }

WebFontConfig = {
    custom: {
        families: ['Nanum Gothic'],
        urls: ['//fonts.googleapis.com/earlyaccess/nanumgothic.css']
    }
  };
  (function() {
    var wf = document.createElement('script');
    wf.src = ('https:' == document.location.protocol ? 'https' : 'http') +
      '://ajax.googleapis.com/ajax/libs/webfont/1.4.10/webfont.js';
    wf.type = 'text/javascript';
    wf.async = 'true';
    var s = document.getElementsByTagName('script')[0];
    s.parentNode.insertBefore(wf, s);
  })(); 

var historyPage ='/';
var PGmate = function() {
		
    // Handle  Settings
    var handlePG = function() {
    	$(document).on("keyup", "input:text[numberOnly], input.numberOnly", function() {$(this).val( $(this).val().replace(/[^0-9]/gi,"") );});
    	$(document).on("keyup", "input:text[numberHypen], input.numberHypen", function() {$(this).val( $(this).val().replace(/[^0-9\-]/gi,"") );});
    	
    	// 날짜 검색 세팅
    	$('.selectpicker').selectpicker('setStyle', 'input-sm', 'add');
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

		// 마스크
		textMask();
		
		//새로고침 후킹
    	$(document).keydown(function(e) { 
    		if (e.which == 116 || e.keyCode == 82 && e.ctrlKey) { //116 = F5 
    	    	document.location.reload();
    	        return false;
    	    }
    	});
    	
    	// AJAX
		$( document ).ajaxError(function( event, jqxhr, settings, thrownError ) {
			if (jqxhr.status == 401) {
				bootbox.alert("세션이 만료되었습니다. 다시 로그인해 주세요.", function() {
					document.location.href = "/";
                });
			} else {
				bootbox.alert("예외가 발생했습니다. 관리자에게 문의하세요. (" + jqxhr.status + ")" );
			}
		});
		$(window).ajaxStart(function(){
    		$('.loading-btn').button('loading');
    		$('body').modalmanager('loading');
    	});
    	$(window).ajaxStop(function(){
    		$('.loading-btn').button('reset');
    		$('body').modalmanager('removeLoading');
    	});
		
		//공지사항 카운트 표시
		refreshAlertCount();
		$('#header_notification_bar').click(function(){
			refreshAlertCount();
		});
		
		//정산 지급
		$(document).on('click', '.settle-pay-out, .settle-pay-hold, .settle-pay-cancel ', function(){
			var status = $(this).hasClass('settle-pay-out') ? '확정' : $(this).hasClass('settle-pay-hold') ? '보류' : $(this).hasClass('settle-pay-cancel') ? '대기' : '' ;
			var stlId = "'" + $(this).closest('tr').attr('data-stlId') + "'" ;
			settleStatusUpdate(status, stlId, $(this).hasClass('is-sub'));
		});
		
		//정산 확정 checkbox
		$(document).on('click', '.settle-pay-out-check', function(){
			var stlId = '';
			$('table.pg-table>tbody>tr').each( function(i, e){
				if($(e).find('input[type="checkbox"]').is(':checked')) {
					stlId += "'" + $(e).attr('data-stlId') + "',";
				}
			});
		
			if(stlId.length < 1) {
				bootbox.alert("정산 확정할 대상을 체크하세요.");
			} else {
				stlId = stlId.substring(0, stlId.length -1);
				settleStatusUpdate("확정", stlId, $(this).hasClass('is-sub'));
			}
		});
		//정산 지급 완료 처리
		$(document).on('click', '.settle-pay-complete', function(){
			var stlId = '';
			$('table.pg-table>tbody>tr').each( function(i, e){
				if($(e).find('input[type="checkbox"]').is(':checked')) {
					stlId += "'" + $(e).attr('data-stlId') + "',";
				}
			});
		
			if(stlId.length < 1) {
				bootbox.alert("지급완료 처리할 대상을 체크하세요.");
			} else {
				stlId = stlId.substring(0, stlId.length -1);
				settlePayStatusUpdate("지급완료", stlId, $(this).hasClass('is-sub'));
			}
		});
		//정산 대상거래 조회
		$(document).on('click', '.settle-detail', function(){
			var stlId = $(this).closest('tr').attr('data-stlId');
			var gradeId = $(this).attr('data-grade');
			settleDetailDownload(gradeId, stlId, $(this).hasClass('is-sub'));
		});

		//가상계좌 정산 지급
		$(document).on('click', '.vactSettle-pay-out, .vactSettle-pay-hold, .vactSettle-pay-cancel ', function(){
			var status = $(this).hasClass('vactSettle-pay-out') ? '확정' : $(this).hasClass('vactSettle-pay-hold') ? '보류' : $(this).hasClass('vactSettle-pay-cancel') ? '대기' : '' ;
			var stlId = "'" + $(this).closest('tr').attr('data-stlId') + "'" ;
			vactSettleStatusUpdate(status, stlId, $(this).hasClass('is-mcht'));
		});
		
		//가상계좌 정산 확정 checkbox
		$(document).on('click', '.vactSettle-pay-out-check', function(){
			var stlId = '';
			$('table.pg-table>tbody>tr').each( function(i, e){
				if($(e).find('input[type="checkbox"]').is(':checked')) {
					stlId += "'" + $(e).attr('data-stlId') + "',";
				}
			});
		
			if(stlId.length < 1) {
				bootbox.alert("정산 확정할 대상을 체크하세요.");
			} else {
				stlId = stlId.substring(0, stlId.length -1);
				vactSettleStatusUpdate("확정", stlId, $(this).hasClass('is-mcht'));
			}
		});
		//가상계좌 정산 지급 완료 처리
		$(document).on('click', '.vactSettle-pay-complete', function(){
			var stlId = '';
			$('table.pg-table>tbody>tr').each( function(i, e){
				if($(e).find('input[type="checkbox"]').is(':checked')) {
					stlId += "'" + $(e).attr('data-stlId') + "',";
				}
			});
		
			if(stlId.length < 1) {
				bootbox.alert("지급완료 처리할 대상을 체크하세요.");
			} else {
				stlId = stlId.substring(0, stlId.length -1);
				vactSettlePayStatusUpdate("지급완료", stlId, $(this).hasClass('is-mcht'));
			}
		});
		
		//가상계좌 정산 대상거래 조회
		$(document).on('click', '.vactSettle-detail', function(){
			var stlId = $(this).closest('tr').attr('data-stlId');
			var gradeId = $(this).attr('data-grade');
			vactSettleDetailDownload(gradeId, stlId, $(this).hasClass('is-sub'));
		});

		//월세 정산 지급
		$(document).on('click', '.rent-settle-pay-out, .rent-settle-pay-hold, .rent-settle-pay-cancel ', function(){
			console.log("rent-settle-pay-out click");
			var status = $(this).hasClass('rent-settle-pay-out') ? '확정' : $(this).hasClass('rent-settle-pay-hold') ? '보류' : $(this).hasClass('rent-settle-pay-cancel') ? '대기' : '' ;
			var stlId = "'" + $(this).closest('tr').attr('data-stlId') + "'" ;
			rentSettleStatusUpdate(status, stlId, $(this).hasClass('is-sub'));
		});

		//월세 정산 확정 checkbox
		$(document).on('click', '.rent-settle-pay-out-check', function(){
			var stlId = '';
			//KJM : 선택한 리스트 확인
			$('table.pg-table>tbody>tr').each( function(i, e){
				if($(e).find('input[type="checkbox"]').is(':checked')) {
					stlId += "'" + $(e).attr('data-stlId') + "',";
				}
			});

			if(stlId.length < 1) {
				bootbox.alert("정산 확정할 대상을 체크하세요.");
			} else {
				stlId = stlId.substring(0, stlId.length -1);
				//KJM : hasClass : 클래스명이 일치하는 것이 있을 경우 true 반환
				//KJM : is-sub : 대표가맹점 구분용
				rentSettleStatusUpdate("확정", stlId, $(this).hasClass('is-sub'));
			}
		});

		//월세 정산 지급 완료 처리
		$(document).on('click', '.rent-settle-pay-complete', function(){
			var stlId = '';
			//KJM : 선택한 리스트 확인
			$('table.pg-table>tbody>tr').each( function(i, e){
				if($(e).find('input[type="checkbox"]').is(':checked')) {
					stlId += "'" + $(e).attr('data-stlId') + "',";
				}
			});

			if(stlId.length < 1) {
				bootbox.alert("지급완료 처리할 대상을 체크하세요.");
			} else {
				stlId = stlId.substring(0, stlId.length -1);
				rentSettlePayStatusUpdate("지급완료", stlId, $(this).hasClass('is-sub'));
			}
		});

		//월세앱 정산 대상거래 조회
		$(document).on('click', '.rent-settle-detail', function(){
			var stlId = $(this).closest('tr').attr('data-stlId');
			var gradeId = $(this).attr('data-grade');
			rentSettleDetailDownload(gradeId, stlId, $(this).hasClass('is-sub'));
		});
		
		$(document).on('click', '.settle-mcht-decide', function(){
			var stlId = '';
			$('table.pg-table>tbody>tr').each( function(i, e){
				if($(e).find('input[type="checkbox"]').is(':checked')) {
					stlId += "'" + $(e).attr('data-stlId') + "',";
				}
			});
		
			if(stlId.length < 1) {
				bootbox.alert("정산 확정할 대상을 체크하세요.");
			} else {
				stlId = stlId.substring(0, stlId.length -1);
				mchtSettleDecide("확정", stlId);
			}
		});
		// 가맹점 정산 - 지급보류 상태 변경
		$(document).on('click', '.settle-hold-status', function(){
			var stlId = '';
			var status = $(this).data('status');
			$('table.pg-table>tbody>tr').each( function(i, e){
				if($(e).find('input[type="checkbox"]').is(':checked')) {
					stlId += "'" + $(e).attr('data-stlId') + "',";
				}
			});
		
			if(stlId.length < 1) {
				bootbox.alert("대상을 체크하세요.");
			} else {
				stlId = stlId.substring(0, stlId.length -1);
				mchtSettleHoldStatus(status, stlId);
			}
		});
		
		// 정산 상태(리스크) 변경
		$(document).on('click', '.trx-stl-status-change', function() {
			var stlStatus = $(this).attr("data-status");
			var capId = '';
			$('table.pg-table>tbody>tr').each(function(i, e) {
				if ($(e).find('input[type="checkbox"]').is(':checked')) {
					capId += "'" + $(e).attr('data-capId') + "',";
				}
			});

			if (capId.length < 1) {
				bootbox.alert("대상을 체크하세요.");
			} else {
				bootbox.prompt({
				    title: "변경 사유를 입력하세요.",
				    inputType: 'textarea',
				    callback: function (summary) {
				    	if(summary === null) {
				    	} else if(summary.length < 1) {
				    		bootbox.alert("변경 사유를 반드시 입력해야 합니다.");
				    	} else {
				    		capId = capId.substring(0, capId.length - 1);
							trxStlStatusChange(stlStatus, capId, summary);
				    	}
				    }
				});
			}
		});

		// 정산 지급 상태 변경
		$(document).on('click', '.settle-mcht-payout', function() {
			var status = $(this).attr("data-status");
			var stlId = '';
			$('table.pg-table>tbody>tr').each(function(i, e) {
				if ($(e).find('input[type="checkbox"]').is(':checked')) {
					stlId += "'" + $(e).attr('data-stlId') + "',";
				}
			});

			if (stlId.length < 1) {
				bootbox.alert("대상을 체크하세요.");
			} else {
				stlId = stlId.substring(0, stlId.length - 1);
				mchtSettlePayoutChange(status, stlId);
			}
		});
		
		
		
		// 정산 상태(리스크) 변경
		$(document).on('click', '.trx-cap-delete', function() {
			
			var capId = '';
			$('table.pg-table>tbody>tr').each(function(i, e) {
				if ($(e).find('input[type="checkbox"]').is(':checked')) {
					capId += "'" + $(e).attr('data-capId') + "',";
				}
			});

			if (capId.length < 1) {
				bootbox.alert("대상을 체크하세요.");
			} else {
				bootbox.prompt({
				    title: "거래가 삭제됩니다. 변경 사유를 입력하세요.",
				    inputType: 'textarea',
				    callback: function (summary) {
				    	if(summary === null) {
				    	} else if(summary.length < 1) {
				    		bootbox.alert("변경 사유를 반드시 입력해야 합니다.");
				    	} else {
				    		capId = capId.substring(0, capId.length - 1);
							trxCapDelete(capId, summary);
				    	}
				    }
				});
			}
		});
		
		// 대출정산 지급 상태 변경
		$(document).on('click', '.settle-loan-payout', function() {
			var status = $(this).attr("data-status");
			var loanStlId = '';
			$('table.pg-table>tbody>tr').each(function(i, e) {
				if ($(e).find('input[type="checkbox"]').is(':checked')) {
					loanStlId += "'" + $(e).attr('data-loanStlId') + "',";
				}
			});

			if (loanStlId.length < 1) {
				bootbox.alert("대상을 체크하세요.");
			} else {
				loanStlId = loanStlId.substring(0, loanStlId.length - 1);
				loanSettlePayoutChange(status, loanStlId);
			}
		});
		
		$(document).on('click', '.float-window', function() {
			var w = '1500';
			var h = '950';
			window.open('/common/viewPopup.jsp', "", "width="+w+", height="+h+", scrollbars=1");
		});

		//노티 재전송
		$(document).on('click', '.noti-retry', function () {
			var notiList = '';
			$('table.pg-table>tbody>tr').each(function(i, e) {
				if ($(e).find('input[type="checkbox"]').is(':checked')) {
					notiList += "'" + $(e).attr('data-notiIndex') + "',";
				}
			});

			console.log(notiList);

			if (notiList.length < 1) {
				bootbox.alert("대상을 체크하세요.");
			} else {
				notiList = notiList.substring(0, notiList.length - 1);
				trxNotiRetry(notiList);
			}

		});
		
		
    };
    
    var handleSessionAlive = function () {
    	if(window.location.pathname == 'error'){
    	}else{
	    	$.ajax({
	    		url : "/sessionAlive",
	    		type : 'GET',
	    		success : function(msg){
	    			if(msg.indexOf('MSG') > -1){
	    				if(msg.split("||")[1] != 'ALIVE'){
	    					bootbox.alert(msg.split("||")[1], function() {
	    						window.top.location.href = "/";
	                        });
	    				}
	    			}
	    		},
	    		error : function(result){
	    			bootbox.alert("Could not load the requested content.");
	    		}
	    	});
    	}
    }
    var handleReload = function() {
    	var frame_id = 'mainMenu';
        if(window.document.getElementById(frame_id).location ) {  
            window.document.getElementById(frame_id).location.reload(true);
        } else if (window.document.getElementById(frame_id).contentWindow.location ) {
            window.document.getElementById(frame_id).contentWindow.location.reload(true);
        } else if (window.document.getElementById(frame_id).src){
            window.document.getElementById(frame_id).src = window.document.getElementById(frame_id).src;
        } else {
            // fail condition, respond as appropriate, or do nothing
            bootbox.alert("Sorry, unable to reload that frame!");
        }
    };

    return {
        init: function() {
            handlePG(); 
            handleSessionAlive();
        },
        getPageContent: function(urls, type) {
        	historyPage= urls;
        	var urld = urls.split("?");
        	$.ajax({
        		url : urld[0],
        		type : (type == undefined) ? 'POST' : type,
        		data : urld[1],
        		success : function(msg){
        			if(msg.indexOf('MSG') == 0){
        				bootbox.alert(msg.split("||")[1], function() {
        					document.location.href = "/sso/websso";
                        });
        			}else{
        				$('.page-content').html(msg);
        				//init
        			}
        		},
        		error : function(result){
        			bootbox.alert("Could not load the requested content.");
        		}
        	});
            
        },
        getLoadPage: function(page) {
        	$.ajax({
        		url : page,
        		type : 'POST',
        		success : function(msg){
        			if(msg.indexOf('MSG') == 0){
        				bootbox.alert(msg.split("||")[1], function() {
        					document.location.href = "/sso/websso.jsp";
                        });
        			}else{
        				$('.page-content').html(msg);	
        				//init
        			}
        		},
        		error : function(result){
        			bootbox.alert("Could not load the requested content.");
        		}
        	});
            
        },

    };
    
}();

var post = function (path, params, method) {
    method = method || "post"; // Set method to post by default if not specified.

    // The rest of this code assumes you are not using a library.
    // It can be made less wordy if you use one.
    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", path);

    for(var key in params) {
        if(params.hasOwnProperty(key)) {
            var hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", params[key]);

            form.appendChild(hiddenField);
         }
    }

    document.body.appendChild(form);
    form.submit();
}

function textMask(){
	// 마스크
	var dateMask = $('.pg-view-group .form-control-static.date, td.date, span.date');
	var timeMask = $('.pg-view-group .form-control-static.time, td.time, span.time');
	var patt = new RegExp("^[0-9]{8}$");
	var pattM = new RegExp("^[0-9]{6}$");
	var pattT = new RegExp("^[0-9]{14}$");
	var pattFULL = new RegExp("^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}");
	$.each(dateMask, function(i, e) {
		var txt = $(e).text().trim();
		if(txt && patt.test(txt)) {
			$(e).text(txt.substr(0,4) + "-" + txt.substr(4,2) + "-" + txt.substr(6,2));
		}else if(txt && pattM.test(txt)){
			$(e).text(txt.substr(0,4) + "-" + txt.substr(4,2));
		}else if(txt && pattT.test(txt)){
			$(e).text(txt.substr(0,4) + "-" + txt.substr(4,2) + "-" + txt.substr(6,2) + " " + txt.substr(8,2) + ":" + txt.substr(10,2) + ":" + txt.substr(12,2) );
		}else if(txt && pattFULL.test(txt)){
			$(e).text(txt.substr(0,19));
		}
	});
	$.each(timeMask, function(i, e) {
		var txt = $(e).text().trim();
		if(txt && pattM.test(txt)) {
			$(e).text(txt.substr(0,2) + ":" + txt.substr(2,2) + ":" + txt.substr(4,2));
		}
	});
	
	
	var currencyMask = $('.form-control-static.digits, td.digits, span.digits');
	var currencyPatt = new RegExp("(^[0-9]*$)|(^-[0-9]*$)");
	$.each(currencyMask, function(i, e) {
		var txt = $(e).text().trim();
		if(txt && currencyPatt.test(txt)) {
			$(e).text(txt.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
		}
	});
	var rateT = $('.rate');
	$.each(rateT, function(i, e) {
		var txt = $(e).text().trim();
		if($.isNumeric(txt) && txt >= 0 && txt <= 99.99) {
			$(e).text((txt * 100).toFixed(2) + ' %');
		}else if($.isNumeric(txt) && txt == 100) {
			$(e).text((txt * 1).toFixed(0) + ' %');
		}
	});
	
	$('[data-toggle="tooltip"]').tooltip();
}

var searchNotification = function(){
	
}

function cpDebug(){
	
	$.ajax({
		url : "/common/debug",
		type : 'GET',
		success : function(msg){
			bootbox.alert(msg, function(result) {
				document.location.href = "/";
			});
		},
		error : function(result){
			bootbox.alert("Could not load the requested content.");
		}
	});
	
}

function notifyDesktop(str) {
	// Let's check if the browser supports notifications
	var options = {
			icon: '/assets/layouts/layout3/img/logo-cyrex.png'
		}
	if (!("Notification" in window)) {
		console.log("This browser does not support desktop notification");
	}
	
	// Let's check whether notification permissions have already been granted
	else if (Notification.permission === "granted") {
		// If it's okay let's create a notification
		var notification = new Notification(str, options);
	}

	// Otherwise, we need to ask the user for permission
	else if (Notification.permission !== 'denied') {
		Notification.requestPermission(function(permission) {
			// If the user accepts, let's create a notification
			if (permission === "granted") {
				var notification = new Notification(str, options);
			}
		});
	}

	// At last, if the user has denied notifications, and you
	// want to be respectful there is no need to bother them any more.
}



jQuery(document).ready(function() {    
	PGmate.init(); // init metronic core componets
	//toastr.info('Are you the 6 fingered man?');
});


