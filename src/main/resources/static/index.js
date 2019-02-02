window.onload = function() {
	console.log(1);
	

	$.ajax({
		url: "getName",
		method: "GET",
		data: {"id": "1"},
		success: function(data) {
			console.log(1);
		}
	});
	
	$.ajax({
		url: "getName",
		method: "GET",
		data: {"id": "2"},
		success: function(data) {
			console.log(2);
		}
	});
}