const lib = require('lib');
//note to self, ruined project by manipulating params and slots and call return in this main.js
/**
* @param {string} name Intent Name to trigger
* @param {object} slots Slot Information
* @param {object} request Request Object (required)
* @returns {any}
*/
module.exports = (name = '', slots = {}, request = {}, context, callback) => {
  request.intent = request.intent || {
    name: name,
    slots: Object.keys(slots).reduce((obj, key) => {
      obj[key] = (slots[key] && typeof slots[key] === 'object') ?
        slots[key] : {name: key, value: slots[key]};
      return obj[key];
    }, {})
  };
  if (!request.intent.name) {
    return callback(new Error('Intent name is required'));
  }

  let params = Object.keys(request.intent.slots || {}).reduce((params, key) => {
    params[key] = request.intent.slots[key].value;
    return params;
  }, {});
  params = {
    foods: params.Foods.split('and')
  };


  lib[`${context.service.identifier}.intents.${request.intent.name}`](params, (err, result) => {
    let resultObj;
    if(err == null){
      resultObj = JSON.parse(result);
    }

    return callback(null, {
      version: context.service.environment,
      sessionAttributes: {links: err ? null : resultObj["listOfLinks"]},
      response: {
        outputSpeech: {
          type: 'PlainText',
          text: err ? `Error: ${err.message}` : resultObj["titles"]
        },
        shouldEndSession: false
      }
    });
  });

};
