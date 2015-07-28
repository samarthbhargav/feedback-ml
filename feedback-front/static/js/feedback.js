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

    /** Function to add new label to a record **/
    this.addLabelValue = function(formID, dataset, recordName) {
        var rawJSON = $("#" + formID).serializeObject();

       labelValue = rawJSON['newLabelName'];
       console.log(labelValue);
       if(labelValue){

        $.ajax({
          type: "post",
          url: this.baseURL + "/dataset/" + dataset + "/record/" + recordName + "/label/"+ labelValue,
          headers: {
            "Content-Type" : "application/json"
          },
          success: function(jqxhr) {
            // Display success message in UI
            $('#labelNameStatus').html("<div style='color:green'>Label Name was added successfully.</div>");
            window.setTimeout(function(){location.reload()}, 1500);
          }
        }).fail(function (jqxhr, exception) {
            // Display failure message in UI
                console.log(" Adding new label value failed!");
              if (jqXHR.status === 0) {
                  $('#labelNameStatus').html("<div style='color:red'>Not connecting, verify your network</div>");
                  window.setTimeout(function(){location.reload()}, 2000);
              } else if (jqXHR.status == 404) {
                  $('#labelNameStatus').html("<div style='color:red'>Requested page not found [404]</div>");
                  window.setTimeout(function(){location.reload()}, 2000);
              } else if (jqXHR.status == 500) {
                  $('#labelNameStatus').html("<div style='color:red'>Internal Server Error [500].</div>");
                  window.setTimeout(function(){location.reload()}, 2000);
              } else if (exception === 'parsererror') {
                  $('#labelNameStatus').html("<div style='color:red'>Requested JSON parse failed.</div>");
                  window.setTimeout(function(){location.reload()}, 2000);
              } else if (exception === 'timeout') {
                  $('#labelNameStatus').html("<div style='color:red'>Time out error.</div>");
                  window.setTimeout(function(){location.reload()}, 2000);
              } else if (exception === 'abort') {
                  $('#labelNameStatus').html("<div style='color:red'>Ajax request aborted.</div>");
                  window.setTimeout(function(){location.reload()}, 2000);
              } else {
                  $('#labelNameStatus').html("<div style='color:red'>Uncaught Error.</div>");
                  window.setTimeout(function(){location.reload()}, 2000);
              }

            if(jqxhr.getResponseHeader('Content-Type') == "text/plain") {
                // Plain Text error - display in UI
                console.log(jqxhr.responseText);
            } else if(jqxhr.getResponseHeader('Content-Type') == "application/json") {
                // Display error message in UI
                var errorJSON = $.parseJSON(jqxhr.responseText);
                console.log(errorJSON);
            }

        });

        return false;
      }
      else{
            // no labelvalue
          $('#labelNameStatus').html("<div style='color:red'>No Label Name</div>");
            window.setTimeout(function(){location.reload()}, 1000);
      }

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

        if(rawJSON['fieldType'] == null){
            $('#labelNameStatus').html("<div style='color:red'>Some Error Occurred</div>");
            window.setTimeout(function(){location.reload()}, 1500);
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
            $('#datasetStatus').html("<div style='color:green'>Dataset was added successfully.</div>");
            window.setTimeout(function(){location.reload()}, 1500);
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
        $('#datasetStatus').html("<div style='color:red'>Some Error Occurred</div>");
          window.setTimeout(function(){location.reload()}, 1500);
    };

    /** Function to delete a dataset by accessing the data from the API by making an AJAX call **/
    this.removeDataset = function(dataset) {

      $.ajax({
                type: "DELETE",
                url: this.baseURL + "/dataset/" + dataset,
                contentType: "application/json",
                crossDomain: true,
                dataType: "json",
                success: function( dataset ){
                    // Put the plain text in the PRE tag.
                    $( "#deleteStatus" ).text( response );
                },
                error: function( error ){
                    // Log any error.
                    $( "#deleteStatus" ).text( error );
                    console.log( "ERROR:", error );
                }
            });
    };


    /** Function to delete a record from a dataset by accessing the data from the API by making an AJAX call **/
    this.removeRecord = function(recordID, recordLabel, dataset, skip, limit) {

      $.ajax({
                type: "DELETE",
                url: this.baseURL +"/dataset/"+ dataset +"/record/"+ recordID,
                success: function( record ){
                    // Put the plain text in the PRE tag.
                    $( "#deleteStatus" ).text( response );
                },
                error: function( error ){
                    // Log any error.
                    $( "#deleteStatus" ).text( error );
                    console.log( "ERROR:", error );
                }
            });
    };

};


// Instantiate
client = new FeedBackClient()
