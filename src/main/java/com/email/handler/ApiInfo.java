package com.email.handler;

public class ApiInfo {
	
    private String controller;
    private String httpMethod;
    private String url;
    private String requestBody;

    public ApiInfo(String controller, String httpMethod, String url, String requestBody) {
        this.controller = controller;
        this.httpMethod = httpMethod;
        this.url = url;
        this.requestBody = requestBody;
    }

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

    // getters
}
