module.exports = function(app, request) {

	var resJson = [];
	var microserviceBaseUrl = "http://catalog-vbudiusibmcom.mybluemix.net/micro";

//	search
	app.get('/api/customer',function(req, res){
		var endPoint = microserviceBaseUrl + '/customer';
		request({
			url: endPoint,
			method: "GET",
			headers: { 'Authorization': req.get('Authorization') }
		}, function (error, response, body) {
            if (0 === body.length) {
                return res.send({"error":"no customer found"});
            }
			var bodyJson = JSON.parse(body);

			if (!error && response.statusCode == 200) {
				return res.json(bodyJson);
			}else {
				return res.send({"error":error});
			}
		});
	});

//	search
	app.get('/api/customer/search',function(req, res){
		var endPoint = microserviceBaseUrl + '/customer/search';
		request({
			url: endPoint,
			method: "GET",
			headers: { 'Authorization': req.get('Authorization'), 'ibm-app-user': req.get('ibm-app-user') }
		}, function (error, response, body) {
            if (0 === body.length) {
                return res.send({"error":"no customer found"});
            }
			var bodyJson = JSON.parse(body);

			if (!error && response.statusCode == 200) {
				return res.json(bodyJson);
			}else {
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
