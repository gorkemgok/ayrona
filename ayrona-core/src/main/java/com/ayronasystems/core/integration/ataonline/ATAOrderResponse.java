package com.ayronasystems.core.integration.ataonline;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by gorkemgok on 12/07/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ATAOrderResponse {

    @JsonProperty(value = "Success")
    private boolean successful;

    @JsonProperty(value = "Message")
    private String message;

    public boolean isSuccessful () {
        return successful;
    }

    public void setSuccessful (boolean successful) {
        this.successful = successful;
    }

    public String getMessage () {
        return message;
    }

    public void setMessage (String message) {
        this.message = message;
    }
}
