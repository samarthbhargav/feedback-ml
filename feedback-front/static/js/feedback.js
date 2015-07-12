
URI = "http://localhost:9999"

function bindEvent(element, type, handler) {
   if(element.addEventListener) {
      element.addEventListener(type, handler, false);
   } else {
      element.attachEvent('on'+type, handler);
   }
}

/**
@function getXHR will create a xmlhttp object.
@return {xmlhttp} - getXHR will return the xmlhttp object.
*/
function getXHR() {
  console.log("getXHR called")
  if (window.XMLHttpRequest) {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {
        // code for IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    return xmlhttp;
}

/**
@function fetch_stats will fetch the data and will append it to the div tag.
@return {output} - The output will be a div element appended with the fetched data .
*/

function fetch_stats() {
  RESOURCE = "/stats/datasets"
  xmlhttp = getXHR()
  xmlhttp.onreadystatechange=function() {
    if (xmlhttp.readyState==4 && xmlhttp.status==200) {
        console.log(xmlhttp.responseText);
        data = xmlhttp.responseText;
        parentDiv = document.getElementById("datastatsDiv")
        if(data) {
          data = JSON.parse(data);
          arr = data.datasetStatistics;
          for (i = 0; i < arr.length; i++) {
              div = document.createElement('div')
              div.innerHTML = arr[i].dataset + "-" + arr[i].numberOfRecords;
              parentDiv.appendChild(div);
          }
        }
     }
  }
  xmlhttp.open("GET", URI + RESOURCE, true);
  xmlhttp.send();
}




// bind
bindEvent(window, 'load', fetch_stats);
