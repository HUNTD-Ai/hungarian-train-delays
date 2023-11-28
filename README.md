# Hungarian Train delay analysis and prediction

*This project is aimed at providing statistics and predictions about hungarian train delays. It is available online now on [huntd.debreczeni.eu](huntd.debreczeni.eu)*

## Motivation

As frequent users of public transport systems, we find delays inconvenient. However delays are to some degree unavoidable. No system can operate without mistakes, let alone one that spans a whole country with countless physical locations. Inherent difficulties aside, we have noticed patterns in these delays, just by using them frequently. This got us wandering if we could avoid the routes or trains that are consistently delayed. This lead us on a journey to create a model that could predict these delays.

To develop the model, we had to dig deep into the data, which gave us unique insight into the realities of train delays. We decided to incorporate some of these enlightening statistics into our application. Although our inital motivation was selfish, we hope to provide our users (and maybe even the infrastructure operators) these same insights, to better understand the problems and hopefully be able to identify the potential weakpoints of the system.

## Provided features

Our application provides a rich featureset for our users, so they make the most informed decision possible:

- **delay predicion**:
    Our model tries to identify trains that will be delayed. A train is considered to be delayed if it is running at least 5 minutes behind schedule.
- **delay cause prediction**:
    If a train is identified as likely to be delayed, we provide the most
    likely reason for the delay. Based on this, our users can decide if
    it is a cause for concern.
- **live train data**:
    By selecting the train number and the route, our users are able to get
    the latest information about currently travelling trains.
- **monthly mean delays**:
    Some months we observed consistently higher mean delays, most likely due to
    the weather. Using this information, railway operators and users alike can
    compare and judge the punctuality of service. Authorities might use this
    metric to measure the impact of their efforts or to identify regressions.
- **total delay per month**:
    This metric helps contextualize the scale of the problem and provides
    better ground for interpreting the monthly mean delay metric.
- **highest daily delays in the given time period**:
    Users of our application can select a time period, where they can observe
    the highest daily delays. Altough these might be one-off cases and not the
    cause of systemic problems, it is important to keep an eye on this metric
    for better handling of these incidents.
- **mean delays per route**:
    This metric is immensely helpful for finding outlier routes. Observing high
    delays on any individual route should be a cause for alarm both for
    passengers and authorities. With the help of this metric, the former can
    decide to take alternative routes while the latter can issue fixes where
    they are most needed.

All of this complex functionality is packed into our intuitive UI, so no matter your background, you will be able to drill down right into the data:

**Home page**

The Home page is the first page, where the user lands when first using our application. This page is also the prediction page. The user has to fill out the form to find out the predictions. The select boxes with From and To labels are the departure and arrival stations of the train. After filling them out and selecting the date. A timetable for the selected train becomes available and the prediction can be made by selecting one of them.

<div style="text-align: center">
    <img src="https://hackmd.io/_uploads/HkZMGYfra.png" alt="home page" width="600"/>
</div>

<p style="text-align: center">
    <i>
    Home page
    </i>
</p>

<div style="text-align: center">
    <img src="https://hackmd.io/_uploads/ry0-U9zSp.png" alt="prediction results" width="600"/>
</div>

<p style="text-align: center">
    <i>
    Prediction results
    </i>
</p>

**Statistics page**

The statistics page contains four charts about the whole railway system and an interactive one for viewing route-specific statistics.

<div style="text-align: center">
    <img src="https://hackmd.io/_uploads/H1_E6OzSp.png" alt="statistics page" width="600"/>
</div>

<p style="text-align: center">
    <i>
    Statistics page
    </i>
</p>

## Installation instructions

Here is a list of increasignly involved ways to use and install our app:

1. If you wish to use the website of our application, just head over to 
    [huntd.debreczeni.eu](huntd.debreczeni.eu), there is nothing to install.
2. If you'd like to use our mobile application for android, head over to our
    [github repository](https://github.com/HUNTD-Ai/hungarian-train-delays/)'s releases section, from where you can download our latest prebuilt apk. 
3. If you wish to self-host the backend contact me for a copy of the data at 
    debreczenim@edu.bme.hu. After you've set up a postgresql database with the
    data-dump, create a .env file with the your credentials in the following
    format:
    ```
    DB_DB=<your database's name>
    DB_HOST=<your database's url>
    DB_PORT=<your database's port>
    DB_USER=<your database's user>
    DB_PW=<your database's pasword>
    MAIN_REPLICA=1
    #only one replica should update the daily stats,
    #only set this to one on a single core api instance
    #if you are planning on a multi-replica deployment
    ```
    Now you can run the backend locally via docker:
    ```bash
    git clone https://github.com/HUNTD-Ai/hungarian-train-delays
    mv .env hungaian-train-delays/backend
    cd hungarian-train-delays/backend
    docker-compose -f docker-compose.yaml --env-file .env up -d
    ```
    **OpenAPI/swagger docs can be accessed on the running servers on /docs**
4. If you wish to self-host the frontend, you can do it by running the web app via docker: 
    ```bash
    cd hungarian-train-delays/web
    docker-compose -f docker-compose.yaml up -d
    ```
    The web app can be accessed on port `80`, but you can modify it in the frontend's `docker-compose.yaml` file.
    
    > The self-hosted frontend has some limitations. It uses our infrastructure by default. If you wish to change this, you should edit the constants in `hungarian-train-delays/web/src/apis/api-constants.ts`. Then you should rebuild the container.

    ```typescript
    export default class ApiConstants {
      static readonly CORE_BASE_URL = 'https://core.example.com';
      static readonly DELAY_PREDICTION_BASE_URL = 'https://delay-pred.example.com';
      static readonly DELAY_CAUSE_PREDICTION_BASE_URL = 'https://cause-pred.example.com';
    }
    ```
