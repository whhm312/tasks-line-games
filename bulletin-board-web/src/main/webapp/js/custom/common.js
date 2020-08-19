const SERVER_URL = "http://localhost:9998";

function callGet(url, successCallback, errorCallback, completeCallback) {
	callAjax(url, "GET", "", successCallback, errorCallback, completeCallback);
}

function callPost(url, data, successCallback, errorCallback, completeCallback) {
	callAjax(url, "POST", data, successCallback, errorCallback, completeCallback);
}

function callDelete(url, successCallback, errorCallback, completeCallback) {
	callAjax(url, "DELETE", "", successCallback, errorCallback, completeCallback);
}

function callAjax(url, type, data, successCallback, errorCallback, completeCallback) {
	console.log("(" + type + ") url : ", url);
	
	var param = "";
	if (data) {
		param = JSON.stringify(data);
		console.log("Param : ", param);
	}
	
  	$.ajax({
  		url: url,
  		type: type,
  		dataType: "json",
  		contentType : "application/json; charset=utf-8",
  		data: param,
  		beforeSend: function(jqXHR, settings) {
  			$("#loadingDiv").fadeIn("slow");
  		},
  		success: function(data, textStatus, jqXHR) {
  			console.log("success status[", jqXHR.status,"], data : ", data);
  			
  			if ( typeof successCallback === "function" ) {
  				successCallback(data, jqXHR.status, jqXHR);
  			}
  		},
  		error: function(jqXHR, textStatus, errorThrown) {
  			console.log("error (", jqXHR.status, ") jqXHR : ", jqXHR);
			console.log(jqXHR.responseJSON);
  			
  			if ( typeof errorCallback === "function" ) {
  				errorCallback(jqXHR, jqXHR.status, errorThrown);
  			}
  		},
  		complete : function(jqXHR, textStatus) {
  			$("#loadingDiv").fadeOut("fast");
  			
  			if ( typeof completeCallback === "function" ) {
  				completeCallback(jqXHR, textStatus);
  			}
  			
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
