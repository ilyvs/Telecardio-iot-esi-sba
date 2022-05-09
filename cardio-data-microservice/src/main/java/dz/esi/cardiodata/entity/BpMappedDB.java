package dz.esi.cardiodata.entity;


import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Data;

import java.time.Instant;



@Measurement(name = "bp")
@Data
public class BpMappedDB {

    @Column
    Integer systolic;

    @Column
    Integer diastolic;

    @Column
    Integer pulse;

    @Column(timestamp = true)
    Instant time;

}
