package com.technical.technicalTest.service;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.AsyncResult;

public interface TraslateService {

    AsyncResult<ResponseEntity<String>> getTraslate(String msg, String lang);
}
