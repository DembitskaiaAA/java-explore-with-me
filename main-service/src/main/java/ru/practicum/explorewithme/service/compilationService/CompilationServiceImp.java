package ru.practicum.explorewithme.service.compilationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.common.checks.CheckExist;
import ru.practicum.explorewithme.dto.compilationDto.CompilationDto;
import ru.practicum.explorewithme.dto.compilationDto.NewCompilationDto;
import ru.practicum.explorewithme.dto.compilationDto.UpdateCompilationRequest;
import ru.practicum.explorewithme.mapper.compilationMapper.CompilationMapper;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.repository.CompilationRepository;
import ru.practicum.explorewithme.repository.EventRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CompilationServiceImp implements CompilationService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CompilationMapper compilationMapper;
    @Autowired
    private CompilationRepository compilationRepository;
    @Autowired
    private CheckExist checkExist;

    @Override
    public CompilationDto postCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        if (newCompilationDto.getEvents() != null) {
            Set<Event> events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
            compilation.setEvents(events);
        }
        compilation.setPinned(newCompilationDto.getPinned());
        compilation.setTitle(newCompilationDto.getTitle());

        Compilation savedCompilation = compilationRepository.save(compilation);
        return compilationMapper.transformCompilationToCompilationDto(savedCompilation);
    }

    @Override
    public void deleteCompilation(Integer compId) {
        Compilation compilation = checkExist.checkCompilationOnExist(compId, String.format("Error when deleting compilation: compilation with id: %s is missing", compId));
        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationDto updateCompilation(Integer compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = checkExist.checkCompilationOnExist(compId, String.format("Error when updating compilation: compilation with id: %s is missing", compId));
        Set<Integer> eventsIds = updateCompilationRequest.getEvents();
        if (eventsIds != null) {
            Set<Event> events = eventRepository.findAllByIdIn(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        return compilationMapper.transformCompilationToCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Pageable pageable) {
        Page<Compilation> compilationsPage;
        if (pinned != null) {
            compilationsPage = compilationRepository.findAllByPinned(pinned, pageable);
        } else {
            compilationsPage = compilationRepository.findAll(pageable);
        }
        return compilationsPage.getContent().stream().map(x -> compilationMapper.transformCompilationToCompilationDto(x)).collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Integer compId) {
        Compilation compilation = checkExist.checkCompilationOnExist(compId, String.format("Error when getting compilation: compilation with id: %s is missing", compId));
        return compilationMapper.transformCompilationToCompilationDto(compilation);
    }
}
