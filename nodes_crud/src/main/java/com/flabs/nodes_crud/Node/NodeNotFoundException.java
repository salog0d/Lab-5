package com.flabs.nodes_crud.Node;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NodeNotFoundException extends RuntimeException {

    public NodeNotFoundException(String message) {
        super(message);
    }
}
