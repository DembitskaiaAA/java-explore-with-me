package ru.practicum.explorewithme.common.checks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.model.*;
import ru.practicum.explorewithme.repository.*;

@Service
public class CheckExist {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private CompilationRepository compilationRepository;
    @Autowired
    private CommentRepository commentRepository;

    public Event checkEventOnExist(Integer id, String message) {
        return eventRepository.findById(id).orElseThrow(() -> new NotFoundException(message));
    }

    public Comment checkCommentOnExist(Integer id, String message) {
        return commentRepository.findById(id).orElseThrow(() -> new NotFoundException(message));
    }

    public Location checkLocationOnExist(Integer id, String message) {
        Event event = eventRepository.findById(id).get();
        if (event.getLocation() == null) {
            throw new NotFoundException(message);
        }
        return event.getLocation();
    }

    public Compilation checkCompilationOnExist(Integer id, String message) {
        return compilationRepository.findById(id).orElseThrow(() -> new NotFoundException(message));
    }

    public Category checkCategoryOnExist(Integer id, String message) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException(message));
    }

    public Request checkRequestOnExist(Integer id, String message) {
        return requestRepository.findById(id).orElseThrow(() -> new NotFoundException(message));
    }

    public User checkUserOnExist(Integer id, String message) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(message));
    }
}
