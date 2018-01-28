const lib = require('lib')({token: "crrAcVC0I-Az2tYtxSRaiQLmqjfxO9X17B5CbAmp89_bJYleQ5KyKc2ymg6N7-A0"});

/**
* Basic "Hello World" intent, can receive a `name` parameter
* @param {array} foods ingredients obj
* @returns {any}
*/
module.exports = (foods = ["lettuce", "bacon"], callback) => {
	//let foodStr = JSON.parse(foods).join(',')
	lib.yhuang133.endpoint['@dev'](JSON.stringify(foods), (err, result) => {
		if (err) {
			return callback(err);
		}
		let parsedResult = JSON.parse(result);
		let count = parsedResult["count"]
		let maxRecipes = count < 5 ? count : 5
		var titles = "" //string that alexa reads out
		var listOfLinks = []
		var titlesToLinks = ""
		for (var i = 0; i < maxRecipes; i++){
			titles += "Recipe " + String(i) + " " + parsedResult["recipes"][i]["title"] + ", "
			listOfLinks.push(parsedResult["recipes"][i]["source_url"])
			titlesToLinks += parsedResult["recipes"][i]["title"] + " " + parsedResult["recipes"][i]["source_url"] + "\n\n"
		}
		console.log(titlesToLinks)
		let response = JSON.stringify({titles: titles, listOfLinks: listOfLinks})
		lib.utils.sms({
		  to: '6477161282',
		  body: titlesToLinks
		}, (err, result) => {
		  return callback(err, response);
		});

	})

};
