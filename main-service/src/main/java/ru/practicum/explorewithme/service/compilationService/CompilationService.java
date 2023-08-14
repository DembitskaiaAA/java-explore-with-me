package ru.practicum.explorewithme.service.compilationService;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.compilationDto.CompilationDto;
import ru.practicum.explorewithme.dto.compilationDto.NewCompilationDto;
import ru.practicum.explorewithme.dto.compilationDto.UpdateCompilationRequest;

import java.util.List;

@Service
public interface CompilationService {
    CompilationDto postCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Integer compId);

    CompilationDto updateCompilation(Integer compId, UpdateCompilationRequest updateCompilationRequest);

    List<CompilationDto> getCompilations(Boolean pinned, Pageable pageable);

    CompilationDto getCompilationById(Integer compId);
}
