package ru.practicum.explorewithme.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.compilationDto.CompilationDto;
import ru.practicum.explorewithme.dto.compilationDto.NewCompilationDto;
import ru.practicum.explorewithme.dto.compilationDto.UpdateCompilationRequest;
import ru.practicum.explorewithme.service.compilationService.CompilationService;

import javax.validation.Valid;

@RestController
@Slf4j
@Validated
@RequestMapping("/admin/compilations")
public class AdminControllerCompilations {
    @Autowired
    private CompilationService compilationService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto postCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("POST /admin/compilations BODY NewCompilationDto {}", newCompilationDto);
        return compilationService.postCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Integer compId) {
        log.info("DELETE /admin/compilations/{}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilation(@PathVariable Integer compId,
                                            @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("PATCH /admin/compilations/{} BODY UpdateCompilationRequest {}", compId, updateCompilationRequest);
        return compilationService.updateCompilation(compId, updateCompilationRequest);
    }
}
