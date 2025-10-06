package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.enums.InteractionType;
import com.freelancemarketplace.backend.recommandation.InteractionService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/interaction", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class InteractionController {
    private final InteractionService interactionService;

    @PostMapping("/freelancer/{freelancerId}/project/{projectId}")
    public ResponseEntity<Void>logInteraction(@PathVariable Long freelancerId,
            @PathVariable Long projectId,
            @RequestParam InteractionType type){
        interactionService.logInteraction(freelancerId, projectId, type);
        return ResponseEntity.ok().build();
    }

}
