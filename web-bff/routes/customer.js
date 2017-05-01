module.exports = function(app, request) {

	var resJson = [];
	var microserviceBaseUrl = "http://customer-vbudiusibmcom.mybluemix.net/micro";

//	search
	app.get('/api/customer',function(req, res){
		var endPoint = microserviceBaseUrl + '/customer';
		request({
			url: endPoint,
			method: "GET",
			headers: { 'ibm-app-user': req.get('ibm-app-user') }
		}, function (error, response, body) {
            if (0 === body.length) {
                return res.send({"error":"no customer found"});
            }
			//var bodyJson = JSON.parse(body);

			if (!error && response.statusCode == 200) {
				return res.json(body);
			}else {
				return res.send({"error":error});
			}
		});
	});

//	search
	app.use('/api/customer/search',function(req, res){
		console.log("Customer search ... ");
		console.log(req.path);
                var q = req.query;
                var username = q["username"];
		console.log(username);
		var endPoint = microserviceBaseUrl + '/customer/search?username=' + username;
		console.log(endPoint);
		request({
			url: endPoint,
			method: "GET",
			headers: { 'Authorization': req.get('Authorization'), 'ibm-app-user': req.get('ibm-app-user') }
		}, function (error, response, body) {
            if (0 === body.length) {
                return res.send({"error":"no customer found"});
            }
			console.log(body);
			var bodyJson = JSON.parse(body);

			if (!error && response.statusCode == 200) {
				return res.json(bodyJson);
			}else {
                                console.log("Error: "+response.statusCode);
                                console.log(body);
				return res.send({"error":error});
			}
		});
	});

//	get item by id
	app.get('/api/customer/:id',function(req, res){

		var endPoint = microserviceBaseUrl + '/customer/' + req.params.id;
		request({
			url: endPoint,
			method: "GET",
			headers: {
    		'Authorization': req.get('Authorization'),  'ibm-app-user': req.get('ibm-app-user')
  		}
		}, function (error, response, body) {
            if (0 === body.length) {
                return res.send({"error":"customer not found"});
            }
			var bodyJson = JSON.parse(body);

			if (!error && response.statusCode == 200) {
				return res.json(bodyJson);
			} else {
				return res.send({"error":error});
			}

		});
	});

//	add item to inventory
	app.post('/api/customer', function (req, res){

		var endPoint = microserviceBaseUrl + '/customer';
		console.log(req.body);
		//send request with json payload
		request({
			url: endPoint,
			method: "POST",
			headers: {
    		'Authorization': req.get('Authorization'),  'ibm-app-user': req.get('ibm-app-user')
  		},
			json: req.body
		}, function(error, response, body){

			if (!error && response.statusCode == 200) {
				return res.json(body);
			} else {
				console.log("E:"+body);
				return res.send({"error":error});
			}
		});
	});

//	update item by id
	app.put('/api/customer/:id',function(req, res){

		var endPoint = microserviceBaseUrl + '/customer/' + req.params.id;
		//send request with json payload
		request({
			url: endPoint,
			method: "PUT",
			headers: {
    		'Authorization': req.get('Authorization'),  'ibm-app-user': req.get('ibm-app-user')
  		},
			json: req.body
		}, function(error, response, body){

			if (!error && response.statusCode == 200) {
				return res.json(body);
			} else {
				return res.send({"error":error});
			}
		});
	});

//	delete item from inventory
	app.delete('/api/customer/:id',function(req, res){

		var endPoint = microserviceBaseUrl + '/customer/' + req.params.id;
		request({url: endPoint,
							method: "DELETE",
							headers: {
				    		'Authorization': req.get('Authorization'),  'ibm-app-user': req.get('ibm-app-user')
				  		}
						}, function (error, response, body) {

			if (!error && response.statusCode == 200) {
				return res.json(body);
			} else {
				return res.send({"error":error});
			}
		});
	});
};
