
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
    this.addRecord = function(formID, dataset) {
        var rawJSON = $("#" + formID).serializeObject();
        console.log(rawJSON)
        json = {
          "id" : rawJSON["RecordID"],
          "label" : rawJSON["RecordLabel"]
        }

        console.log(json);
        json.content = Object();

        if(rawJSON['content-key'] instanceof Array){
          for(var i=0; i<rawJSON['content-key'].length;i++){
            console.log(rawJSON['content-key'][i]);
            console.log(rawJSON['content-value'][i]);
            json.content[rawJSON['content-key'][i]] = rawJSON['content-value'][i];
          }
        }
        else{
          json.content[rawJSON['content-key']]= rawJSON['content-value'];
        }
        
        $.ajax({
          type: 'POST', 
          url: this.baseURL + "/dataset/" + dataset + "/record/",
          dataType: 'json',
          headers: {
            "Content-Type" : "application/json"
          },
          data : JSON.stringify(json),
          success: function(data) {
            success("Record created successfully");
          }
        
        }).fail(function (data, exception){
              console.log(data.status);
              if (data.status == 200) {
                console.log("successful");
                success("Record created successfully");
              }
              else if (data.status === 0) {
                  failure("Not connecting, verify your network");
              } else if (data.status == 404) {
                  failure("Requesting page not found [404]");
              } else if (data.status == 500) {
                  failure("Internal Error [500]");
              } else if (exception === 'parsererror') {
                  failure("Requested JSON parser failed");
              } else if (exception === 'timeout') {
                  failure("Time Out Error");
              } else if (exception === 'abort') {
                  failure("AJAX request aborted");
              } else {
                  failure("Uncaught Error");
              }

              if(data.getResponseHeader('Content-Type') == "text/plain") {
                  // TODO:Mahesh Plain Text error - display in UI
                  console.log(jqxhr.responseText);
              } else if(data.getResponseHeader('Content-Type') == "application/json") {
                  // TODO:Mahesh Display error message in UI
                  var errorJSON = $.parseJSON(data.responseText);
                  console.log(errorJSON);
              }
        });
        
        failure("Some Error Occurred");
        
        function success(msg){
        $('#recordStatus').html("<div style='color:green'>"+msg+"</div>");
            window.setTimeout(function(){location.reload()}, 150);
        }
        
        function failure(msg){
          $('#recordStatus').html("<div style='color:red'>"+msg+"</div>");            
            window.setTimeout(function(){location.reload()}, 200);
        }
    };

    /** Function to add new label to a record **/
    this.addLabelValue = function(formID, dataset, recordName) {
      var rawJSON = $("#" + formID).serializeObject();
      labelValue = rawJSON['newLabelName'];
      if(labelValue){
        $.ajax({
          type: "post",
          url: this.baseURL + "/dataset/" + dataset + "/record/" + recordName + "/label/"+ labelValue,
          headers: {
            "Content-Type" : "application/json"
          },
          success: function() {
            // Display success message in UI
            success("Label Name Added Successfully");
          }
        
        }).fail(function (jqxhr, exception) {
            // Display failure message in UI
            console.log(" Adding new label value failed!");
                
            if (jqXHR.status === 0) {
                failure("Not connecting, verify your network");
            } else if (jqXHR.status == 404) {
                failure("Requesting page not found [404]");
            } else if (jqXHR.status == 500) {
                failure("Internal Error [500]");
            } else if (exception === 'parsererror') {
                failure("Requested JSON parser failed");
            } else if (exception === 'timeout') {
                failure("Time Out Error");
            } else if (exception === 'abort') {
                failure("AJAX request aborted");
            } else {
                failure("Uncaught Error");
            }
              
            if(jqxhr.getResponseHeader('Content-Type') == "text/plain") {
                // Plain Text error - display in UI
              console.log(jqxhr.responseText);
            } 
            else if(jqxhr.getResponseHeader('Content-Type') == "application/json") {
                // Display error message in UI
                var errorJSON = $.parseJSON(jqxhr.responseText);
                console.log(errorJSON);
            }

          });
      }
      else{
            // no labelvalue
            failure("No Label Name");
      }

      function success(msg){
        $('#labelNameStatus').html("<div style='color:green'>"+msg+"</div>");
            window.setTimeout(function(){location.reload()}, 150);
      }
      
      function failure(msg){
        $('#labelNameStatus').html("<div style='color:red'>"+msg+"</div>");            
          window.setTimeout(function(){location.reload()}, 200);
      }

    };


    /** Function to parse a form and post the parsed JSON data to the API **/
    this.addDataset = function(formID) {
        var rawJSON = $("#" + formID).serializeObject();
        console.log(rawJSON);

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

        $.ajax({
          type: "POST",
          url: this.baseURL + "/dataset/",
          headers: {
            "Content-Type" : "application/json"
          },
          data: JSON.stringify(json),
          success: function(data) {
            $('#datasetStatus').html("<div style='color:green'>Dataset was added successfully</div>");
            window.setTimeout(function(){location.reload()}, 150);
          }
        }).fail(function (jqxhr) {
            $('#datasetStatus').html("<div style='color:red'>Some Error Occurred</div>");
            window.setTimeout(function(){location.reload()}, 150);

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
          window.setTimeout(function(){location.reload()}, 150);
    };

    /** Function to delete a dataset by accessing the data from the API by making an AJAX call **/
    this.removeDataset = function(dataset) {

      $.ajax({
        type: "DELETE",
        url: this.baseURL + "/dataset/" + dataset,
        contentType: "application/json",
        crossDomain: true,
        dataType: "json",
        success: function( record ){
          success("Record was successfully deleted.")  
        },
        error: function( error ){
            // Log any error.
            console.log("Sucess");
            if(error.status == 200){
              success("Record was deleted successfully")
              console.log("successfully deleted")
            }
            else{
              failure("Some Error Occurred")
            }
        }
      });
        function success(msg){
          $('#datasetStatus').html("<div style='color:green'>"+ msg +"</div>");
        }
        function failure(msg){
          $('#datasetStatus').html("<div style='color:red'>"+ msg +"</div>"); 
        }
        window.setTimeout(function(){location.reload()}, 10);
    };


    /** Function to delete a record from a dataset by accessing the data from the API by making an AJAX call **/
    this.removeRecord = function(recordID, recordLabel, dataset, skip, limit) {

      $.ajax({
        type: "DELETE",
        url: this.baseURL +"/dataset/"+ dataset +"/record/"+ recordID,
        success: function( record ){
          window.setTimeout(function(){location.reload()}, 10);
          success("Record was successfully deleted.")  
        },
        error: function( error ){
            // Log any error.
            if(error.status == 200){
              window.setTimeout(function(){location.reload()}, 10);
              success("Record was deleted successfully")
            }
            else{
              window.setTimeout(function(){location.reload()}, 10);
              failure("Some Error Occurred")
            }
        }
      });
        function success(msg){
          $('#labelNameStatus').html("<div style='color:green'>"+ msg +"</div>");
        }
        function failure(msg){
          $('#labelNameStatus').html("<div style='color:red'>"+ msg +"</div>"); 
        }
    };

/* end of FeedBackClient function */
};


// Instantiate
client = new FeedBackClient()
