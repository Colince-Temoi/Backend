package tech.csm.wrappers;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.csm.domain.Location;

@Setter
@Getter
@ToString
@JacksonXmlRootElement(localName = "Locations")
public class Locations {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Location")
    private List<Location> locations;

    public Locations() {
        this.locations = new ArrayList<>();
    }

    public Locations(List<Location> locations) {
        this.locations = locations;
    }
}