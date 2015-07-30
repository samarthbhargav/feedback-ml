import client
from client import FeedBackClient
import requests
import json

BASE_URL = 'https://archive.ics.uci.edu/ml/machine-learning-databases/iris/iris.data'
POST_RECORDS_RESOURCE = "http://localhost:9999/dataset/iris-dataset/record/"
DATASET_NAME = "iris_dataset"
JSON_HEADERS = {'Content-Type': 'application/json'}

iris_data = requests.get(BASE_URL).text
iris_data_lines = iris_data.split('\n')
iris_data_list = []
record = {}

for i in range(len(iris_data_lines)):
	if iris_data_lines[i]:
	    iris_data_list.append(iris_data_lines[i].split(','))	
	    record = {
	    	"id" : i,
	    	"label" : iris_data_list[i][4],
	    	"content" : {
	    		"sepal length" : float(iris_data_list[i][0]),
	    		"sepal width" : float(iris_data_list[i][1]),
	    		"petal length" : float(iris_data_list[i][2]),
	    		"petal width" : float(iris_data_list[i][3]),
	    	},
	    }
	    jsons = json.dumps(record)
	    resp = requests.post("http://localhost:9999/dataset/iris-dataset/record/", data=jsons, headers=JSON_HEADERS)
        print resp
print record

