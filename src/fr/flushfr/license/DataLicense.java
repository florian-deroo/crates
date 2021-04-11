package fr.flushfr.license;

public class DataLicense {
    private boolean license_valid;
    private boolean up_to_date;
    private boolean ip_authorized;
    private boolean ip_limit_reached;

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

}
