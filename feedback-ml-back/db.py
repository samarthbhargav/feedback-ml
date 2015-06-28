
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

    def save(self, dataset, record):
        chk_type(record, Record)
        chk_type(dataset, str)
        record.validate()
        obj = record.to_primitive()
        obj["_id"] = { "dataset" : dataset, "id" : record.document.ID}
        self.records.save(obj)


    def get(self, dataset, ID):
        chk_type(dataset, str)
        query = {"id": ID, "dataset" : dataset}
        obj = self.records.find_one()
        if not obj:
            return None
        # remove Mongo fields
        del obj["_id"]
        return Record(obj)
