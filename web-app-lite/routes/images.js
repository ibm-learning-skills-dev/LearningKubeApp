var express = require('express');
var router = express.Router();
var http = require('request-promise-json');
var Promise = require('promise');
var UrlPattern = require('url-pattern');
var config = require('config');
var pkgcloud = require('pkgcloud');


// VCAP_SERVICES contains all the credentials of services bound to
// this application. For details of its content, please refer to
// the document or sample of each service.
var _myApp = config.get('Application');

//Download the file
router.get('/:fileName', function(req, res){



    //res.writeHead(200, {'Content-Type': 'text/html'});
    //res.write();
    //res.end();


});


function renderErrorPage(function_input) {
  var err = function_input.err;
  var res = function_input.res;
  res.render('error', {reason: err});
}

module.exports = router;
