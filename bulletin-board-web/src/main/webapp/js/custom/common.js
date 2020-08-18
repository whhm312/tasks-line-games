const SERVER_URL = "http://localhost:9998";

function callGet(url, callback) {
	$("#loadingDiv").fadeIn("slow");
	
	console.log("Url : (GET) " + url);
	$.get( url , function( data, status ) {
		console.log("Data: ", data);
		console.log("Status: ", status);
		
		callback(data, status);

		$("#loadingDiv").fadeOut("fast");
	});
}

function getUrlParam(name) {
    var results = new RegExp('[\?&amp;]' + name + '=([^&amp;#]*)').exec(window.location.href);
    if (results) {
    	return results[1];
	} else {
		return null;
	}
}
