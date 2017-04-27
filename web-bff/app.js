var express = require('express');
var http = require('http');
var request = require('request');
var path = require('path');
var fs = require('fs');
//cfenv provides access to your Cloud Foundry environment
//for more info, see: https://www.npmjs.com/package/cfenv
var cfenv = require('cfenv');
var bodyParser = require('body-parser');
var router = express.Router();

//all environments
var app = express();
app.set('port', process.env.PORT || 3001);
app.set('json spaces', 2);
app.use(bodyParser.json());
app.use(router);
app.use(express.static(path.join(__dirname, 'public')));

//route to rest controller
var orders = require('./routes/orders.js');
var customer = require('./routes/customer.js');
var catalog = require('./routes/catalog.js');
orders(app, request);
customer(app, request);
catalog(app, request);

http.createServer(app).listen(app.get('port'), function() {
	console.log('Express server listening on port ' + app.get('port'));
});
