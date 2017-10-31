package at.ac.ait.ariadne.routeformat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.common.base.Optional;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.Constants.Status;
import at.ac.ait.ariadne.routeformat.util.Utils;

/**
 * A {@link RoutingResponse} encapsulates the response of routing services. It
 * contains, amongst others, an optional {@link RoutingRequest}, a status, and
 * of course one or more {@link Route}(s).
 * <p>
 * Since most attributes are mandatory no <code>createMinimal</code> method is
 * offered.
 * <p>
 * The coordinate reference system is not directly set in the GeoJSON elements
 * (as the specification would allow) because this would lead to unnecessarily
 * big file sizes. Instead the CRS can optionally be set with the attribute
 * {@link RoutingResponse#getCoordinateReferenceSystem()}.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class RoutingResponse implements Validatable {

    private String routeFormatVersion;
    private String requestId;
    private ZonedDateTime processedTime;
    private Status status;
    private Optional<String> debugMessage = Optional.absent();
    private String coordinateReferenceSystem;
    private Optional<RoutingRequest> request = Optional.absent();
    private List<Route> routes = new ArrayList<>();
    private Map<String, Object> additionalInfo = new TreeMap<>();

    // -- getters

    @JsonProperty(required = true)
    public String getRouteFormatVersion() {
        return routeFormatVersion;
    }

    @JsonProperty(required = true)
    public String getRequestId() {
        return requestId;
    }

    /**
     * Time when request / calculations were finished or deemed not possible in
     * case of an error.
     */
    @JsonProperty(required = true)
    public String getProcessedTime() {
        return processedTime.toString();
    }

    @JsonIgnore
    public ZonedDateTime getProcessedTimeAsZonedDateTime() {
        return processedTime;
    }

    @JsonProperty(required = true)
    public Status getStatus() {
        return status;
    }

    /** debug message explaining errors */
    public Optional<String> getDebugMessage() {
        return debugMessage;
    }

    /** in the form of EPSG:*, e.g. EPSG:4326 */
    @JsonProperty(required = true)
    public String getCoordinateReferenceSystem() {
        return coordinateReferenceSystem;
    }

    /**
     * @return The original request used to calculate the route(s). It is
     *         guaranteed that if at least one route is returned there is also a
     *         request here. The request will only be omitted if the request
     *         itself could not be created due to invalid request parameters.
     */
    public Optional<RoutingRequest> getRequest() {
        return request;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    @JsonProperty(required = true)
    public List<Route> getRoutes() {
        return routes;
    }

    // -- setters

    public RoutingResponse setRouteFormatVersion(String routeFormatVersion) {
        this.routeFormatVersion = routeFormatVersion;
        return this;
    }

    public RoutingResponse setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public RoutingResponse setProcessedTimeNow() {
        this.processedTime = ZonedDateTime.now();
        return this;
    }

    @JsonIgnore
    public RoutingResponse setProcessedTime(ZonedDateTime processedTime) {
        this.processedTime = processedTime;
        return this;
    }

    @JsonProperty
    public RoutingResponse setProcessedTime(String processedTime) {
        this.processedTime = Utils.parseZonedDateTime(processedTime, "processedTime");
        return this;
    }

    public RoutingResponse setStatus(Status status) {
        this.status = status;
        return this;
    }

    public RoutingResponse setDebugMessage(String debugMessage) {
        this.debugMessage = Optional.fromNullable(debugMessage);
        return this;
    }

    public RoutingResponse setCoordinateReferenceSystem(String coordinateReferenceSystem) {
        this.coordinateReferenceSystem = coordinateReferenceSystem;
        return this;
    }

    public RoutingResponse setDefaultCoordinateReferenceSystem() {
        this.coordinateReferenceSystem = "EPSG:4326";
        return this;
    }

    public RoutingResponse setRequest(RoutingRequest request) {
        this.request = Optional.fromNullable(request);
        return this;
    }

    public RoutingResponse setRoutes(List<Route> routes) {
        this.routes = new ArrayList<>(routes);
        return this;
    }

    public RoutingResponse setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = new TreeMap<>(additionalInfo);
        return this;
    }

    // --

    // no createMinimal! see javadoc for explanation.

    @Override
    public void validate() {
        Preconditions.checkArgument(routeFormatVersion != null, "routeFormatVersion is mandatory but missing");
        Preconditions.checkArgument(requestId != null, "requestId is mandatory but missing");
        Preconditions.checkArgument(processedTime != null, "processedTime is mandatory but missing");
        Preconditions.checkArgument(status != null, "status is mandatory but missing");
        Preconditions.checkArgument(coordinateReferenceSystem != null,
                "coordinateReferenceSystem is mandatory but missing");
        Preconditions.checkArgument(coordinateReferenceSystem.startsWith("EPSG:"),
                "coordinateReferenceSystem must start with EPSG:");
        request.ifPresent(r -> r.validate());
        routes.forEach(r -> r.validate());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
        result = prime * result + ((coordinateReferenceSystem == null) ? 0 : coordinateReferenceSystem.hashCode());
        result = prime * result + ((debugMessage == null) ? 0 : debugMessage.hashCode());
        result = prime * result + ((processedTime == null) ? 0 : processedTime.hashCode());
        result = prime * result + ((request == null) ? 0 : request.hashCode());
        result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
        result = prime * result + ((routeFormatVersion == null) ? 0 : routeFormatVersion.hashCode());
        result = prime * result + ((routes == null) ? 0 : routes.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RoutingResponse other = (RoutingResponse) obj;
        if (additionalInfo == null) {
            if (other.additionalInfo != null)
                return false;
        } else if (!additionalInfo.equals(other.additionalInfo))
            return false;
        if (coordinateReferenceSystem == null) {
            if (other.coordinateReferenceSystem != null)
                return false;
        } else if (!coordinateReferenceSystem.equals(other.coordinateReferenceSystem))
            return false;
        if (debugMessage == null) {
            if (other.debugMessage != null)
                return false;
        } else if (!debugMessage.equals(other.debugMessage))
            return false;
        if (processedTime == null) {
            if (other.processedTime != null)
                return false;
        } else if (!processedTime.equals(other.processedTime))
            return false;
        if (request == null) {
            if (other.request != null)
                return false;
        } else if (!request.equals(other.request))
            return false;
        if (requestId == null) {
            if (other.requestId != null)
                return false;
        } else if (!requestId.equals(other.requestId))
            return false;
        if (routeFormatVersion == null) {
            if (other.routeFormatVersion != null)
                return false;
        } else if (!routeFormatVersion.equals(other.routeFormatVersion))
            return false;
        if (routes == null) {
            if (other.routes != null)
                return false;
        } else if (!routes.equals(other.routes))
            return false;
        if (status != other.status)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RoutingResponse [requestId=" + requestId + ", processedTime=" + processedTime + ", status=" + status
                + ", routes=" + routes.size() + "]";
    }
}
