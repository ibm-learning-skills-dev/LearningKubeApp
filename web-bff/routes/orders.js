module.exports = function(app, request) {

	var resJson = [];
	var microserviceBaseUrl = "http://orders-vbudiusibmcom.mybluemix.net/micro";
//	get list of items in inventory
	app.get('/api/orders',function(req, res){
		var endPoint = microserviceBaseUrl + '/orders';
		request({
			url: endPoint,
			method: "GET",
			headers: { 'Authorization': req.get('Authorization'), 'ibm-app-user': req.get('ibm-app-user') }
		}, function (error, response, body) {
                        if (0 === body.length) {
                	        return res.send({"error":"no orders"});
               		}
			var bodyJson = JSON.parse(body);
			if (!error && response.statusCode == 200) {
				return res.json(bodyJson);
			}else {
				return res.send({"error":error});
			}
		});
	});

//	get order by id
	app.get('/api/orders/:id',function(req, res){
		var endPoint = microserviceBaseUrl + '/orders/' + req.params.id;
		request({
			url: endPoint,
			method: "GET",
			headers: { 'Authorization': req.get('Authorization'), 'ibm-app-user': req.get('ibm-app-user') }
		}, function (error, response, body) {
     		       if (0 === body.length) {
                		return res.send({"error":"item not found"});
            		}
			var bodyJson = JSON.parse(body);
			if (!error && response.statusCode == 200) {
				return res.json(bodyJson);
			} else {
				return res.send({"error":error});
			}
		});
	});

//	add order
	app.post('/api/orders', function (req, res){
		var endPoint = microserviceBaseUrl + '/orders';
		//send request with json payload
		request({
			url: endPoint,
			method: "POST",
			headers: { 'Authorization': req.get('Authorization'), 'ibm-app-user': req.get('ibm-app-user') },
			json: req.body
		}, function(error, response, body){
			if (!error && response.statusCode == 200) {
				return res.json(body);
			} else {
				return res.send({"error":error});
			}
		});
	});
};
