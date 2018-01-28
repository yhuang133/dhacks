const lib = require('lib');

/**
* Basic "Hello World" intent, can receive a `name` parameter
* @param {array} foods ingredients obj
* @returns {any}
*/
module.exports = (foods = ["lettuce", "bacon"], callback) => {
	//let foodStr = JSON.parse(foods).join(',')
	lib.yhuang133.endpoint['@dev'](JSON.stringify(foods), (err, result) => {
		let parsedResult = JSON.parse(result);
		let count = parsedResult["count"]
		let maxRecipes = count < 5 ? count : 5
		var titles = "" //string that alexa reads out
		var listOfLinks = []
		for (var i = 0; i < maxRecipes; i++){
			titles += "Recipe " + String(i) + " " + parsedResult["recipes"][i]["title"] + ", "
			listOfLinks.push(parsedResult["recipes"][i]["source_url"])
		}
		let response = JSON.stringify({titles: titles, listOfLinks: listOfLinks})
		if (err) {
			return callback(err);
		}

		return callback(null, response);

	})

};
