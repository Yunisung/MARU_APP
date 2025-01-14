$(document).ready(function(){

  try {
	  
  if (window.matchMedia("(min-device-width: 737px)").matches) {/*desk top*/
    /*popup 以묒븰�꾩튂�섍린*/
    jQuery.fn.center = function(){
    var p_width = this.width();
    var p_height = this.height();
    this.css("top", ($(window).height() / 2 ) - ( p_height / 2));
    this.css("left", ($(window).width() / 2 ) - ( p_width / 2));
    return this;
    };
    $(".popup").center();

  } else {/*Mobile*/
    
    /*�덉씠�� popup �믪씠*/
    var popup_scroll_height = function() {
      return window.innerHeight - 90;
    };
  
    //�ㅽ겕濡ㅼ쁺�� �믪씠
    $('.popup_scroll').css('height', popup_scroll_height());
    
    function onResizeCall(){
        window.innerHeight.refresh;
        $('.popup_scroll').css('height', popup_scroll_height());
        popup_scroll_height.refresh;   
    };
    
    $(window).resize(onResizeCall());

    $(".btn_close").click(function(){
      $(".float_wrap").css("display","none");
      $(".dim").css("display","none");
      $('body').css("overflow","auto");
      scroll_enable();
    });

    //紐⑤컮�� input ���낅�寃�
    function inputSecurity(){
        $('.security').attr('type','number');  
    };
    inputSecurity();

    };/*matchMedia END*****************************************************************************/
    
  }catch(exception){
	  
  }
   
  $(".btn_close").click(function(){
      $(".float_wrap").css("display","none");
      $(".dim").css("display","none");
      $('body').css("overflow","auto");
      scroll_enable();
   });

  //popup �ㅽ겕濡ㅻ컮
  /*$(".popup_scroll").mCustomScrollbar({
    theme     : "dark",
    scrollButtons : { scrollType: "minimal" },
    live      : "on",
  });*/

 /* $(".con_scroll").mCustomScrollbar({
    theme     : "dark",
    scrollButtons : { scrollType: "minimal" },
    live      : "on",
    scrollInertia : 300,
  });*/

  //a留곹겕 �ㅽ겕濡ㅼ씠�� 留됯린
  $('a[href="#"]').click(function(event) {
    event.preventDefault();      
  });

  //input effect
  $('.input_section').hover(
    function() {
      $(this).css("border-color","#6e91d2");
    },
    function() {
      if($(this).find('input').is(":focus")){
        $(this).css("border-color","#6e91d2");
      }else if($(this).find('select').is(":focus")){
        $(this).css("border-color","#6e91d2");
      }else{
        $(this).css("border-color","#ddd");
      };
    }
  );

  /*popup 以묒븰�꾩튂�섍린*/  
  jQuery.fn.center = function(){
    var p_width = this.width();
    var p_height = this.height();
    this.css("top", ($(window).height() / 2 ) - ( p_height / 2));
    this.css("left", ($(window).width() / 2 ) - ( p_width / 2));
    return this;
  };
  $(".card_pay").center();
  $(window).resize(function() {
    $(".card_pay").center();
  });
  $(".popup_notice .text").center();
  $(window).resize(function() {
    $(".popup_notice .text").center();
  });
   $(".card_etc").center();
  $(window).resize(function() {
    $(".card_etc").center();
  });
  $(".popup").center();
  $(window).resize(function() {
	  $(".popup").center();
  });


  /*input �ъ빱��*/
  $('input').on('focus',function() {
    $(this).parents(".input_section").css("border-color","#6e91d2");
  });
  $('input').on('blur',function() {
    $(this).parents(".input_section").css("border-color","#ddd");
  });
  $('select').on('focus',function() {
    $(this).parents(".input_section").css("border-color","#6e91d2");
  });
  $('select').on('blur',function() {
    $(this).parents(".input_section").css("border-color","#ddd");
  });

//�쎄��숈쓽 check 201811
  $(".terms .all_check").click(function(){
    if($(this).find('input[type=checkbox]').prop("checked")) {
      $(this).siblings('ul').find('input[type=checkbox]').prop("checked",true);
      $(this).parents('.terms').find('ul').addClass('off');
    } else {
      $(".terms input[type=checkbox]").prop("checked",false);
      $(this).parents('.terms').find('ul').removeClass('off');
    };  
  });
  $(".terms ul .checks").click(function(){
    var group = $(this).parents('ul'); 
    var check_li = group.find('input[type=checkbox]');
    var check_total = check_li.length;
    var checked_total = group.find('input:checked').length;
    if(check_total==checked_total) {
      $(this).parents('ul').siblings(".terms .all_check").find('input[type=checkbox]').prop("checked",true);
      $(this).parents('.terms').find('ul').addClass('off');
    } else {
      $(this).parents('ul').siblings(".terms .all_check").find('input[type=checkbox]').prop("checked",false);
      $(this).parents('.terms').find('ul').removeClass('off');
    };  
  }); 
  $(".terms ul .terms_li_show").click(function(){
    $(this).parents('.terms').find('ul').removeClass('off');
  });

/*湲고�移대뱶�� popup �닿린*/
  $(".card_more").click(function(){
	  cardFrameOn();
  });

  /*移대뱶�� popup �닿린*/
  $(".install_notice_btn").click(function(){
	  goNext();
  });
  
  $(".bank_more").click(function(){
	  bankFrameOn();
  });
  
  /*popup btn 201811*/
  $(".popup_btn").click(function(){
    var popup_class = $(this).attr('data');
    $(popup_class).css('display','block');
    $(popup_class).find('.pop_dim').css('opacity','0');
    $(popup_class).find('.pop_dim').animate({opacity: '0.7'}, 500);
    $(".popup").center();
    scroll_disable();/*201811*/
  });

  //input �ъ빱�ㅼ씠��
  function customInput(el){
    $('.'+el+' input').on('change keydown keyup',function(e){
    
    var max_length = $('input:focus').attr('maxLength'),
        $focused = $('.'+el+' :focus'),
        key = e.keyCode || e.charCode,
        count = 0;
    
    if(key == 8 && $focused.prev().val() != '' && $focused.val() == '' || key == 46 && $focused.prev().val() != '' && $focused.val() == '' ){
        $focused.prev().focus();
       }

    if($focused){
     
      setTimeout(function(){
        if($focused.val().length == max_length && $focused.next().val().length != max_length){
          $focused.next().focus();
          $('#'+el).attr('id','');
          $focused.next().attr('id',el);
          max_length.refresh;
        }
      },300);
    };
  });
  };

  customInput('phone_num');
  customInput('biz_num');
  customInput('phone_num_r');
  customInput('personal_num');
  customInput('card_num');


  //select input �좏깮

  function showInput(el){
    $('.'+el+' select').change(function(e){
    
    var showClass = $(this).val(),
        allClass = showClass.slice(0,2);
            
        $('.'+allClass).hide();
        $('.'+showClass).css('display','inline-table');

        if(showClass == 's3_2'){
          $('.s3_2').find('select').find("option:eq(0)").prop("selected", true);
        };

    });
  };

//  showInput('select3');
//  showInput('select4');

  // popup �レ쓣�� 遺�紐⑥갹 留덉뒪�� �④린湲�
  function dimInvisable(){
	try{
		$('.popup_notice', opener.document).css('display','none');
		$('body', opener.document).css('overflow','auto');
	}catch(e) {}
  };

  $('.dim_btn').click(function(){
    dimInvisable();
  });


var bank_item2 = $('.card_list2 a')
bank_item2.click(
  function(e) {
    bank_item2.removeClass('on');
    $(this).addClass('on');
  }
);

var bank_item4 = $('.bank_list2 a')
bank_item4.click(
  function(e) {
    bank_item4.removeClass('on');
    $(this).addClass('on');
  }
);

//tab Ui 201811  
var tab_on = function(){
  var on_index = $(".on").parent("li").index();
  $(".tab_con").css("display","none");
  $(".tab_con_wrap .tab_con:eq(" + on_index + ")").css("display","block");
};
tab_on();
$(".tab_wrap ul li a").click(function(index){
  $(".tab_wrap ul a").removeClass("on");
  $(this).addClass("on");
  tab_on();
});
//faq 201811 
$('.ac_toggles .ac_toggle h3').click(function(){   
    if($(this).parents('.ac_toggle').hasClass('on')){
      $(this).parents('.ac_toggle').find('> div').slideUp(300);
      $(this).parents('.ac_toggle').removeClass('on');
    }
    else{
      $(this).parents('.ac_toggles').find('.ac_toggle > div').slideUp(300);
      $(this).parents('.ac_toggles').find('.ac_toggle').removeClass('on');
      $(this).parents('.ac_toggle').find('> div').slideDown(300);
      $(this).parents('.ac_toggle').addClass('on');
    }

    if( $(this).parents('.ac_toggle').hasClass('on') ){
      $(this).parents('.ac_toggles').find('i').attr('class','icon-plus-sign');
      $(this).find('i').attr('class','icon-minus-sign');
    } else {
      $(this).find('i').attr('class','icon-plus-sign');
    }
});
///*怨듭��ы빆 �щ씪�대뱶*/
//$('.system_notice').bxSlider({
//  slideWidth: 'auto',
//  ticker: true,
//  speed: 60000,
//  wrapperClass: 'system_notice_wrap'
//});


});
// end document.ready****************************************************************************************

