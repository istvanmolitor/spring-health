package molitor.springhealth.exercise;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ExerciseSeeder implements CommandLineRunner {

    private final ExerciseGroupRepository exerciseGroupRepository;
    private final ExerciseRepository exerciseRepository;

    public ExerciseSeeder(ExerciseGroupRepository exerciseGroupRepository, ExerciseRepository exerciseRepository) {
        this.exerciseGroupRepository = exerciseGroupRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public void run(String... args) {
        if (exerciseGroupRepository.count() > 0 || exerciseRepository.count() > 0) {
            return;
        }

        seedGroup("Hát gyakorlatok",
                "Lehúzás", "Evezés", "Egykaros evezés", "Felhúzás (húzódzkodás)",
                "Hyperextension", "Törzsdöntés (deadlift)", "Fekvő evezés", "Egykaros kábeles húzás");

        seedGroup("Mell gyakorlatok",
                "Fekvenyomás", "Ferde fekvenyomás", "Mellhúzó gép", "Mellprés gép (pec deck)",
                "Fekvőtámasz", "Mell dip", "Kábeles keresztezés", "Súlyzós fekvenyomás");

        seedGroup("Váll gyakorlatok",
                "Vállból nyomás", "Oldalemelés", "Elölemelés", "Hátsó vállemelés",
                "Arnold nyomás", "Vonás állva (upright row)", "Kábeles oldalemelés");

        seedGroup("Kar gyakorlatok",
                "Bicepsz hajlítás rúddal", "Kalapács hajlítás", "Koncentrációs hajlítás",
                "Tricepsz nyomás kötéllel", "Francia nyomás", "Tricepsz dip", "Fekvő tricepsz nyomás");

        seedGroup("Láb gyakorlatok",
                "Guggolás", "Lábtolás (leg press)", "Lábnyújtó gép", "Lábhajlító gép",
                "Kitörés", "Vádliemelés", "Bolgár kitörés", "Román felhúzás (RDL)");

        seedGroup("Has és törzs gyakorlatok",
                "Felülés", "Plank", "Hasprés gépben", "Orosz csavarás",
                "Lábemelés függeszkedésben", "Oldalplank", "Bicikli crunch");

        seedGroup("Kardió gyakorlatok",
                "Futás", "Kerékpározás", "Evezőgép", "Ugrókötelezés",
                "Elliptikus tréner", "Úszás", "Lépcsőzőgép");

        seedGroup("Nyújtás és mobilitás gyakorlatok",
                "Statikus nyújtás", "Dinamikus bemelegítés", "Csípőnyitó gyakorlat",
                "Foam rolling", "Vállkör nyújtás", "Hamstring nyújtás");
    }

    private void seedGroup(String groupName, String... exerciseNames) {
        ExerciseGroup group = exerciseGroupRepository.save(new ExerciseGroup(groupName));
        for (String exerciseName : exerciseNames) {
            Exercise exercise = new Exercise();
            exercise.setName(exerciseName);
            exercise.setExerciseGroup(group);
            exerciseRepository.save(exercise);
        }
    }

}
