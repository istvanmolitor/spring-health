package molitor.springhealth.controller;

import jakarta.validation.Valid;
import molitor.springhealth.exercise.ExerciseGroup;
import molitor.springhealth.exercise.ExerciseGroupRepository;
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

@Controller
@RequestMapping("/exercise-groups")
public class ExerciseGroupController {

    private final ExerciseGroupRepository exerciseGroupRepository;

    public ExerciseGroupController(ExerciseGroupRepository exerciseGroupRepository) {
        this.exerciseGroupRepository = exerciseGroupRepository;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "name") String sort,
                        @RequestParam(defaultValue = "asc") String dir,
                        @RequestParam(required = false) String q,
                        Model model) {
        Sort.Direction direction = "desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "name"));

        Page<ExerciseGroup> exerciseGroups = (q != null && !q.isBlank())
                ? exerciseGroupRepository.findByNameContainingIgnoreCase(q.trim(), pageable)
                : exerciseGroupRepository.findAll(pageable);

        model.addAttribute("exerciseGroups", exerciseGroups);
        model.addAttribute("q", q);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("size", size);
        return "exercise-groups/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("exerciseGroup", new ExerciseGroup());
        return "exercise-groups/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("exerciseGroup") ExerciseGroup exerciseGroup, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "exercise-groups/form";
        }
        exerciseGroupRepository.save(exerciseGroup);
        return "redirect:/exercise-groups";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        ExerciseGroup exerciseGroup = exerciseGroupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("exerciseGroup", exerciseGroup);
        return "exercise-groups/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("exerciseGroup") ExerciseGroup exerciseGroup, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "exercise-groups/form";
        }
        exerciseGroup.setId(id);
        exerciseGroupRepository.save(exerciseGroup);
        return "redirect:/exercise-groups";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        exerciseGroupRepository.deleteById(id);
        return "redirect:/exercise-groups";
    }

}