/*201811 �ㅽ겕濡�& �ㅽ겕濡� �대룞 留됯린*/
var posY;
var posY2;
var scroll_disable = function(){
  posY = $(window).scrollTop();
  $("html, body").addClass("not_scroll");
  $(".innopay").css("top",-posY);
  posY2 = posY;
};
var scroll_enable = function(){
  $("html, body").removeClass("not_scroll");
  $(window).scrollTop(posY2);
};
//移대뱶�� �좏깮
$(function() {
	$('.payment_input .card_list a').click(function() {
		if ( ($(this).children('p').attr('class') == "card_more") || ($(this).attr('onclick') == "javascript:noService()") ) {
		} else {
			$('.payment_input .card_list a').removeClass('on');
		    $(this).addClass('on');
		}
	});
});

//���� �좏깮 201811
$(function() {
	$('.payment_input .bank_list a').click(function() {
		if ( ($(this).children('p').attr('class') == "bank_more") || ($(this).attr('onclick') == "javascript:noService()") ) {
		} else {
			$('.payment_input .bank_list a').removeClass('on');
		    $(this).addClass('on');
		}
	});
});


function error_notice_on(){
  window.open('auto_payment_error.html','_blank', 'toolbar=no,location=no,directories=no, status=no,menubar=no,scrollbars=no, resizable=no,width=600,height=600,top=0,left=0');
};

