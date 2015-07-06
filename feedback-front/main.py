from flask import Flask
from flask import render_template
import requests

app = Flask(__name__)

STATS_RESOURCE = "http://localhost:9999/stats/datasets"



def chunks(l, n):
    n = max(1, n)
    return [l[i:i + n] for i in range(0, len(l), n)]

@app.route("/")
def index():
    return render_template("index.html")


@app.route("/stats/")
def stats_page():
    stats = requests.get(STATS_RESOURCE).json()["datasetStatistics"]

    # break it up into chunks of 3
    stat_chunks = chunks(stats, 3)
    return render_template("stats.html", stats=stats, stat_chunks = stat_chunks)


if __name__ == "__main__":
    app.run(debug=True)
