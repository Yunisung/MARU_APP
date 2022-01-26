
// KBR : 페이지 로딩 시 바로 실행 ( 이 js파일 제일 하단에 실행시키고 있음)
var initForm = function() {
	$.validator.setDefaults({
		ignore: "",
		debug: true,
		highlight: function (element) { // hightlight error inputs
            $(element).closest('.form-group').addClass('has-error'); // set error class to the control group
        },
        unhighlight: function (element) { // revert the change done by hightlight
            $(element).closest('.form-group').removeClass('has-error'); // set error class to the control group
        },
        success: function (label) {
        	label.closest('.form-group').removeClass('has-error'); 
            label.closest('.form-group').addClass('has-success'); 
        }
	});
	
	// Add validator Role
	// 함수를 통해 커스텀 밸리데이션 추가하기
	jQuery.validator.addMethod("userId", function(value, element) {
	    var isValid =  /(^[0-9a-zA-Z]{5,30}$)/.test(value);
	    // KBR : 공백이면 true를 반환
	    // KBR : optional 검사는 기본적으로 규칙에 맞는지 여부를 평가하기 전에 필드가 비어 있는지 확인
	    return this.optional(element) || isValid;
	},"영문, 숫자로된 5~30자를 입력할 수 있습니다.");
	
	jQuery.validator.addMethod("money", function(value, element) {
		    var isValidMoney =  /^[0-9,₩ ]*$/.test(value) ;
		    if(isValidMoney && value.replace(/[, ₩]'/g, '') < 0) isValidMoney = false;
		    return this.optional(element) || isValidMoney;
		},"금액만 입력할 수 있습니다.");
	
	jQuery.validator.addMethod("maxMoney", function(value, element, options) {
			value = replaceAll(value, ',', '');
			value = replaceAll(value, ' ₩', '');
			var isValidMoney = (value <= options) ? true : false;
			return this.optional(element) || isValidMoney;
		}, function(params, element) {
			return '최대 한도 : ' + params + ' ₩';
		});
	
	jQuery.validator.addMethod("minMoney", function(value, element, options) {
		value = replaceAll(value, ',', '');
		value = replaceAll(value, ' ₩', '');
		var isValidMoney = (value > options) ? true : false;
		return this.optional(element) || isValidMoney;
	}, function(params, element) {
		return '값이너무 작습니다. 연관된 하위 멤버의 한도를 다시 설정해야 합니다.';
	});
	
	jQuery.validator.addMethod("rate", function(value, element) {
		var isValid = /0.[0-9]{1,5}/g.test(value);
		if(isValid && value >= 1) isValid = false;
		
		return this.optional(element) || isValid;
		},"1 % 이하의 값을 입력하세요.");
	
	jQuery.validator.addMethod("settle", function(value, element) {
		var isValid = /0.[0-9]{1,5}/g.test(value);
		if(isValid && value >= 1) isValid = false;
		
		return this.optional(element) || isValid;
		},"1 % 이하의 값을 입력하세요.");
	
	jQuery.validator.addMethod("equals", function(value, element, param) { 
		  return this.optional(element) || value !== param; 
		}, "기존과 동일한 소속입니다.");
	
	jQuery.validator.addMethod(
	        "regex",
	        function(value, element, regexp) {
	            var re = new RegExp(regexp);
	            return this.optional(element) || re.test(value);
	        },"입력값을 확인해주세요.");
	
	// 필수항목 표시
	$('#writeFrm label.req-label').append('<span class="required">*</span>');
	
	// 토글다운 메뉴 변경사항 반영 (identity 등)
	$('#writeFrm .dropdown-menu>li>a').click(function() {
		var target = $(this).closest('ul').data('target');
		var buttonLabel = $(this).closest('.input-group-btn').find('.dropdown-toggle>span');
		var targetTxt = $(this).text();
		var inp = $('#writeFrm').find('input[name="'+target+'"]');
		inp.val(targetTxt);
		buttonLabel.text($(this).text());
	});
	
	// 은행 코드 변경시 은행 이름 자동기입
	$('#writeFrm .bankCd').bind('change',function(e) {
		var selected = $(this);
		var selectedId = $(this).val();
		var selectedText = $(this).find('option[value="'+selectedId+'"]').text();
		selectedText = selectedText == '은행 선택' ? '' : selectedText;
		$('#writeFrm').find('input[name="bankName"]').val(selectedText);
	});
	
	// 식별정보 변경 버튼 클릭 
	$('#writeFrm').on('click', '.change-identity', function() {
		var $modal = $('#pgmate-modal');
		if($modal.children().length < 1) {
			$modal.empty();
		}
		var groupElement = $(this).closest('.form-group');
		var typeTargetElement = groupElement.find('.change-target-idType');
		var identityElement = groupElement.find('.change-target-identity');
		
	    $modal.load('/common/changeIdentity.jsp', '', function(responseTxt, statusTxt, xhr){
	        if(statusTxt == "success"){
	        	$modal.modal();
	        	if(identityElement.hasClass('ceoIdentity')) {
	        		$modal.find('.dropdown-toggle>span').text("주민번호");
	        		$modal.find('input[name="idType"]').val("주민번호");
	        		$modal.find('.dropdown-toggle').attr('disabled',true);
	        	}
	        }
	    });
	    
	    $modal.on('hidden.bs.modal', function () {
	    	if($('#modalForm input[name="change-flag"]').val() == 'true') {
	    		typeTargetElement.val($('#modalForm input[name="idType"]').val());
	    		identityElement.val($('#modalForm input[name="identity"]').val());
	    	}
	    	$modal.empty();
	    });
	});
} 

function replaceAll(str, searchStr, replaceStr) {
    return str.split(searchStr).join(replaceStr);
}

// B : 1회한도 , 1일한도, 1개월 한도
var addLimitRules = function(limitOnce, limitDay, limitMonth) {
					// addClassRules(이름, 규칙)
	limitOnce > 0 && $.validator.addClassRules('limitOnce', {
		money : true,
		maxMoney : limitOnce
	});

	limitDay > 0 && $.validator.addClassRules('limitDay', {
		money : true,
		maxMoney : limitDay
	});

	limitMonth > 0 && $.validator.addClassRules('limitMonth', {
		money : true,
		maxMoney : limitMonth
	});
}

var addLimitLowRules = function(limitOnceMin, limitDayMin, limitMonthMin) {
	limitOnceMin > 0 && $.validator.addClassRules('limitOnce', {
		money : true,
		minMoney : limitOnceMin
	});
	limitDayMin > 0 && $.validator.addClassRules('limitDay', {
		money : true,
		minMoney : limitDayMin
	});
	limitMonthMin > 0 && $.validator.addClassRules('limitMonth', { money : true, minMoney : limitMonthMin });
}

var addLimitHighLowRules = function(limitOnce, limitDay, limitMonth, limitOnceMin, limitDayMin, limitMonthMin) {
	limitOnce > 0 && limitOnceMin > 0 && $.validator.addClassRules('limitOnce', { money : true, maxMoney : limitOnce, minMoney : limitOnceMin });
	limitDay > 0 && limitDayMin > 0 && $.validator.addClassRules('limitDay', { money : true, maxMoney : limitDay, minMoney : limitDayMin });
	limitMonth > 0 && limitMonthMin > 0 && $.validator.addClassRules('limitMonth', { money : true, maxMoney : limitMonth, minMoney : limitMonthMin });
}

// 정보 수정 시 ajax를 통한 수정 (멤버관리 > 대행사 정보 수정)
function ajaxFormSubmit(form, redirectUrl){
	
	event.preventDefault();
	// form안에 submit버튼 눌러도 새로 실행하지 않게 하기(submit은 작동됨)..??
	
	var action = "";
	var keyDataArr = new Array();
	// activeType = update/...
	var activeType = $(form).find("[name='action_type']").val();
	//console.log('activeType' , activeType)
	var searchKeyCnt = 0;
	
	// 폼 안에 입력 값 모두 가져옴
	$(form).find('input, select, textarea').each(function(i, e) {
		
		// input hidden값으로 id값 true 셋팅 돼 있음
		var isSearchKey = $(e).data("key");
		
		// KBR : 컬럼 값 
		var name = $(e).attr("name");
		var type = $(e).attr("type");
		var value = $(e).val();
		
		var checked = false;
		// 입력값이 체크박스 / 라디오 박스일 경우 체크 확인
		if(type == "checkbox" || type == "radio"){
			if($(e).prop("checked") == true){
				checked = true;
			}
			else {
				checked = false;
			}
		// 입력값이 체크박스 / 라디오 박스 외의 경우 체크 = true
		}else{
			checked = true;
		}
		
		var keyData = new Object();
		
		keyData.name = name;
		keyData.order = "";
		// KBR : 모든 입력 값 
		var val = $(e).val();
		if($(e).attr('name') == 'summary' && $('#summary-result')){
			val = $('#summary-result').html();
		}else if($(e).parent().hasClass('input-daterange') || $(e).hasClass('datepicker')){
			val = $(e).val().replace(/-/gi, '');
		}else if($(e).hasClass('datepicker')){
			val = $(e).val().replace(/-/gi, '');
		}else if($(e).hasClass('timeInput')){
			val = $(e).val().replace(/:/gi, '');
		}
		
		
		// keyData.val에 모든 입력값 개별로 들어감
		keyData.val = val;
		
		// KBR : 업데이트의 경우 id 의 key 값만 true로 설정하기 위한 조건문 
		// 수정하는 업체의 아이디만 key = true (대행사 정보 수정의 경우)
		if(isSearchKey == true){
			// distId(컬럼명) = distId(값)
			keyData.oper = "eq";
			keyData.key = true;
			keyDataArr.push(keyData);
			// 수정하는 대상이 있는지 파악하기 위한 변수
			searchKeyCnt++;
			
		// KBR : id 값 외 모든 key 값 false로 설정
		// action_type 인풋 경우 data('reg') = false
		// action_type이 아니면서 name, value가 입력 되었고, checked가 true일 때
		}else if($(e).data("reg") != false && name && value && checked) {
			keyData.oper = "";
			keyData.key = false;
			keyDataArr.push(keyData);
		}
	});
	
	// KBR : 검색 대상(즉, 가맹점 수정일 경우 mchtId )이 나오지 않을 때 예외처리
	// update인데 수정하는 대상이 없을 경우 에러
	if(activeType == 'update' && searchKeyCnt < 1) {
		bootbox.alert('업데이트에 대상이 올바르게 설정되지 않았습니다.');
		return;
	}
	
	var executeData = new Object();
	// 컨트롤러에서 받아서 수행할 url
	executeData.type = action;
	// KBR : 수정 일 경우 값 = update
	executeData.reason = activeType;
	// 컨트롤러에서 쿼리문 수행 결과 보내줄 url
	executeData.redirect = redirectUrl;
	// KBR : 업데이트 할 데이터들
	executeData.data = keyDataArr;
	
	var dataJson = JSON.stringify(executeData);
	
	$.ajax({
		type : "post",
		// /member/dist/ + update
		url : $(form).attr('action') + activeType,
		// 서버에서 받을 데이터 타입 = json
		dataType : "json",
		beforeSend : function(xhr){
			xhr.setRequestHeader("Content-type","application/json");
		},
		// 서버로 보낼 데이터 
		data : dataJson,
		success: function(json, textStatus){
			// 데이터 조회 성공 시
			if(json.result.code == "200"){
				bootbox.alert(json.result.message, function() {
					// 쿼리문 수행 결과 보내줄 url로 이동 
					if(json.redirect){
						location.href = json.redirect;
					}
				});
			}else{
				bootbox.dialog({
				    title: json.result.message,
				    message: '<p>'+json.result.error+'</p>'
				});
			}
		},
		error: function(xhr, textStatus, errorThrown){
			bootbox.alert('Error ' + errorThrown);
		}
	});
}

// B  
/** 
 * 소속 선택
 * submitForm : 전달 form id 이름  
 * userGrade  : 로그인 업체 구분 (본사, 지사 등등.. )
 * targetGrade: 선택 업체 ( 대행, 에이전시 , 지사 등)
 * targetInput: 선택 업체 컬럼명 
 * 
 * **/
var gradeSelector = function(submitForm, userGrade, targetGrade, targetInput, debug) {
	
	if(debug) console.log("INIT : USER = " + userGrade + " / TARGET = " + targetGrade);
	
	//if(subText(userGrade) == targetGrade || userGrade == targetGrade){
	
	// 로그인 중인 계정의 소속이 [선정산, 지사, 터미널, 가맹점]일 때 소속 선택 구역 숨김
	if(userGrade == targetGrade || userGrade == '선정산' || userGrade == '지사'  || userGrade == '터미널' || userGrade.indexOf('가맹점') > -1){	
		$('#gradeSelector').hide(); 
		return;
	}
	
	// submitForm의 type이 string일 때 
	if(typeof submitForm === 'string'){
		// submitForm은 jsp 파일의 해당 이름을 가진 form울 가리킨다.
		submitForm = $('#' + submitForm);
		// submitForm이 존재하지 않을 때 submitForm은 writeFrm이란 form을 가리킨다.
		if(!submitForm) {
			submitForm = $('#writeFrm');
		} 
	}
	
	// submitForm이 존재하지 않을 때 콘솔로 메시지 띄워줌
	if(!submitForm) {
		if(debug) console.log("소속선택에 사용할 폼 정보가 없습니다.");
	}
	
	// jsp파일의 "$submitFrom"의 id가 "searchForm"일 때
	if($(submitForm).attr("id") == "searchForm") {
		// targetInput은 jsp파일의 grade_search란 id를 가진 요소를 가리킨다
		targetInput = $('#grade_search');
	// targetInput이 존재하지 않을 때 targetInput은 "parentId"란 name을 가진 요소를 가리킨다
	} else if(!targetInput) {
		targetInput = $(submitForm).find('input[name="parentId"]');
	// targetInput의 type이 string일 때 targetInput은 "tagetInput"란 name을 가진 요소를 가리킨다
	} else if(typeof targetInput === 'string') {
		targetInput = $(submitForm).find('input[name="'+targetInput+'"]');
	}
	
	// targetInput이 존재하지 않을 때 콘솔에 해당 메시지 띄워줌
	if(!targetInput) {
		if(debug) console.log("소속선택에 사용할 ID 를 입력할 INPUT이 지정되지 않았습니다.");
	}
	
	// 소속 선택 범위 => 로그인 중 계정 소속의 하위 소속들로 세팅
	$('#gradeSelector .selectedTargetGrade').val(subText(userGrade)); // b : 본사일 경우 value 값에 '대행사' 추가됨
	// targetGrade가 존재 할 때
	if(targetGrade) {
		// 소속 선택 박스에서 targetGrade를 선택 상태로 세팅
		$('#gradeSelector select.selectedTargetGrade').append('<option value="'+targetGrade+'">'+targetGrade+'</option>');
		// 소속 선택 박스 숨김
		$('#gradeSelector .selectedTargetGrade').closest('.selecter-wrapper').addClass('hide');
		// "대행사 | 에이전시 | 지사 " 선택
		$('#gradeSelector .selectGrade-label').text(targetGrade + ' 선택');
	// targetGrade가 존재하지 않을 때
	}else {
		// targetGrade = 로그인 중인 계정 소속의 하위 소속 정보로 selectbox 세팅
		targetGrade = subText(userGrade);
		
		if(targetGrade == '대행사') {
			$('#gradeSelector select.selectedTargetGrade').append('<option value="대행사">대행사</option>');
			$('#gradeSelector select.selectedTargetGrade').append('<option value="에이전시">에이전시</option>');
			$('#gradeSelector select.selectedTargetGrade').append('<option value="지사">지사</option>');
		}else if(targetGrade == '에이전시'){
			$('#gradeSelector select.selectedTargetGrade').append('<option value="에이전시">에이전시</option>'); 
			$('#gradeSelector select.selectedTargetGrade').append('<option value="지사">지사</option>');
		}else if(targetGrade == '지사'){
			$('#gradeSelector select.selectedTargetGrade').append('<option value="지사">지사</option>'); 
		}
	}
	// B :  
	$('#gradeSelector select.depth1').attr('name', subText(userGrade));
	$('#gradeSelector select.depth1').prepend('<option value="">'+subText(userGrade)+' 선택</option>'); 
	$('#gradeSelector select.depth1').val(1);
	
	// 선택 된 소속이 바뀔 때
	$('#gradeSelector .selecterGrade, .selectedTargetGrade').bind('change',function(e) {
		var selected = $(this); 																			// 선택된 객체
		// 소속 선택 시 "selectedTargetGrade" / 소속의 업체 선택 시 "대행사 | 에이전시 | 지사" 
		var selectedGrade = $(this).attr("name"); 															// 선택된 소속
		// 소속 선택 시 아이디 = "대행사 | 에이전시 | 지사" / 소속의 업체명 선택 시 아이디 = 업체명의 아이디
		var selectedId = $(this).val(); 																	// 선택된 아이디	
		// 선택된 소속 = 목표 소속
		var targetGrade = $('#gradeSelector .selectedTargetGrade').selectpicker('val'); 					// 목표 소속
		if(debug) console.log("선택된 소속: " + selectedGrade+ " /선택된 아이디: " + selectedId + " /목표 소속: "+ targetGrade);
		// 선택 된 소속에서 다른 소속 선택 시
		if (selected.hasClass('selectedTargetGrade')) { // IF : 소속 선택이 바뀜
			if(debug) console.log("목표 소속 변경 => " + targetGrade);
			// 소속된 업체 선택 초기화
			$('#gradeSelector select.depth1').val(1);
			$('#gradeSelector select.depth1').selectpicker('refresh');
			$('#gradeSelector select.depth1').closest('.selecter-wrapper').nextAll('.selecter-wrapper').addClass('hide');
			$('#gradeSelector').attr("data-last-selected", "false");
			// 선택 버튼 비활성화
			$('#gradeSelector .confirm-btn').addClass('disabled');
		// 소속된 업체 선택 초기화 시
		} else if (selectedId == "") { // IF : 현재 단계 초기화됨
			if(debug) console.log("단계 초기화");
			selected.closest('.selecter-wrapper').nextAll('.selecter-wrapper').addClass('hide');
			$('#gradeSelector').attr("data-last-selected", "false");
			// 선택 버튼 비활성화
			$('#gradeSelector .confirm-btn').addClass('disabled');
		// 선택 옵션들을 모두 맞게 선택했을 때
		} else if (targetGrade == selectedGrade) { // IF : 최종 단계를 선택함
			var targetId = selected.selectpicker('val');
			var targetName = selected.find("option[value='"+ selected.selectpicker('val')+ "']").text();
			var targetGrade = targetGrade;
			if(debug) console.log("최종 단계(" + selectedGrade+ ") 선택 : " + targetId + " "+ targetName);
			// 최종 선택 업체 정보 세팅
			$('#gradeSelector').attr("data-res-id", selected.selectpicker('val'));
			$('#gradeSelector').attr("data-res-name", selected.find("option[value='"+ selected.selectpicker('val')+ "']").text());
			$('#gradeSelector').attr("data-res-grade", targetGrade);
			$('#gradeSelector').attr("data-last-selected", "true");
			// 선택 버튼 활성화
			$('#gradeSelector .confirm-btn').removeClass('disabled');
		// 대행사 외 소속 선택 시 선택 소속의 상위 소속 업체 선택 시
		// 에이전시의 경우 => 에이전시 - "대행사 업체" - 에이전시 업체 순서 중 대행사 업체 선택 시 
		} else { // IF : 단계(최종이 아닌)를 선택함
			if(debug) console.log("단계(최종이 아닌)를 선택 =>" + selectedGrade);
			// HTTP POST 요청으로 데이터 가져옴
			$.post("/member/user/childrenList",{
				"grade" : selectedGrade,"parentId" : selectedId
				// 가져온 데이터를 이용해 jsp 세팅
				},function(data) {
				// 다음 선택 박스 표시함
				// 에이전시 선택 시 [에이전시 - 대행사 업체명 ] -> 에이전시 업체명 select box 표시
				selected.closest('.selecter-wrapper').next().removeClass('hide');
				// 다음 선택 박스
				var nextSelect = selected.closest('.selecter-wrapper').next().find("select");
				nextSelect.empty();
				// "에이전시 | 지사" 선택
				nextSelect.prepend('<option value="">'+subText(selectedGrade)+' 선택</option>');
				// data = 반환받은 데이터 리스트 / data 크기만큼 반복문 수행
				$.each(data,function(i,e) {console.log('<option value="'+ e.id +'">'+ e.name+ '</option>');
					// 소속 업체 아이디를 value값으로 두고 업체명을 보여주게 세팅
					nextSelect.append('<option value="'+ e.id +'">'+ e.name+ '</option>');
				});
				// 다음 선택해야할 소속 넣어줌 (에이전시 | 지사)
				nextSelect.attr("name",subText(selectedGrade));
				nextSelect.selectpicker('refresh');
			});
		}
	});
	
	// 선택 확정 버튼 클릭 (선택 변경 버튼으로 토클)
	$('#gradeSelector .confirm-btn').bind('click', function() {
		// 선택 확정 상태에서(변경 버튼) 클릭했을 때
		if($('#gradeSelector .confirm-btn').hasClass("change-btn")){
			// 선택 박스 활성화
			$('#gradeSelector .selectpicker').attr('disabled', false).selectpicker('refresh');
			// 버튼 색깔 바꾸기 위해 클래스명 바꿔준다. blue-dark -> green / 버튼 텍스트 선택으로 변경
			$('#gradeSelector .confirm-btn').addClass('green').removeClass('blue-dark').removeClass("change-btn").text("선택");
			
			// B : 인자 값으로 들어온 변수명 !! 
			$(targetInput).val('');
			//???? submitForm, targetInput 뭔지모르겠...
			$(submitForm).find('input[name="parentName"]').val('');
			$(submitForm).find('input[name="grade"]').val('');
		// 모두 선택한 상황에서 선택 확정 시(선택 버튼)
		} else if($('#gradeSelector').attr("data-last-selected") == "true") {
			// 버튼의 클래스명과 텍스트 바꿔줌
			$('#gradeSelector .confirm-btn').text("변경").addClass("change-btn").removeClass('green').addClass('blue-dark');
			// 선택 박스 비활성화
			$('#gradeSelector .selectpicker').attr('disabled', true).selectpicker('refresh');
			
			$(targetInput).val($('#gradeSelector').attr("data-res-id"));
			
			if($(submitForm).attr("id") == 'searchForm') {
				$(targetInput).attr("name", toTableId($('#gradeSelector').attr("data-res-grade")));
			} else {
				$(submitForm).find('input[name="parentName"]').val($('#gradeSelector').attr("data-res-name"));
				$(submitForm).find('input[name="grade"]').val($('#gradeSelector').attr("data-res-grade"));
			}
		}
	});
	
	// 현재 소속의 하위 소속 가져옴 (대행사일 경우 에이전시 가져옴)
	function subText(orgText) {
		if(orgText == "본사") {return "대행사";
		} else if (orgText == "대행사") {return "에이전시";
		} else if (orgText == "에이전시") {return "지사";
		} else {return "";}
	}
	
	function toTableId(orgText) {
		if (orgText == "대행사") {return "distId";
		} else if (orgText == "에이전시") {return "agencyId";
		} else if (orgText == "지사") {return "salesId";
		} else {return "";}
	}
}

// KBR : 다음 주소 API ( 참고 :https://postcode.map.daum.net/guide)
function postCode($this, zip, addr1, addr2, lat, lng) {
	
//	var geocoder = new daum.maps.services.Geocoder();
	
	new daum.Postcode({
        oncomplete: function(data) {
        	// 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var fullAddr = ''; // 최종 주소 변수
            var extraAddr = ''; // 조합형 주소 변수

            // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                fullAddr = data.roadAddress;

            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                fullAddr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
            if(data.userSelectedType === 'R'){
                //법정동명이 있을 경우 추가한다.
                if(data.bname !== ''){
                    extraAddr += data.bname;
                }
                // 건물명이 있을 경우 추가한다.
                if(data.buildingName !== ''){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
                fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            zip.val(data.zonecode);
            addr1.val(fullAddr);
            if(addr2) addr2.val(extraAddr);
            
//            geocoder.addressSearch(data.address, function(results, status) {
//                // 정상적으로 검색이 완료됐으면
//                if (status === daum.maps.services.Status.OK) {
//
//                    var result = results[0]; //첫번째 결과의 값을 활용
//                    if(lat) lat.val(result.y); // latitude 위도
//                	if(lng) lng.val(result.x); // longitude  경도
//                }
//            });
            
            // 커서를 상세주소 필드로 이동한다.
            addr2.focus();
        }
    }).open();
}

// 하위 사용자 비밀번호 초기화
function resetPassword(grade, id ) {
	bootbox.confirm('비밀번호를 재설정하고 임시 비밀번호를 발급하시겠습니까?', function(result) {
		// 비밀번호 발급 확인창에서 확인 눌렀을 때
		if(result) {
			if(grade == 'user') {
				$.get("/member/user/resetPassword/" + id, function( data ) {
					// 반환값 {result : OK | NOK , msg }
					// 반환값 중 메시지를 알림창으로 표시
					bootbox.alert(data.msg);
				});
			}
		}
	});
}


// KBR : 바로 실행 
(function() {
	initForm();

}());

