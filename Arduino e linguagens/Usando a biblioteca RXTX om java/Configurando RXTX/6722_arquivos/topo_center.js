$(document).ready(function() {
if($('#pesquisar').val().length>0) $('#lblsearch').hide();
	if($.browser.msie){
		if($.browser.version <= 8){
			$('.top_busca_div1').wrap('<div class="top_busca_outer"></div>');
			$('.top_busca_div1').css({ position:"relative", top:"0px", right:"0px" });
			$(".top_busca_div1").corner("round 7px").parent().css({ padding:'1px' }).corner("cc:#F3EFD9 7px");
			//$(".outer").corner("round 7px").parent().css('padding', '1px').corner("round 10px")
			$('.top_busca_div1 .busca').width("214px");
			$('.top_busca_div1 .busca').height("17px");
			$('.top_busca_div1 .busca').css({ marginTop:"2px" });
			
			$('.top_menu_div2').css({ paddingTop:"5px" });
			$('.top_menu_link3').css({ marginTop:"6px" });
		}
		
		$('.top_busca_div1 #topbuscaimg').css({ marginTop:"-1px" });
		
		/*if(parseInt($.browser.version) == 9){
			$('.rodape_div1_floatLeft').css({ marginLeft:"100px" });
			$('#rodape_div1_floatLeft_first').css({ marginLeft:"0px" });
		}*/
	}/*else if($.browser.mozilla){
		$('.h3ClassRodape').css({ fontSize:"11px" });
		//$('#rodape_div1_floatLeft_first').css({ marginLeft:"0px" });
	}*/
});

function fora(Campo, Mensagem) {
  if (!Campo.value) Campo.value = Mensagem;
  return false;
}

function dentro(Campo, Mensagem) {
  if (Campo.value == Mensagem) Campo.value = '';
  return false;
}

function menuDropDown(mouseover,objeto,objeto2){
	if(mouseover){
		$(objeto).children(".top_menu_link3").css({ backgroundPosition:"3px 0px" });
		$(objeto).children(objeto2).show();
	}else{
		$(objeto).children(".top_menu_link3").css({ backgroundPosition:"3px -3px" });
		$(objeto).children(objeto2).hide();
	}
}