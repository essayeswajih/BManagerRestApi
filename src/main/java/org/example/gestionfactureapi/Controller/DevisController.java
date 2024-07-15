package org.example.gestionfactureapi.Controller;

import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Service.DevisService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/vente/devis")
public class DevisController {
    private final DevisService devisService;
}
