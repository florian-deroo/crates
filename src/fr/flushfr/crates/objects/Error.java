package fr.flushfr.crates.objects;

public class Error {

    private String fileName;
    private String errorVariableName;
    private ErrorCategory errorCategory;
    private String errorSectionName;
    private ErrorType errorType;

    public Error(ErrorCategory errorCategory) {
        this.errorCategory = errorCategory;
    }


    public Error(ErrorCategory errorCategory, String fileName, String errorSectionName, String errorVariableName) {
        this.errorCategory = errorCategory;
        this.fileName = fileName;
        this.errorVariableName = errorVariableName;
        this.errorSectionName = errorSectionName;
    }

    public Error(ErrorCategory errorCategory, String fileName, String errorSectionName, String errorVariableName, ErrorType errorType) {
        this.errorCategory = errorCategory;
        this.fileName = fileName;
        this.errorVariableName = errorVariableName;
        this.errorSectionName = errorSectionName;
        this.errorType = errorType;
    }
    public Error () {}

    public String getFileName() {
        return fileName;
    }

    public String getVariableName() {
        return errorVariableName;
    }

    public String getErrorSectionName() {
        return errorSectionName;
    }

    public ErrorCategory getErrorCategory() {
        return errorCategory;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

}
