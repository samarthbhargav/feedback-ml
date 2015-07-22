from flask import Flask
from flask import render_template
from flask import request, flash
from flask import redirect
import json
import requests
from client import FeedBackClient
from math import ceil


app = Flask(__name__)

client = FeedBackClient()

class Pagination(object):

    def __init__(self, page, per_page, total_count):
        self.page = page
        self.per_page = per_page
        self.total_count = total_count

    @property
    def pages(self):
        return int(ceil(self.total_count / float(self.per_page)))

    @property
    def has_prev(self):
        return self.page > 1

    @property
    def has_next(self):
        return self.page < self.pages

    def iter_pages(self, left_edge=2, left_current=2,
                   right_current=5, right_edge=2):
        last = 0
        for num in xrange(1, self.pages + 1):
            if num <= left_edge or \
               (num > self.page - left_current - 1 and \
                num < self.page + right_current) or \
               num > self.pages - right_edge:
                if last + 1 != num:
                    yield None
                yield num
                last = num
PER_PAGE = 9

@app.route('/', defaults={'page': 1})
@app.route('/page/<int:page>')
def index(page):
    stats = client.fetch_dataset_statistics()
    datasetFields = client.fetch_dataset_fields()
    # break it up into chunks of 3
    stat_chunks = chunks(stats, 3)
    count = len(stats)
    pagination = Pagination(page, PER_PAGE, count)
    if not pagination and page != 1:
        abort(404)
    return render_template("index.html", stats = stats, datasetFields = datasetFields, stat_chunks = stat_chunks, pagination=pagination)

def url_for_other_page(page):
    args = "http://localhost:9999/stats/datasets"
    args['page'] = page
    return url_for('index.html',**args)
app.jinja_env.globals['url_for_other_page'] = url_for_other_page

def chunks(l, n):
    n = max(1, n)
    return [l[i:i + n] for i in range(0, len(l), n)]

def chkNotNoneAndNotEmpty(value, name):
    print "value", value
    if value is None or len(value) == 0:
        raise ValueError("{} cannot be empty".format(name))
'''
@app.route("/")
def index():
    stats = client.fetch_dataset_statistics()
    datasetFields = client.fetch_dataset_fields()
    # break it up into chunks of 3
    stat_chunks = chunks(stats, 3)
    limit = 9
    return render_template("index.html", stats = stats, datasetFields = datasetFields, stat_chunks = stat_chunks)
'''

@app.route("/stats/")
def stats_page():
    stats = client.fetch_dataset_statistics()
    # break it up into chunks of 3
    stat_chunks = chunks(stats, 3)
    return render_template("stats.html", stats=stats, stat_chunks = stat_chunks)

@app.route("/record_statistics/<string:dataset>")
def record_stats_page(dataset):
    valid, stats = client.fetch_record_statistics(dataset)
    if valid:
        return render_template("record_statistics.html", stats=stats["stats"], dataset = dataset)
    else:
        return render_template("record_statistics.html", error = stats)

@app.route("/add_record/<string:dataset>")
def add_record_page(dataset):
    return render_template("add_record.html", dataset=dataset)

@app.route("/add_dataset")
def add_dataset_page():
    return render_template("add_dataset.html")

@app.route("/post_record/<string:dataset>", methods = ["POST"])
def post_record(dataset):
    stats = client.fetch_dataset_statistics()
    try:
        data = request.form
        print data
        keys = ["ID"]
        for key in keys:
            chkNotNoneAndNotEmpty(data.get(key), key)

        contentKeys = filter(lambda key: key.startswith("content-"), data.keys())
        content = {}
        for key in contentKeys:
            content[key.split("-")[1]] = data[key]

        record = {
            "id" : data["ID"],
            "label" : data.get("label"),
            "content" : content
        }

        valid, status  =  client.save_record(record, dataset)
        if valid:
            return render_template("add_record.html", dataset=dataset, success="Record added successfully", errno=None)
        else:
            return render_template("add_record.html", error=status["message"], errno=status["statusCode"])
            
    except ValueError, e:
        return render_template("add_record.html", dataset=dataset,  stats=stats, error=e.message)


# code for adding new record
@app.route("/post_dataset", methods=["POST"])
def post_dataset():
    try:
        data = request.form
        print data
        keys = ["DatasetName"]
        for key in keys:
            chkNotNoneAndNotEmpty(data.get(key), key)

    #   if key in keys:
    #       if key is None or len(key) == 0:
    #              raise ValueError("{} cannot be empty".format(key))

        new_dataset = {
            "name" : data["DatasetName"]
        }

        if client.save_dataset(new_dataset):
            return render_template("add_dataset.html", success="Dataset added successfully")
        else:
            return render_template("add_dataset.html", error="Some error occurred while trying to create new")

    except ValueError, e:
            return render_template("add_dataset.html", error=e.message)


@app.route("/records/<string:dataset>/<int:skip>/<int:limit>")
def records(dataset, skip, limit):
    valid, data =  client.get_records(dataset, skip, limit)
    if valid:
        records = data["records"]
        return render_template("records.html", dataset=dataset, records=records)
    else:
        # TODO
        return render_template("records.html", dataset=dataset, records=[], error="Some error occurred")

if __name__ == "__main__":
    app.run(debug=True)
