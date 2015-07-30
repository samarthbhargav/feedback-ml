import requests
import json

IRIS_DATASET_URL = 'https://archive.ics.uci.edu/ml/machine-learning-databases/iris/iris.data'

DATASET_NAME = "iris"
URL = "http://localhost:9999/dataset/{}/record/".format(DATASET_NAME)
# creates dataset

datasetJSON = {
  "name" : "iris",
  "strictValidation" : True,
  "fields": [
    { "name" : "sepal-length", "type" : "NUMERICAL"},
    { "name" : "sepal-width", "type" : "NUMERICAL"},
    { "name" : "petal-length", "type" : "NUMERICAL"},
    { "name" : "petal-width", "type" : "NUMERICAL"},
  ]
}

resp = requests.post("http://localhost:9999/dataset/",
              data=json.dumps(datasetJSON), 
              headers={'Content-Type': 'application/json'})
              
assert resp.status_code == 200, "can't create iris dataset"

skipped = 0
saved = 0
non_200 = 0

for i, line in enumerate(requests.get(IRIS_DATASET_URL).text.split('\n')):        
    data = line.split(",")
    if len(data) != 5:
        print "skipping line {}".format(line)
        skipped += 1
        continue
    record = {
        "id" : i,
        "label" : data[4],
        "content" : {
            "sepal-length" : float(data[0]),
            "sepal-width" : float(data[1]),
            "petal-length" : float(data[2]),
            "petal-width" : float(data[3]),
	   },
    }
    jsons = json.dumps(record)
    resp = requests.post(
        URL, 
        data=jsons, 
        headers={'Content-Type': 'application/json'})
        
    if resp.status_code == 200:
        saved += 1
    else:
        non_200 += 1

assert saved == 150, "incorrect number of records were saved"        
print "Saved: {}, Skipped: {}, Non-200-Response: {}, Total: {}".format(saved, skipped, non_200, saved+skipped+non_200)