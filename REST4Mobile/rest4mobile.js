/*!
 * REST4Mobile JavaScript Library v1.0
 * http://rest4mobile.com/
 *
 * Copyright 2015, REST4Mobile Foundation, Inc. and other contributors
 * Released under the Ethiopian MoST license
 *
 * Date: 2015-10-01
 */
	
var i=1, count=0;
var detail=[];

function addElement() {
    i = i + 1;
    var contentID = document.getElementById('content');
    var newTBDiv = document.createElement('div');
    newTBDiv.setAttribute('id','strText'+i);
    newTBDiv.innerHTML = "<input type='text' id='uri" + i + "' name='uri" + i + "' placeholder='Type Android localhost - http://10.0.2.2:8080/uri' size='60'/>";
    contentID.appendChild(newTBDiv);
}
function removeElement() {
    if(i != 0) 
    { 
        var contentID = document.getElementById('content');
        contentID.removeChild(document.getElementById('strText'+i)); 
        i = i - 1;
    }
}

$(document).ready(function() {
	 $("#frmRest").submit(function(e){
	        e.preventDefault();
	 }); 
	 $("#btnCreate").click(function(e){     
		   //get RESTful APIs and items
		   
		   item 	= document.forms['frmRest']['itemCategory'].value;
		   uri1 	= document.forms['frmRest']['uri'+'1'].value;
		   uri2 	= document.forms['frmRest']['uri'+'2'].value;
		   
		   $.getJSON(uri1, function(data1) {
			   $.getJSON(uri2, function(data2) {
				   data = data1.concat(data2); 	
				   detail=data;
			       $("#ajaxResponse").html("");
			       htmlstr = "<table align='center'><thead><tr><td>Name</td><td>Detail</td><td>Consent</td><td>Price</td></tr></thead>";
			       htmlstr += "<tr><td>-------</td><td>---------</td><td>---------</td><td>---------</td></tr>";
			       i = 0;
			       for(d in data){
			    	   count=i; 
			    	   if((data[i].name==item)||(item=="All")) {
			    		   lnkId='lnkDetail'+i.toString();
			    		   chkId='chkItem'+i.toString();
			    		   txtId='txtPrice'+i.toString();
				    	   htmlstr += "<tbody><tr><td>"+data[i].name+"</td>"+
		   					  			"<td><a id="+lnkId+" href='javascript:void' onClick='showDetail(this.id)'>detail</a></td>"+
										"<td><input id="+chkId+" type='checkbox'/></td>"+
		   					  			"<td><input id="+txtId+" type='text' size='5'/></td></tr>";
			    	   }   
			    	   i++;  
			       }
			       $("#ajaxResponse").append(htmlstr+"</tbody></table>"); 
			   });     
		   }); 
	 });
	 
	 //Send consent and quotations
	 $("#btnSubmit").click(function(e){
		 
		 var data = '';
		 for (var i=0; i<=count; i++) {
			 
			 var chk 	= document.getElementById('chkItem'+i.toString()).value;			 
			 var price 	= document.getElementById('txtPrice'+i.toString()).value;
			 
			 if((chk=='on') && (price!='')) {		 
				data = '{"item_code":' + detail[i].code +', "supplier_id":1, "amount_quoted":'+ price+'}';
				$.post('http://10.0.2.2:8080/demand/product/bid', data);
		 	 }			
		 }
		 if (data!='') alert("Successfully delivered!");
		 else alert("Please input your consent & quotation!"); 
	 });  
});

function showDetail(lnkid){ 
	var i=lnkid[9];
	alert("Name:"+detail[i].name+
			"\nDescription:"+detail[i].description+
			"\nQuantity:"+detail[i].qty+ 
			"\nUnit price:"+detail[i].price+
			"\nURI:"+detail[i].uri);
}