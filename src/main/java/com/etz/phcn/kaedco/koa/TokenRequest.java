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
public class TokenRequest {
    private String grant_type;
	private String client_id;
 	private String client_secret;

    @Override
    public String toString() {
        return "TokenRequest{" + "grant_type=" + grant_type + ", client_id=" + client_id + ", client_secret=" + client_secret + '}';
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public TokenRequest(String grant_type, String client_id, String client_secret) {
        this.grant_type = grant_type;
        this.client_id = client_id;
        this.client_secret = client_secret;
    }
	
}
