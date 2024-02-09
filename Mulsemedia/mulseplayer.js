/*
 * --------------------------------------------------------------------------------------------------------------
 * -	Brunel University London & Dublin City University														-
 * -	H2020 NEWTON Project		       																		-
 * -	A Mulseplayer that uses Exhalia, RIVAL 700 haptic mouse, and Silent Shark Fan wind device 				-
 * -	for rendering mulsemedia effects on the Web										       					-
 * -	2018																									-
 * -	Version 3.0																								-
 * --------------------------------------------------------------------------------------------------------------
 */
var inputVideo = document.getElementById("videoFile");
var inputAddress = document.getElementById("haptic");
var fileName = "";
var jsonFile = [];
var json_counter = 0;
var arraySize = 0;
var olf = false;
var hap = false;
var win = false;
var engineAddress = "";
var vid = $('#v0')[0];
var FRAME_RATE = 29.97;

function reloadPage(){
	location.reload();
};

inputVideo.addEventListener("change", function () {
	document.getElementById("olfaction").checked = false;
	document.getElementById("haptic").checked = false;
	document.getElementById("wind").checked = false;
	fileName = inputVideo.options[inputVideo.selectedIndex].text;
	vid.setAttribute('src', "./videos/"+fileName);
	var data;
	var fname = fileName.split(".");
	/*
		Read the JSON file from the local "annoatations" folder
	*/
	var url = "./annotations/"+fname[0]+".json";
	var file = new XMLHttpRequest();

	file.onreadystatechange = function(data2){
		if(file.readyState ==4 && file.status == 200){
			data = file.responseText;
		}
	}
	file.open("GET", url, false);
	file.send();	
	/*
		Get an array of JSON based annotations
	*/
	jsonFile = JSON.parse(data);
	arraySize = jsonFile.length;
	vid.load();		
	
});
inputAddress.addEventListener("change", function () {
	var data = "";
	var url = "http://127.0.0.1:8080/mulsemedia/vibrate";
	var file = new XMLHttpRequest();

	file.onreadystatechange = function(data2){
		if(file.readyState ==4 && file.status == 200){
			data = file.responseText;
		}
	}
	file.open("GET", url, false);
	file.send();	
	/*
		Get an array of JSON based annotations
	*/
	var toJSON = JSON.parse(data);
	engineAddress = toJSON.address;
});

$("input").on("click", function(){
	if($('#olfaction').is(":checked")){
		olf = true;
	}else olf = false;
});
$("input").on("click", function(){
	if($('#haptic').is(":checked")){
		hap = true;
	}else hap = false;
});
$("input").on("click", function(){
	if($('#wind').is(":checked")){
		win = true;
	}else win = false;
});

function effectAt(currentTime){
	
	if (json_counter < arraySize)
	{
		var seekTime = jsonFile[json_counter].seektime * FRAME_RATE;
		var effect = (currentTime >= seekTime)&&(currentTime <= seekTime + 3);
		if (effect)
		{
			/*
				check for user preference for rendering olfaction
				.. and render the effects
			*/
			if (olf == true){
				diffuse(jsonFile[json_counter].olfaction);
			}
			/*
				check for user preference for rendering hapic
				.. and render the effects
			*/			
			if (hap == true){
				vibrate(jsonFile[json_counter].haptic);
			}
			/*
				check for user preference for rendering wind
				.. and render the effects
			*/			
			if (win == true){
				wind(jsonFile[json_counter].wind);
			}			
			//Go to next record of annotation (seek time and SEM value).
			json_counter = json_counter + 1;
		}
	}	
};
/*
	Mulsemedia effectes at each seektime value of the array of annotaions
*/
vid.onplay = vid.onclick = function() {
    setInterval(function() {		
		currentTime = vid.currentTime * FRAME_RATE;
        $('#time').html("#frames:" +(currentTime).toPrecision(5));
		effectAt(currentTime);
    }, 100);
};

/*
	Olfaction implementation for Exhalia
*/
function diffuse(data){
	var SEMdata = "," + data.scent +",,0," + data.duration + "," + data.intensity +",";
	var device_uri = "http://127.0.0.1:8080/mulsemedia/diffuse";
	diffuseCommand(device_uri, JSON.stringify(SEMdata));
};
var diffuseCommand = function (device_uri, SEMdata)
{
  $.ajax({
        type: "POST",
        url: device_uri,
        data: { "conf": SEMdata }
  }).done(function( msg ) {});
};

/*
	Wind implementation
*/
function wind(data){
	var SEMdata = data.port + "," + data.duration + "," + data.intensity;
	var device_uri = "http://127.0.0.1:8080/mulsemedia/wind";
	windCommand(device_uri, JSON.stringify(SEMdata));
};
var windCommand = function (device_uri, SEMdata)
{
  $.ajax({
        type: "POST",
        url: device_uri,
        data: { "conf": SEMdata }
  }).done(function( msg ) {});
};

/*
	Haptic implementation using RIVAL 700 mouse
*/
	
function vibrate(effect) {
	bind_game_event(effect);
	send_game_event ();
};
function send_game_event(){
	var url = "http://" + engineAddress + "/game_event";
	var payload = {
		"game": "RIVAL",
		"event": "HAPTIC",
		"data": {}
	};
	do_post(url, payload);
};
var bind_game_event = function (effect){
  var url = "http://" + engineAddress + "/bind_game_event";
  var payload =
  {
    "game": "RIVAL",
    "event": "HAPTIC",
    "handlers": [
      {
        "device-type": "tactile",
        "zone": "one",
        "mode": "vibrate",
        "pattern": effect.pattern,
        "rate": effect.rate
      }
    ]
  }
  do_post(url, payload);
};
var do_post = function(url, data){
	var http = new XMLHttpRequest();
	http.open("POST", url, true);
	http.setRequestHeader("Content-Type", "application/json");
	http.send(JSON.stringify(data));
};
