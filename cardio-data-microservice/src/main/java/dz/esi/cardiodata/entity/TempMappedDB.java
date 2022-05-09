package dz.esi.cardiodata.entity;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Data;

import java.time.Instant;

@Measurement(name = "temperature")
@Data
public class TempMappedDB {

    @Column
    Double value;

    @Column(timestamp = true)
    Instant time;

}