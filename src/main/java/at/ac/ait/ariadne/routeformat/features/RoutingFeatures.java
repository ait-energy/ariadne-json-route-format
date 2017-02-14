package at.ac.ait.ariadne.routeformat.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.Validatable;

/**
 * Defines all features supported by a routing service.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class RoutingFeatures implements Validatable {

    private String currentLanguage;
    private List<String> supportedLanguages = new ArrayList<>();
    private String name;
    private Optional<String> url = Optional.empty();
    private Optional<String> logoUrl = Optional.empty();
    private Optional<String> credits = Optional.empty();
    private List<Site> sites = new ArrayList<>();
    private Map<String, Object> additionalInfo = new TreeMap<>();

    // -- getters

    /**
     * @return the language used to describe the {@link RoutingFeatures} in the
     *         form of an IETF BCP 47 language tag string (see
     *         {@link Locale#forLanguageTag(String)})
     */
    @JsonProperty(required = true)
    public String getCurrentLanguage() {
        return currentLanguage;
    }

    /**
     * @return the supported languages, e.g. for navigation instructions, in the
     *         form of IETF BCP 47 language tag strings (see
     *         {@link Locale#forLanguageTag(String)}). (optional)
     */
    public List<String> getSupportedLanguages() {
        return supportedLanguages;
    }

    /**
     * @return a human-readable (potentially internationalized) name of the
     *         routing service
     */
    @JsonProperty(required = true)
    public String getName() {
        return name;
    }

    /**
     * @return a URL e.g. to the website of the research project this routing
     *         service is part of
     */
    public Optional<String> getUrl() {
        return url;
    }

    /**
     * @return a URL e.g. the logo of the research project this routing service
     *         is part of
     */
    public Optional<String> getLogoUrl() {
        return logoUrl;
    }

    /**
     * @return credits / copyright / usage information about this service (plain
     *         text or HTML)
     */
    public Optional<String> getCredits() {
        return credits;
    }

    /**
     * @return all {@link Site}s supported by this service
     */
    @JsonProperty(required = true)
    public List<Site> getSites() {
        return sites;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    // -- setters

    public RoutingFeatures setCurrentLanguage(String currentLanguage) {
        this.currentLanguage = currentLanguage;
        return this;
    }

    public RoutingFeatures setSupportedLanguages(List<String> supportedLanguages) {
        this.supportedLanguages = new ArrayList<>(supportedLanguages);
        return this;
    }

    public RoutingFeatures setName(String name) {
        this.name = name;
        return this;
    }

    public RoutingFeatures setUrl(String url) {
        this.url = Optional.ofNullable(url);
        return this;
    }

    public RoutingFeatures setLogoUrl(String logoUrl) {
        this.logoUrl = Optional.ofNullable(logoUrl);
        return this;
    }

    public RoutingFeatures setCredits(String credits) {
        this.credits = Optional.ofNullable(credits);
        return this;
    }

    public RoutingFeatures setSites(List<Site> sites) {
        this.sites = new ArrayList<>(sites);
        return this;
    }

    public RoutingFeatures setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = new TreeMap<>(additionalInfo);
        return this;
    }

    // --

    public static RoutingFeatures createMinimal(String currentLanguage, String name, List<Site> sites) {
        return new RoutingFeatures().setCurrentLanguage(currentLanguage).setName(name).setSites(sites);
    }

    @Override
    public void validate() {
        Preconditions.checkArgument(currentLanguage != null, "currentLanguage is mandatory but missing");
        Preconditions.checkArgument(name != null, "name is mandatory but missing");
    }

    @Override
    public String toString() {
        return "RoutingFeatures [currentLanguage=" + currentLanguage + ", supportedLanguages=" + supportedLanguages
                + ", name=" + name + ", url=" + url + ", logoUrl=" + logoUrl + ", credits=" + credits + ", sites="
                + sites + ", additionalInfo=" + additionalInfo + "]";
    }

}
