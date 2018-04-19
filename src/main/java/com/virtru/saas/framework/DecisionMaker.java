package com.virtru.saas.framework;


public interface DecisionMaker extends IdComponent {
     boolean canPerformWork(Object requestObject);
}

