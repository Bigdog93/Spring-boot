package com.koreait.facebook_clone.security;

import javax.naming.AuthenticationException;

public class OAuth2AuthenticationProcessingException extends AuthenticationException {
    /*public OAuth2AuthenticationProcessingException(String msg, Throwable t) {
        super(msg, t);
    }*/

    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }
}