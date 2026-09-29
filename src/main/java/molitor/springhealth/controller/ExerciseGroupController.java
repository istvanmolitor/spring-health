package molitor.springhealth.controller;

import jakarta.validation.Valid;
import molitor.springhealth.exercise.ExerciseGroup;
import molitor.springhealth.exercise.ExerciseGroupRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/exercise-groups")
public class ExerciseGroupController {

    private final ExerciseGroupRepository exerciseGroupRepository;

    public ExerciseGroupController(ExerciseGroupRepository exerciseGroupRepository) {
        this.exerciseGroupRepository = exerciseGroupRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("exerciseGroups", exerciseGroupRepository.findAll());
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
