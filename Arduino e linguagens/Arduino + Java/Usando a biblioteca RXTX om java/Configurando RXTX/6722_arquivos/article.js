//Implementacao TYNT
if(document.location.protocol=='http:'){
 var Tynt=Tynt||[];Tynt.push('bjGqbWII8r4A2pacwqm_6r');Tynt.i={"ap":"Leia mais em:","st":true};
 (function(){var s=document.createElement('script');s.async="async";s.type="text/javascript";s.src='http://tcr.tynt.com/ti.js';var h=document.getElementsByTagName('script')[0];h.parentNode.insertBefore(s,h);})();
}


function changeTab(nrotab){

	if (nrotab==0){
		document.getElementById('divBox_3').style.display='block';
		document.getElementById('divBox_1').style.display='none';
		document.getElementById('divBox_2').style.display='none';

		document.getElementById('divTab1').style.borderTop='1px solid #000000';
		document.getElementById('divTab1').style.borderLeft='1px solid #000000';
		document.getElementById('divTab1').style.borderRight='1px solid #000000';
		document.getElementById('divTab1').style.borderBottom='1px solid white';
		document.getElementById('divTab1').style.color="black";
		

		document.getElementById('divTab2').style.borderTop='0px';
		document.getElementById('divTab2').style.borderLeft='0px';
		document.getElementById('divTab2').style.borderRight='0px';
		document.getElementById('divTab2').style.borderBottom='0px';
		document.getElementById('divTab2').style.color="#7F7F7F";
		

		document.getElementById('divTab3').style.borderTop='0px';
		document.getElementById('divTab3').style.borderLeft='0px';
		document.getElementById('divTab3').style.borderRight='0px';
		document.getElementById('divTab3').style.borderBottom='0px';
		document.getElementById('divTab3').style.color="#7F7F7F";


	}
	else if (nrotab==1)
	{
	document.getElementById('divBox_3').style.display='none';
	document.getElementById('divBox_2').style.display='none';

	document.getElementById('divBox_1').style.display='block';
	document.getElementById('divBox_1').style.height='385px';
	document.getElementById('divBox_1').style.overflow='auto';

	


	document.getElementById('divTab2').style.borderTop='1px solid #000000';
	document.getElementById('divTab2').style.borderLeft='1px solid #000000';
	document.getElementById('divTab2').style.borderRight='1px solid #000000';
	document.getElementById('divTab2').style.borderBottom='1px solid white';
	document.getElementById('divTab2').style.color="black";

		
		document.getElementById('divTab1').style.borderTop='0px';
		document.getElementById('divTab1').style.borderLeft='0px';
		document.getElementById('divTab1').style.borderRight='0px';
		document.getElementById('divTab1').style.borderBottom='0px';
		document.getElementById('divTab1').style.color="#7F7F7F";



		document.getElementById('divTab3').style.borderTop='0px';
		document.getElementById('divTab3').style.borderLeft='0px';
		document.getElementById('divTab3').style.borderRight='0px';
		document.getElementById('divTab3').style.borderBottom='0px';
		document.getElementById('divTab3').style.color="#7F7F7F";

	}
	else if (nrotab==2){
		document.getElementById('divBox_3').style.display='none';
		document.getElementById('divBox_1').style.display='none';
		document.getElementById('divBox_2').style.display='block';
		

		document.getElementById('divTab3').style.borderTop='1px solid #000000';
		document.getElementById('divTab3').style.borderLeft='1px solid #000000';
		document.getElementById('divTab3').style.borderRight='1px solid #000000';
		document.getElementById('divTab3').style.borderBottom='1px solid white';
		document.getElementById('divTab3').style.color="black";
		document.getElementById('divTab3').style.fontWeight='normal';
		
		document.getElementById('divTab1').style.borderTop='0px';
		document.getElementById('divTab1').style.borderLeft='0px';
		document.getElementById('divTab1').style.borderRight='0px';
		document.getElementById('divTab1').style.borderBottom='0px';
		document.getElementById('divTab1').style.color="#7F7F7F";


		document.getElementById('divTab2').style.borderTop='0px';
		document.getElementById('divTab2').style.borderLeft='0px';
		document.getElementById('divTab2').style.borderRight='0px';
		document.getElementById('divTab2').style.borderBottom='0px';
		document.getElementById('divTab2').style.color="#7F7F7F";


	}
	else {
		document.getElementById('divBox_3').style.display='none';
		document.getElementById('divBox_1').style.display='none';
		document.getElementById('divBox_2').style.display='none';
		
		
		document.getElementById('divTab1').style.borderTop='0px';
		document.getElementById('divTab1').style.borderLeft='0px';
		document.getElementById('divTab1').style.borderRight='0px';
		document.getElementById('divTab1').style.borderBottom='0px';
		document.getElementById('divTab1').style.color="#7F7F7F";
		document.getElementById('divTab1').style.fontWeight='normal';

		document.getElementById('divTab2').style.borderTop='0px';
		document.getElementById('divTab2').style.borderLeft='0px';
		document.getElementById('divTab2').style.borderRight='0px';
		document.getElementById('divTab2').style.borderBottom='0px';
		document.getElementById('divTab2').style.color="#7F7F7F";
		document.getElementById('divTab2').style.fontWeight='normal';

		document.getElementById('divTab3').style.borderTop='0px';
		document.getElementById('divTab3').style.borderLeft='0px';
		document.getElementById('divTab3').style.borderRight='0px';
		document.getElementById('divTab3').style.borderBottom='0px';
		document.getElementById('divTab3').style.color="#7F7F7F";
		document.getElementById('divTab3').style.fontWeight='normal';

	}



}

