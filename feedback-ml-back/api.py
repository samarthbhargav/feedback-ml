from flask import Flask
from flask import jsonify
from flask_restful import Resource, Api
from flask_restful import reqparse


app = Flask(__name__)
api = Api(app)
from db import RecordDAO

recordDAO = RecordDAO()

class DatasetResource(Resource):
    def get(self):
        return recordDAO.get_datasets()


class RecordsPageResource(Resource):
    parser = reqparse.RequestParser()
    parser.add_argument('skip', type=int, default=0)
    parser.add_argument('limit', type=int, default=100) #TODO make config

    def get(self, dataset):
        args = RecordsPageResource.parser.parse_args()
        skip = args["skip"]
        limit = args["limit"]
        records = recordDAO.page(dataset, skip, limit)
        return {"results" : [r.to_primitive() for r in records]}

api.add_resource(DatasetResource, '/datasets')
api.add_resource(RecordsPageResource, '/records/<string:dataset>')

if __name__ == '__main__':
    app.run(debug=True)
