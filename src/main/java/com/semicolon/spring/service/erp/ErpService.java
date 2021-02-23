package com.semicolon.spring.service.erp;

import com.semicolon.spring.dto.ErpDTO;

import java.io.IOException;
import java.net.MalformedURLException;

public interface ErpService {
    ErpDTO.Supply supply(ErpDTO.Url url) throws IOException;
}
