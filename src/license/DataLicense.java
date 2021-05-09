package license;

public class DataLicense {
    private boolean license_valid = false;
    private boolean up_to_date = false;
    private boolean ip_authorized = false;
    private boolean ip_limit_reached = false;

    public boolean isLicense_valid() {
        return license_valid;
    }
    public boolean isUp_to_date() {
        return up_to_date;
    }
    public boolean isIp_authorized() {
        return ip_authorized;
    }
    public boolean isIp_limit_reached() {
        return ip_limit_reached;
    }

    public DataLicense setLicense_valid(boolean license_valid) {
        this.license_valid = license_valid;
        return this;
    }

    public DataLicense setUp_to_date(boolean up_to_date) {
        this.up_to_date = up_to_date;
        return this;
    }

    public DataLicense setIp_authorized(boolean ip_authorized) {
        this.ip_authorized = ip_authorized;
        return this;
    }

    public DataLicense setIp_limit_reached(boolean ip_limit_reached) {
        this.ip_limit_reached = ip_limit_reached;
        return this;
    }
}
