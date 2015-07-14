import json
import requests


STATS_RESOURCE = "http://localhost:9999/stats/datasets"
RECORDS_STATS_RESOURCE = "http://localhost:9999/stats/datasets/{}"
POST_RECORDS_RESOURCE = "http://localhost:9999/dataset/{}/record/"
RECORDS_PAGE_RESOURCE = "http://localhost:9999/dataset/{}/records/{}/{}"
POST_DATASET_RESOURCE = "http://localhost:9999/dataset/"

JSON_HEADERS = {'Content-Type': 'application/json'}


class FeedBackClient(object):

    def __init__(self):
        pass

    def fetch_dataset_statistics(self):
        return requests.get(STATS_RESOURCE).json()["datasetStatistics"]

    def fetch_record_statistics(self, dataset):
        endpoint = RECORDS_STATS_RESOURCE.format(dataset)
        response = requests.get(endpoint)
        if response.status_code == 200:
            return True, response.json()
        else:
            return False, "Error Message"
        #return requests.get(RECORDS_STATS_RESOURCE).json()["datasetStatistics"]

    def save_record(self, record, dataset):

        endpoint = POST_RECORDS_RESOURCE.format(dataset)
        jsons = json.dumps(record)

        resp = requests.post(endpoint, data=jsons, headers=JSON_HEADERS)
        print resp

        if resp.status_code == 200:
            return True
        else:
            # TODO add more desc message
            return False

    def save_dataset(self, new_dataset):

        endpoint = POST_DATASET_RESOURCE.format(new_dataset)

        resp = requests.post(endpoint)
        print resp

        if resp.status_code == 200:
            return True
        else:
            return False

    def get_records(self, dataset, skip, limit):
        endpoint = RECORDS_PAGE_RESOURCE.format(dataset, skip, limit)
        response = requests.get(endpoint)
        if response.status_code == 200:
            return True, response.json()
        else:
            return False, None
