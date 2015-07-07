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
    # break it up into chunks of 3
    stat_chunks = chunks(stats, 3)
    return render_template("index.html", stats = stats, stat_chunks = stat_chunks)


@app.route("/stats/")
def stats_page():
    stats = client.fetch_dataset_statistics()
    # break it up into chunks of 3
    stat_chunks = chunks(stats, 3)
    return render_template("stats.html", stats=stats, stat_chunks = stat_chunks)

@app.route("/record_statistics/<string:dataset>")
def record_stats_page(dataset):
    stats = [{ "label" : "Unlabelled", "numberOfRecords" : 102 },{ "label" : "A", "numberOfRecords" : 343 }, { "label" : "B", "numberOfRecords" : 233 }]
    stat_chunks = chunks(stats, 3)
    return render_template("record_statistics.html", stats=stats, dataset = dataset, stat_chunks = stat_chunks)


@app.route("/add_record")
def add_record_page():
    return render_template("add_record.html")

@app.route("/post_record", methods = ["POST"])
def post_record():
    try:
        data = request.form
        print data
        keys = ["ID", "dataset"]
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

        if client.save_record(record, data["dataset"]):
            return render_template("add_record.html", success="Record added successfully")
        else:
            # TODO add more desc message
            return render_template("add_record.html", error="Some error occurred")

    except ValueError, e:
        return render_template("add_record.html", error=e.message)

@app.route("/records/<string:dataset>/<int:skip>/<int:limit>")
def records(dataset, skip, limit):
    valid, data =  client.get_records(dataset, skip, limit)
    if valid:
        records = data["records"]
        return render_template("records.html", records=records)
    else:
        # TODO
        return render_template("records.html", records=[], error="Some error occurred")


if __name__ == "__main__":
    app.run(debug=True)
