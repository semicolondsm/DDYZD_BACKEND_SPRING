package com.semicolon.spring.service.erp;

import com.semicolon.spring.dto.ErpDTO;

import java.io.IOException;

public interface ErpService {
    ErpDTO.Supply supply(ErpDTO.Url url) throws IOException;
}
