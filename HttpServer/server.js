const express = require('express');
const app = express();
const bodyParser = require("body-parser");
const port = 3000;

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

const server = app.listen(port, '<change-this-with-your-host-ip>');

app.post('/post-request-url', (request, response) => {
	response.send('response from server to the post request!');
	console.log(JSON.stringify(request.body.bassam));
})

server.on('listening', (error) => {
	if (error) {
		console.error('error ancountered: ' + error);
	} else {
		console.log('Express server started on port %s at %s', 
			server.address().port, 
			server.address().address);
	}
});