$(window).on("beforeunload",function(){
    if(opener&&opener!=this){
    	try{
	        var obj = opener.document.body;
	        var _layer = opener.document.getElementById("infiniPopLayer");
	        if(_layer!=null && _layer!=undefined) obj.removeChild(_layer);
    	}catch(exception){
    		
    	}
    }
});

//�대찓�� 二쇱냼 �좏슚�� 寃���
function chkEmail(mail){
	var check = 1;
	var check1 = 1;
	var cont = 0;
	var c;
	if( mail == "" ) return true;
		
	c = mail.charAt(0);
	if( !( (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c == '-') ) ){
		return false;
	}
	
	c = mail.charAt(mail.length-1 );
	if( !( (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) ){
		return false;
	}
						
	for(var i=1; i<mail.length; i++){
		c = mail.charAt(i);			
		if( !((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) ){
			if(c == '.' || c == '-' || c == '_' ){
				if(cont==1){
					return false;
				}else{
					check++ ;
				}
			}else if( c == '@'){
				if( check1 == 5 ){
					return false;
				}else{
					check1 =5;
					cont=1;
				}
			}else{
				return false;
			}
		}else{
			cont = 0;
		}
	}

	if( check == 1 ||  check1 == 1){
		return false;
	}
	return true;
}

function cellNumCheck(number){
 	var val = number.substring(0 , 3);
 	if(val == "010" || val == "011" || val == "016" || val == "017" || val == "018" || val == "019" ){ 
 		return true;
 	}else return false;
 }

function isJuminNo(juminNo1, juminNo2) {
	var f1 = juminNo1.substring(0, 1);
	var f2 = juminNo1.substring(1, 2);
	var f3 = juminNo1.substring(2, 3);
	var f4 = juminNo1.substring(3, 4);
	var f5 = juminNo1.substring(4, 5);
	var f6 = juminNo1.substring(5, 6);	
	
	var l1 = juminNo2.substring(0, 1);
	var l2 = juminNo2.substring(1, 2);
	var l3 = juminNo2.substring(2, 3);
	var l4 = juminNo2.substring(3, 4);
	var l5 = juminNo2.substring(4, 5);
	var l6 = juminNo2.substring(5, 6);
	var l7 = juminNo2.substring(6, 7);
	
	var sum = f1 * 2 + f2 * 3 + f3 * 4 + f4 * 5 + f5 * 6 + f6 * 7;
	sum = sum + l1 * 8 + l2 * 9 + l3 * 2 + l4 * 3 + l5 * 4 + l6 * 5;
	sum = sum % 11;
	sum = 11 - sum;
	sum = sum % 10;
	
	if (sum != l7) {return false;}
	
	return true;
}

function isBusiNoByValue(strNo) { 
	var sum = 0;
	var getlist =new Array(10);
	var chkvalue =new Array('1','3','7','1','3','7','1','3','5'); 

	for(var i = 0; i < 10; i++){
		getlist[i] = strNo.substring(i, i+1); 
	}
	for(var i = 0; i < 9; i++) {
		sum += getlist[i]*chkvalue[i];
	}

	sum = sum + parseInt((getlist[8]*5)/10); 
	sidliy = sum%10;
	sidchk = 0;

	if(sidliy != 0){
		sidchk = 10 - sidliy; 
	}else{
		sidchk = 0; 
	}
	if(sidchk != getlist[9]) return false;
	
	return true;
}

function cardFrameOn() {
    $(".card_frame").css('display','block');
    $(".card_frame").find('.pop_dim').css('opacity','0');
    $(".card_frame").find('.pop_dim').animate({opacity: '0.7'}, 500);
    $(".card_etc").center();
    scroll_disable();
}

function popupTerms(value) {
	if (value == "1") {
		var popup_class = $("#popup_terms1").attr('data');
	} else if (value == "2") {
		var popup_class = $("#popup_terms2").attr('data');
	} else if (value == "3") {
		var popup_class = $("#popup_terms3").attr('data');
	} else if (value == "4") {
		var popup_class = $("#popup_terms4").attr('data');
	}
	
	$(popup_class).css('display','block');
	$(popup_class).find('.pop_dim').css('opacity','0');
	$(popup_class).find('.pop_dim').animate({opacity: '0.7'}, 500);
	scroll_disable();
	if (value == "1") {
		$("#conditions_text1").center();
	} else if (value == "2") {
		$("#conditions_text2").center();
	} else if (value == "3") {
		$("#conditions_text3").center();
	} else if (value == "4") {
		$("#conditions_text4").center();
	}
	
}