<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>영수증 조회 샘플</title>
	<style>
		input[type=text] {
		  width: 100%;
		  padding: 12px 20px;
		  margin: 8px 0;
		  display: inline-block;
		  border: 1px solid #ccc;
		  border-radius: 4px;
		  box-sizing: border-box;
		}
		
		input[type=button] {
		  width: 100%;
		  background-color: #4CAF50;
		  color: white;
		  padding: 14px 20px;
		  margin: 8px 0;
		  border: none;
		  border-radius: 4px;
		  cursor: pointer;
		}
		
		input[type=button]:hover {
		  background-color: #45a049;
		}
		
		div {
		  border-radius: 5px;
		  background-color: #f2f2f2;
		  padding: 20px;
		}
		
		.required {
			display: inline-block;
			color: red;
			padding:0;
			padding-left: 10px;
		}
	</style>
	<script>
		function popup(){
			if(check() == true){
				var openWin = window.open('about:blank','payviewer','width=460,height=750');
				var frm =document.receiptForm;
				frm.target ="payviewer";
				frm.submit();
			} else {
				alert('값을 입력하세요.');
				receiptForm.trxId.focus();
			}
				
		}
		
		function check(){
			if(receiptForm.trxId.value == "" && receiptForm.mchtId.value == "" && receiptForm.tmnId.value == ""){
				return false;
			} else if(receiptForm.trxId.value == "" && receiptForm.mchtId.value != "" && receiptForm.trackId.value == ""){
				return false;
			} else if(receiptForm.trxId.value == "" && receiptForm.mchtId.value != "" && receiptForm.authCd.value == ""){
				return false;
			} else if(receiptForm.trxId.value == "" && receiptForm.mchtId.value != "" && receiptForm.amount.value == ""){
				return false;
			} else if(receiptForm.trxId.value == "" && receiptForm.mchtId.value != "" && receiptForm.regDay.value == ""){
				return false;
			} else if(receiptForm.trxId.value == "" && receiptForm.tmnId.value != "" && receiptForm.trackId.value == ""){
				return false;
			} else if(receiptForm.trxId.value == "" && receiptForm.tmnId.value != "" && receiptForm.authCd.value == ""){
				return false;
			} else if(receiptForm.trxId.value == "" && receiptForm.tmnId.value != "" && receiptForm.amount.value == ""){
				return false;
			} else if(receiptForm.trxId.value == "" && receiptForm.tmnId.value != "" && receiptForm.regDay.value == ""){
				return false;
			} else {
				return true;
			}
		}
		
		window.onload = function(){
			document.getElementById("trxId").onkeydown = function(event){
				document.getElementById("trxR").innerHTML = 'required';
				document.getElementById("trxR").style = 'color:red'
				
				document.getElementById("mchtId").value = '';
				document.getElementById("mchtR").innerHTML = '';
				
				document.getElementById("tmnId").value = '';
				document.getElementById("tmnR").innerHTML = '';
				
				document.getElementById("trackId").value = '';
				document.getElementById("trackR").innerHTML = '';
				
				document.getElementById("authCd").value = '';
				document.getElementById("authR").innerHTML = '';
				
				document.getElementById("amount").value = '';
				document.getElementById("amountR").innerHTML = '';
				
				document.getElementById("regDay").value = '';
				document.getElementById("regR").innerHTML = '';
			}
			
			document.getElementById("mchtId").onkeydown = function(event){
				document.getElementById("trxId").value = '';
				
				document.getElementById("trxR").innerHTML = '';
				
				document.getElementById("mchtR").innerHTML = 'required';
				document.getElementById("mchtR").style = 'color:red'
				
				document.getElementById("tmnR").innerHTML = '';
				
				document.getElementById("trackR").innerHTML = 'required';
				document.getElementById("trackR").style = 'color:red'
				
				document.getElementById("authR").innerHTML = 'required';
				document.getElementById("authR").style = 'color:red'
				
				document.getElementById("amountR").innerHTML = 'required';
				document.getElementById("amountR").style = 'color:red'
				
				document.getElementById("regR").innerHTML = 'required';
				document.getElementById("regR").style = 'color:red'
			}
			
			document.getElementById("tmnId").onkeydown = function(event){
				document.getElementById("trxId").value = '';
				
				document.getElementById("trxR").innerHTML = '';
				
				document.getElementById("mchtR").innerHTML = '';
				
				document.getElementById("tmnR").innerHTML = 'required';
				document.getElementById("tmnR").style = 'color:red'
				
				document.getElementById("trackR").innerHTML = 'required';
				document.getElementById("trackR").style = 'color:red'
				
				document.getElementById("authR").innerHTML = 'required';
				document.getElementById("authR").style = 'color:red'
				
				document.getElementById("amountR").innerHTML = 'required';
				document.getElementById("amountR").style = 'color:red'
				
				document.getElementById("regR").innerHTML = 'required';
				document.getElementById("regR").style = 'color:red'
			}
			
		}
		
		
   </script>

</head>

<body>

	<h3>영수증 조회 양식 샘플</h3>
	
	<div>
	  <form action="/receipt" method="get" name ="receiptForm" id="receiptForm" >
	    <label>거래번호 (trxId)</label><div class="required" id="trxR">required</div>
	    <input type="text" name="trxId" id="trxId">
	
	    <label>가맹점 아이디 (mchtId)</label><div class="required" id="mchtR">required</div>
	    <input type="text" name="mchtId" id="mchtId">
	
	    <label>터미널 아이디 (tmnId)</label><div class="required" id="tmnR">required</div>
	    <input type="text" name="tmnId" id="tmnId">
	    
	    <label>주문번호 (trackId)</label><div class="required" id="trackR"></div>
	    <input type="text" name="trackId" id="trackId">
	    
	    <label>승인번호 (authCd)</label><div class="required" id="authR"></div>
	    <input type="text" name="authCd" id="authCd">
	    
	    <label>금액 (amount)</label><div class="required" id="amountR"></div>
	    <input type="text" name="amount" id="amount">
	    
	    <label>거래일자 (regDay)</label><div class="required" id="regR"></div>
	    <input type="text" name="regDay" id="regDay">
	  
	    <input type="button" value="submit" id="btn" onClick="popup();">
	  </form>
	</div>
	
</body>

</html>
