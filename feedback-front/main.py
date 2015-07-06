from flask import Flask
from flask import render_template
from flask import request, flash
import json
import requests

app = Flask(__name__)

STATS_RESOURCE = "http://localhost:9999/stats/datasets"
POST_RECORDS_RESOURCE = "http://localhost:9999/record/{}/"


JSON_HEADERS = {'Content-Type': 'application/json'}


def chunks(l, n):
    n = max(1, n)
    return [l[i:i + n] for i in range(0, len(l), n)]

def chkNotNoneAndNotEmpty(value, name):
    print "value", value
    if value is None or len(value) == 0:
        raise ValueError("{} cannot be empty".format(name))

@app.route("/")
def index():
    return render_template("index.html")


@app.route("/stats/")
def stats_page():
    stats = requests.get(STATS_RESOURCE).json()["datasetStatistics"]

    # break it up into chunks of 3
    stat_chunks = chunks(stats, 3)
    return render_template("stats.html", stats=stats, stat_chunks = stat_chunks)


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
        endpoint = POST_RECORDS_RESOURCE.format(data["dataset"])
        jsons = json.dumps(record)
        app.logger.info("posting data {} to endpoint {}".format(jsons, endpoint))

        resp = requests.post(endpoint, data=jsons, headers=JSON_HEADERS)
        print resp

        if resp.status_code == 200:
            return render_template("add_record.html", success="Record added successfully")
        else:
            # TODO add more desc message
            return render_template("add_record.html", error="Some error occurred")
    except ValueError, e:
        return render_template("add_record.html", error=e.message)




if __name__ == "__main__":
    app.run(debug=True)
