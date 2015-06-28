
from config import conf
import pymongo

from common import  chk_type
from models import Record, Document

class MongoConnector(object):
    db = pymongo.MongoClient(host=conf["mongo.host"], port=conf["mongo.port"])[conf["mongo.db"]]

    @staticmethod
    def collection(name):
        return MongoConnector.db[name]


class RecordDAO(object):

    def __init__(self):
        self.records = MongoConnector.collection(conf["mongo.collections.records"])
        self.records.create_index("dataset")

    def __marshal(self, record):
        record.validate()
        obj = record.to_primitive()
        obj["_id"] = { "dataset" : dataset, "id" : record.document.ID}
        return record

    def __unmarshal(self, obj):
        if not obj:
            return None
        # remove Mongo fields
        del obj["_id"]
        obj["document"] = Document(obj["document"])
        record = Record(obj)
        record.validate()
        return record

    def save(self, dataset, record):
        chk_type(record, Record)
        chk_type(dataset, str)
        obj = self.__marshal(record)
        self.records.save(obj)


    def get(self, dataset, ID):
        chk_type(dataset, str)
        query = {"id": ID, "dataset" : dataset}
        obj = self.records.find_one()
        return self.__unmarshal(obj)

    def get_datasets(self):
        pipeline = [
            { "$group" : { "_id" : "$_id.dataset", "records" : { "$sum" : 1 } } },
            { "$project" : { "_id" : 0, "dataset" : "$_id", "records" : "$records"}}
        ]
        results = self.records.aggregate(pipeline)
        datasets = []
        for result in results:
            datasets.append(result)
        return { "datasets" : datasets }

    def page(self, dataset, skip, limit):
        results = self.records.find({"_id.dataset" : dataset}).skip(skip).limit(limit)
        records = []
        for result in results:
            records.append(self.__unmarshal(result))
        return records
