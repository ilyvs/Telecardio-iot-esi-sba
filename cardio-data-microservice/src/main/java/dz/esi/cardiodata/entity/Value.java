package dz.esi.cardiodata.entity;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "systolic",
        "diastolic",
        "pulse"
})
@Generated("jsonschema2pojo")
public class Value {

    @JsonProperty("systolic")
    private Integer systolic;
    @JsonProperty("diastolic")
    private Integer diastolic;
    @JsonProperty("pulse")
    private Integer pulse;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("systolic")
    public Integer getSystolic() {
        return systolic;
    }

    @JsonProperty("systolic")
    public void setSystolic(Integer systolic) {
        this.systolic = systolic;
    }

    @JsonProperty("diastolic")
    public Integer getDiastolic() {
        return diastolic;
    }

    @JsonProperty("diastolic")
    public void setDiastolic(Integer diastolic) {
        this.diastolic = diastolic;
    }

    @JsonProperty("pulse")
    public Integer getPulse() {
        return pulse;
    }

    @JsonProperty("pulse")
    public void setPulse(Integer pulse) {
        this.pulse = pulse;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return '{' +
                "systolic=" + systolic +
                ", diastolic=" + diastolic +
                ", pulse=" + pulse +
                '}';
    }

}