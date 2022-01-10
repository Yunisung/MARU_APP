<%@page contentType="text/html; charset=UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<c:import url="/include/head.jsp" />	
</head>
<!-- END HEAD -->
<body class="page-header-fixed page-sidebar-closed-hide-logo page-content-white">
	<div class="page-wrapper">
		<c:import url="/include/header.jsp" />
		<!-- BEGIN CONTAINER -->
		<div class="page-container">
			<c:import url="/include/nav.jsp" />
			<!-- BEGIN CONTENT -->
			<div class="page-content-wrapper">
				<div class="page-content">
					<div class="mtouch-container">
						 <!-- BEGIN PAGE BAR -->
						<div class="page-bar">
							<ul class="page-breadcrumb">
									<li><a href="/">Home</a><i class="fa fa-circle"></i></li>
									<li><span>전자계약서</span><i class="fa fa-circle"></i></li>
									<li><span>전자계약서 발송</span></li>
							</ul>
							<div class="page-toolbar">
								<div class="btn-group btn-theme-panel">
									<a class="btn float-window"><i class="icon-size-fullscreen"></i></a>
									<a href="javascript:;" class="btn dropdown-toggle" data-toggle="dropdown">
										<i class="icon-settings"></i>
									</a>
									<div class="dropdown-menu theme-panel pull-right dropdown-custom hold-on-click panel-warning">
										<div class="panel-heading">도움말</div>
									</div>
								</div>
							</div>
						</div>
	<!-- END PAGE BAR -->
	<!-- BEGIN PAGE CONTENT - MARU - INNER -->
	<div class="page-content-inner">
		<div class="portlet light">
			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-reorder"></i>
					<span class="caption-title">전자계약서 작성</span>
				</div>
			</div>
			
		<div class="portlet-body form">
			<form class="form-horizontal form-bordered" method="post">
				<div class="form-group col-sm-12 form-subtitle">
	                <label><i class="fa fa-reorder"></i>계약서 선택</label>
	            </div>
				<!-- 전자계약서 권한 설정된 계정에 등록된 form_id 리스트 -->
				<div class="form-group col-sm-6">
					<label class="control-label input-sm col-sm-4 req-label">계약서 선택</label>	
						<c:if test="${fn:length(DATAMAPFORM) > 0 }">
							<select class="selectpicker col-sm-6" id='authFormList' onchange="listChange(this)">
								<option value="" selected>-- 계약서 선택 --</option>
									<c:forEach var="entryMap" items="${DATAMAPFORM}">
										<option value="${entryMap['form_id']}">${entryMap['doc_name']}</option>
									</c:forEach>
							</select>
						</c:if>
					    <c:if test="${empty DATAMAPFORM}">
						 	<div class="col-sm-6">
								<span>계약서 설정 필요</span><br>
							</div>
					    </c:if>
				</div>
			</form>
				
			<form class="form-horizontal form-bordered" method="post" id="writeFrm" name="contractForm">
				<div class="form-body row">
					<input type="hidden" id="form_id" name="form_id" value="">
					<input type="hidden" id="expiration_date" name="expiration_date" value="">
					<input type="hidden" id="send_type" name="send_type" value="SAMETIME">
										
				<div class="form-group col-sm-6">
                    <label class="control-label input-sm col-sm-4 req-label">계약서 이름</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control input-sm" id="title" name="title" maxlength="50">
				 		계약서 이름을 변경하여 전송할 수 있습니다.
                    </div>
	            </div>
				<!-- 전송방식 동시전송 고정 -->
				<!-- <div class="form-group col-sm-6">
					<label class="control-label input-sm col-sm-4 req-label">전송방식</label>
					<select class="selectpicker col-sm-6" id="send_type" name="send_type">
						<option selected>-- 전송방식 선택 --</option>
						<option id="SAMETIME" value="SAMETIME">동시전송</option>
						<option id="SEQUENTIAL" value="SEQUENTIAL">순차전송</option>
					</select>
				</div> -->
				<!-- <div class="form-group col-sm-6">
					<label class="control-label input-sm col-sm-4 req-label">전송방식</label>
					<div class="col-sm-6">
						<span>동시전송 : 동일 계약서 동시에 전송<br>순차전송 : 다자간 계약시 순서대로 전송</span>
					</div>
				</div> -->
				<div class="form-group col-sm-12 form-subtitle">
	                <label><i class="fa fa-reorder"></i>수신자 입력</label>
	            </div>
	            <div class="form-group col-sm-6">
	            	<label class="control-label input-sm col-sm-4 req-label">수신자 입력</label>
					  <div class="col-sm-4">
					  	<button class="btn green btn-sm listClick" type="button" id="listClick">수신자 추가</button>
					  	<button class="btn green btn-sm listDelete" type="button" id="listDelete">수신자 삭제</button>
					  </div>
				</div>
				<div class="form-group col-sm-6">
				  	<div class="col-sm-12" id="receiver_list"></div>
				</div>
				<div class="form-group col-sm-12 form-subtitle">
	                <label><i class="fa fa-reorder"></i>메일 작성</label>
	            </div>
	            <div class="form-group col-sm-8">
					<label class="control-label input-sm col-sm-3 req-label">제목</label>
					<div class="col-sm-8">
						<span>계약서 작성 요청 이메일의 제목과 문구를 입력해 주세요.</span>
						<input type="text" id="mail_title" name="mail_title" class="form-control" placeholder="예) [e.Form] 근로계약서 작성을 요청 드립니다.">
					</div>
				</div>
				<div class="form-group col-sm-8">
					<label class="control-label input-sm col-sm-3 req-label">내용</label>
					<div class="col-sm-8">
						<textarea class="form-control" id="mail_content" name="mail_content" cols="20" rows="10" 
							placeholder="예) 근로계약서를 전자계약으로 전송 드립니다. 내용을 읽어 보신 뒤 작성 및 서명해 주세요. 더불어,사업자 등록증 첨부 부탁드립니다."></textarea>
					</div>
				</div>
	            <div class="form-group col-sm-12 form-subtitle">
	                <label><i class="fa fa-reorder"></i>옵션 설정</label>
	            </div>
	            <!-- <div class="form-group col-sm-6">
					<label class="control-label input-sm col-sm-4 req-label">비밀번호</label>
					<div class="col-sm-6">
						<input type="text" id="password" name="password" class="form-control input-sm" maxlength="10" onkeypress='checkNumber();'>
						<span>수신자가 계약서 작성을 하기 위한 비밀번호 설정</span>
					</div>
				</div> -->
				<div class="form-group col-sm-12">
					<label class="control-label input-sm col-sm-2 req-label">휴대폰 본인인증 사용여부</label>
					<select class="selectpicker col-sm-5" id="auth_phone" name="auth_phone">
						<option selected>-- 휴대폰 본인인증 사용여부 선택 --</option>
						<option value="Y">사용</option>
						<option value="N">사용안함</option>
					</select>
					<span>계약서를 작성하는 사용자 이름과 휴대폰 번호가 일치해야 합니다.</span>
				</div>
				<div class="form-group col-sm-12 form-subtitle">
	                <label><i class="fa fa-reorder"></i>발신자 선입력 항목</label>
	            </div>
	            <div class="form-group col-sm-6">
					<label class="control-label input-sm col-sm-4 req-label">발신자 선입력 항목</label>
					<div class="col-sm-4">
						<span>발송 전 고정 입력 값 설정.<br>수신자는 확인만 가능합니다.</span>
					</div>
				</div>
				<div class="form-group col-sm-6">
				  	<div class="col-sm-5" id="items"></div>
				</div>
			</div>
				<div class="alert alert-danger display-hide"></div>
				<div class="form-actions right">
					<div class="">
						<button type="button" id="contractSend" class="btn green btn-sm loading-btn" data-loading-text="Loading...">
							<i class="fa fa-search"></i>&nbsp;Submit
						</button>
					</div>
				</div>
				</form>
			</div>
		</div>
	</div>
				<!-- END PAGE CONTENT INNER -->
				</div>
			</div>
		</div>
	</div>
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />

	<script type="text/javascript">
	setTimeout(function(){
		alert("토큰이 만료되었습니다. 새로고침 해주세요.");
	}, 600000);
	
	var date = new Date();
	var year = date.getFullYear();
    var month = date.getMonth()+2;
    var day = date.getDate();
    
    if(month < 10){
        month = "0"+month;
    }
    if(day < 10){
        day = "0"+day;
    }
    var today = year + '' + month + '' + day;
    $("#expiration_date").val(today);
    
	var count = 0;
	var listCount = 1;
	$("#listClick").click(function(){
		if (count >= 20){
			alert("최대 20명까지만 추가하실 수 있습니다.");
			return false;
		}
		var tmp ="<div class='input-group' id='rec_list" + count + "' name='rec_list'>";
			tmp+="<span class='input-group-addon btn white' id='checkCnt" + listCount + "'>" + listCount + "</span>";
			tmp+="<span class='input-group-addon btn blue-dark'>이름</span><input type='text' id='name' name='receiver_list[" + count + "][name]' class='form-control' >";
			tmp+="<span class='input-group-addon btn blue-dark'>이메일</span><input type='text' id='email' name='receiver_list[" + count + "][email]' class='form-control' placeholder='예) abc@gmail.com'>";
			tmp+="<span class='input-group-addon btn blue-dark'>전화번호</span><input type='text' id='mobile' name='receiver_list[" + count + "][mobile]' class='form-control' placeholder='예) 01012345678'>";
			tmp+="</div>&nbsp;";
			
		count++;
		listCount++;
		$("#receiver_list").append(tmp);
	});
	
	$("#listDelete").click(function(){
		var obj = document.getElementById('receiver_list');
		var delObj = document.getElementById("rec_list"+(--count));
		var delCnt = document.getElementById("checkCnt"+(--listCount));
		
		if (count <= 0){
			count = 0;
			listCount = 1;
		}
		obj.removeChild(delObj);
	});
	
	var token = '${token}';
	
	function checkNumber() {
		if(event.key === '.' || event.key === '-' || event.key >= 0 && event.key <= 9){
          return true;
        }
		alert("숫자를 입력해주세요.");
		return false;
	}
	
	function listChange(obj){
		$("#writeFrm")[0].reset();
		$("#receiver_list").value=null;
		$("#items").empty();
		
		var formName = $("#authFormList option:selected").text();
		var formId = $("#authFormList option:selected").val();
		
		$.ajax({
			type:"POST",
			url:"/eform/eform_detail",
			dataType:'text',
			data:({
				token:token,
				formId:formId,
				formName:formName
			}),
			success:function(data){
				console.log("detail method call success");
				contractForm(data);
			},
			error:function(){
				console.log("detail method call failed");
			},
		});
	}
	
	function contractForm(data){
		var dataParse = JSON.parse(data);
		var token = dataParse.token; 
		
		var item = dataParse.resJson.result;
		var arr = item[0].items;
		
		var arrIdx = 0;
		var item_name;
		
		for (var i=0; i<item.length; i++){
			var getItems = item[i].items;
			
			for (var j=0; j<getItems.length; j++){
				var item_id = item[i].items[j].id;
				item_name = item[i].items[j].name;
				
				if (item_name.indexOf("[선입력]") != -1){
					
					var form = document.getElementById("items");  
					form.innerHTML +="<div class='input-group mb-3' id='items' name='item_list'>";
					form.innerHTML +="<input type='hidden' id='id' name='items["+ arrIdx +"][id]' value=" + item_id + ">";
					form.innerHTML +="<span class='input-group-addon btn blue-dark'>" + item_name.substring(5,20) + "</span>";
					form.innerHTML +="<input type='text' id='value' name='items["+ arrIdx +"][value]' class='form-control'>";
					form.innerHTML +="</div>";
					arrIdx++;
				}
			}
		}
		
		var formName = $("#authFormList option:selected").text();
		var formId = $("#authFormList option:selected").val();
		$("#title").val(formName);
		$("#form_id").val(formId);
	}
	
	$("#contractSend").on("click", function(){
        send(token);
    });
	
	function send(token){
		//Validation
		var checkType = $("select[name=send_type]").val();
		var checkedType = $("#send_type option:selected").val();
		var checkPhone = $("select[name=auth_phone]").val()
		var checkNum = $("#authFormList option:selected").val();
		var checkRecv = $('div[name=rec_list]').length;
		var checkPw = $("#password").val();
		var checkLength = $('div[name=item_list]').length;
		var findItem = document.getElementById("items");
		var checkItem = findItem.getElementsByTagName("INPUT");
		var checkList = $('div[name=rec_list]').find('input');
		var value = "";
		for (var i = 0; i < checkList.length; i++){
			var checkInfo = checkList[i];
			value = checkInfo.value;
		}
			if (checkNum == null || checkNum == "") {
				alert("계약서를 선택해주세요.");
				document.getElementById("authFormList").options[0].selected = true;
				document.getElementById("authFormList").focus();
				return false;
			}/* else if (checkType.indexOf("선택") != -1) {
				alert("전송 방식 선택해주세요.");
				document.getElementById("send_type").options[0].selected = true;
				document.getElementById("send_type").focus();
				return false;
			}else if (checkNum.indexOf("동시") > -1 && checkedType.indexOf("SEQUENTIAL") > -1) {
				alert("계약서와 전송방식을 확인해주세요.");
				return false;
			} else if (checkNum.indexOf("순차") > -1 && checkedType.indexOf("SAMETIME") > -1) {
				alert("계약서와 전송방식을 확인해주세요.");
				return false;
			} else if (checkNum.indexOf("1") != -1 && count != 2) {
				alert("수신자 1명으로 지정해주세요.");
				return false;
			} else if (checkNum.indexOf("2") != -1 && count != 3) {
				alert("수신자 2명으로 지정해주세요.");
				return false;
			} else if (checkNum.indexOf("3") != -1 && count != 4) {
				alert("수신자 3명으로 지정해주세요.");
				return false;
			} else if (checkNum.indexOf("4") != -1 && count != 5) {
				alert("수신자 4명으로 지정해주세요.");
				return false;
			} else if (checkNum.indexOf("5") != -1 && count != 6) {
				alert("수신자 5명으로 지정해주세요.");
				return false;
			}*/  
			else if (checkRecv == 0) {
				alert("수신자 추가 해주세요.");
				return false;	
			}else if(value == ""){
				alert("수신자 정보 입력해주세요.");
				return false;
			}
			for (var j = 0; j < checkList.length; j++){
				var checkInfo = checkList[j];
				var id = checkInfo.id;
				var value = checkInfo.value;
				
				if(id == "name"){
					for (var i = 0; i<=value.length; i++){
						var check = value.substring(i, i+1);
						if(check.match(/[0-9]|[a-z]|[A-Z]/)) { 
					    	alert("이름을 정확히 입력해주세요");
					        return;
					    }else if(check.match(/([^가-힣\x20])/i)){
					    	alert("이름을 정확히 입력해주세요");
					        return;
					    }else if(value == ""){
					    	alert("이름을 정확히 입력해주세요");
					        return;
					    }
					}
				}else if(id == "email"){
					 var reg_email = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
				     if(!reg_email.test(value)) {
				    	alert("이메일 형식 확인해주세요. 예)abc@gmail.com");
				    	return false;
				     }
				}else if(id == "mobile"){
					var reg_phone = /(01[016789])([1-9]{1}[0-9]{2,3})([0-9]{4})$/;
					if(!reg_phone.test(value)) {
				    	alert("전화번호 형식 확인해주세요. 예)01012345678");
						return false;
				    }
				}
			}
			if ($("#mail_title").val() == "") {
				alert("요청 메일 제목 입력해주세요.");
				document.contractForm.mail_title.focus();
				return false;
			} else if ($("#mail_content").val() == "") {
				alert("요청 메일 내용 입력해주세요.");
				document.contractForm.mail_content.focus();
				return false;
			}/* else if (checkPw == "") {
				alert("비밀번호 입력해주세요.");
				document.contractForm.password.focus();
				return false;
			}  else if (isNaN(checkPw)) {
				alert("비밀번호 최대 10자리 숫자를 입력해주세요.");
				document.contractForm.password.focus();
				return false;
			} */ else if (checkPhone.indexOf("선택") != -1) {
				alert("휴대폰 본인인증 사용여부 지정해주세요.");
				document.getElementById("auth_phone").options[0].selected = true;
				document.getElementById("auth_phone").focus();
				return false;
			} else if(checkLength != 0){
				for (var k = 0; k < checkItem.length; k++){
					var checkValue = checkItem[k];
					var value = checkValue.value;
					
					if (value == "") {
						alert("발신자 선입력 항목을 입력해주세요.");
						return false;
					}
				}
			}
			
			var is_empty = false;
			$("#writeFrm").find('input[type!="hidden"]').each(function(){
				if(!$(this).val()){
					is_empty=true;
				}
			});
			if(is_empty){
				alert("입력되지 않은 값이 있습니다. 확인해주세요.");
				return false;
			}
			//Validation

			var params = $("#writeFrm").serializeObject();
			var setParams = JSON.stringify(params);
			
			if(confirm("입력한 값을 제대로 확인해주세요. 확인 후 전송하시겠습니까?") == true){
				$.ajax({
					type : "POST",
					url : "/eform/eform_send",
					dataType : 'json',
					data : ({
						token : token,
						setParams : setParams
					}),
					success : function(data) {
						console.log("send method call success");
						alert("계약서가 전송되었습니다.");
						$('#authFormList').val('default').selectpicker("refresh");
						$('#auth_phone').val('default').selectpicker("refresh");
						$('#send_type').val('default').selectpicker("refresh");
						$("#writeFrm")[0].reset();
					},
					error : function() {
						console.log("send method call failed");
						alert("입력 값을 확인해주세요.");
					},
				});
			}else{
				return false;
			}
		}
	
	$('#nav-eform').addClass('active');
	</script>
</body>
</html>