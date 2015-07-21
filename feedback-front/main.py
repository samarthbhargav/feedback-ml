from flask import Flask
from flask import render_template
from flask import request, flash
import json
import requests
from client import FeedBackClient


app = Flask(__name__)

client = FeedBackClient()


def chunks(l, n):
    n = max(1, n)
    return [l[i:i + n] for i in range(0, len(l), n)]

def chkNotNoneAndNotEmpty(value, name):
    print "value", value
    if value is None or len(value) == 0:
        raise ValueError("{} cannot be empty".format(name))

@app.route("/")
def index():
    stats = client.fetch_dataset_statistics()
    datasetFields = client.fetch_dataset_fields()
    # break it up into chunks of 3
    stat_chunks = chunks(stats, 3)
    return render_template("index.html", stats = stats, datasetFields = datasetFields, stat_chunks = stat_chunks)


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
            # TODO add more desc message
                if status['statusCode'] == 404:
                    return render_template("add_record.html", error=status["message"], errno=status["statusCode"])
                else:   
                    return render_template("add_record.html", dataset=dataset, error="Some Error Occurred", errno = None)

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
