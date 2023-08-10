package ru.practicum.explorewithme.shared;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.common.pagination.Pagination;
import ru.practicum.explorewithme.dto.compilationDto.CompilationDto;
import ru.practicum.explorewithme.service.compilationService.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/compilations")
public class SharedControllerCompilations {
    @Autowired
    private CompilationService compilationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("GET /compilations PARAMS pinned {}, from {}, size {}", pinned, from, size);
        return compilationService.getCompilations(pinned, Pagination.splitByPages(from, size));
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilationById(@PathVariable Integer compId) {
        log.info("GET /compilations/{}", compId);
        return compilationService.getCompilationById(compId);
    }


}
