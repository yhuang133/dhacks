const request = require('request');
/**
* takes ingredient list and returns relevant recipes
* @param {string} foods the ingredients in JSON
* @returns {string}
*/
module.exports = (foods = '["lettuce", "bacon"]', context, callback) => {
	let foodsArr = JSON.parse(foods)
	if(!(foodsArr instanceof Array)){
		callback(error, "That's not an array.")
	}

	let foodsList = foodsArr.join(',')

	request.post('http://food2fork.com/api/search', {form:{key:'4a81206fc815a3571e94d6c529df33b5', q: foodsList}}, function(err,httpResponse,body){
		callback(null, httpResponse["body"])
	})


};
