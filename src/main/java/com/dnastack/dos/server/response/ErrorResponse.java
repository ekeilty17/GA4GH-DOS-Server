package com.dnastack.dos.server.response;

import lombok.Data;

@Data
//@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
//@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
public class ErrorResponse {

    private String msg;
	private int status;

    public ErrorResponse(int status) {
        this();
        this.status = status;
    }
    
    public ErrorResponse(int status, Throwable ex) {
        this();
        this.status = status;
        this.msg = "An unexpected error occurred.";
    }

    public ErrorResponse(String msg, int status, Throwable ex) {
        this();
        this.status = status;
        this.msg = msg;
    }

}

/*
class LowerCaseClassNameResolver extends TypeIdResolverBase {

    @Override
    public String idFromValue(Object value) {
        return value.getClass().getSimpleName().toLowerCase();
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return idFromValue(value);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
*/