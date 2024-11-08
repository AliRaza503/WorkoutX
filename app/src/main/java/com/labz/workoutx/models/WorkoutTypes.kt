package com.labz.workoutx.models

import android.util.Log


object WorkoutTypes {
    val workoutTypes: Map<Goal, List<Workout>> = mapOf(
        Goal.LOSE_WEIGHT to listOf(
            Workout(
                name = "Running",
                description = "A high-intensity workout to boost cardiovascular health and burn calories.",
                targetMinutes = 30,
                benefit = "Improves endurance, burns calories, and strengthens legs.",
                steps = listOf(
                    "Warm up with a 5-minute walk.",
                    "Start running at a moderate pace.",
                    "Gradually increase your speed after 5 minutes.",
                    "Cool down with a 5-minute walk at the end."
                ),
                imagePath = "file:///android_asset/images/running.png"
            ),
            Workout(
                name = "Cycling",
                description = "A low-impact exercise that strengthens the lower body and improves stamina.",
                targetMinutes = 40,
                benefit = "Burns calories, strengthens leg muscles, and improves cardiovascular health.",
                steps = listOf(
                    "Adjust the bike seat to a comfortable height.",
                    "Start cycling at a moderate pace.",
                    "Gradually increase your speed after 10 minutes.",
                    "Cool down by slowing your pace for the last 5 minutes."
                ),
                imagePath = "file:///android_asset/images/cycling.png"
            ),
            Workout(
                name = "Jumping Jacks",
                description = "A full-body exercise that boosts your heart rate and helps burn calories.",
                targetMinutes = 10,
                benefit = "Improves cardiovascular health and enhances agility.",
                steps = listOf(
                    "Stand with your feet together and arms by your sides.",
                    "Jump up while spreading your legs and raising your arms overhead.",
                    "Return to the starting position.",
                    "Repeat the movement continuously."
                ),
                imagePath = "file:///android_asset/images/jumping_jacks.png"
            ),
            Workout(
                name = "Burpees",
                description = "A full-body exercise that builds strength and cardiovascular endurance.",
                targetMinutes = 15,
                benefit = "Enhances full-body strength, burns calories, and boosts cardio fitness.",
                steps = listOf(
                    "Stand with your feet shoulder-width apart.",
                    "Lower into a squat position with your hands on the floor.",
                    "Kick your feet back to a plank position.",
                    "Jump your feet back towards your hands and stand up."
                ),
                imagePath = "file:///android_asset/images/burpees.png"
            ),
            Workout(
                name = "Jump Rope",
                description = "A simple cardio workout that improves coordination and burns calories.",
                targetMinutes = 15,
                benefit = "Burns calories, improves footwork, and strengthens leg muscles.",
                steps = listOf(
                    "Hold the jump rope handles at each side.",
                    "Swing the rope over your head and jump as it reaches your feet.",
                    "Land on the balls of your feet.",
                    "Continue jumping at a steady rhythm."
                ),
                imagePath = "file:///android_asset/images/jump_rope.png"
            ),
            Workout(
                name = "Rowing",
                description = "A low-impact workout that engages the entire body for strength and endurance.",
                targetMinutes = 30,
                benefit = "Builds strength, improves cardio fitness, and tones the back and arms.",
                steps = listOf(
                    "Sit on the rowing machine with your feet strapped in. (Or tie a knot of a flexible band to a fixed object and sit on a chair)",
                    "Grab the handle and push with your legs first.",
                    "Lean back slightly and pull the handle towards your chest.",
                    "Extend your arms and bend your knees to return to the starting position."
                ),
                imagePath = "file:///android_asset/images/rowing.png"
            ),
            Workout(
                name = "Mountain Climbers",
                description = "A dynamic exercise that strengthens the core and boosts cardio.",
                targetMinutes = 10,
                benefit = "Engages the core, shoulders, and legs, and burns calories quickly.",
                steps = listOf(
                    "Start in a plank position with your arms straight.",
                    "Bring one knee towards your chest, then quickly switch legs.",
                    "Continue alternating legs at a fast pace.",
                    "Keep your core engaged throughout the movement."
                ),
                imagePath = "file:///android_asset/images/mountain_climbers.png"
            ),
            Workout(
                name = "High-Knees",
                description = "A high-intensity cardio exercise that increases your heart rate and agility.",
                targetMinutes = 5,
                benefit = "Boosts heart rate, improves coordination, and strengthens the lower body.",
                steps = listOf(
                    "Stand with feet hip-width apart.",
                    "Lift one knee toward your chest, then quickly switch legs.",
                    "Continue lifting knees as high as possible in a running motion.",
                    "Keep your back straight and core engaged."
                ),
                imagePath = "file:///android_asset/images/high_knees.png"
            ),
            Workout(
                name = "Box Jumps",
                description = "A plyometric exercise that builds lower body strength and power.",
                targetMinutes = 10,
                benefit = "Strengthens legs, improves explosive power, and enhances coordination.",
                steps = listOf(
                    "Stand in front of a sturdy box or platform.",
                    "Bend your knees and jump onto the box with both feet.",
                    "Stand up straight on top of the box, then step down carefully.",
                    "Repeat the jump, maintaining control."
                ),
                imagePath = "file:///android_asset/images/box_jumps.png"
            ),
            Workout(
                name = "Sprints",
                description = "A high-intensity cardio workout that improves speed and endurance.",
                targetMinutes = 15,
                benefit = "Enhances cardiovascular fitness, burns calories, and builds leg strength.",
                steps = listOf(
                    "Start with a 5-minute warm-up jog.",
                    "Sprint at full speed for 30 seconds.",
                    "Recover by walking or jogging for 1 minute.",
                    "Repeat the sprint and recovery sequence for the desired time."
                ),
                imagePath = "file:///android_asset/images/sprints.png",
//                stepsAndTargetSecondsOfEach = listOf(
//                    Pair("Warm-up Jog", 5 * 60),
//                    Pair("Sprint", 30),
//                    Pair("Recovery", 60)
//                ),
            ),
            Workout(
                name = "Speed Skaters",
                description = "A lateral movement exercise that improves balance and agility.",
                targetMinutes = 10,
                benefit = "Strengthens legs, enhances lateral movement, and boosts cardio.",
                steps = listOf(
                    "Start with feet together, then jump to the side landing on one foot.",
                    "Bring the other foot behind you.",
                    "Alternate jumping from side to side.",
                    "Maintain balance and keep your core engaged."
                ),
                imagePath = "file:///android_asset/images/speed_skaters.png"
            ),
            Workout(
                name = "Plank",
                description = "A core exercise that strengthens the abdominal muscles and improves posture.",
                targetMinutes = 3,
                benefit = "Builds core strength, improves balance, and supports spinal health.",
                steps = listOf(
                    "Start in a push-up position with forearms on the ground.",
                    "Engage your core and hold a straight line from head to heels.",
                    "Keep your body stable and breathe deeply.",
                    "Increase duration as you get stronger."
                ),
                imagePath = "file:///android_asset/images/plank.png"
            ),
            Workout(
                name = "Leg Raises",
                description = "An abdominal exercise that targets the lower abs and hip flexors.",
                targetMinutes = 5,
                benefit = "Strengthens the core, improves hip flexibility, and tones the lower abs.",
                steps = listOf(
                    "Lie on your back with legs straight and arms by your sides.",
                    "Lift your legs towards the ceiling, keeping them straight.",
                    "Lower your legs back down without touching the ground.",
                    "Repeat for the desired number of reps."
                ),
                imagePath = "file:///android_asset/images/leg_raises.png"
            )
        ),
        Goal.GAIN_WEIGHT to listOf(
            Workout(
                name = "Deadlifts",
                description = "A powerful weightlifting exercise targeting multiple muscle groups.",
                targetMinutes = 15,
                benefit = "Builds strength in the legs, back, and core.",
                steps = listOf(
                    "Stand with feet hip-width apart and barbell in front of you.",
                    "Bend your knees, keep your back straight, and grab the barbell.",
                    "Lift the barbell by straightening your legs and back.",
                    "Lower the barbell slowly back to the ground."
                ),
                imagePath = "file:///android_asset/images/deadlifts.png"
            ),
            Workout(
                name = "Bench Press",
                description = "A compound exercise targeting the chest, shoulders, and triceps.",
                targetMinutes = 15,
                benefit = "Builds upper body strength, focusing on the chest and shoulders.",
                steps = listOf(
                    "Lie back on a flat bench with feet firmly on the ground.",
                    "Grip the barbell with hands slightly wider than shoulder-width.",
                    "Lower the barbell to your chest, keeping elbows at a 45-degree angle.",
                    "Push the barbell back up until your arms are straight."
                ),
                imagePath = "file:///android_asset/images/bench_press.png"
            ),
            Workout(
                name = "Pull-ups",
                description = "An upper body exercise that strengthens the back and biceps.",
                targetMinutes = 5,
                benefit = "Builds strength in the back, shoulders, and arms.",
                steps = listOf(
                    "Grab the pull-up bar with an overhand grip, hands shoulder-width apart.",
                    "Pull your body up until your chin is above the bar.",
                    "Lower yourself back down with control.",
                    "Repeat for the desired number of reps."
                ),
                imagePath = "file:///android_asset/images/pull_ups.png"
            ),
            Workout(
                name = "Squats",
                description = "A compound lower body exercise focusing on legs and core.",
                targetMinutes = 15,
                benefit = "Strengthens legs, glutes, and core muscles.",
                steps = listOf(
                    "Stand with feet shoulder-width apart.",
                    "Lower your body by bending your knees and hips as if sitting back.",
                    "Keep your chest up and knees over your toes.",
                    "Return to standing by pushing through your heels."
                ),
                imagePath = "file:///android_asset/images/squats.png"
            ),
            Workout(
                name = "Push-ups",
                description = "A basic bodyweight exercise targeting the chest, shoulders, and triceps.",
                targetMinutes = 10,
                benefit = "Builds upper body strength and core stability.",
                steps = listOf(
                    "Start in a plank position with hands slightly wider than shoulder-width.",
                    "Lower your body until your chest is close to the ground.",
                    "Push back up to the starting position.",
                    "Keep your body in a straight line throughout."
                ),
                imagePath = "file:///android_asset/images/push_ups.png"
            ),
            Workout(
                name = "Overhead Press",
                description = "A shoulder exercise that improves upper body strength and stability.",
                targetMinutes = 10,
                benefit = "Strengthens shoulders and triceps, enhancing upper body power.",
                steps = listOf(
                    "Stand with feet shoulder-width apart and hold the barbell at shoulder height.",
                    "Press the barbell overhead until arms are fully extended.",
                    "Lower the barbell back down to shoulder height with control.",
                    "Repeat for the desired number of reps."
                ),
                imagePath = "file:///android_asset/images/overhead_press.png"
            ),
            Workout(
                name = "Bent-Over Rows",
                description = "A back exercise that improves upper body strength and posture.",
                targetMinutes = 12,
                benefit = "Targets the back, shoulders, and core for balanced upper body strength.",
                steps = listOf(
                    "Stand with a barbell in front of you, feet shoulder-width apart.",
                    "Bend at the hips, keeping your back straight and knees slightly bent.",
                    "Pull the barbell towards your torso, squeezing your shoulder blades.",
                    "Lower the barbell back down with control."
                ),
                imagePath = "file:///android_asset/images/bent_over_row.png"
            ),
            Workout(
                name = "Dips",
                description = "A bodyweight exercise that targets the triceps, chest, and shoulders.",
                targetMinutes = 10,
                benefit = "Builds triceps, chest, and shoulder strength.",
                steps = listOf(
                    "Place your hands on parallel bars and lift yourself up.",
                    "Lower your body by bending your elbows until shoulders are just below elbows.",
                    "Push yourself back up to the starting position.",
                    "Keep your body stable throughout the movement."
                ),
                imagePath = "file:///android_asset/images/dips.png"
            ),
            Workout(
                name = "Bicep Curls",
                description = "An isolation exercise focusing on the biceps for arm strength.",
                targetMinutes = 8,
                benefit = "Builds bicep strength and muscle definition.",
                steps = listOf(
                    "Stand with a dumbbell in each hand, arms at your sides.",
                    "Curl the dumbbells towards your shoulders, keeping elbows stationary.",
                    "Lower the weights back to the starting position with control.",
                    "Repeat for the desired number of reps."
                ),
                imagePath = "file:///android_asset/images/bicep_curls.png"
            ),
            Workout(
                name = "Tricep Extensions",
                description = "An isolation exercise focusing on the triceps for arm strength.",
                targetMinutes = 8,
                benefit = "Strengthens the triceps for balanced arm development.",
                steps = listOf(
                    "Hold a dumbbell with both hands above your head.",
                    "Lower the dumbbell behind your head by bending your elbows.",
                    "Extend your arms back to the starting position.",
                    "Keep your upper arms stationary throughout the movement."
                ),
                imagePath = "file:///android_asset/images/tricep_extensions.png"
            ),
            Workout(
                name = "Dumbbell Rows",
                description = "A back exercise that improves upper body strength and stability.",
                targetMinutes = 10,
                benefit = "Targets the back, shoulders, and core.",
                steps = listOf(
                    "Place your left knee and hand on a bench for support, holding a dumbbell in your right hand.",
                    "Pull the dumbbell towards your torso, keeping your back straight.",
                    "Lower the dumbbell back down with control.",
                    "Switch sides and repeat."
                ),
                imagePath = "file:///android_asset/images/dumbbell_rows.png"
            ),
            Workout(
                name = "Chest Flyes",
                description = "An isolation exercise focusing on the chest muscles for strength and definition.",
                targetMinutes = 8,
                benefit = "Builds chest strength and enhances muscle definition.",
                steps = listOf(
                    "Lie on a bench holding a dumbbell in each hand above your chest.",
                    "Lower the weights out to the sides in an arc, keeping a slight bend in your elbows.",
                    "Bring the weights back up to the starting position.",
                    "Repeat for the desired number of reps."
                ),
                imagePath = "file:///android_asset/images/chest_fly.png"
            ),
            Workout(
                name = "Lateral Raises",
                description = "A shoulder exercise that enhances upper body width and strength.",
                targetMinutes = 8,
                benefit = "Strengthens shoulders and improves shoulder definition.",
                steps = listOf(
                    "Stand with a dumbbell in each hand, arms at your sides.",
                    "Raise your arms out to the sides until they reach shoulder height.",
                    "Lower the weights back down with control.",
                    "Keep a slight bend in your elbows throughout."
                ),
                imagePath = "file:///android_asset/images/lateral_raises.png"
            ),
            Workout(
                name = "Hammer Curls",
                description = "An arm exercise that targets the biceps and forearms.",
                targetMinutes = 8,
                benefit = "Builds arm and forearm strength for balanced upper body development.",
                steps = listOf(
                    "Hold a dumbbell in each hand with palms facing each other.",
                    "Curl the weights towards your shoulders.",
                    "Lower the weights back to the starting position.",
                    "Repeat for the desired number of reps."
                ),
                imagePath = "file:///android_asset/images/hammer_curls.png"
            )
        ),
        Goal.BUILD_MUSCLE to listOf(
            Workout(
                name = "Squats",
                description = "A compound exercise that primarily targets the lower body, especially the quads, glutes, and hamstrings.",
                targetMinutes = 15,
                benefit = "Builds leg and core strength, enhances stability and balance.",
                steps = listOf(
                    "Stand with feet shoulder-width apart.",
                    "Lower your body by bending your knees and hips as if sitting back.",
                    "Keep your chest up and knees aligned with your toes.",
                    "Push through your heels to return to a standing position."
                ),
                imagePath = "file:///android_asset/images/squats.png"
            ),
            Workout(
                name = "Deadlifts",
                description = "A powerful lower body and back exercise that also engages core muscles.",
                targetMinutes = 12,
                benefit = "Strengthens the entire posterior chain, improving lower back and core stability.",
                steps = listOf(
                    "Stand with feet hip-width apart and a barbell over your mid-foot.",
                    "Bend at your hips and knees to grasp the bar with an overhand grip.",
                    "Lift the bar by extending your hips and knees, keeping your back straight.",
                    "Lower the bar back down with control to the starting position."
                ),
                imagePath = "file:///android_asset/images/deadlifts.png"
            ),
            Workout(
                name = "Bench Press",
                description = "An upper body strength exercise that targets the chest, shoulders, and triceps.",
                targetMinutes = 15,
                benefit = "Develops upper body strength and muscle definition in the chest.",
                steps = listOf(
                    "Lie back on a bench with feet on the ground and grip the barbell slightly wider than shoulder-width.",
                    "Lower the bar to your chest, keeping elbows at a 45-degree angle.",
                    "Push the bar back up until your arms are straight.",
                    "Repeat for desired reps."
                ),
                imagePath = "file:///android_asset/images/bench_press.png"
            ),
            Workout(
                name = "Lunges",
                description = "A leg exercise that targets the quadriceps, hamstrings, and glutes.",
                targetMinutes = 10,
                benefit = "Improves leg strength, balance, and stability.",
                steps = listOf(
                    "Stand with feet together and step forward with one foot.",
                    "Lower your body until both knees are at a 90-degree angle.",
                    "Push back up to the starting position and switch legs.",
                    "Repeat for desired reps."
                ),
                imagePath = "file:///android_asset/images/lunges.png"
            ),
            Workout(
                name = "Kettlebell Swings",
                description = "A dynamic full-body exercise that focuses on the lower body and core.",
                targetMinutes = 8,
                benefit = "Increases power and endurance, particularly in the lower body.",
                steps = listOf(
                    "Stand with feet shoulder-width apart and hold a kettlebell with both hands.",
                    "Hinge at your hips, swinging the kettlebell back between your legs.",
                    "Thrust your hips forward to swing the kettlebell to shoulder height and higher.",
                    "Control the kettlebell back down and repeat."
                ),
                imagePath = "file:///android_asset/images/kettlebell_swings.png"
            ),
            Workout(
                name = "Barbell Rows",
                description = "A back exercise that also engages the shoulders and arms.",
                targetMinutes = 10,
                benefit = "Strengthens the back and improves posture and stability.",
                steps = listOf(
                    "Stand with feet hip-width apart, bending slightly at the knees and hips.",
                    "Grip the barbell with an overhand grip, hands shoulder-width apart.",
                    "Pull the bar towards your torso, squeezing shoulder blades together.",
                    "Lower the bar back down with control."
                ),
                imagePath = "file:///android_asset/images/barbell_rows.png"
            ),
            Workout(
                name = "Chin-ups",
                description = "An upper body exercise focusing on the biceps and back.",
                targetMinutes = 10,
                benefit = "Improves upper body strength and builds biceps and lats.",
                steps = listOf(
                    "Grab the bar with an underhand grip, hands shoulder-width apart.",
                    "Pull your body up until your chin is over the bar.",
                    "Lower yourself back down with control.",
                    "Repeat for desired reps."
                ),
                imagePath = "file:///android_asset/images/chin_ups.png"
            ),
            Workout(
                name = "Power Cleans",
                description = "An explosive movement that works the entire body, improving power and coordination.",
                targetMinutes = 8,
                benefit = "Builds total-body strength, power, and speed.",
                steps = listOf(
                    "Stand with feet hip-width apart, gripping the bar with hands shoulder-width.",
                    "Lift the bar by extending your hips and pulling it to your shoulders.",
                    "Catch the bar on your shoulders, standing upright.",
                    "Lower the bar back down with control."
                ),
                imagePath = "file:///android_asset/images/power_cleans.png"
            ),
            Workout(
                name = "Push Press",
                description = "An upper body exercise targeting the shoulders, chest, and triceps.",
                targetMinutes = 8,
                benefit = "Builds upper body strength and shoulder stability.",
                steps = listOf(
                    "Stand with feet shoulder-width apart, holding a barbell at shoulder height.",
                    "Slightly bend your knees, then push the bar overhead as you straighten your legs.",
                    "Lower the bar back to shoulder height with control.",
                    "Repeat for desired reps."
                ),
                imagePath = "file:///android_asset/images/push_press.png"
            ),
            Workout(
                name = "Chest Dips",
                description = "An exercise that primarily targets the chest, triceps, and shoulders.",
                targetMinutes = 10,
                benefit = "Builds chest and tricep strength.",
                steps = listOf(
                    "Grip the dip bars (Or any two strong bars) and lift your body up.",
                    "Lower yourself by bending your elbows until shoulders are level with elbows.",
                    "Push back up to the starting position.",
                    "Repeat for desired reps."
                ),
                imagePath = "file:///android_asset/images/chest_dips.png"
            ),
            Workout(
                name = "Dumbbell Snatches",
                description = "A full-body explosive movement that builds power and coordination.",
                targetMinutes = 8,
                benefit = "Increases total-body strength and enhances athletic performance.",
                steps = listOf(
                    "Stand with feet shoulder-width apart, holding a dumbbell in one hand.",
                    "Drive the dumbbell overhead by extending your hips and straightening your arm.",
                    "Return to the starting position with control.",
                    "Repeat on both sides for desired reps."
                ),
                imagePath = "file:///android_asset/images/dumbbell_snatches.png"
            ),
            Workout(
                name = "Weighted Pull-ups",
                description = "An advanced version of pull-ups with added weight for extra resistance.",
                targetMinutes = 10,
                benefit = "Increases upper body strength, focusing on the back and biceps.",
                steps = listOf(
                    "Wear a weight belt or hold a dumbbell between your feet.",
                    "Perform a pull-up by lifting your body until your chin is above the bar.",
                    "Lower yourself back down with control.",
                    "Repeat for desired reps."
                ),
                imagePath = "file:///android_asset/images/weighted_pull_ups.png"
            )
        ),
        Goal.MAINTAIN_WEIGHT to listOf(
            Workout(
                name = "Yoga",
                description = "A mind-body practice that combines physical postures, breathing exercises, and meditation.",
                targetMinutes = 30,
                benefit = "Improves flexibility, strength, and mental well-being.",
                steps = listOf(
                    "Start with a few minutes of deep breathing.",
                    "Move through a series of yoga poses, focusing on alignment and breath.",
                    "End with a relaxation pose or meditation.",
                    "Practice regularly for best results."
                ),
                imagePath = "file:///android_asset/images/yoga.png"
            ),
            Workout(
                name = "Stretching",
                description = "A physical exercise that strengthens muscles and improves flexibility.",
                targetMinutes = 3,
                benefit = "Reduces muscle tension, improves range of motion, and prevents injury.",
                steps = listOf(
                    "Perform dynamic stretches to warm up the muscles.",
                    "Hold static stretches for 15-30 seconds each.",
                    "Focus on major muscle groups like hamstrings, quads, and shoulders.",
                    "Breathe deeply and relax into each stretch."
                ),
                imagePath = "file:///android_asset/images/stretches.png"
            ),
            Workout(
                name = "Planks",
                description = "A core exercise that strengthens the abdominal muscles and improves posture.",
                targetMinutes = 5,
                benefit = "Builds core strength, improves balance, and supports spinal health.",
                steps = listOf(
                    "Start in a push-up position with forearms on the ground.",
                    "Engage your core and hold a straight line from head to heels.",
                    "Keep your body stable and breathe deeply.",
                    "Increase duration as you get stronger."
                ),
                imagePath = "file:///android_asset/images/plank.png"
            ),
            Workout(
                name = "Cycling",
                description = "A low-impact exercise that strengthens the lower body and improves stamina.",
                targetMinutes = 40,
                benefit = "Burns calories, strengthens leg muscles, and improves cardiovascular health.",
                steps = listOf(
                    "Adjust the bike seat to a comfortable height.",
                    "Start cycling at a moderate pace.",
                    "Gradually increase your speed after 10 minutes.",
                    "Cool down by slowing your pace for the last 5 minutes."
                ),
                imagePath = "file:///android_asset/images/cycling.png"
            ),
            Workout(
                name = "Light Weight Bicep Curls",
                description = "A resistance training workout using light weights to tone and strengthen muscles.",
                targetMinutes = 20,
                benefit = "Improves muscle tone, endurance, and overall fitness.",
                steps = listOf(
                    "Choose light dumbbells or resistance bands for the workout.",
                    "Perform bicep curls.",
                    "Focus on proper form and controlled movements.",
                    "Increase repetitions or weights as needed."
                ),
                imagePath = "file:///android_asset/images/bicep_curls.png"
            ),
            Workout(
                name = "Light Weight Tricep Extensions",
                description = "An exercise that targets the triceps using light weights for muscle toning.",
                targetMinutes = 20,
                benefit = "Strengthens and tones the triceps for improved arm definition.",
                steps = listOf(
                    "Select light dumbbells or resistance bands for the workout.",
                    "Perform tricep extensions.",
                    "Maintain proper form and controlled movements.",
                    "Increase repetitions or weights as you progress."
                ),
                imagePath = "file:///android_asset/images/tricep_extensions.png"
            ),
            Workout(
                name = "Light Weight Shoulder Press",
                description = "A shoulder exercise using light weights to tone and strengthen the deltoids.",
                targetMinutes = 20,
                benefit = "Builds shoulder strength and improves upper body definition.",
                steps = listOf(
                    "Choose light dumbbells or resistance bands for the workout.",
                    "Perform shoulder presses.",
                    "Focus on proper form and controlled movements.",
                    "Increase repetitions or weights as you advance."
                ),
                imagePath = "file:///android_asset/images/shoulder_press.png"
            ),
            Workout(
                name = "Pilates",
                description = "A low-impact exercise that focuses on core strength, flexibility, and body awareness.",
                targetMinutes = 3,
                benefit = "Improves posture, balance, and overall muscle tone.",
                steps = listOf(
                    "Stand up straight in a normal pose.",
                    "Bend over but keep your legs as straight as possible.",
                    "Touch the ground with your hands.",
                    "Take deep breaths and hold the pose."
                ),
                imagePath = "file:///android_asset/images/pilates.png"
            ),
            Workout(
                name = "Walking",
                description = "A low-impact aerobic exercise that improves cardiovascular health and burns calories.",
                targetMinutes = 30,
                benefit = "Boosts heart health, strengthens muscles, and aids in weight management.",
                steps = listOf(
                    "Start with a brisk pace that raises your heart rate.",
                    "Maintain a steady speed for the duration of the walk.",
                    "Focus on good posture and swing your arms as you walk.",
                    "Cool down with a slower pace at the end."
                ),
                imagePath = "file:///android_asset/images/walking.gif"
            ),
            Workout(
                name = "Low-Impact step touches",
                description = "Aerobic exercises that raise the heart rate without high-impact movements.",
                targetMinutes = 30,
                benefit = "Improves cardiovascular fitness, coordination, and endurance.",
                steps = listOf(
                    "Start with a warm-up to prepare your body for exercise.",
                    "Perform low-impact step touches.",
                    "Maintain a steady pace and follow the cues in the image.",
                    "Cool down with stretches and deep breathing."
                ),
                imagePath = "file:///android_asset/images/step_touches.png"
            ),
            Workout(
                name = "Rowing",
                description = "A full-body workout that engages multiple muscle groups for strength and endurance.",
                targetMinutes = 30,
                benefit = "Builds strength, improves cardio fitness, and tones the back and arms.",
                steps = listOf(
                    "Sit on the rowing machine with your feet strapped in.",
                    "Grab the handle and push with your legs first.",
                    "Lean back slightly and pull the handle towards your chest.",
                    "Extend your arms and bend your knees to return to the starting position."
                ),
                imagePath = "file:///android_asset/images/rowing.png"
            ),
            Workout(
                name = "Jump Rope",
                description = "A simple cardio workout that improves coordination and burns calories.",
                targetMinutes = 15,
                benefit = "Burns calories, improves footwork, and strengthens leg muscles.",
                steps = listOf(
                    "Hold the jump rope handles at each side.",
                    "Swing the rope over your head and jump as it reaches your feet.",
                    "Land on the balls of your feet.",
                    "Continue jumping at a steady rhythm."
                ),
                imagePath = "file:///android_asset/images/jump_rope.png"
            )
        )
    )

    // Function to get a random workout by goal
    fun getRandomWorkout(goal: Goal): Workout? {
        return workoutTypes[goal]?.random()
    }

    // Function to get a workout by id:
    fun getWorkoutById(id: String): Workout? {
        Log.d("WorkoutTypes", "getWorkoutById: id = $id")
        workoutTypes.forEach { (_, workouts) ->
            workouts.forEach { workout ->
                if (workout.id.toString() == id) {
                    Log.d("WorkoutTypes", "getWorkoutById: workout = $workout")
                    return workout
                }
            }
        }
        return null
    }
}

