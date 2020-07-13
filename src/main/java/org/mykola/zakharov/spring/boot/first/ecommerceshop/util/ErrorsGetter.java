package org.mykola.zakharov.spring.boot.first.ecommerceshop.util;

public class ErrorsGetter {
    public static String getException(Exception _ex) {
        String errorString = "";
        if (_ex.getMessage() == null) {
            String errorTrace = "";
            for (StackTraceElement el : _ex.getStackTrace()) {
                errorTrace += el.toString();
            }
            if (errorTrace.equals("")) {
            } else {
                errorString = errorTrace;
            }
        } else {
            Integer lineNumber = 0;
            String className = "";
            try {
                lineNumber = _ex.getStackTrace()[0].getLineNumber();
                className = _ex.getStackTrace()[0].getClassName();
            } catch (Exception ex) {
                try {
                    lineNumber = _ex.getCause().getStackTrace()[0].getLineNumber();
                    className = _ex.getCause().getStackTrace()[0].getClassName();
                } catch (Exception e) {
                }
            }
            errorString = _ex.getMessage() + "line: " + lineNumber + "; class: " + className;
        }
        return errorString;
    }
}
