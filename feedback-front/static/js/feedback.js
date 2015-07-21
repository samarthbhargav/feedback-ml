// TODO remove console.logs

var FeedBackClient = function(name) {
    this.baseURL = "http://localhost:9999";

    this.getAvailableFieldTypes = function() {
        if(this.availableFieldTypes !== undefined) {
            return this.availableFieldTypes;
        }
        // strUrl is whatever URL you need to call
        var types = [];

        $.ajax({
          url: this.baseURL + "/meta/availableFieldTypes",
          success: function(json) {
            types = json["types"];
          },
          async:false
        });

        this.availableFieldTypes = types;
        return types;
    };

    /** Function to parse a form and post the parsed JSON data to the API **/
    this.addRecord = function(formID) {
        // TODO:Mahesh use the next function as a template and complete this
    };

    /** Function to parse a form and post the parsed JSON data to the API **/
    this.addDataset = function(formID) {
        var rawJSON = $("#" + formID).serializeObject();

        // TODO:Mahesh Validate the data
        // TODO:Mahesh Dataset should be defined and non-empty
        // TODO:Mahesh fields, if present, should be defined, non-empty and have a corresponding
        // TODO:Mahesh field type which belongs to this.availableFieldTypes

        json = {
          "name" : rawJSON["DatasetName"],
          "fields" : []
        }
        
        // checking if the rawJSON['field'] is of Array instance
        if(rawJSON['field'] instanceof Array){
          for(var i=0; i<rawJSON['field'].length;i++){
            console.log(rawJSON['field'][i]);
            console.log(rawJSON['fieldType'][i]);
            json.fields.push({
              "name" : rawJSON['field'][i],
              "type" : rawJSON['fieldType'][i]
            });
          }
        }
        else{
          json.fields.push({
            "name" : rawJSON['field'],
            "type" : rawJSON['fieldType']
          });
        }

        console.log(json);
        console.log(rawJSON['field'].length);


        console.log(json);
        console.log(rawJSON['field'].length);

        $.ajax({
          type: "POST",
          url: this.baseURL + "/dataset/",
          headers: {
            "Content-Type" : "application/json"
          },
          data: JSON.stringify(json),
          success: function(data) {
            // TODO:Mahesh Display success message in UI
            console.log(data);
            console.log("Success!");
          }
        }).fail(function (jqxhr) {
            // TODO:Mahesh Display failure message in UI
            console.log("Dataset creation failed!");

            if(jqxhr.getResponseHeader('Content-Type') == "text/plain") {
                // TODO:Mahesh Plain Text error - display in UI
                console.log(jqxhr.responseText);
            } else if(jqxhr.getResponseHeader('Content-Type') == "application/json") {
                // TODO:Mahesh Display error message in UI
                var errorJSON = $.parseJSON(jqxhr.responseText);
                console.log(errorJSON);
            }

        });

        return false;
    };
};


// Instantiate
client = new FeedBackClient()
