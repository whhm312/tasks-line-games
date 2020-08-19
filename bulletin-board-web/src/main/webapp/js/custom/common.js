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

function callPost(url, data, successCallback, errorCallback, completeCallback) {
	$("#loadingDiv").fadeIn("slow");
  	$.ajax({
  		url: url,
  		type: "POST",
  		dataType: "json",
  		contentType : "application/json; charset=utf-8",
  		data: JSON.stringify(data),
  		success: function(data) {
  			console.log("success : ", data);
  			successCallback(data);
  		},
  		error: function(jqXHR, textStatus) {
  			console.log("error : ", jqXHR, textStatus);
  			errorCallback(jqXHR, textStatus);
  		},
  		complete : function() {
  			$("#loadingDiv").fadeOut("fast");
  			completeCallback();
  		}
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
