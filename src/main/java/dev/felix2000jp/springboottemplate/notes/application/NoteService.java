package dev.felix2000jp.springboottemplate.notes.application;

import dev.felix2000jp.springboottemplate.security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    private static final Logger log = LoggerFactory.getLogger(NoteService.class);

    private final SecurityService securityService;

    NoteService(SecurityService securityService) {
        this.securityService = securityService;
    }

}
