package dz.esi.cardiodata.entity;

import lombok.Data;

@Data
public class ECGJsonResult {


    private Double valueSaveTime;
    private Double value;

    public ECGJsonResult(Double value, Double valueSaveTime) {
        this.value = value;
        this.valueSaveTime= valueSaveTime;
    }
}
