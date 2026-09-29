package molitor.springhealth.controller;

import jakarta.validation.Valid;
import molitor.springhealth.exercise.Exercise;
import molitor.springhealth.exercise.ExerciseGroupRepository;
import molitor.springhealth.exercise.ExerciseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Controller
@RequestMapping("/exercises")
public class ExerciseController {

    private static final Map<String, String> SORT_PROPERTIES = Map.of(
            "name", "name",
            "group", "exerciseGroup.name",
            "description", "description"
    );

    private final ExerciseRepository exerciseRepository;
    private final ExerciseGroupRepository exerciseGroupRepository;

    public ExerciseController(ExerciseRepository exerciseRepository, ExerciseGroupRepository exerciseGroupRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseGroupRepository = exerciseGroupRepository;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "name") String sort,
                        @RequestParam(defaultValue = "asc") String dir,
                        @RequestParam(required = false) String q,
                        Model model) {
        String sortProperty = SORT_PROPERTIES.getOrDefault(sort, "name");
        Sort.Direction direction = "desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));

        Page<Exercise> exercises = (q != null && !q.isBlank())
                ? exerciseRepository.findByNameContainingIgnoreCase(q.trim(), pageable)
                : exerciseRepository.findAll(pageable);

        model.addAttribute("exercises", exercises);
        model.addAttribute("q", q);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("size", size);
        return "exercises/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("exercise", new Exercise());
        model.addAttribute("exerciseGroups", exerciseGroupRepository.findAll());
        return "exercises/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("exercise") Exercise exercise, BindingResult bindingResult,
                          @RequestParam(name = "exerciseGroupId", required = false) Long exerciseGroupId,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("exerciseGroups", exerciseGroupRepository.findAll());
            return "exercises/form";
        }
        exercise.setExerciseGroup(exerciseGroupId != null ? exerciseGroupRepository.findById(exerciseGroupId).orElse(null) : null);
        exerciseRepository.save(exercise);
        return "redirect:/exercises";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("exercise", exercise);
        model.addAttribute("exerciseGroups", exerciseGroupRepository.findAll());
        return "exercises/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("exercise") Exercise exercise, BindingResult bindingResult,
                          @RequestParam(name = "exerciseGroupId", required = false) Long exerciseGroupId,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("exerciseGroups", exerciseGroupRepository.findAll());
            return "exercises/form";
        }
        exercise.setId(id);
        exercise.setExerciseGroup(exerciseGroupId != null ? exerciseGroupRepository.findById(exerciseGroupId).orElse(null) : null);
        exerciseRepository.save(exercise);
        return "redirect:/exercises";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        exerciseRepository.deleteById(id);
        return "redirect:/exercises";
    }

}
