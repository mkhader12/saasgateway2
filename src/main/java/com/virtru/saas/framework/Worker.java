package com.virtru.saas.framework;


public interface Worker extends IdComponent {
     void performWork(Object requestObject);
}
