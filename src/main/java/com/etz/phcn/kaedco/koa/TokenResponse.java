/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etz.phcn.kaedco.koa;

/**
 *
 * @author jennifer.okosisi
 */
public class TokenResponse {
    private String access_token;
	private String expires_in;
 	private String token_type;
 	private String scope;

    @Override
    public String toString() {
        return "TokenResponse{" + "access_token=" + access_token + ", expires_in=" + expires_in + ", token_type=" + token_type + ", scope=" + scope + '}';
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public TokenResponse(String access_token, String expires_in, String token_type, String scope) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.token_type = token_type;
        this.scope = scope;
    }
}
