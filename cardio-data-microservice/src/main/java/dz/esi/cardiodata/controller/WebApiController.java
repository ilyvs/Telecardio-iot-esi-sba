package dz.esi.cardiodata.controller;

import dz.esi.cardiodata.entity.ECGJsonResult;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("")
public class WebApiController {

    private String serverURL = "http://127.0.0.1:8086";
    private String databaseName = "test_db";
    private String username = "admin";
    private String password = "";

    /** Make connection to influxdb */
    InfluxDB influxDB = InfluxDBFactory.connect(serverURL, username, password);

    @GetMapping("get-ECG-Data")
    public List<ECGJsonResult> getECGData(){

        QueryResult queryResult = influxDB.query(new Query("SELECT * FROM ecg","test_db"));

        List<List<Object>> cols = new ArrayList<>();
        List<ECGJsonResult> ECGJsonResults = new ArrayList<>();

        for(QueryResult.Result result : queryResult.getResults())
            for(QueryResult.Series series : result.getSeries())
                cols = series.getValues();

        for (int i = 0; i <cols.size() ; i++) {
            ECGJsonResult ecg = new ECGJsonResult(Double.valueOf(cols.get(i).get(1).toString()),
                    Double.valueOf(cols.get(i).get(2).toString()));

            ECGJsonResults.add(ecg);
        }
        return ECGJsonResults;
    }



    @GetMapping("get-Bp-Data")
    public List<List<Object>> getBpData(){

        QueryResult queryResult = influxDB.query(new Query("SELECT * FROM bp","test_db"));

        List<List<Object>> cols = new ArrayList<>();
        for(QueryResult.Result result : queryResult.getResults())
            for(QueryResult.Series series : result.getSeries())
                cols = series.getValues();

        return cols;
    }


    @GetMapping("get-Temp-Data")
    public List<List<Object>> getTempData(){

        QueryResult queryResult = influxDB.query(new Query("SELECT * FROM temperature","test_db"));

        List<List<Object>> cols = new ArrayList<>();
        for(QueryResult.Result result : queryResult.getResults())
            for(QueryResult.Series series : result.getSeries())
                cols = series.getValues();


        return cols;
    }

}
