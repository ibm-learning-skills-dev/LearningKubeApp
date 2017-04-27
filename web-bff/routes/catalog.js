module.exports = function(app, request) {

	var resJson = [];
	var microserviceBaseUrl = "http://catalog-vbudiusibmcom.mybluemix.net/micro";

//	get list of items in inventory
	app.get('/api/items',function(req, res){
		var endPoint = microserviceBaseUrl + '/items';
		request({
			url: endPoint,
			method: "GET",
			headers: {
    		'Authorization': req.get('Authorization'), 'ibm-app-user': req.get('ibm-app-user')
  		}
		}, function (error, response, body) {
            if (0 === body.length) {
                return res.send({"error":"no items in inventory"});
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
	app.get('/api/items/:id',function(req, res){

		var endPoint = microserviceBaseUrl + '/items/' + req.params.id;
		request({
			url: endPoint,
			method: "GET",
			headers: {
    		'Authorization': req.get('Authorization'), 'ibm-app-user': req.get('ibm-app-user')
  		}
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

};
